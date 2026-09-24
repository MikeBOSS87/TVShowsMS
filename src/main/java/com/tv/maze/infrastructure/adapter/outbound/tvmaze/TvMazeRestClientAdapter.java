package com.tv.maze.infrastructure.adapter.outbound.tvmaze;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.tv.maze.domain.ShowSummary;
import com.tv.maze.domain.model.ShowDetail;
import com.tv.maze.domain.port.outbound.TvShowClientPort;
import com.tv.maze.infrastructure.adapter.outbound.tvmaze.dto.TvMazeSearchResponseDto;
import com.tv.maze.infrastructure.adapter.outbound.tvmaze.dto.TvMazeShowDto;



@Component
public class TvMazeRestClientAdapter implements TvShowClientPort{

	private final RestClient restClient;

    public TvMazeRestClientAdapter( RestClient restClient ) {
        this.restClient = restClient;
    }
	
	@Override
	public List<ShowSummary> searchShows( String query ) {
		List<TvMazeSearchResponseDto> response = restClient.get()
                .uri( "http://api.tvmaze.com/search/shows?q={query}", query )
                .retrieve()
                .body( new ParameterizedTypeReference<>() {} );

        if (response == null) {
            return List.of();
        }
        
        return response.stream()
                .filter(item -> item.show() != null)
                .map(item -> new ShowSummary(
                        item.show().id(),
                        item.show().name(),
                        item.show().getChannelName(),
                        item.show().summary(),
                        item.show().genres()
                ))
                .toList();
	}

	@Override
	public Optional<ShowDetail> getShowById(Long showId) {
		try {
            TvMazeShowDto dto = restClient.get()
                    .uri( "https://api.tvmaze.com/shows/{show_id}", showId )
                    .retrieve()
                    .body( TvMazeShowDto.class );

            if (dto == null) {
                return Optional.empty();
            }

            ShowDetail detail = new ShowDetail(
                    dto.id(),
                    dto.url(),
                    dto.name(),
                    dto.type(),
                    dto.language(),
                    dto.genres(),
                    dto.status(),
                    dto.runtime(),
                    dto.premiered(),
                    dto.officialSite(),
                    dto.summary(),
                    Map.of("channel", dto.getChannelName())
            );

            return Optional.of(detail);
        } catch (Exception e) {
            return Optional.empty();
        }
	}
}
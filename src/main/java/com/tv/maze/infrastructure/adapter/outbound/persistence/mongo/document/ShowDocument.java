package com.tv.maze.infrastructure.adapter.outbound.persistence.mongo.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "tvShows")
public class ShowDocument{
	
	@Id
    private Long id;
    private String url;
    private String name;
    private String type;
    private String language;
    private List<String> genres;
    private String status;
    private Integer runtime;
    private String premiered;
    private String officialSite;
    private String summary;
    private Map<String, Object> additionalAttributes;

    public ShowDocument(){}

    public ShowDocument( 
    		Long id
    		, String url
    		, String name
    		, String type
    		, String language
    		, List<String> genres
    		, String status
    		, Integer runtime
    		, String premiered
    		, String officialSite
    		, String summary
    		, Map<String, Object> additionalAttributes ) {
        this.id = id;
        this.url = url;
        this.name = name;
        this.type = type;
        this.language = language;
        this.genres = genres;
        this.status = status;
        this.runtime = runtime;
        this.premiered = premiered;
        this.officialSite = officialSite;
        this.summary = summary;
        this.additionalAttributes = additionalAttributes;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUrl() { return url; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getLanguage() { return language; }
    public List<String> getGenres() { return genres; }
    public String getStatus() { return status; }
    public Integer getRuntime() { return runtime; }
    public String getPremiered() { return premiered; }
    public String getOfficialSite() { return officialSite; }
    public String getSummary() { return summary; }
    public Map<String, Object> getAdditionalAttributes() { return additionalAttributes; }
}
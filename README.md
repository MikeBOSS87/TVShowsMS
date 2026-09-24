# Microservicio TVShowsMS - Arquitectura Hexagonal y Principios SOLID

Este proyecto es una API REST desarrollada en Java 21 y Spring Boot para la gestión y consulta de programas de televisión consumiendo la API pública de TVMaze y persisitiendo información en MongoDB Atlas.

---

## Estructura del Proyecto (Arquitectura Hexagonal)

El proyecto está organizado siguiendo el patrón de **Puertos y Adaptadores (Arquitectura Hexagonal)**, dividiendo la aplicación en tres capas principales desacopladas:

```text
com.tv.maze
├── domain/                          # Capa de Dominio (Core de Negocio)
│   ├── model/                       # Entidades e Inmutabilidad (Records)
│   │   ├── ShowSummary.java         # DTO / Modelo de resumen de shows
│   │   ├── ShowDetail.java          # Modelo de detalle completo de show
│   │   └── Comment.java             # Modelo de comentario y calificación
│   └── port/                        # Puertos de Salida (Outbound Ports)
│       └── outbound/
│           ├── TvShowClientPort.java        # Puerto para cliente de API externa
│           ├── TvShowRepositoryPort.java    # Puerto para persistencia de shows
│           └── CommentRepositoryPort.java   # Puerto para persistencia de comentarios
│
├── application/                     # Capa de Aplicación (Casos de Uso)
│   ├── port/                        # Puertos de Entrada (Inbound Ports)
│   │   └── inbound/
│   │       ├── SearchTvShowsUseCase.java    # Caso de uso: Búsqueda de shows
│   │       ├── GetTvShowByIdUseCase.java    # Caso de uso: Consulta por ID (Caché)
│   │       └── AddCommentUseCase.java       # Caso de uso: Guardar comentario
│   └── service/
│       └── TvShowService.java       # Servicio que orquesta la lógica de negocio
│
└── infrastructure/                  # Capa de Infraestructura (Adaptadores y Config)
    ├── adapter/
    │   ├── inbound/                 # Adaptadores de Entrada (Primary/Driving)
    │   │   └── rest/
    │   │       ├── TvShowController.java    # Controlador REST
    │   │       └── dto/
    │   │           └── CommentRequestDto.java # Request body para comentarios
    │   └── outbound/                # Adaptadores de Salida (Secondary/Driven)
    │       ├── tvmaze/              # Adaptador API Externa TVMaze
    │       │   ├── TvMazeRestClientAdapter.java
    │       │   └── dto/
    │       │       ├── TvMazeSearchResponseDto.java
    │       │       └── TvMazeShowDto.java
    │       └── persistence/mongo/   # Adaptador Persistencia MongoDB Atlas
    │           ├── TvShowMongoAdapter.java
    │           ├── CommentMongoAdapter.java
    │           ├── document/
    │           │   ├── ShowDocument.java
    │           │   └── CommentDocument.java
    │           └── repository/
    │               ├── SpringDataMongoShowRepository.java
    │               └── SpringDataMongoCommentRepository.java
    └── config/
        └── RestClientConfig.java    # Configuración de Beans (Spring)

```

# Microservicio TVShowsMS - Arquitectura Hexagonal y Principios SOLID

Este proyecto es una API REST desarrollada en Java 21 y Spring Boot para la gestión y consulta de programas de televisión consumiendo la API pública de TVMaze y persisitiendo información en MongoDB Atlas.


## Diagrama de Arquitectura Hexagonal

```mermaid
graph TD
    subgraph Adaptadores_Entrada [Adaptadores de Entrada - Primary / Inbound]
        Controller[TvShowController REST]
    end

    subgraph Puertos_Entrada [Puertos de Entrada - Inbound Ports]
        UC1[SearchTvShowsUseCase]
        UC2[GetTvShowByIdUseCase]
        UC3[AddCommentUseCase]
    end

    subgraph Capa_Dominio [Capa de Dominio & Aplicación - Core]
        Service[TvShowService]
        Model1[ShowSummary]
        Model2[ShowDetail]
        Model3[Comment]
    end

    subgraph Puertos_Salida [Puertos de Salida - Outbound Ports]
        P1[TvShowClientPort]
        P2[TvShowRepositoryPort]
        P3[CommentRepositoryPort]
    end

    subgraph Adaptadores_Salida [Adaptadores de Salida - Secondary / Outbound]
        A1[TvMazeRestClientAdapter]
        A2[TvShowMongoAdapter]
        A3[CommentMongoAdapter]
    end

    subgraph Externos [Sistemas Externos]
        TVMazeAPI[API Externa TVMaze]
        MongoAtlas[(MongoDB Atlas)]
    end

    %% Relaciones
    Controller --> UC1
    Controller --> UC2
    Controller --> UC3

    UC1 .-> Service
    UC2 .-> Service
    UC3 .-> Service

    Service --> Model1
    Service --> Model2
    Service --> Model3

    Service --> P1
    Service --> P2
    Service --> P3

    A1 .-> P1
    A2 .-> P2
    A3 .-> P3

    A1 --> TVMazeAPI
    A2 --> MongoAtlas
    A3 --> MongoAtlas
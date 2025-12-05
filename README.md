# Desafio Técnico - Order Service API

Este projeto é um microsserviço desenvolvido para o gerenciamento, processamento e consulta de pedidos de alta volumetria.

## 🚀 Tecnologias Utilizadas

* **Java 21 (LTS):** Versão mais recente e performática do Java.
* **Spring Boot 3.4.0:** Framework base para agilidade e configuração.
* **Spring Data JPA:** Para persistência e manipulação de dados.
* **Spring Boot Actuator:** Para monitoramento (Health Check) e métricas operacionais.
* **Micrometer:** Para instrumentação de métricas customizadas de negócio.
* **H2 Database:** Banco de dados em memória (para facilidade de execução e testes).
* **SpringDoc OpenAPI (Swagger):** Documentação automática e interativa da API.
* **Maven:** Gerenciamento de dependências.

---

## 📖 Documentação Interativa (Swagger)

A API possui documentação automática gerada pelo OpenAPI. Com a aplicação rodando, acesse:

👉 **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

Lá você poderá visualizar todos os endpoints, modelos de dados (Schemas) e testar as requisições diretamente pelo navegador.

---

## 📊 Observabilidade e Monitoramento

A aplicação foi projetada com foco em operabilidade ("Production-Ready"), expondo endpoints para monitoramento de saúde, métricas técnicas e de negócio, além de utilizar logs estruturados (SLF4J).

### Endpoints de Gerenciamento (Actuator)

| Recurso | Método | URL | Descrição |
| :--- | :--- | :--- | :--- |
| **Health Check** | `GET` | [`/actuator/health`](http://localhost:8080/actuator/health) | Monitora o status da API e a conectividade com o banco de dados. |
| **Métricas JVM** | `GET` | [`/actuator/metrics`](http://localhost:8080/actuator/metrics) | Dados técnicos (Uso de CPU, Memória Heap, Threads, GC). |
| **Métrica de Negócio** | `GET` | [`/actuator/metrics/pedidos.processados`](http://localhost:8080/actuator/metrics/pedidos.processados) | **Contador Customizado**: Monitora em tempo real o volume de pedidos processados com sucesso. |
| **Info da App** | `GET` | [`/actuator/info`](http://localhost:8080/actuator/info) | Informações de build e versão da aplicação. |

---

## 🏗️ Arquitetura do Projeto

O diagrama abaixo ilustra o fluxo de processamento de um pedido, desde a requisição até a persistência, destacando as camadas de validação e instrumentação de métricas.

```mermaid
graph TD
    Client([Client / External System]) -->|"POST /api/pedidos"| Controller[PedidoController]
    
    subgraph "Application Core"
        Controller -->|"@Valid"| DTO[PedidoInputDTO]
        DTO -- "Validates Data" --> Controller
        Controller -->|"Success"| Service[PedidoService]
        
        Service -->|"Check Duplicate"| Repository[PedidoRepository]
        Repository -->|"Query"| DB[(H2 Database)]
        
        Service -->|"Calculate Total"| Logic(Business Logic)
        Logic -->|"Save"| Repository
        
        Service -.->|"Record Metric"| Micrometer[Micrometer / Actuator]
        Service -.->|"Log Info/Error"| SLF4J[SLF4J Logging]
    end
    
    Micrometer -->|"Expose"| PrometheusEndpoint([/actuator/prometheus])
    SLF4J -->|"Output"| ConsoleLogs[Console / Logs]


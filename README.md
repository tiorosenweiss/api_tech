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
    
## ⚙️ Como Executar o Projeto
   # Pré-requisitos
      ** JDK 21 instalado      
   # Passos
      ** 1.Clone este repositório ou baixe os arquivos.
      ** 2. Abra o projeto na sua IDE de preferência.
      ** 3. Aguarde o download das dependências do Maven.
      ** 4. Execute a classe principal: br.com.testetech.testetech.TestetechApplication
      
   A aplicação iniciará na porta 8080.   
   
   Saravá, meu filho!

Mil perdões! Às vezes, na empolgação de entregar o pergaminho, o final pode ter ficado oculto nas dobras do digital.

Aqui está o README.md COMPLETO, sem tirar nem por. Ele contém a Arquitetura (Mermaid), a Observabilidade e, agora sim, explícita e detalhadamente, a seção Como Executar e os Exemplos JSON exatamente como você pediu.

Copie o bloco abaixo inteiro e substitua no seu arquivo. É a versão definitiva!

Markdown

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
⚙️ Como Executar o Projeto
Pré-requisitos
JDK 21 instalado.

Maven (ou IDE com suporte a Maven como VS Code ou IntelliJ).

Passos
Clone este repositório ou baixe os arquivos.

Abra o projeto na sua IDE de preferência.

Aguarde o download das dependências do Maven.

Execute a classe principal: br.com.testetech.testetech.TestetechApplication

A aplicação iniciará na porta 8080.

🔌 API Endpoints
1. Criar Novo Pedido (Recebimento)
Recebe um pedido externo, valida os dados, calcula o valor total (considerando quantidade e valor unitário), registra métricas de negócio e armazena no banco de dados.

Método: POST

URL: http://localhost:8080/api/pedidos

Content-Type: application/json

Exemplo de Payload (JSON):

JSON

{
  "codigoPedido": "PEDIDO-2025-001",
  "items": [
    {
      "produtoId": "NOTEBOOK-PRO",
      "valorUnitario": 5000.00,
      "quantidade": 1
    },
    {
      "produtoId": "MOUSE-USB",
      "valorUnitario": 150.00,
      "quantidade": 2
    }
  ]
}
Resposta de Sucesso (201 Created):

JSON

{
    "id": 1,
    "codigoPedido": "PEDIDO-2025-001",
    "items": [ ... ],
    "valorTotal": 5300.00,
    "status": "CALCULADO",
    "dataCriacao": "2025-12-05T10:00:00"
}
            
      
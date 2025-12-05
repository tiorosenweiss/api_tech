# Desafio Técnico - Order Service API

Este projeto é um microsserviço desenvolvido para o gerenciamento, processamento e consulta de pedidos de alta volumetria.

## 🚀 Tecnologias Utilizadas

* **Java 21 (LTS):** Versão mais recente e performática do Java.
* **Spring Boot 3.4.0:** Framework base para agilidade e configuração.
* **Spring Data JPA:** Para persistência e manipulação de dados.
* **H2 Database:** Banco de dados em memória (para facilidade de execução e testes).
* **SpringDoc OpenAPI (Swagger):** Documentação automática e interativa da API.
* **Maven:** Gerenciamento de dependências.

---

## 📖 Documentação Interativa (Swagger)

A API possui documentação automática gerada pelo OpenAPI. Com a aplicação rodando, acesse:

👉 **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

Lá você poderá visualizar todos os endpoints, modelos de dados (Schemas) e testar as requisições diretamente pelo navegador.

---

## ⚙️ Como Executar o Projeto

### Pré-requisitos
* JDK 21 instalado.
* Maven (ou IDE com suporte a Maven como VS Code ou IntelliJ).

### Passos
1. Clone este repositório ou baixe os arquivos.
2. Abra o projeto na sua IDE de preferência.
3. Aguarde o download das dependências do Maven.
4. Execute a classe principal:
   `br.com.testetech.testetech.TestetechApplication`

A aplicação iniciará na porta **8080**.

---

## 🔌 API Endpoints

### 1. Criar Novo Pedido (Recebimento)
Recebe um pedido externo, calcula o valor total (considerando quantidade e valor unitário) e armazena no banco de dados.

* **Método:** `POST`
* **URL:** `http://localhost:8080/api/pedidos`
* **Body (JSON Exemplo):**

```json
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
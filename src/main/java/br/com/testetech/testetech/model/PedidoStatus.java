package br.com.testetech.testetech.model;

/**
 * Define a máquina de estados (ciclo de vida) de um pedido dentro do sistema.
 * Utilizado para controle de fluxo e rastreabilidade do processamento.
 */
public enum PedidoStatus {

    /**
     * Estado inicial indicando que a requisição foi recebida pela API,
     * mas ainda aguarda processamento ou validação de regras de negócio.
     */
    RECEBIDO,

    /**
     * Indica que o pedido foi processado com sucesso, todos os itens foram iterados,
     * o valor total foi computado e o registro foi persistido no banco de dados.
     */
    CALCULADO
}
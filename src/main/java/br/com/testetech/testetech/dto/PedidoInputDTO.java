package br.com.testetech.testetech.dto;

import java.util.List;

/**
 * DTO de entrada para a criação de novos pedidos.
 * Encapsula o código de identificação do pedido e a lista de itens associados.
 */
public class PedidoInputDTO {

    /**
     * Código identificador do pedido vindo do sistema externo.
     * Deve ser único no sistema para evitar duplicidade.
     */
    private String codigoPedido;

    /**
     * Lista de itens que compõem o pedido.
     */
    private List<ItemDTO> items;

    public PedidoInputDTO() {}

    public PedidoInputDTO(String codigoPedido, List<ItemDTO> items) {
        this.codigoPedido = codigoPedido;
        this.items = items;
    }

    public String getCodigoPedido() {
        return codigoPedido;
    }

    public void setCodigoPedido(String codigoPedido) {
        this.codigoPedido = codigoPedido;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }
}
package br.com.testetech.testetech.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

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
    @NotBlank(message = "O código do pedido não pode estar em branco")
    private String codigoPedido;

    /**
     * Lista de itens que compõem o pedido.
     */
    @NotEmpty(message = "O pedido deve conter pelo menos um item")
    @Valid
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
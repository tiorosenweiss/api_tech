package br.com.testetech.testetech.dto;

import java.math.BigDecimal;

/**
 * Objeto de Transferência de Dados (DTO) que representa um item individual dentro de um pedido.
 * Utilizado para trafegar dados de produtos, valores e quantidades na camada de entrada.
 */
public class ItemDTO {

    /**
     * Identificador único ou SKU do produto (ex: "NOTEBOOK-DELL").
     */
    private String produtoId;

    /**
     * Valor monetário unitário do produto.
     * Espera-se um valor positivo para cálculos.
     */
    private BigDecimal valorUnitario;

    /**
     * Quantidade de itens solicitados.
     */
    private Integer quantidade;

    public ItemDTO(){}

    public ItemDTO(String produtoId, BigDecimal valorUnitario, Integer quantidade) {
        this.produtoId = produtoId;
        this.valorUnitario = valorUnitario;
        this.quantidade = quantidade;
    }

    public String getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(String produtoId) {
        this.produtoId = produtoId;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}
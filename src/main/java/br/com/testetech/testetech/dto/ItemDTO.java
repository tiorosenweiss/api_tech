package br.com.testetech.testetech.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/**
 * Objeto de Transferência de Dados (DTO) que representa um item individual dentro de um pedido.
 * Utilizado para trafegar dados de produtos, valores e quantidades na camada de entrada.
 */
public class ItemDTO {

    /**
     * Identificador único do produto (ex: "NOTEBOOK-DELL").
     */
    @NotNull(message = "O ID do produto é obrigatório")
    private String produtoId;

    /**
     * Valor monetário unitário do produto.
     * Espera-se um valor positivo para cálculos.
     */
    @NotNull(message = "O valor unitário é obrigatório")
    @Positive(message = "O valor deve ser positivo")
    private BigDecimal valorUnitario;

    /**
     * Quantidade de itens solicitados.
     */
    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser pelo menos 1")
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
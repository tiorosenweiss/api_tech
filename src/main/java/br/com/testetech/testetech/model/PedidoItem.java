package br.com.testetech.testetech.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Entidade que representa um item de linha (line item) dentro de um pedido.
 * <p>
 * Esta classe armazena o "snapshot" (retrato) dos dados do produto no momento da compra,
 * garantindo o histórico de preço, independente de alterações futuras no catálogo de produtos.
 * </p>
 */
@Entity
@Table(name = "itens_pedido")
public class PedidoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Identificador lógico ou SKU do produto adquirido.
     */
    private String produtoId;

    /**
     * Valor unitário do produto no momento da efetivação do pedido.
     * Armazenado para garantir integridade histórica financeira.
     */
    private BigDecimal valorUnitario;

    /**
     * Quantidade de unidades adquiridas deste item.
     */
    private Integer quantidade;

    public PedidoItem() { }

    public PedidoItem(String produtoId, BigDecimal valorUnitario, Integer quantidade) {
        this.produtoId = produtoId;
        this.valorUnitario = valorUnitario;
        this.quantidade = quantidade;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
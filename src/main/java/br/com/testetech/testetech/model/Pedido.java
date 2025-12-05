package br.com.testetech.testetech.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidade principal que representa um Pedido no sistema.
 * <p>
 * Mapeada para a tabela 'pedidos', esta classe é a raiz do agregado de pedidos,
 * gerenciando o ciclo de vida dos itens associados através de cascata (CascadeType.ALL).
 * Possui índice único para garantir a integridade de códigos externos.
 * </p>
 */
@Entity
@Table(name = "pedidos", indexes = {
        @Index(name = "idx_codigo_pedido", columnList = "codigo_pedido", unique = true)
})
public class Pedido {

    /**
     * Identificador técnico (Surrogate Key) gerado automaticamente pelo banco.
     * Uso exclusivo para integridade referencial interna.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Código de negócio (Business Key) recebido do sistema externo.
     * Este campo possui restrição de unicidade (unique constraint) para evitar duplicidade de pedidos.
     */
    @Column(name = "codigo_pedido", nullable = false, unique = true)
    private String codigoPedido;

    /**
     * Lista de itens que compõem o pedido.
     * Configurada com FetchType.LAZY para performance e CascadeType.ALL para
     * que os itens sejam persistidos/removidos automaticamente junto com o pedido.
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private List<PedidoItem> items;

    /**
     * Valor total calculado do pedido. Armazenado para evitar recálculos em consultas de leitura.
     */
    private BigDecimal valorTotal;

    @Enumerated(EnumType.STRING)
    private PedidoStatus status;

    /**
     * Data de recebimento/criação do registro no banco de dados.
     */
    private LocalDateTime dataCriacao;


    public Pedido() {}

    public Pedido(Long id, String codigoPedido, List<PedidoItem> items, BigDecimal valorTotal, PedidoStatus status, LocalDateTime dataCriacao) {
        this.id = id;
        this.codigoPedido = codigoPedido;
        this.items = items;
        this.valorTotal = valorTotal;
        this.status = status;
        this.dataCriacao = dataCriacao;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoPedido() {
        return codigoPedido;
    }

    public void setCodigoPedido(String codigoPedido) {
        this.codigoPedido = codigoPedido;
    }

    public List<PedidoItem> getItems() {
        return items;
    }

    public void setItems(List<PedidoItem> items) {
        this.items = items;
    }

    public PedidoStatus getStatus() {
        return status;
    }

    public void setStatus(PedidoStatus status) {
        this.status = status;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    /**
     * Callback de ciclo de vida do JPA executado antes da persistência.
     * Garante que a data de criação seja sempre preenchida automaticamente pelo servidor,
     * independente da origem da requisição.
     */
    @PrePersist
    public void prePersist() {
        this.dataCriacao = LocalDateTime.now();
    }
}
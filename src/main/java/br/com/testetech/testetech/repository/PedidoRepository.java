package br.com.testetech.testetech.repository;

import br.com.testetech.testetech.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface de repositório responsável pela camada de acesso a dados da entidade {@link Pedido}.
 * <p>
 * Estende {@link JpaRepository} para herdar operações CRUD padrão e paginação,
 * abstraindo a complexidade do JDBC/SQL através do Spring Data JPA.
 * </p>
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    /**
     * Verifica de forma otimizada a existência de um pedido através do seu código de negócio.
     * <p>
     * Este é um <i>Derived Query Method</i>: o Spring Data interpreta o nome do método
     * e gera a consulta SQL automaticamente (ex: SELECT COUNT(x)... > 0).
     * Ideal para validações de unicidade antes de operações de escrita, pois evita
     * o overhead de carregar a entidade completa em memória.
     * </p>
     *
     * @param codigoPedido O código identificador externo do pedido (Business Key).
     * @return {@code true} se um pedido com este código já estiver persistido, {@code false} caso contrário.
     */
    boolean existsByCodigoPedido(String codigoPedido);
}
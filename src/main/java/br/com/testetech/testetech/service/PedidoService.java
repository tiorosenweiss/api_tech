package br.com.testetech.testetech.service;

import br.com.testetech.testetech.dto.PedidoInputDTO;
import br.com.testetech.testetech.exception.PedidoDuplicadoException;
import br.com.testetech.testetech.model.Pedido;
import br.com.testetech.testetech.model.PedidoItem;
import br.com.testetech.testetech.model.PedidoStatus;
import br.com.testetech.testetech.repository.PedidoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Camada de serviço responsável pelas regras de negócio e orquestração do fluxo de pedidos.
 * <p>
 * Atua como fronteira transacional para operações de escrita, garantindo a atomicidade
 * e consistência dos dados processados (ACID).
 * </p>
 */
@Service
public class PedidoService {

    private final PedidoRepository repository;

    public PedidoService(PedidoRepository repository) {
        this.repository = repository;
    }

    /**
     * Registra um novo pedido no sistema, executando validações, cálculos e persistência.
     * <p>
     * O fluxo de execução compreende:
     * <ol>
     * <li>Verificação de duplicidade (Idempotência) baseada no código externo.</li>
     * <li>Mapeamento de DTO para Entidade de domínio.</li>
     * <li>Cálculo do valor total agregado dos itens via Stream API.</li>
     * <li>Persistência transacional no banco de dados.</li>
     * </ol>
     * </p>
     *
     * @param dto Objeto contendo os dados de entrada do pedido.
     * @return A entidade {@link Pedido} persistida e atualizada com ID e Data de Criação.
     * @throws PedidoDuplicadoException Se já existir um pedido com o mesmo código identificador.
     */
    @Transactional
    public Pedido registrarPedido(PedidoInputDTO dto) {

        // Log de rastreabilidade simples para fins de demonstração
        System.out.println("Processando novo pedido: " + dto.getCodigoPedido());

        // 1. Validação de Unicidade (Regra de Negócio)
        if (repository.existsByCodigoPedido(dto.getCodigoPedido())) {
            throw new PedidoDuplicadoException("Pedido já existe: " + dto.getCodigoPedido());
        }

        Pedido pedido = new Pedido();
        pedido.setCodigoPedido(dto.getCodigoPedido());
        pedido.setStatus(PedidoStatus.CALCULADO);

        // 2. Transformação de Dados (Map)
        List<PedidoItem> items = dto.getItems().stream().map(i -> {
            PedidoItem item = new PedidoItem();
            item.setProdutoId(i.getProdutoId());
            item.setQuantidade(i.getQuantidade());
            item.setValorUnitario(i.getValorUnitario());
            return item;
        }).collect(Collectors.toList());

        pedido.setItems(items);

        // 3. Regra de Cálculo (Reduce)
        // Utilizando Stream para cálculo monetário preciso e thread-safe
        BigDecimal total = items.stream()
                .map(item -> item.getValorUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setValorTotal(total);

        try {
            return repository.save(pedido);
        } catch (DataIntegrityViolationException e) {
            // Tratamento defensivo para Race Conditions (Concorrência)
            throw new PedidoDuplicadoException("Erro de concorrência: Pedido duplicado.");
        }
    }

    /**
     * Recupera a listagem paginada de todos os pedidos.
     *
     * @param pageable Configuração de paginação (tamanho, página e ordenação).
     * @return Um objeto {@link Page} contendo os pedidos e metadados da consulta.
     */
    public Page<Pedido> listarTodos(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
package br.com.testetech.testetech.service;

import br.com.testetech.testetech.dto.PedidoInputDTO;
import br.com.testetech.testetech.exception.PedidoDuplicadoException;
import br.com.testetech.testetech.model.Pedido;
import br.com.testetech.testetech.model.PedidoItem;
import br.com.testetech.testetech.model.PedidoStatus;
import br.com.testetech.testetech.repository.PedidoRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Além disso, implementa instrumentação para observabilidade (Logs e Métricas).
 * </p>
 */
@Service
public class PedidoService {

    /**
     * Logger para registro de eventos e rastreabilidade da aplicação.
     */
    private final Logger logger = LoggerFactory.getLogger(PedidoService.class);

    private final PedidoRepository repository;

    /**
     * Métrica customizada para contagem de pedidos processados com sucesso.
     */
    private final Counter pedidosProcessadosCounter;

    /**
     * Construtor com injeção de dependências.
     *
     * @param repository Acesso aos dados do pedido.
     * @param meterRegistry Registro de métricas do Micrometer para criação de contadores customizados.
     */
    public PedidoService(PedidoRepository repository, MeterRegistry meterRegistry) {
        this.repository = repository;

        // Inicializa o contador customizado 'pedidos.processados'
        this.pedidosProcessadosCounter = Counter.builder("pedidos.processados")
                .description("Total de pedidos processados com sucesso pela API")
                .register(meterRegistry);
    }

    /**
     * Registra um novo pedido no sistema, executando validações, cálculos e persistência.
     * <p>
     * O fluxo de execução compreende:
     * <ol>
     * <li>Verificação de duplicidade (Idempotência) baseada no código externo.</li>
     * <li>Mapeamento de DTO para Entidade de domínio.</li>
     * <li>Cálculo do valor total agregado dos itens via Stream API.</li>
     * <li>Persistência transacional no banco de dados e atualização de métricas.</li>
     * </ol>
     * </p>
     *
     * @param dto Objeto contendo os dados de entrada do pedido.
     * @return A entidade {@link Pedido} persistida e atualizada com ID e Data de Criação.
     * @throws PedidoDuplicadoException Se já existir um pedido com o mesmo código identificador.
     */
    @Transactional
    public Pedido registrarPedido(PedidoInputDTO dto) {


        logger.info("Iniciando processamento do novo pedido. Código: {}", dto.getCodigoPedido());


        if (repository.existsByCodigoPedido(dto.getCodigoPedido())) {
            logger.warn("Tentativa de criação de pedido duplicado rejeitada. Código: {}", dto.getCodigoPedido());
            throw new PedidoDuplicadoException("Pedido já existe: " + dto.getCodigoPedido());
        }

        Pedido pedido = new Pedido();
        pedido.setCodigoPedido(dto.getCodigoPedido());
        pedido.setStatus(PedidoStatus.CALCULADO);


        List<PedidoItem> items = dto.getItems().stream().map(i -> {
            PedidoItem item = new PedidoItem();
            item.setProdutoId(i.getProdutoId());
            item.setQuantidade(i.getQuantidade());
            item.setValorUnitario(i.getValorUnitario());
            return item;
        }).collect(Collectors.toList());

        pedido.setItems(items);


        // Utilizando Stream para cálculo monetário preciso e thread-safe
        BigDecimal total = items.stream()
                .map(item -> item.getValorUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setValorTotal(total);

        try {
            Pedido pedidoSalvo = repository.save(pedido);

            // Incrementa a métrica de negócio para monitoramento
            pedidosProcessadosCounter.increment();

            logger.info("Pedido {} processado e salvo com sucesso. Valor Total: {}",
                    pedidoSalvo.getCodigoPedido(), total);

            return pedidoSalvo;
        } catch (DataIntegrityViolationException e) {
            // Tratamento defensivo para Race Conditions (Concorrência)
            logger.error("Erro de integridade/concorrência ao salvar o pedido: {}", dto.getCodigoPedido(), e);
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
        logger.debug("Consultando lista de pedidos com parâmetros de paginação: {}", pageable);
        return repository.findAll(pageable);
    }
}
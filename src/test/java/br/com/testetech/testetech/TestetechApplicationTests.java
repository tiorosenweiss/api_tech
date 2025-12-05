package br.com.testetech.testetech;

import br.com.testetech.testetech.dto.ItemDTO;
import br.com.testetech.testetech.dto.PedidoInputDTO;
import br.com.testetech.testetech.exception.PedidoDuplicadoException;
import br.com.testetech.testetech.model.Pedido;
import br.com.testetech.testetech.model.PedidoStatus;
import br.com.testetech.testetech.repository.PedidoRepository;
import br.com.testetech.testetech.service.PedidoService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository repository;

    /**
     * Isso cria um registro de métricas real em memória para o teste,
     * evitando que o contador seja nulo e cause erro no construtor.
     */
    @Spy
    private MeterRegistry meterRegistry = new SimpleMeterRegistry();

    @InjectMocks
    private PedidoService service;

    @Test
    @DisplayName("Deve calcular o valor total corretamente e salvar o pedido")
    void shouldCalculateTotalAndSaveOrder() {
        // GIVEN
        ItemDTO item1 = new ItemDTO("PRODUTO-A", new BigDecimal("10.00"), 2);
        ItemDTO item2 = new ItemDTO("PRODUTO-B", new BigDecimal("30.00"), 1);

        PedidoInputDTO input = new PedidoInputDTO("PEDIDO-123", List.of(item1, item2));

        when(repository.existsByCodigoPedido("PEDIDO-123")).thenReturn(false);
        when(repository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // ACTION
        Pedido result = service.registrarPedido(input);

        // THEN
        assertNotNull(result);
        assertEquals(new BigDecimal("50.00"), result.getValorTotal());
        assertEquals(PedidoStatus.CALCULADO, result.getStatus());

        verify(repository, times(1)).save(any(Pedido.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando o ID externo já existe")
    void shouldThrowExceptionWhenOrderIsDuplicated() {
        // GIVEN
        PedidoInputDTO input = new PedidoInputDTO();
        input.setCodigoPedido("PEDIDO-DUPLICADO");

        when(repository.existsByCodigoPedido("PEDIDO-DUPLICADO")).thenReturn(true);

        // THEN
        assertThrows(PedidoDuplicadoException.class, () -> {
            service.registrarPedido(input);
        });

        verify(repository, never()).save(any());
    }
}
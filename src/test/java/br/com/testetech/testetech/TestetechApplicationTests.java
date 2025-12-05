package br.com.testetech.testetech;

import br.com.testetech.testetech.dto.ItemDTO;
import br.com.testetech.testetech.dto.PedidoInputDTO;
import br.com.testetech.testetech.exception.PedidoDuplicadoException;
import br.com.testetech.testetech.model.Pedido;
import br.com.testetech.testetech.model.PedidoStatus;
import br.com.testetech.testetech.repository.PedidoRepository;
import br.com.testetech.testetech.service.PedidoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Suite de testes unitários para a classe de serviço {@link PedidoService}.
 * <p>
 * Utiliza o Mockito para isolar a camada de serviço, simulando o comportamento
 * do repositório (Mock) para validar exclusivamente as regras de negócio
 * (cálculo de valores, status e validações).
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    /**
     * Mock do repositório para evitar chamadas reais ao banco de dados durante os testes.
     */
    @Mock
    private PedidoRepository repository;

    /**
     * Injeta automaticamente os mocks criados (repository) na instância do serviço.
     */
    @InjectMocks
    private PedidoService service;

    /**
     * Cenário Positivo: Criação de pedido com sucesso.
     * <p>
     * Valida se:
     * 1. O cálculo do valor total (Quantidade * Valor Unitário) está correto.
     * 2. O status inicial é definido como CALCULADO.
     * 3. O método save do repositório é chamado corretamente.
     * </p>
     */
    @Test
    @DisplayName("Deve calcular o valor total corretamente e salvar o pedido")
    void shouldCalculateTotalAndSaveOrder() {
        // GIVEN (Dado um cenário com itens válidos)
        ItemDTO item1 = new ItemDTO("PRODUTO-A", new BigDecimal("10.00"), 2); // Subtotal: 20.00
        ItemDTO item2 = new ItemDTO("PRODUTO-B", new BigDecimal("30.00"), 1); // Subtotal: 30.00

        PedidoInputDTO input = new PedidoInputDTO("PEDIDO-123", List.of(item1, item2));

        // WHEN (Quando o repositório não encontra duplicidade)
        when(repository.existsByCodigoPedido("PEDIDO-123")).thenReturn(false);
        // (Simula o salvamento retornando o próprio objeto passado)
        when(repository.save(any(Pedido.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // ACTION (Executa a lógica)
        Pedido result = service.registrarPedido(input);

        // THEN (Então os resultados devem ser consistentes)
        assertNotNull(result);
        // Validação da Regra de Negócio: 20 + 30 = 50.00
        assertEquals(new BigDecimal("50.00"), result.getValorTotal());
        assertEquals(PedidoStatus.CALCULADO, result.getStatus());

        // Verifica se o repositório foi chamado exatamente uma vez
        verify(repository, times(1)).save(any(Pedido.class));
    }

    /**
     * Cenário Negativo: Tentativa de duplicação.
     * <p>
     * Valida se o serviço bloqueia a operação e lança {@link PedidoDuplicadoException}
     * quando o código do pedido já existe no banco, garantindo a idempotência.
     * </p>
     */
    @Test
    @DisplayName("Deve lançar exceção quando o ID externo já existe")
    void shouldThrowExceptionWhenOrderIsDuplicated() {
        // GIVEN (Dado um pedido com ID já existente)
        PedidoInputDTO input = new PedidoInputDTO();
        input.setCodigoPedido("PEDIDO-DUPLICADO");

        // WHEN (O repositório confirma a existência)
        when(repository.existsByCodigoPedido("PEDIDO-DUPLICADO")).thenReturn(true);

        // THEN (Deve lançar exceção e NUNCA tentar salvar)
        assertThrows(PedidoDuplicadoException.class, () -> {
            service.registrarPedido(input);
        });

        // Garante que o método save NUNCA foi chamado (Segurança)
        verify(repository, never()).save(any());
    }
}
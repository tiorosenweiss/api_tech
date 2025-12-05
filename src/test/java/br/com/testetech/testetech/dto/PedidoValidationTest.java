package br.com.testetech.testetech.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Teste Unitário focado exclusivamente na validação dos DTOs via Bean Validation.
 * Garante que as anotações (@NotNull, @Positive, etc) estão blindando a entrada de dados.
 */
class PedidoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve passar quando o DTO está totalmente preenchido e válido")
    void shouldPassWhenDtoIsValid() {
        // GIVEN
        ItemDTO item = new ItemDTO("PRODUTO-1", new BigDecimal("100.00"), 1);
        PedidoInputDTO input = new PedidoInputDTO("PEDIDO-VALIDO", List.of(item));

        // WHEN
        Set<ConstraintViolation<PedidoInputDTO>> violations = validator.validate(input);

        // THEN
        assertTrue(violations.isEmpty(), "Não deve haver violações de validação");
    }

    @Test
    @DisplayName("Deve falhar quando o Código do Pedido for nulo ou vazio")
    void shouldFailWhenCodigoPedidoIsInvalid() {
        // GIVEN
        ItemDTO item = new ItemDTO("PRODUTO-1", new BigDecimal("100.00"), 1);
        PedidoInputDTO input = new PedidoInputDTO(null, List.of(item)); // Código Nulo

        // WHEN
        Set<ConstraintViolation<PedidoInputDTO>> violations = validator.validate(input);

        // THEN
        assertFalse(violations.isEmpty());
        // Verifica se a mensagem de erro é a que definimos no DTO
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("O código do pedido não pode estar em branco")));
    }

    @Test
    @DisplayName("Deve falhar quando a lista de itens estiver vazia")
    void shouldFailWhenItemsListIsInvalid() {
        // GIVEN
        PedidoInputDTO input = new PedidoInputDTO("PEDIDO-SEM-ITENS", List.of()); // Lista Vazia

        // WHEN
        Set<ConstraintViolation<PedidoInputDTO>> violations = validator.validate(input);

        // THEN
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("O pedido deve conter pelo menos um item")));
    }

    @Test
    @DisplayName("Deve falhar quando o item do pedido tiver valor negativo")
    void shouldFailWhenItemPriceIsNegative() {
        // GIVEN
        ItemDTO item = new ItemDTO("PRODUTO-ERRADO", new BigDecimal("-50.00"), 1); // Valor Negativo
        PedidoInputDTO input = new PedidoInputDTO("PEDIDO-X", List.of(item));

        // WHEN
        Set<ConstraintViolation<PedidoInputDTO>> violations = validator.validate(input);

        // THEN
        assertFalse(violations.isEmpty());
        // Aqui o validador pega o erro aninhado dentro da lista de itens
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("O valor deve ser positivo")));
    }
}
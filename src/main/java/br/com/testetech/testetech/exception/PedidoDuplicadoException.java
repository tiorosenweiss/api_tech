package br.com.testetech.testetech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção de negócio personalizada lançada quando ocorre uma tentativa de inserção
 * de um pedido com código identificador já existente no banco de dados.
 * <p>
 * Mapeia automaticamente para o Status HTTP 409 (Conflict) se não tratada globalmente.
 * </p>
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class PedidoDuplicadoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PedidoDuplicadoException(String message) {
        super(message);
    }
}
package br.com.testetech.testetech.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Interceptador global de exceções da aplicação.
 * <p>
 * Esta classe utiliza o mecanismo de AOP (Aspect Oriented Programming) do Spring
 * para capturar exceções lançadas em qualquer nível da camada de controle e transformá-las
 * em respostas HTTP padronizadas (JSON) para o cliente.
 * </p>
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Manipula a exceção de negócio {@link PedidoDuplicadoException}.
     * <p>
     * Este método é acionado automaticamente quando uma tentativa de cadastro viola
     * a regra de unicidade do código do pedido.
     * </p>
     *
     * @param ex A exceção capturada contendo a mensagem de erro específica.
     * @return Um ResponseEntity contendo um Map com detalhes do erro (timestamp, status, erro e mensagem)
     * e o status HTTP 409 (Conflict).
     */
    @ExceptionHandler(PedidoDuplicadoException.class)
    public ResponseEntity<Object> tratarPedidoDuplicado(PedidoDuplicadoException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", "Conflito de Dados");
        body.put("message", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
}
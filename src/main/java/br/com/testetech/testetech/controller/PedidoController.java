package br.com.testetech.testetech.controller;

import br.com.testetech.testetech.dto.PedidoInputDTO;
import br.com.testetech.testetech.model.Pedido;
import br.com.testetech.testetech.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST responsável pela exposição dos endpoints de pedidos.
 * Gerencia a entrada de novos registros e a consulta paginada.
 */
@RestController
@RequestMapping("/api/pedidos")

public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service){
        this.service = service;
    }

    /**
     * Recebe e processa uma requisição para criação de um novo pedido.
     * O método delega o cálculo de valores e validações para a camada de serviço.
     *
     * @param dto Objeto de transferência contendo o código do pedido e lista de itens.
     * @return ResponseEntity contendo o pedido persistido e o status HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<Pedido> criar(@RequestBody @Valid PedidoInputDTO dto) {
        Pedido pedidoSalvo = service.registrarPedido(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoSalvo);
    }

    /**
     * Recupera a lista de pedidos cadastrados com suporte a paginação.
     * A ordenação padrão é decrescente pela data de criação (mais recentes primeiro).
     *
     * @param pageable Objeto contendo os parâmetros de paginação (page, size, sort).
     * @return ResponseEntity contendo a página (Page) de pedidos encontrados.
     */
    @GetMapping
    public ResponseEntity<Page<Pedido>> listar(
            @org.springdoc.core.annotations.ParameterObject
            @PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.listarTodos(pageable));
    }
}
package br.com.fiap.techfood.adapters.controller;

import br.com.fiap.techfood.adapters.request.UserRequest;
import br.com.fiap.techfood.application.exceptions.InvalidCredentialsException;
import br.com.fiap.techfood.application.exceptions.ResourceAlreadyExistsException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/test/exceptions")
public class TestExceptionController {

    @GetMapping("/not-found")
    public void notFound() {
        throw new NoSuchElementException("Usuário não encontrado");
    }

    @GetMapping("/illegal")
    public void illegal() {
        throw new IllegalArgumentException("Requisição inválida");
    }

    @GetMapping("/conflict")
    public void conflict() {
        throw new ResourceAlreadyExistsException("Recurso já existe");
    }

    @GetMapping("/unauthorized")
    public void unauthorized() {
        throw new InvalidCredentialsException("Não autorizado");
    }

    @GetMapping("/generic")
    public void generic() {
        throw new RuntimeException("Erro inesperado");
    }

    @PostMapping("/validate")
    public void validate(@RequestBody @Valid UserRequest dto) {
        // endpoint auxiliar para disparar validação
    }
}

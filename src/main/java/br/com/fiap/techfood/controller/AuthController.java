package br.com.fiap.techfood.controller;

import br.com.fiap.techfood.dto.request.ChangePasswordDTO;
import br.com.fiap.techfood.dto.request.LoginRequestDTO;
import br.com.fiap.techfood.dto.response.api.ApiSuccessResponse;
import br.com.fiap.techfood.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Autenticação de usuários")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login de usuário",
            description = "Valida login e senha usando BCrypt"
    )
    @ApiResponse(responseCode = "200", description = "Login válido")
    public ResponseEntity<ApiSuccessResponse<?>> login(@RequestBody @Valid LoginRequestDTO dto) {
        service.login(dto);
        return ResponseEntity.ok(ApiSuccessResponse.success("Usuário autenticado com sucesso"));
    }

    @PutMapping("/users/{id}/password")
    @Operation(summary = "Trocar senha do usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário ou senha invalidos"),
    })
    public ResponseEntity<ApiSuccessResponse<?>> changePassword(@PathVariable UUID id, @RequestBody @Valid ChangePasswordDTO dto) {
        service.changePassword(id, dto);
        return ResponseEntity.ok(ApiSuccessResponse.success(
                String.format("Senha do usuário %s alterada com sucesso", dto.login())
        ));
    }
}



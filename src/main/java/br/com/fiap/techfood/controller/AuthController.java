package br.com.fiap.techfood.controller;

import br.com.fiap.techfood.dto.request.ChangePasswordDTO;
import br.com.fiap.techfood.dto.request.LoginRequestDTO;
import br.com.fiap.techfood.dto.response.api.ApiSuccessResponse;
import br.com.fiap.techfood.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/auth")
@Tag(name = "Auth", description = "Autenticação de usuários")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login de usuário",
            description = "Valida login e senha. Não utiliza Spring Security — a verificação é feita diretamente no banco com BCrypt"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login válido"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<ApiSuccessResponse<String>> login(@RequestBody @Valid LoginRequestDTO dto) {
        service.login(dto);
        return ApiSuccessResponse.okMessage("Usuário autenticado com sucesso");
    }

    @PutMapping("/users/{id}/password")
    @Operation(
            summary = "Trocar senha do usuário",
            description = "Altera a senha do usuário. Requer o login e a senha atual para confirmação"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Usuário ou senha inválidos",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<ApiSuccessResponse<String>> changePassword(@PathVariable UUID id, @RequestBody @Valid ChangePasswordDTO dto) {
        service.changePassword(id, dto);
        return ApiSuccessResponse.okMessage("Senha alterada com sucesso");
    }
}

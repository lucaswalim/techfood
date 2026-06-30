package br.com.fiap.techfood.adapters.controller;

import br.com.fiap.techfood.adapters.mapper.AdapterMapper;
import br.com.fiap.techfood.adapters.presenter.ApiResponseFactory;
import br.com.fiap.techfood.adapters.request.ChangePasswordRequest;
import br.com.fiap.techfood.adapters.request.LoginRequest;
import br.com.fiap.techfood.adapters.response.TokenResponse;
import br.com.fiap.techfood.adapters.response.api.ApiSuccessResponse;
import br.com.fiap.techfood.application.dto.TokenResult;
import br.com.fiap.techfood.application.usecase.auth.ChangePasswordUseCase;
import br.com.fiap.techfood.application.usecase.login.LoginUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    private final LoginUseCase loginUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final AdapterMapper adapterMapper;
    private final ApiResponseFactory presenter;

    public AuthController(LoginUseCase loginUseCase,
                          ChangePasswordUseCase changePasswordUseCase,
                          AdapterMapper adapterMapper,
                          ApiResponseFactory presenter) {
        this.loginUseCase = loginUseCase;
        this.changePasswordUseCase = changePasswordUseCase;
        this.adapterMapper = adapterMapper;
        this.presenter = presenter;
    }

    @PostMapping("/login")
    @Operation(summary = "Login de usuário", description = "Valida login e senha e retorna um token JWT para uso nas demais requisições")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login válido - token JWT retornado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<ApiSuccessResponse<TokenResponse>> login(@RequestBody @Valid LoginRequest request) {
        TokenResult result = loginUseCase.execute(adapterMapper.toCommand(request));
        return presenter.ok(adapterMapper.toResponse(result), "Usuário autenticado com sucesso");
    }

    @PutMapping("/users/{id}/password")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Trocar senha do usuário", description = "Altera a senha do usuário. Requer o login e a senha atual para confirmação")
    public ResponseEntity<ApiSuccessResponse<String>> changePassword(@PathVariable UUID id, @RequestBody @Valid ChangePasswordRequest request) {
        changePasswordUseCase.execute(id, adapterMapper.toCommand(request));
        return presenter.okMessage("Senha alterada com sucesso");
    }
}

package br.com.fiap.techfood.adapters.controller;

import br.com.fiap.techfood.adapters.mapper.AdapterMapper;
import br.com.fiap.techfood.adapters.presenter.ApiResponseFactory;
import br.com.fiap.techfood.adapters.request.UserPatchRequest;
import br.com.fiap.techfood.adapters.request.UserRequest;
import br.com.fiap.techfood.adapters.response.UserResponse;
import br.com.fiap.techfood.adapters.response.api.ApiSuccessResponse;
import br.com.fiap.techfood.application.dto.UserResult;
import br.com.fiap.techfood.application.usecase.createuser.CreateUserUseCase;
import br.com.fiap.techfood.application.usecase.user.DeleteUserUseCase;
import br.com.fiap.techfood.application.usecase.user.FindUserByIdUseCase;
import br.com.fiap.techfood.application.usecase.user.PatchUserUseCase;
import br.com.fiap.techfood.application.usecase.user.SearchUsersByNameUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
@Tag(name = "Users", description = "Gerenciamento de usuários")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final PatchUserUseCase patchUserUseCase;
    private final FindUserByIdUseCase findUserByIdUseCase;
    private final SearchUsersByNameUseCase searchUsersByNameUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final AdapterMapper adapterMapper;
    private final ApiResponseFactory presenter;

    public UserController(CreateUserUseCase createUserUseCase,
                          PatchUserUseCase patchUserUseCase,
                          FindUserByIdUseCase findUserByIdUseCase,
                          SearchUsersByNameUseCase searchUsersByNameUseCase,
                          DeleteUserUseCase deleteUserUseCase,
                          AdapterMapper adapterMapper,
                          ApiResponseFactory presenter) {
        this.createUserUseCase = createUserUseCase;
        this.patchUserUseCase = patchUserUseCase;
        this.findUserByIdUseCase = findUserByIdUseCase;
        this.searchUsersByNameUseCase = searchUsersByNameUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.adapterMapper = adapterMapper;
        this.presenter = presenter;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID", description = "Retorna os dados de um usuário a partir do seu ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<ApiSuccessResponse<UserResponse>> findById(@PathVariable UUID id) {
        UserResult result = findUserByIdUseCase.execute(id);
        return presenter.ok(adapterMapper.toResponse(result));
    }

    @GetMapping
    @Operation(summary = "Buscar usuários por nome", description = "Retorna uma lista paginada de usuários cujo nome contenha o termo informado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    public ResponseEntity<ApiSuccessResponse<java.util.List<UserResponse>>> searchByName(
            @Parameter(description = "Nome ou parte do nome do usuário", example = "João")
            @RequestParam String name,
            @Parameter(description = "Número da página (começa em 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Quantidade de itens por página", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Page<UserResult> result = searchUsersByNameUseCase.execute(name, PageRequest.of(page, size));
        Page<UserResponse> response = result.map(adapterMapper::toResponse);
        return presenter.okPage(response);
    }

    @PostMapping
    @Operation(summary = "Criar usuário", description = "Cadastra um novo usuário do tipo CUSTOMER ou RESTAURANT_OWNER")
    public ResponseEntity<ApiSuccessResponse<UserResponse>> create(@RequestBody @Valid UserRequest request) {
        UserResult result = createUserUseCase.execute(adapterMapper.toCommand(request));
        return presenter.created(adapterMapper.toResponse(result), "Usuário criado com sucesso");
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar dados do usuário", description = "Atualiza parcialmente os dados do usuário, exceto senha")
    public ResponseEntity<ApiSuccessResponse<UserResponse>> patch(@PathVariable UUID id, @RequestBody @Valid UserPatchRequest request) {
        UserResult result = patchUserUseCase.execute(id, adapterMapper.toCommand(request));
        return presenter.ok(adapterMapper.toResponse(result));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover usuário", description = "Exclui permanentemente um usuário pelo seu ID")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteUserUseCase.execute(id);
        return presenter.noContent();
    }
}

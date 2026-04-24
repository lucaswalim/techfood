package br.com.fiap.techfood.controller;

import br.com.fiap.techfood.dto.request.UserPatchDTO;
import br.com.fiap.techfood.dto.request.UserRequestDTO;
import br.com.fiap.techfood.dto.response.UserResponseDTO;
import br.com.fiap.techfood.dto.response.api.ApiSuccessResponse;
import br.com.fiap.techfood.dto.response.api.Meta;
import br.com.fiap.techfood.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
@Tag(name = "Users", description = "Gerenciamento de usuários")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<ApiSuccessResponse<UserResponseDTO>> findById(@PathVariable UUID id) {
        UserResponseDTO response = service.findById(id);
        return ApiSuccessResponse.ok(response);
    }

    @GetMapping
    @Operation(summary = "Buscar usuários por nome")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso")
    })
    public ResponseEntity<ApiSuccessResponse<List<UserResponseDTO>>> searchByName(
            @Parameter(description = "Nome ou parte do nome do usuário", example = "João")
            @RequestParam String name,
            @Parameter(description = "Número da página (começa em 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Quantidade de itens por página", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserResponseDTO> result = service.searchByName(name, pageable);
        return ApiSuccessResponse.ok(result.getContent(), Meta.from(result));
    }

    @PostMapping
    @Operation(summary = "Criar usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Usuário já existe")
    })
    public ResponseEntity<ApiSuccessResponse<UserResponseDTO>> create(@RequestBody @Valid UserRequestDTO dto) {
        UserResponseDTO response = service.create(dto);
        return ApiSuccessResponse.created(response, "Usuário criado com sucesso");
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar parcialmente um usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados")
    })
    public ResponseEntity<ApiSuccessResponse<UserResponseDTO>> patch(@PathVariable UUID id, @RequestBody @Valid UserPatchDTO dto) {
        UserResponseDTO response = service.patch(id, dto);
        return ApiSuccessResponse.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ApiSuccessResponse.noContent();
    }
}

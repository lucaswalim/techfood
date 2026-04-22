package br.com.fiap.techfood.controller;

import br.com.fiap.techfood.dto.request.UserPatchDTO;
import br.com.fiap.techfood.dto.request.UserRequestDTO;
import br.com.fiap.techfood.dto.response.api.ApiSuccessResponse;
import br.com.fiap.techfood.dto.response.UserResponseDTO;
import br.com.fiap.techfood.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Gerenciamento de usuários")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Usuário já existe")
    })
    public ApiSuccessResponse<UserResponseDTO> create(@RequestBody @Valid UserRequestDTO dto) {
        UserResponseDTO response = service.create(dto);
        return ApiSuccessResponse.success("Usuário criado com sucesso", response);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar parcialmente um usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados")
    })
    public ApiSuccessResponse<UserResponseDTO> patch(
            @PathVariable UUID id,
            @RequestBody @Valid UserPatchDTO dto) {

        UserResponseDTO response = service.patch(id, dto);
        return ApiSuccessResponse.success("Usuário atualizado com sucesso", response);
    }
}

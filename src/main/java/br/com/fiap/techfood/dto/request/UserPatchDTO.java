package br.com.fiap.techfood.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para atualização parcial do usuário")
public record UserPatchDTO(
    @Size(min = 3, max = 255)
    @Schema(example = "João Silva")
    String name,

    @Email(message = "Email inválido")
    @Schema(example = "joao@email.com")
    String email,

    @Size(min = 3, max = 255)
    @Schema(example = "joao123")
    String login,

    @Valid
    AddressDTO address
) {
    
}

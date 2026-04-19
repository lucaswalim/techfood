package br.com.fiap.techfood.dto.request;

import br.com.fiap.techfood.model.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação de usuário")
public record UserRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 255)
        @Schema(example = "João Silva")
        String name,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        @Schema(example = "joao@email.com")
        String email,

        @NotBlank(message = "Login é obrigatório")
        @Size(min = 3, max = 255)
        @Schema(example = "joao123")
        String login,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, max = 255)
        @Schema(example = "123456")
        String password,

        @Valid
        @NotNull(message = "Endereço é obrigatório")
        AddressDTO address,

        @NotNull(message = "Tipo de usuário é obrigatório")
        @Schema(example = "CUSTOMER")
        UserType type
) {
}
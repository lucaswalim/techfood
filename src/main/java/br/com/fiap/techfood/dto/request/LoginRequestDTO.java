package br.com.fiap.techfood.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO de autenticação de usuário")
public record LoginRequestDTO(

        @NotBlank(message = "Login é obrigatório")
        @Size(min = 3, max = 255)
        @Schema(example = "joao123")
        String login,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, max = 255)
        @Schema(example = "123456")
        String password
) {
}

package br.com.fiap.techfood.dto.response;

import br.com.fiap.techfood.dto.request.AddressDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Dados retornados do usuário")
public record UserResponseDTO(

        @Schema(example = "b3fa85e7-9c9b-4c2a-a9c3-123456789abc")
        UUID id,

        @Schema(example = "João Silva")
        String name,

        @Schema(example = "joao@email.com")
        String email,

        @Schema(example = "joao123")
        String login,

        AddressDTO address
) {
}
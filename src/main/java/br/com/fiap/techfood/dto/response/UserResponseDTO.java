package br.com.fiap.techfood.dto.response;

import br.com.fiap.techfood.dto.request.AddressDTO;
import br.com.fiap.techfood.model.UserType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
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

        @Schema(example = "CUSTOMER")
        UserType type,

        AddressDTO address,

        @Schema(example = "2024-01-15T10:30:00")
        LocalDateTime lastUpdatedAt
) {
}

package br.com.fiap.techfood.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Token JWT de autenticação")
public record TokenResponseDTO(

        @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2FvMTIzIn0.abc")
        String token,

        @Schema(example = "Bearer")
        String type
) {
    public TokenResponseDTO(String token) {
        this(token, "Bearer");
    }
}

package br.com.fiap.techfood.adapters.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Token JWT de autenticação")
public record TokenResponse(
        @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2FvMTIzIn0.abc")
        String token,

        @Schema(example = "Bearer")
        String type
) {
}

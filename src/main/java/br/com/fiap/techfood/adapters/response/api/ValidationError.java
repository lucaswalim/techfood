package br.com.fiap.techfood.adapters.response.api;

public record ValidationError(
        String field,
        String message
) {
}

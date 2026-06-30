package br.com.fiap.techfood.application.dto;

public record TokenResult(
        String token,
        String type
) {
    public TokenResult(String token) {
        this(token, "Bearer");
    }
}

package br.com.fiap.techfood.application.dto;

public record LoginCommand(
        String login,
        String password
) {
}

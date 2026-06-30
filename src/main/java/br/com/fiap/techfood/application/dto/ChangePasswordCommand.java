package br.com.fiap.techfood.application.dto;

public record ChangePasswordCommand(
        String login,
        String currentPassword,
        String newPassword
) {
}

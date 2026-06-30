package br.com.fiap.techfood.application.dto;

public record UserPatchCommand(
        String name,
        String email,
        String login,
        AddressData address
) {
}

package br.com.fiap.techfood.application.dto;

import br.com.fiap.techfood.model.UserType;

public record UserCreateCommand(
        String name,
        String email,
        String login,
        String password,
        AddressData address,
        UserType type
) {
}

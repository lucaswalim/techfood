package br.com.fiap.techfood.application.dto;

import br.com.fiap.techfood.domain.user.UserType;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResult(
        UUID id,
        String name,
        String email,
        String login,
        UserType type,
        AddressData address,
        LocalDateTime lastUpdatedAt
) {
}

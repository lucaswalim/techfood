package br.com.fiap.techfood.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class User {

    private UUID id;
    private String name;
    private String email;
    private String login;
    private String password;
    private LocalDateTime lastUpdatedAt;
    private Address address;

    public abstract UserType getType();
}

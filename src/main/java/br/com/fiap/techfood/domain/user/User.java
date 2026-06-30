package br.com.fiap.techfood.domain.user;

import br.com.fiap.techfood.domain.valueobject.Address;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class User {

    private UUID id;
    private String name;
    private String email;
    private String login;
    private String password;
    private LocalDateTime lastUpdatedAt;
    private Address address;

    protected User() {
    }

    protected User(UUID id, String name, String email, String login, String password,
                   LocalDateTime lastUpdatedAt, Address address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.login = login;
        this.password = password;
        this.lastUpdatedAt = lastUpdatedAt;
        this.address = address;
    }

    public abstract UserType getType();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(LocalDateTime lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void touch(LocalDateTime updatedAt) {
        this.lastUpdatedAt = updatedAt;
    }
}

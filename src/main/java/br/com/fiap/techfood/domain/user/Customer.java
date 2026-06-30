package br.com.fiap.techfood.domain.user;

import br.com.fiap.techfood.domain.valueobject.Address;

import java.time.LocalDateTime;
import java.util.UUID;

public class Customer extends User {

    public Customer() {
    }

    public Customer(UUID id, String name, String email, String login, String password,
                    LocalDateTime lastUpdatedAt, Address address) {
        super(id, name, email, login, password, lastUpdatedAt, address);
    }

    @Override
    public UserType getType() {
        return UserType.CUSTOMER;
    }
}

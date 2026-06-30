package br.com.fiap.techfood.domain.user;

import br.com.fiap.techfood.domain.valueobject.Address;

import java.time.LocalDateTime;
import java.util.UUID;

public class RestaurantOwner extends User {

    public RestaurantOwner() {
    }

    public RestaurantOwner(UUID id, String name, String email, String login, String password,
                           LocalDateTime lastUpdatedAt, Address address) {
        super(id, name, email, login, password, lastUpdatedAt, address);
    }

    @Override
    public UserType getType() {
        return UserType.RESTAURANT_OWNER;
    }
}

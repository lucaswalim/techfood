package br.com.fiap.techfood.infrastructure.persistence.mapper;

import br.com.fiap.techfood.infrastructure.persistence.entity.AddressEmbeddable;
import br.com.fiap.techfood.infrastructure.persistence.entity.CustomerEntity;
import br.com.fiap.techfood.infrastructure.persistence.entity.RestaurantOwnerEntity;
import br.com.fiap.techfood.infrastructure.persistence.entity.UserEntity;
import br.com.fiap.techfood.model.Address;
import br.com.fiap.techfood.model.Customer;
import br.com.fiap.techfood.model.RestaurantOwner;
import br.com.fiap.techfood.model.User;
import br.com.fiap.techfood.model.UserType;
import org.springframework.stereotype.Component;

@Component
public class PersistenceUserMapper {

    public User toModel(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        User model = switch (entity.getType()) {
            case CUSTOMER -> Customer.builder().build();
            case RESTAURANT_OWNER -> RestaurantOwner.builder().build();
        };

        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setEmail(entity.getEmail());
        model.setLogin(entity.getLogin());
        model.setPassword(entity.getPassword());
        model.setLastUpdatedAt(entity.getLastUpdatedAt());
        model.setAddress(toModelAddress(entity.getAddress()));
        return model;
    }

    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        UserEntity entity = createEntity(user.getType());
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setLogin(user.getLogin());
        entity.setPassword(user.getPassword());
        entity.setLastUpdatedAt(user.getLastUpdatedAt());
        entity.setAddress(toEmbeddable(user.getAddress()));
        return entity;
    }

    private UserEntity createEntity(UserType type) {
        return switch (type) {
            case CUSTOMER -> new CustomerEntity();
            case RESTAURANT_OWNER -> new RestaurantOwnerEntity();
        };
    }

    private Address toModelAddress(AddressEmbeddable embeddable) {
        if (embeddable == null) {
            return null;
        }

        return Address.builder()
                .street(embeddable.getStreet())
                .number(embeddable.getNumber())
                .city(embeddable.getCity())
                .zipCode(embeddable.getZipCode())
                .build();
    }

    private AddressEmbeddable toEmbeddable(Address address) {
        if (address == null) {
            return null;
        }

        AddressEmbeddable embeddable = new AddressEmbeddable();
        embeddable.setStreet(address.getStreet());
        embeddable.setNumber(address.getNumber());
        embeddable.setCity(address.getCity());
        embeddable.setZipCode(address.getZipCode());
        return embeddable;
    }
}

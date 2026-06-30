package br.com.fiap.techfood.application.mapper;

import br.com.fiap.techfood.application.dto.AddressData;
import br.com.fiap.techfood.application.dto.UserCreateCommand;
import br.com.fiap.techfood.application.dto.UserResult;
import br.com.fiap.techfood.model.Address;
import br.com.fiap.techfood.model.Customer;
import br.com.fiap.techfood.model.RestaurantOwner;
import br.com.fiap.techfood.model.User;
import org.springframework.stereotype.Component;

@Component
public class ApplicationUserMapper {

    public User toModel(UserCreateCommand command) {
        User user = switch (command.type()) {
            case CUSTOMER -> Customer.builder().build();
            case RESTAURANT_OWNER -> RestaurantOwner.builder().build();
        };

        user.setName(trim(command.name()));
        user.setEmail(trim(command.email()));
        user.setLogin(trim(command.login()));
        user.setAddress(toModel(command.address()));
        return user;
    }

    public Address toModel(AddressData data) {
        if (data == null) {
            return null;
        }

        return Address.builder()
                .street(trim(data.street()))
                .number(trim(data.number()))
                .city(trim(data.city()))
                .zipCode(trim(data.zipCode()))
                .build();
    }

    public UserResult toResult(User user) {
        return new UserResult(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getType(),
                toData(user.getAddress()),
                user.getLastUpdatedAt()
        );
    }

    public AddressData toData(Address address) {
        if (address == null) {
            return null;
        }

        return new AddressData(address.getStreet(), address.getNumber(), address.getCity(), address.getZipCode());
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}

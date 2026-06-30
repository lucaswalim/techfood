package br.com.fiap.techfood.application.mapper;

import br.com.fiap.techfood.application.dto.AddressData;
import br.com.fiap.techfood.application.dto.UserCreateCommand;
import br.com.fiap.techfood.application.dto.UserResult;
import br.com.fiap.techfood.domain.user.Customer;
import br.com.fiap.techfood.domain.user.RestaurantOwner;
import br.com.fiap.techfood.domain.user.User;
import br.com.fiap.techfood.domain.valueobject.Address;

public class ApplicationUserMapper {

    public User toModel(UserCreateCommand command) {
        User user = switch (command.type()) {
            case CUSTOMER -> new Customer();
            case RESTAURANT_OWNER -> new RestaurantOwner();
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

        return new Address(
                trim(data.street()),
                trim(data.number()),
                trim(data.city()),
                trim(data.zipCode())
        );
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

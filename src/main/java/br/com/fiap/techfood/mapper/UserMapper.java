package br.com.fiap.techfood.mapper;

import br.com.fiap.techfood.dto.request.AddressDTO;
import br.com.fiap.techfood.dto.request.UserRequestDTO;
import br.com.fiap.techfood.dto.response.UserResponseDTO;
import br.com.fiap.techfood.model.Address;
import br.com.fiap.techfood.model.Customer;
import br.com.fiap.techfood.model.RestaurantOwner;
import br.com.fiap.techfood.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    default User toEntity(UserRequestDTO dto) {
        return switch (dto.type()) {
            case CUSTOMER -> toCustomer(dto);
            case RESTAURANT_OWNER -> toOwner(dto);
        };
    }

    default String trim(String value) {
        return value == null ? null : value.trim();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastUpdatedAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "email", expression = "java(trim(dto.email()))")
    @Mapping(target = "login", expression = "java(trim(dto.login()))")
    @Mapping(target = "name", expression = "java(trim(dto.name()))")
    Customer toCustomer(UserRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lastUpdatedAt", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "email", expression = "java(trim(dto.email()))")
    @Mapping(target = "login", expression = "java(trim(dto.login()))")
    @Mapping(target = "name", expression = "java(trim(dto.name()))")
    RestaurantOwner toOwner(UserRequestDTO dto);

    UserResponseDTO toResponse(User user);

    @Mapping(target = "street", expression = "java(trim(dto.street()))")
    @Mapping(target = "city", expression = "java(trim(dto.city()))")
    @Mapping(target = "number", expression = "java(trim(dto.number()))")
    @Mapping(target = "zipCode", expression = "java(trim(dto.zipCode()))")
    Address toAddress(AddressDTO dto);

    AddressDTO toAddressDTO(Address address);
}
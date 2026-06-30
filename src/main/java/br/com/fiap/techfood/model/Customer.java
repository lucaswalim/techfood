package br.com.fiap.techfood.model;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
public class Customer extends User {

    @Override
    public UserType getType() {
        return UserType.CUSTOMER;
    }
}

package br.com.fiap.techfood.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("RESTAURANT_OWNER")
@SuperBuilder
@NoArgsConstructor
public class RestaurantOwner extends User {

    @Override
    public UserType getType() {
        return UserType.RESTAURANT_OWNER;
    }
}

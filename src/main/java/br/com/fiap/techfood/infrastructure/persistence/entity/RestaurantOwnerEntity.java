package br.com.fiap.techfood.infrastructure.persistence.entity;

import br.com.fiap.techfood.model.UserType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("RESTAURANT_OWNER")
public class RestaurantOwnerEntity extends UserEntity {

    @Override
    public UserType getType() {
        return UserType.RESTAURANT_OWNER;
    }
}

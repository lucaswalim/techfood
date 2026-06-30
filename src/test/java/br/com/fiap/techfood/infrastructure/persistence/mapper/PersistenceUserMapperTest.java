package br.com.fiap.techfood.infrastructure.persistence.mapper;

import br.com.fiap.techfood.domain.user.Customer;
import br.com.fiap.techfood.domain.user.User;
import br.com.fiap.techfood.domain.user.UserType;
import br.com.fiap.techfood.domain.valueobject.Address;
import br.com.fiap.techfood.infrastructure.persistence.entity.CustomerEntity;
import br.com.fiap.techfood.infrastructure.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PersistenceUserMapperTest {

    private final PersistenceUserMapper mapper = new PersistenceUserMapper();

    @Test
    void shouldMapModelToEntityAndBack() {
        UUID id = UUID.randomUUID();
        Customer model = new Customer(
                id,
                "Maria",
                "maria@email.com",
                "maria123",
                "hashed",
                LocalDateTime.of(2026, 1, 10, 8, 30),
                new Address("Rua A", "10", "Sao Paulo", "01010101")
        );

        UserEntity entity = mapper.toEntity(model);
        User remapped = mapper.toModel(entity);

        assertThat(entity).isInstanceOf(CustomerEntity.class);
        assertThat(entity.getType()).isEqualTo(UserType.CUSTOMER);
        assertThat(remapped.getType()).isEqualTo(UserType.CUSTOMER);
        assertThat(remapped.getId()).isEqualTo(id);
        assertThat(remapped.getEmail()).isEqualTo("maria@email.com");
        assertThat(remapped.getAddress().getStreet()).isEqualTo("Rua A");
    }
}

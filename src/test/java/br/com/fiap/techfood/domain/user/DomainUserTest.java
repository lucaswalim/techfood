package br.com.fiap.techfood.domain.user;

import br.com.fiap.techfood.domain.valueobject.Address;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class DomainUserTest {

    @Test
    void shouldExposeCustomerType() {
        Customer customer = new Customer();

        assertThat(customer.getType()).isEqualTo(UserType.CUSTOMER);
    }

    @Test
    void shouldExposeRestaurantOwnerType() {
        RestaurantOwner owner = new RestaurantOwner();

        assertThat(owner.getType()).isEqualTo(UserType.RESTAURANT_OWNER);
    }

    @Test
    void shouldUpdateUserStateWithoutFrameworkDependencies() {
        Address address = new Address("Main Street", "123", "Sao Paulo", "01310100");
        LocalDateTime createdAt = LocalDateTime.of(2026, 1, 10, 8, 30);
        Customer customer = new Customer(
                UUID.randomUUID(),
                "Maria",
                "maria@email.com",
                "maria123",
                "hashed-password",
                createdAt,
                address
        );

        LocalDateTime updatedAt = LocalDateTime.of(2026, 1, 11, 9, 45);
        customer.setName("Maria Silva");
        customer.setEmail("maria.silva@email.com");
        customer.touch(updatedAt);

        assertThat(customer.getName()).isEqualTo("Maria Silva");
        assertThat(customer.getEmail()).isEqualTo("maria.silva@email.com");
        assertThat(customer.getAddress()).isEqualTo(address);
        assertThat(customer.getLastUpdatedAt()).isEqualTo(updatedAt);
    }
}

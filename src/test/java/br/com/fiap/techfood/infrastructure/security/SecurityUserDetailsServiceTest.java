package br.com.fiap.techfood.infrastructure.security;

import br.com.fiap.techfood.application.gateway.UserGateway;
import br.com.fiap.techfood.domain.user.Customer;
import br.com.fiap.techfood.domain.valueobject.Address;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityUserDetailsServiceTest {

    @InjectMocks
    private SecurityUserDetailsService service;
    @Mock
    private UserGateway userGateway;

    @Test
    void shouldLoadUserByUsername() {
        Customer user = new Customer(
                UUID.randomUUID(),
                "Joao da Silva",
                "joao@email.com",
                "joao",
                "hashed",
                LocalDateTime.now(),
                new Address("Rua", "123", "Pindamonhangaba", "12345678")
        );
        when(userGateway.findByLogin("joao")).thenReturn(Optional.of(user));

        var userDetails = service.loadUserByUsername("joao");

        assertThat(userDetails.getUsername()).isEqualTo("joao");
        assertThat(userDetails.getPassword()).isEqualTo("hashed");
    }
}

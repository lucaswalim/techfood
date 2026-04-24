package br.com.fiap.techfood.security;

import br.com.fiap.techfood.model.Address;
import br.com.fiap.techfood.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private static final String SECRET =
            "dGVjaGZvb2Qtand0LXNlY3JldC1rZXktZmlhcC0yMDI0LXRlY2hjaGFsbGVuZ2U=";

    private JwtService jwtService;

    @BeforeEach
    void setup() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", SECRET);
        ReflectionTestUtils.setField(jwtService, "expirationMs", 86400000L);
    }

    private Customer buildUser() {
        return Customer.builder()
                .id(UUID.randomUUID())
                .name("João Silva")
                .email("joao@email.com")
                .login("joao123")
                .password("hashed-password")
                .address(new Address("Rua das Flores", "123", "São Paulo", "01310100"))
                .lastUpdatedAt(LocalDateTime.now())
                .build();
    }

    @Nested
    class GenerateTokenTests {
        @Test
        void shouldGenerateNonNullToken() {
            String token = jwtService.generateToken(buildUser());
            assertThat(token).isNotNull().isNotBlank();
        }

        @Test
        void shouldGenerateTokenWithThreeParts() {
            String token = jwtService.generateToken(buildUser());
            assertThat(token.split("\\.")).hasSize(3);
        }
    }

    @Nested
    class ExtractLoginTests {
        @Test
        void shouldExtractLoginFromToken() {
            Customer user = buildUser();
            String token = jwtService.generateToken(user);

            assertThat(jwtService.extractLogin(token)).isEqualTo("joao123");
        }
    }

    @Nested
    class ValidateTokenTests {
        @Test
        void shouldReturnTrueForValidToken() {
            String token = jwtService.generateToken(buildUser());
            assertThat(jwtService.isTokenValid(token)).isTrue();
        }

        @Test
        void shouldReturnFalseForTamperedToken() {
            assertThat(jwtService.isTokenValid("token.invalido.aqui")).isFalse();
        }

        @Test
        void shouldReturnFalseForBlankToken() {
            assertThat(jwtService.isTokenValid("")).isFalse();
        }

        @Test
        void shouldReturnFalseForExpiredToken() {
            ReflectionTestUtils.setField(jwtService, "expirationMs", -1000L);
            String token = jwtService.generateToken(buildUser());
            assertThat(jwtService.isTokenValid(token)).isFalse();
        }
    }
}

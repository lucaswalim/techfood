package br.com.fiap.techfood.application.usecase.auth;

import br.com.fiap.techfood.application.gateway.PasswordEncoderGateway;
import br.com.fiap.techfood.application.gateway.TokenGateway;
import br.com.fiap.techfood.application.gateway.UserGateway;
import br.com.fiap.techfood.application.usecase.login.LoginUseCase;
import br.com.fiap.techfood.dto.request.ChangePasswordDTO;
import br.com.fiap.techfood.dto.request.LoginRequestDTO;
import br.com.fiap.techfood.dto.response.TokenResponseDTO;
import br.com.fiap.techfood.exceptions.InvalidCredentialsException;
import br.com.fiap.techfood.model.Address;
import br.com.fiap.techfood.model.Customer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthUseCasesTest {

    @InjectMocks
    private LoginUseCase loginUseCase;
    @InjectMocks
    private ChangePasswordUseCase changePasswordUseCase;
    @Mock
    private UserGateway userGateway;
    @Mock
    private PasswordEncoderGateway passwordEncoderGateway;
    @Mock
    private TokenGateway tokenGateway;

    private static final UUID USER_ID = UUID.randomUUID();

    private Customer buildUser(String login, String password) {
        return Customer.builder()
                .id(USER_ID)
                .name("Joao da Silva")
                .email("joao@email.com")
                .login(login)
                .password(password)
                .address(new Address("Rua", "123", "Pindamonhangaba", "12345678"))
                .lastUpdatedAt(LocalDateTime.now())
                .build();
    }

    @Nested
    class LoginTests {
        @Test
        void shouldLoginSuccessfully() {
            LoginRequestDTO dto = new LoginRequestDTO("joao", "123");
            Customer user = buildUser("joao", "hashed");
            when(userGateway.findByLogin("joao")).thenReturn(Optional.of(user));
            when(passwordEncoderGateway.matches("123", "hashed")).thenReturn(true);
            when(tokenGateway.generateToken(user)).thenReturn("fake-jwt-token");

            TokenResponseDTO result = loginUseCase.execute(dto);

            assertThat(result.token()).isEqualTo("fake-jwt-token");
            assertThat(result.type()).isEqualTo("Bearer");
        }

        @Test
        void shouldThrowWhenUserNotFound() {
            LoginRequestDTO dto = new LoginRequestDTO("maria", "123");
            when(userGateway.findByLogin("maria")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> loginUseCase.execute(dto))
                    .isInstanceOf(InvalidCredentialsException.class)
                    .hasMessage("Usuário ou senha incorreta");
        }
    }

    @Nested
    class ChangePasswordTests {
        @Test
        void shouldChangePasswordSuccessfully() {
            ChangePasswordDTO dto = new ChangePasswordDTO("ana", "oldPass", "newPass");
            Customer user = buildUser("ana", "hashed-old");
            when(userGateway.findById(USER_ID)).thenReturn(Optional.of(user));
            when(passwordEncoderGateway.matches("oldPass", "hashed-old")).thenReturn(true);
            when(passwordEncoderGateway.encode("newPass")).thenReturn("hashed-new");

            changePasswordUseCase.execute(USER_ID, dto);

            assertThat(user.getPassword()).isEqualTo("hashed-new");
            verify(userGateway).save(user);
        }

        @Test
        void shouldThrowWhenCurrentPasswordIsWrong() {
            ChangePasswordDTO dto = new ChangePasswordDTO("ernesto", "wrongOld", "newPass");
            Customer user = buildUser("ernesto", "hashed-old");
            when(userGateway.findById(USER_ID)).thenReturn(Optional.of(user));
            when(passwordEncoderGateway.matches("wrongOld", "hashed-old")).thenReturn(false);

            assertThatThrownBy(() -> changePasswordUseCase.execute(USER_ID, dto))
                    .isInstanceOf(InvalidCredentialsException.class)
                    .hasMessage("Usuário ou senha inválidos");
        }
    }
}

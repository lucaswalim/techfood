package br.com.fiap.techfood.application.usecase.auth;

import br.com.fiap.techfood.application.dto.ChangePasswordCommand;
import br.com.fiap.techfood.application.dto.LoginCommand;
import br.com.fiap.techfood.application.dto.TokenResult;
import br.com.fiap.techfood.application.gateway.PasswordEncoderGateway;
import br.com.fiap.techfood.application.gateway.TokenGateway;
import br.com.fiap.techfood.application.gateway.UserGateway;
import br.com.fiap.techfood.application.usecase.login.LoginUseCase;
import br.com.fiap.techfood.domain.user.Customer;
import br.com.fiap.techfood.domain.valueobject.Address;
import br.com.fiap.techfood.application.exceptions.InvalidCredentialsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        return new Customer(
                USER_ID,
                "Joao da Silva",
                "joao@email.com",
                login,
                password,
                LocalDateTime.now(),
                new Address("Rua", "123", "Pindamonhangaba", "12345678")
        );
    }

    @Test
    void shouldLoginSuccessfully() {
        LoginCommand command = new LoginCommand("joao", "123");
        Customer user = buildUser("joao", "hashed");
        when(userGateway.findByLogin("joao")).thenReturn(Optional.of(user));
        when(passwordEncoderGateway.matches("123", "hashed")).thenReturn(true);
        when(tokenGateway.generateToken(user)).thenReturn("fake-jwt-token");

        TokenResult result = loginUseCase.execute(command);

        assertThat(result.token()).isEqualTo("fake-jwt-token");
        assertThat(result.type()).isEqualTo("Bearer");
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        ChangePasswordCommand command = new ChangePasswordCommand("ana", "oldPass", "newPass");
        Customer user = buildUser("ana", "hashed-old");
        when(userGateway.findById(USER_ID)).thenReturn(Optional.of(user));
        when(passwordEncoderGateway.matches("oldPass", "hashed-old")).thenReturn(true);
        when(passwordEncoderGateway.encode("newPass")).thenReturn("hashed-new");

        changePasswordUseCase.execute(USER_ID, command);

        assertThat(user.getPassword()).isEqualTo("hashed-new");
        verify(userGateway).save(user);
    }

    @Test
    void shouldThrowWhenCurrentPasswordIsWrong() {
        ChangePasswordCommand command = new ChangePasswordCommand("ernesto", "wrongOld", "newPass");
        Customer user = buildUser("ernesto", "hashed-old");
        when(userGateway.findById(USER_ID)).thenReturn(Optional.of(user));
        when(passwordEncoderGateway.matches("wrongOld", "hashed-old")).thenReturn(false);

        assertThatThrownBy(() -> changePasswordUseCase.execute(USER_ID, command))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessage("Usuário ou senha inválidos");
    }
}

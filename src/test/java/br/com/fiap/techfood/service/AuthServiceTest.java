package br.com.fiap.techfood.service;

import br.com.fiap.techfood.application.dto.ChangePasswordCommand;
import br.com.fiap.techfood.application.dto.LoginCommand;
import br.com.fiap.techfood.application.dto.TokenResult;
import br.com.fiap.techfood.application.gateway.UserGateway;
import br.com.fiap.techfood.application.usecase.auth.ChangePasswordUseCase;
import br.com.fiap.techfood.application.usecase.login.LoginUseCase;
import br.com.fiap.techfood.model.Address;
import br.com.fiap.techfood.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthServiceImpl service;
    @Mock
    private UserGateway userGateway;
    @Mock
    private LoginUseCase loginUseCase;
    @Mock
    private ChangePasswordUseCase changePasswordUseCase;

    private Customer buildUser(String login, String password) {
        return Customer.builder()
                .id(UUID.randomUUID())
                .name("Joao da Silva")
                .email("joao@email.com")
                .login(login)
                .password(password)
                .address(new Address("Rua", "123", "Pindamonhangaba", "12345678"))
                .lastUpdatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void shouldDelegateLogin() {
        LoginCommand command = new LoginCommand("joao", "123");
        TokenResult response = new TokenResult("fake-jwt-token");
        when(loginUseCase.execute(command)).thenReturn(response);

        TokenResult result = service.login(command);

        assertThat(result).isEqualTo(response);
        verify(loginUseCase).execute(command);
    }

    @Test
    void shouldDelegateChangePassword() {
        UUID userId = UUID.randomUUID();
        ChangePasswordCommand command = new ChangePasswordCommand("joao", "oldPass", "newPass");
        doNothing().when(changePasswordUseCase).execute(userId, command);

        service.changePassword(userId, command);

        verify(changePasswordUseCase).execute(userId, command);
    }

    @Test
    void shouldLoadUserByUsername() {
        Customer user = buildUser("joao", "hashed");
        when(userGateway.findByLogin("joao")).thenReturn(Optional.of(user));

        var userDetails = service.loadUserByUsername("joao");

        assertThat(userDetails.getUsername()).isEqualTo("joao");
        assertThat(userDetails.getPassword()).isEqualTo("hashed");
    }
}

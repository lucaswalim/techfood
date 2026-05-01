package br.com.fiap.techfood.service;

import br.com.fiap.techfood.dto.request.ChangePasswordDTO;
import br.com.fiap.techfood.dto.request.LoginRequestDTO;
import br.com.fiap.techfood.dto.response.TokenResponseDTO;
import br.com.fiap.techfood.exceptions.InvalidCredentialsException;
import br.com.fiap.techfood.model.Address;
import br.com.fiap.techfood.model.Customer;
import br.com.fiap.techfood.repository.UserRepository;
import br.com.fiap.techfood.security.JwtService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthServiceImpl service;
    @Mock
    private UserRepository repository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

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

            when(repository.findByLogin("joao")).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("123", "hashed")).thenReturn(true);
            when(jwtService.generateToken(user)).thenReturn("fake-jwt-token");

            TokenResponseDTO result = service.login(dto);

            assertThat(result.token()).isEqualTo("fake-jwt-token");
            assertThat(result.type()).isEqualTo("Bearer");
            verify(repository).findByLogin("joao");
            verify(passwordEncoder).matches("123", "hashed");
            verify(jwtService).generateToken(user);
        }

        @Test
        void shouldThrowWhenUserNotFound() {
            LoginRequestDTO dto = new LoginRequestDTO("maria", "123");

            when(repository.findByLogin("maria")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.login(dto))
                    .isInstanceOf(InvalidCredentialsException.class)
                    .hasMessage("Usuário ou senha incorreta");
        }

        @Test
        void shouldThrowWhenPasswordIsWrong() {
            LoginRequestDTO dto = new LoginRequestDTO("pedro", "wrong");
            Customer user = buildUser("pedro", "hashed");

            when(repository.findByLogin("pedro")).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

            assertThatThrownBy(() -> service.login(dto))
                    .isInstanceOf(InvalidCredentialsException.class)
                    .hasMessage("Usuário ou senha incorreta");
        }
    }

    @Nested
    class LoadUserByUsernameTests {
        @Test
        void shouldLoadUserByUsername() {
            Customer user = buildUser("joao", "hashed");
            when(repository.findByLogin("joao")).thenReturn(Optional.of(user));

            var userDetails = service.loadUserByUsername("joao");

            assertThat(userDetails.getUsername()).isEqualTo("joao");
            assertThat(userDetails.getPassword()).isEqualTo("hashed");
        }

        @Test
        void shouldThrowWhenUserNotFoundOnLoad() {
            when(repository.findByLogin("desconhecido")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.loadUserByUsername("desconhecido"))
                    .isInstanceOf(org.springframework.security.core.userdetails.UsernameNotFoundException.class);
        }
    }

    @Nested
    class ChangePasswordTests {
        @Test
        void shouldChangePasswordSuccessfully() {
            ChangePasswordDTO dto = new ChangePasswordDTO("ana", "oldPass", "newPass");
            Customer user = buildUser("ana", "hashed-old");

            when(repository.findById(USER_ID)).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("oldPass", "hashed-old")).thenReturn(true);
            when(passwordEncoder.encode("newPass")).thenReturn("hashed-new");

            service.changePassword(USER_ID, dto);

            assertThat(user.getPassword()).isEqualTo("hashed-new");
            verify(repository).save(user);
        }

        @Test
        void shouldThrowWhenUserNotFound() {
            ChangePasswordDTO dto = new ChangePasswordDTO("tiago", "oldPass", "newPass");

            when(repository.findById(USER_ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.changePassword(USER_ID, dto))
                    .isInstanceOf(InvalidCredentialsException.class)
                    .hasMessage("Usuário ou senha inválidos");
        }

        @Test
        void shouldThrowWhenLoginMismatch() {
            ChangePasswordDTO dto = new ChangePasswordDTO("wrongLogin", "oldPass", "newPass");
            Customer user = buildUser("liandra", "hashed-old");

            when(repository.findById(USER_ID)).thenReturn(Optional.of(user));

            assertThatThrownBy(() -> service.changePassword(USER_ID, dto))
                    .isInstanceOf(InvalidCredentialsException.class)
                    .hasMessage("Login informado não corresponde ao usuário");
        }

        @Test
        void shouldThrowWhenCurrentPasswordIsWrong() {
            ChangePasswordDTO dto = new ChangePasswordDTO("ernesto", "wrongOld", "newPass");
            Customer user = buildUser("ernesto", "hashed-old");

            when(repository.findById(USER_ID)).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("wrongOld", "hashed-old")).thenReturn(false);

            assertThatThrownBy(() -> service.changePassword(USER_ID, dto))
                    .isInstanceOf(InvalidCredentialsException.class)
                    .hasMessage("Usuário ou senha inválidos");
        }
    }
}
package br.com.fiap.techfood.service;

import br.com.fiap.techfood.dto.request.UserRequestDTO;
import br.com.fiap.techfood.dto.response.UserResponseDTO;
import br.com.fiap.techfood.exceptions.ResourceAlreadyExistsException;
import br.com.fiap.techfood.mapper.UserMapper;
import br.com.fiap.techfood.model.User;
import br.com.fiap.techfood.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService service;
    @Mock
    private UserRepository repository;
    @Mock
    private UserMapper mapper;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private UserRequestDTO dto;
    private User user;
    private User savedUser;
    private UserResponseDTO responseDTO;

    @BeforeEach
    void setup() {
        dto = mock(UserRequestDTO.class);
        user = mock(User.class);
        savedUser = mock(User.class);
        responseDTO = mock(UserResponseDTO.class);
    }

    @Nested
    class CreateUserTests {
        @Test
        void shouldCreateUserSuccessfully() {
            when(dto.email()).thenReturn("test@email.com");
            when(dto.login()).thenReturn("testlogin");
            when(dto.password()).thenReturn("123456");

            when(repository.existsByEmail("test@email.com")).thenReturn(false);
            when(repository.existsByLogin("testlogin")).thenReturn(false);

            when(mapper.toEntity(dto)).thenReturn(user);
            when(passwordEncoder.encode("123456")).thenReturn("hashed-password");
            when(repository.save(user)).thenReturn(savedUser);
            when(mapper.toResponse(savedUser)).thenReturn(responseDTO);

            UserResponseDTO result = service.create(dto);

            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(responseDTO);

            verify(repository).existsByEmail("test@email.com");
            verify(repository).existsByLogin("testlogin");
            verify(passwordEncoder).encode("123456");
            verify(repository).save(user);
            verify(mapper).toResponse(savedUser);
        }

        @Test
        void shouldThrowExceptionWhenEmailAlreadyExists() {
            when(dto.email()).thenReturn("test@email.com");
            when(repository.existsByEmail("test@email.com")).thenReturn(true);

            assertThatThrownBy(() -> service.create(dto))
                    .isInstanceOf(ResourceAlreadyExistsException.class)
                    .hasMessage("Email já cadastrado");

            verify(repository, never()).save(any());
            verify(repository, never()).existsByLogin(any());
        }

        @Test
        void shouldThrowExceptionWhenLoginAlreadyExists() {
            when(dto.email()).thenReturn("test@email.com");
            when(dto.login()).thenReturn("testlogin");

            when(repository.existsByEmail("test@email.com")).thenReturn(false);
            when(repository.existsByLogin("testlogin")).thenReturn(true);

            assertThatThrownBy(() -> service.create(dto))
                    .isInstanceOf(ResourceAlreadyExistsException.class)
                    .hasMessage("Login já cadastrado");

            verify(repository, never()).save(any());
        }

        @Test
        void shouldEncodePasswordBeforeSaving() {
            when(dto.email()).thenReturn("test@email.com");
            when(dto.login()).thenReturn("testlogin");
            when(dto.password()).thenReturn("123456");

            when(repository.existsByEmail(any())).thenReturn(false);
            when(repository.existsByLogin(any())).thenReturn(false);

            when(mapper.toEntity(dto)).thenReturn(user);
            when(passwordEncoder.encode("123456")).thenReturn("encrypted");
            when(repository.save(user)).thenReturn(savedUser);
            when(mapper.toResponse(savedUser)).thenReturn(responseDTO);

            service.create(dto);

            verify(passwordEncoder).encode("123456");
            verify(user).setPassword("encrypted");
        }
    }

    @Nested
    class DeleteUserTests {

        UUID userId = UUID.randomUUID();

        @Test
        void shouldDeleteUserSuccessfully() {
            when(repository.existsById(userId)).thenReturn(true);

            service.delete(userId);

            verify(repository).deleteById(userId);
        }

        @Test
        void shouldThrowExceptionWhenUserNotFoundOnDelete() {
            when(repository.existsById(userId)).thenReturn(false);

            assertThatThrownBy(() -> service.delete(userId))
                    .isInstanceOf(java.util.NoSuchElementException.class)
                    .hasMessage("Usuário não encontrado");

            verify(repository, never()).deleteById(any());
        }
    }
}
package br.com.fiap.techfood.application.usecase.user;

import br.com.fiap.techfood.application.dto.AddressData;
import br.com.fiap.techfood.application.dto.UserCreateCommand;
import br.com.fiap.techfood.application.dto.UserPatchCommand;
import br.com.fiap.techfood.application.dto.UserResult;
import br.com.fiap.techfood.application.gateway.PageQuery;
import br.com.fiap.techfood.application.gateway.PageResult;
import br.com.fiap.techfood.application.gateway.PasswordEncoderGateway;
import br.com.fiap.techfood.application.gateway.UserGateway;
import br.com.fiap.techfood.application.mapper.ApplicationUserMapper;
import br.com.fiap.techfood.application.usecase.createuser.CreateUserUseCase;
import br.com.fiap.techfood.domain.user.Customer;
import br.com.fiap.techfood.domain.user.User;
import br.com.fiap.techfood.domain.user.UserType;
import br.com.fiap.techfood.domain.valueobject.Address;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUseCasesTest {

    @InjectMocks
    private CreateUserUseCase createUserUseCase;
    @InjectMocks
    private PatchUserUseCase patchUserUseCase;
    @InjectMocks
    private FindUserByIdUseCase findUserByIdUseCase;
    @InjectMocks
    private SearchUsersByNameUseCase searchUsersByNameUseCase;
    @InjectMocks
    private DeleteUserUseCase deleteUserUseCase;
    @Mock
    private UserGateway userGateway;
    @Mock
    private ApplicationUserMapper mapper;
    @Mock
    private PasswordEncoderGateway passwordEncoderGateway;

    private Customer buildCustomer(UUID id, String email, String login) {
        return new Customer(
                id,
                "João Silva",
                email,
                login,
                "hashed-password",
                LocalDateTime.now(),
                new Address("Rua das Flores", "123", "São Paulo", "01310100")
        );
    }

    @Test
    void shouldCreateUserSuccessfully() {
        UserCreateCommand command = new UserCreateCommand("João", "test@email.com", "testlogin", "123456", new AddressData("Rua", "1", "SP", "01310100"), UserType.CUSTOMER);
        User user = buildCustomer(UUID.randomUUID(), "test@email.com", "testlogin");
        User savedUser = buildCustomer(UUID.randomUUID(), "test@email.com", "testlogin");
        UserResult result = new UserResult(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getLogin(), savedUser.getType(), null, savedUser.getLastUpdatedAt());

        when(userGateway.existsByEmail("test@email.com")).thenReturn(false);
        when(userGateway.existsByLogin("testlogin")).thenReturn(false);
        when(mapper.toModel(command)).thenReturn(user);
        when(passwordEncoderGateway.encode("123456")).thenReturn("hashed-password");
        when(userGateway.save(user)).thenReturn(savedUser);
        when(mapper.toResult(savedUser)).thenReturn(result);

        UserResult response = createUserUseCase.execute(command);

        assertThat(response).isEqualTo(result);
        verify(userGateway).save(user);
    }

    @Test
    void shouldFindUserById() {
        UUID id = UUID.randomUUID();
        Customer customer = buildCustomer(id, "joao@email.com", "joao123");
        UserResult result = new UserResult(id, customer.getName(), customer.getEmail(), customer.getLogin(), customer.getType(), null, customer.getLastUpdatedAt());
        when(userGateway.findById(id)).thenReturn(Optional.of(customer));
        when(mapper.toResult(customer)).thenReturn(result);

        UserResult response = findUserByIdUseCase.execute(id);

        assertThat(response).isEqualTo(result);
    }

    @Test
    void shouldReturnPageOfMatchingUsers() {
        UUID id = UUID.randomUUID();
        Customer customer = buildCustomer(id, "joao@email.com", "joao123");
        UserResult resultItem = new UserResult(id, customer.getName(), customer.getEmail(), customer.getLogin(), customer.getType(), null, customer.getLastUpdatedAt());
        PageResult<User> pageResult = new PageResult<>(List.of(customer), 1, 1, 0, 10, false, false);
        when(userGateway.searchByName(eq("João"), any(PageQuery.class))).thenReturn(pageResult);
        when(mapper.toResult(customer)).thenReturn(resultItem);

        Page<UserResult> result = searchUsersByNameUseCase.execute("João", PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    void shouldPatchAddressSuccessfully() {
        UUID id = UUID.randomUUID();
        Customer customer = buildCustomer(id, "joao@email.com", "joao123");
        UserPatchCommand command = new UserPatchCommand(null, null, null, new AddressData("Nova Rua", "456", "Rio", "20040020"));
        Address mappedAddress = new Address("Nova Rua", "456", "Rio", "20040020");
        UserResult result = new UserResult(id, customer.getName(), customer.getEmail(), customer.getLogin(), customer.getType(), null, customer.getLastUpdatedAt());
        when(userGateway.findById(id)).thenReturn(Optional.of(customer));
        when(mapper.toModel(command.address())).thenReturn(mappedAddress);
        when(userGateway.save(customer)).thenReturn(customer);
        when(mapper.toResult(customer)).thenReturn(result);

        patchUserUseCase.execute(id, command);

        assertThat(customer.getAddress()).isEqualTo(mappedAddress);
    }

    @Test
    void shouldThrowWhenUserNotFoundOnDelete() {
        UUID userId = UUID.randomUUID();
        when(userGateway.existsById(userId)).thenReturn(false);

        assertThatThrownBy(() -> deleteUserUseCase.execute(userId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Usuário não encontrado");

        verify(userGateway, never()).deleteById(any());
    }
}

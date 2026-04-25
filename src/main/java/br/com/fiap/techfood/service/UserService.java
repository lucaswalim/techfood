package br.com.fiap.techfood.service;

import br.com.fiap.techfood.dto.request.UserPatchDTO;
import br.com.fiap.techfood.dto.request.UserRequestDTO;
import br.com.fiap.techfood.dto.response.UserResponseDTO;
import br.com.fiap.techfood.exceptions.ResourceAlreadyExistsException;
import br.com.fiap.techfood.mapper.UserMapper;
import br.com.fiap.techfood.model.User;
import br.com.fiap.techfood.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, UserMapper mapper, BCryptPasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO create(UserRequestDTO dto) {
        if (repository.existsByEmail(dto.email())) {
            throw new ResourceAlreadyExistsException("Email já cadastrado");
        }

        if (repository.existsByLogin(dto.login())) {
            throw new ResourceAlreadyExistsException("Login já cadastrado");
        }

        User user = mapper.toEntity(dto);
        user.setPassword(passwordEncoder.encode(dto.password()));
        user = repository.save(user);

        return mapper.toResponse(user);
    }

    public UserResponseDTO patch(UUID id, UserPatchDTO dto) {

        User user = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));

        if (dto.email() != null && !dto.email().equals(user.getEmail())) {
            if (repository.existsByEmail(dto.email())) {
                throw new ResourceAlreadyExistsException("Email já cadastrado");
            }
            user.setEmail(dto.email());
        }

        if (dto.login() != null && !dto.login().equals(user.getLogin())) {
            if (repository.existsByLogin(dto.login())) {
                throw new ResourceAlreadyExistsException("Login já cadastrado");
            }
            user.setLogin(dto.login());
        }

        if (dto.name() != null) {
            user.setName(dto.name());
        }

        if (dto.address() != null) {
            user.setAddress(mapper.toAddress(dto.address()));
        }

        user = repository.save(user);

        return mapper.toResponse(user);
    }

    public UserResponseDTO findById(UUID id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuário não encontrado"));
        return mapper.toResponse(user);
    }

    public Page<UserResponseDTO> searchByName(String name, Pageable pageable) {
        return repository.findByNameContainingIgnoreCase(name, pageable)
                .map(mapper::toResponse);
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Usuário não encontrado");
        }

        repository.deleteById(id);
    }
}

package br.com.fiap.techfood.service;

import br.com.fiap.techfood.dto.request.ChangePasswordDTO;
import br.com.fiap.techfood.dto.request.LoginRequestDTO;
import br.com.fiap.techfood.dto.response.TokenResponseDTO;
import br.com.fiap.techfood.exceptions.InvalidCredentialsException;
import br.com.fiap.techfood.model.User;
import br.com.fiap.techfood.repository.UserRepository;
import br.com.fiap.techfood.security.JwtService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements UserDetailsService, AuthService {

    private final UserRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(UserRepository repository, BCryptPasswordEncoder passwordEncoder, JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = repository.findByLogin(login.trim())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + login));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .roles(user.getType().name())
                .build();
    }

    public TokenResponseDTO login(LoginRequestDTO dto) {
        String login = dto.login().trim();

        User user = repository.findByLogin(login)
                .orElseThrow(() -> new InvalidCredentialsException("Usuário ou senha incorreta"));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Usuário ou senha incorreta");
        }

        String token = jwtService.generateToken(user);
        return new TokenResponseDTO(token);
    }

    public void changePassword(UUID id, ChangePasswordDTO dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new InvalidCredentialsException("Usuário ou senha inválidos"));

        if (!user.getLogin().equals(dto.login())) {
            throw new InvalidCredentialsException("Login informado não corresponde ao usuário");
        }

        if (!passwordEncoder.matches(dto.currentPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Usuário ou senha inválidos");
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        repository.save(user);
    }
}

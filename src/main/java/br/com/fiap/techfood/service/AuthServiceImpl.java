package br.com.fiap.techfood.service;

import br.com.fiap.techfood.application.gateway.UserGateway;
import br.com.fiap.techfood.application.usecase.auth.ChangePasswordUseCase;
import br.com.fiap.techfood.application.usecase.login.LoginUseCase;
import br.com.fiap.techfood.dto.request.ChangePasswordDTO;
import br.com.fiap.techfood.dto.request.LoginRequestDTO;
import br.com.fiap.techfood.dto.response.TokenResponseDTO;
import br.com.fiap.techfood.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements UserDetailsService, AuthService {

    private final UserGateway userGateway;
    private final LoginUseCase loginUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;

    public AuthServiceImpl(UserGateway userGateway,
                           LoginUseCase loginUseCase,
                           ChangePasswordUseCase changePasswordUseCase) {
        this.userGateway = userGateway;
        this.loginUseCase = loginUseCase;
        this.changePasswordUseCase = changePasswordUseCase;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userGateway.findByLogin(login.trim())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + login));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .roles(user.getType().name())
                .build();
    }

    public TokenResponseDTO login(LoginRequestDTO dto) {
        return loginUseCase.execute(dto);
    }

    public void changePassword(UUID id, ChangePasswordDTO dto) {
        changePasswordUseCase.execute(id, dto);
    }
}

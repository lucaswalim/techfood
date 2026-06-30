package br.com.fiap.techfood.infrastructure.config;

import br.com.fiap.techfood.adapters.mapper.AdapterMapper;
import br.com.fiap.techfood.adapters.presenter.ApiResponseFactory;
import br.com.fiap.techfood.application.gateway.PasswordEncoderGateway;
import br.com.fiap.techfood.application.gateway.TokenGateway;
import br.com.fiap.techfood.application.gateway.UserGateway;
import br.com.fiap.techfood.application.mapper.ApplicationUserMapper;
import br.com.fiap.techfood.application.usecase.auth.ChangePasswordUseCase;
import br.com.fiap.techfood.application.usecase.createuser.CreateUserUseCase;
import br.com.fiap.techfood.application.usecase.login.LoginUseCase;
import br.com.fiap.techfood.application.usecase.user.DeleteUserUseCase;
import br.com.fiap.techfood.application.usecase.user.FindUserByIdUseCase;
import br.com.fiap.techfood.application.usecase.user.PatchUserUseCase;
import br.com.fiap.techfood.application.usecase.user.SearchUsersByNameUseCase;
import br.com.fiap.techfood.infrastructure.persistence.mapper.PersistenceUserMapper;
import br.com.fiap.techfood.infrastructure.persistence.repository.JpaUserGateway;
import br.com.fiap.techfood.infrastructure.persistence.repository.SpringDataUserRepository;
import br.com.fiap.techfood.infrastructure.security.BCryptPasswordEncoderGateway;
import br.com.fiap.techfood.infrastructure.security.JwtService;
import br.com.fiap.techfood.infrastructure.security.JwtTokenGateway;
import br.com.fiap.techfood.infrastructure.security.SecurityUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ApplicationBeanConfig {

    @Bean
    public ApplicationUserMapper applicationUserMapper() {
        return new ApplicationUserMapper();
    }

    @Bean
    public PersistenceUserMapper persistenceUserMapper() {
        return new PersistenceUserMapper();
    }

    @Bean
    public UserGateway userGateway(SpringDataUserRepository repository, PersistenceUserMapper mapper) {
        return new JpaUserGateway(repository, mapper);
    }

    @Bean
    public PasswordEncoderGateway passwordEncoderGateway(BCryptPasswordEncoder passwordEncoder) {
        return new BCryptPasswordEncoderGateway(passwordEncoder);
    }

    @Bean
    public TokenGateway tokenGateway(JwtService jwtService) {
        return new JwtTokenGateway(jwtService);
    }

    @Bean
    public CreateUserUseCase createUserUseCase(UserGateway userGateway,
                                               ApplicationUserMapper mapper,
                                               PasswordEncoderGateway passwordEncoderGateway) {
        return new CreateUserUseCase(userGateway, mapper, passwordEncoderGateway);
    }

    @Bean
    public PatchUserUseCase patchUserUseCase(UserGateway userGateway, ApplicationUserMapper mapper) {
        return new PatchUserUseCase(userGateway, mapper);
    }

    @Bean
    public FindUserByIdUseCase findUserByIdUseCase(UserGateway userGateway, ApplicationUserMapper mapper) {
        return new FindUserByIdUseCase(userGateway, mapper);
    }

    @Bean
    public SearchUsersByNameUseCase searchUsersByNameUseCase(UserGateway userGateway, ApplicationUserMapper mapper) {
        return new SearchUsersByNameUseCase(userGateway, mapper);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase(UserGateway userGateway) {
        return new DeleteUserUseCase(userGateway);
    }

    @Bean
    public LoginUseCase loginUseCase(UserGateway userGateway,
                                     PasswordEncoderGateway passwordEncoderGateway,
                                     TokenGateway tokenGateway) {
        return new LoginUseCase(userGateway, passwordEncoderGateway, tokenGateway);
    }

    @Bean
    public ChangePasswordUseCase changePasswordUseCase(UserGateway userGateway,
                                                       PasswordEncoderGateway passwordEncoderGateway) {
        return new ChangePasswordUseCase(userGateway, passwordEncoderGateway);
    }

    @Bean
    public SecurityUserDetailsService securityUserDetailsService(UserGateway userGateway) {
        return new SecurityUserDetailsService(userGateway);
    }

    @Bean
    public AdapterMapper adapterMapper() {
        return new AdapterMapper();
    }

    @Bean
    public ApiResponseFactory apiResponseFactory() {
        return new ApiResponseFactory();
    }
}

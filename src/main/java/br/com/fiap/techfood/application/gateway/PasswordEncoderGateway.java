package br.com.fiap.techfood.application.gateway;

public interface PasswordEncoderGateway {

    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}

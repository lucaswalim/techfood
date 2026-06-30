package br.com.fiap.techfood.domain.exceptions;

public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }
}

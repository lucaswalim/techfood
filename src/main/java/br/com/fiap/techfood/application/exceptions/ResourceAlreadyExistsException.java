package br.com.fiap.techfood.application.exceptions;

public class ResourceAlreadyExistsException extends BusinessException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}

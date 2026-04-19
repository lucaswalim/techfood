package br.com.fiap.techfood.exceptions;

public class ResourceAlreadyExistsException extends BusinessException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}

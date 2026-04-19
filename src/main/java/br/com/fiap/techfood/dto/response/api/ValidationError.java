package br.com.fiap.techfood.dto.response.api;

/**
 * Representa um erro de validação de campo.
 * <p>
 * Utilizado como propriedade de extensão no ProblemDetail (RFC 7807),
 * informando qual campo falhou e qual a mensagem de validação.
 *
 * @param field   campo que apresentou erro
 * @param message mensagem de validação
 */
public record ValidationError(String field, String message) {
}

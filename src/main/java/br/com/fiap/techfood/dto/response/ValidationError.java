package br.com.fiap.techfood.dto.response;

/**
 * Representa um erro de validação de campo.
 * <p>
 * Utilizado para retornar os erros gerados pelo Bean Validation
 * dentro do ApiErrorResponse, informando qual campo falhou
 * e qual a mensagem de validação.
 *
 * @param field   campo que apresentou erro
 * @param message mensagem de validação
 */
public record ValidationError(String field, String message) {
}

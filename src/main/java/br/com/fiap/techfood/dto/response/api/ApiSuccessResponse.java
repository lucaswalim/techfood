package br.com.fiap.techfood.dto.response.api;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

/**
 * DTO padrão para respostas de sucesso da API.
 *
 * <p>
 * Padroniza as respostas retornadas pela aplicação,
 * garantindo consistência entre os endpoints.
 * </p>
 *
 * @param message   Mensagem da resposta
 * @param data      Dados retornados
 * @param meta      Metadados opcionais (dados de paginação)
 * @param timestamp Data e hora da resposta
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiSuccessResponse<T>(String message, T data, Meta meta, LocalDateTime timestamp) {

    /**
     * Cria uma resposta de sucesso com dados.
     *
     * @param message Mensagem da resposta
     * @param data    Dados retornados
     * @return ApiResponse com payload
     */
    public static <T> ApiSuccessResponse<T> success(String message, T data) {
        return new ApiSuccessResponse<>(
                message,
                data,
                null,
                LocalDateTime.now()
        );
    }

    /**
     * Cria uma resposta de sucesso com dados e metadados.
     *
     * <p>
     * Normalmente utilizado em respostas paginadas.
     * </p>
     *
     * @param message Mensagem da resposta
     * @param data    Dados retornados
     * @param meta    Metadados (paginação, total, etc.)
     * @return ApiResponse com payload e meta
     */
    public static <T> ApiSuccessResponse<T> success(String message, T data, Meta meta) {
        return new ApiSuccessResponse<>(
                message,
                data,
                meta,
                LocalDateTime.now()
        );
    }

    /**
     * Cria uma resposta de sucesso sem dados.
     *
     * <p>
     * Usado normalmente em operações de:
     * </p>
     *
     * <ul>
     *     <li>Delete</li>
     *     <li>Update</li>
     *     <li>Ações sem retorno</li>
     * </ul>
     *
     * @param message Mensagem da resposta
     * @return ApiResponse sem payload
     */
    public static ApiSuccessResponse<Void> success(String message) {
        return new ApiSuccessResponse<>(
                message,
                null,
                null,
                LocalDateTime.now()
        );
    }

    /**
     * Cria uma resposta apenas com dados (sem mensagem).
     *
     * <p>
     * Usado quando a API precisa retornar apenas o conteúdo.
     * </p>
     *
     * @param data Dados retornados
     * @return ApiResponse
     */
    public static <T> ApiSuccessResponse<T> of(T data) {
        return new ApiSuccessResponse<>(
                null,
                data,
                null,
                LocalDateTime.now()
        );
    }

    /**
     * Cria uma resposta com dados e metadados sem mensagem.
     *
     * @param data Dados retornados
     * @param meta Metadados
     * @return ApiResponse
     */
    public static <T> ApiSuccessResponse<T> of(T data, Meta meta) {
        return new ApiSuccessResponse<>(
                null,
                data,
                meta,
                LocalDateTime.now()
        );
    }
}

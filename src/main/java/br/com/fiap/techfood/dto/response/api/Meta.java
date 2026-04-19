package br.com.fiap.techfood.dto.response.api;

import org.springframework.data.domain.Page;

/**
 * DTO responsável por representar metadados das respostas da API.
 *
 * <p>
 * Utilizado em respostas paginadas para fornecer
 * informações adicionais sobre os dados retornados.
 * </p>
 */
public record Meta(
        long totalElements,
        int totalPages,
        int page,
        int size,
        boolean hasNext,
        boolean hasPrevious
) {

    /**
     * Cria metadados automaticamente a partir de um Page do Spring.
     *
     * @param page Objeto Page retornado pelo Spring Data
     * @return Meta
     */
    public static Meta from(Page<?> page) {
        return new Meta(
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber(),
                page.getSize(),
                page.hasNext(),
                page.hasPrevious()
        );
    }
}
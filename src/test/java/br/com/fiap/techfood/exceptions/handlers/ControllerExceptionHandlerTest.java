package br.com.fiap.techfood.exceptions.handlers;

import br.com.fiap.techfood.config.TestSecurityConfig;
import br.com.fiap.techfood.controller.TestExceptionController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TestExceptionController.class)
@Import({ControllerExceptionHandler.class, TestSecurityConfig.class})
@AutoConfigureMockMvc(addFilters = false)
class ControllerExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturn404WhenNoSuchElementException() throws Exception {
        mockMvc.perform(get("/test/exceptions/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Recurso não encontrado"))
                .andExpect(jsonPath("$.detail").value("Usuário não encontrado"))
                .andExpect(jsonPath("$.instance").exists());
    }

    @Test
    void shouldReturn400WhenIllegalArgumentException() throws Exception {
        mockMvc.perform(get("/test/exceptions/illegal"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Requisição inválida"))
                .andExpect(jsonPath("$.detail").value("Requisição inválida"));
    }

    @Test
    void shouldReturn409WhenResourceAlreadyExists() throws Exception {
        mockMvc.perform(get("/test/exceptions/conflict"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.title").value("Recurso já existe"));
    }

    @Test
    void shouldReturn401WhenInvalidCredentials() throws Exception {
        mockMvc.perform(get("/test/exceptions/unauthorized"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.title").value("Não autorizado"));
    }

    @Test
    void shouldReturn500WhenUnexpectedException() throws Exception {
        mockMvc.perform(get("/test/exceptions/generic"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.title").value("Erro interno do servidor"));
    }

    @Test
    void shouldReturnValidationError() throws Exception {
        mockMvc.perform(post("/test/exceptions/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "",
                                  "email": "joao@gmail.com"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[?(@.field == 'name')].message").value(hasItem("Nome é obrigatório")))
                .andExpect(jsonPath("$.title").value("Erro de validação"))
                .andExpect(jsonPath("$.errors").isArray());
    }
}

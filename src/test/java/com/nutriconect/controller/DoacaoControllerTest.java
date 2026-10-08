package com.nutriconect.controller;

import com.nutriconect.dto.DoacaoDTO;
import com.nutriconect.dto.DoacaoResponseDTO;
import com.nutriconect.exception.GlobalExceptionHandler;
import com.nutriconect.exception.RecursoNaoEncontradoException;
import com.nutriconect.model.StatusDoacao;
import com.nutriconect.service.DoacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DoacaoControllerTest {

    private DoacaoService service;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(DoacaoService.class);
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mvc = MockMvcBuilders.standaloneSetup(new DoacaoController(service))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void retorna201AoCriarDoacao() throws Exception {
        when(service.registrar(any(DoacaoDTO.class))).thenReturn(
                new DoacaoResponseDTO(1L, 5.0, StatusDoacao.PENDENTE, LocalDateTime.now(), 1L, null, 2L));

        mvc.perform(post("/api/doacoes").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantidade\":5,\"doadorId\":1,\"ingredienteId\":2}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDENTE"));
    }

    @Test
    void retorna400QuandoFaltamCamposObrigatorios() throws Exception {
        mvc.perform(post("/api/doacoes").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.quantidade").exists());
    }

    @Test
    void retorna404QuandoRecursoNaoExiste() throws Exception {
        when(service.registrar(any(DoacaoDTO.class)))
                .thenThrow(new RecursoNaoEncontradoException("Doador não encontrado: 9"));

        mvc.perform(post("/api/doacoes").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantidade\":5,\"doadorId\":9,\"ingredienteId\":2}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Doador não encontrado: 9"));
    }
}

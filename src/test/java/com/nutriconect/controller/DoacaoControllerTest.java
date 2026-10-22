package com.nutriconect.controller;

import com.nutriconect.dto.DoacaoDTO;
import com.nutriconect.dto.DoacaoResponseDTO;
import com.nutriconect.exception.AcessoNegadoException;
import com.nutriconect.exception.GlobalExceptionHandler;
import com.nutriconect.exception.RecursoNaoEncontradoException;
import com.nutriconect.service.DoacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DoacaoControllerTest {

    private DoacaoService service;
    private MockMvc mvc;
    private JwtAuthenticationToken doadorLogado;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(DoacaoService.class);
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mvc = MockMvcBuilders.standaloneSetup(new DoacaoController(service))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();

        Jwt jwt = Jwt.withTokenValue("token-de-teste")
                .header("alg", "HS256")
                .claim("uid", 1L)
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60))
                .build();
        doadorLogado = new JwtAuthenticationToken(jwt);
    }

    @Test
    void retorna201AoCriarDoacaoEmNomeDoUsuarioLogado() throws Exception {
        when(service.registrar(any(DoacaoDTO.class), eq(1L))).thenReturn(
                new DoacaoResponseDTO(1L, "PENDENTE", LocalDate.now(), 1L, null,
                        List.of(new DoacaoResponseDTO.Item(2L, new BigDecimal("5")))));

        mvc.perform(post("/api/doacoes").principal(doadorLogado).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itens\":[{\"ingredienteId\":2,\"quantidade\":5}]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDENTE"))
                .andExpect(jsonPath("$.doadorId").value(1));
    }

    @Test
    void retorna400QuandoFaltamItens() throws Exception {
        mvc.perform(post("/api/doacoes").principal(doadorLogado)
                        .contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.itens").exists());
    }

    @Test
    void retorna404QuandoRecursoNaoExiste() throws Exception {
        when(service.registrar(any(DoacaoDTO.class), eq(1L)))
                .thenThrow(new RecursoNaoEncontradoException("Ingrediente não encontrado: 9"));

        mvc.perform(post("/api/doacoes").principal(doadorLogado).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itens\":[{\"ingredienteId\":9,\"quantidade\":5}]}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Não encontrado"))
                .andExpect(jsonPath("$.mensagem").value("Ingrediente não encontrado: 9"));
    }

    @Test
    void retorna403QuandoTentaDoarEmNomeDeOutro() throws Exception {
        when(service.registrar(any(DoacaoDTO.class), eq(1L)))
                .thenThrow(new AcessoNegadoException("Você só pode registrar doações em seu próprio nome."));

        mvc.perform(post("/api/doacoes").principal(doadorLogado).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"doadorId\":7,\"itens\":[{\"ingredienteId\":2,\"quantidade\":5}]}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.erro").value("Acesso negado"));
    }
}

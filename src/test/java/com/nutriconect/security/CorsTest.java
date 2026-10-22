package com.nutriconect.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CorsTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deve retornar Access-Control-Allow-Origin para chamadas GET de localhost:5173")
    void deveRetornarCabecalhoCorsParaOrigemPermitida() throws Exception {
        mockMvc.perform(get("/")
                        .header("Origin", "http://localhost:5173"))
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"));
    }

    @Test
    @DisplayName("Preflight OPTIONS não deve ser bloqueado pelo Security e deve retornar os headers CORS")
    void preflightOptionsDevePassar() throws Exception {
        mockMvc.perform(options("/")
                        .header("Origin", "http://localhost:5173")
                        .header("Access-Control-Request-Method", "POST")
                        .header("Access-Control-Request-Headers", "Authorization"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:5173"))
                .andExpect(header().string("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS"));
    }
}
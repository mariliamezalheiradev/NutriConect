package com.nutriconect.service;

import com.nutriconect.exception.IAIndisponivelException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class IAGenerativaReceitasTest {

    @Test
    void enviaChaveNoCabecalhoEDevolveTexto() {
        RestTemplate rest = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.createServer(rest);
        server.expect(requestTo("https://generativelanguage.googleapis.com/v1beta/models/modelo-teste:generateContent"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("x-goog-api-key", "chave-falsa"))
                .andRespond(withSuccess(
                        "{\"candidates\":[{\"content\":{\"parts\":[{\"text\":\"Arroz com frango\"}]}}]}",
                        MediaType.APPLICATION_JSON));

        IAGenerativaReceitas ia = new IAGenerativaReceitas(rest, "chave-falsa", "modelo-teste");

        assertEquals("Arroz com frango", ia.gerarReceitaAproveitamentoTotal(List.of("arroz", "frango")));
        server.verify();
    }

    @Test
    void falhaSemChaveConfigurada() {
        IAGenerativaReceitas ia = new IAGenerativaReceitas(new RestTemplate(), "", "modelo-teste");
        assertThrows(IAIndisponivelException.class, () -> ia.gerarReceitaAproveitamentoTotal(List.of("arroz")));
    }

    @Test
    void falhaQuandoApiRetornaErro() {
        RestTemplate rest = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.createServer(rest);
        server.expect(anything()).andRespond(withServerError());

        IAGenerativaReceitas ia = new IAGenerativaReceitas(rest, "chave-falsa", "modelo-teste");
        assertThrows(IAIndisponivelException.class, () -> ia.gerarReceitaAproveitamentoTotal(List.of("arroz")));
    }
}

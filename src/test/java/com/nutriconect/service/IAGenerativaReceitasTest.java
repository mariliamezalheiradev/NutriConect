package com.nutriconect.service;

import com.nutriconect.exception.IAIndisponivelException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import org.springframework.test.web.client.ExpectedCount;

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

        IAGenerativaReceitas ia = new IAGenerativaReceitas(rest, "chave-falsa", "modelo-teste", 3, 0);

        assertEquals("Arroz com frango", ia.gerarReceitaAproveitamentoTotal(List.of("arroz", "frango")));
        server.verify();
    }

    @Test
    void falhaSemChaveConfigurada() {
        IAGenerativaReceitas ia = new IAGenerativaReceitas(new RestTemplate(), "", "modelo-teste", 3, 0);
        assertThrows(IAIndisponivelException.class, () -> ia.gerarReceitaAproveitamentoTotal(List.of("arroz")));
    }

    @Test
    void falhaQuandoApiRetornaErro() {
        RestTemplate rest = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.createServer(rest);
        server.expect(ExpectedCount.times(3), anything()).andRespond(withServerError());

        IAGenerativaReceitas ia = new IAGenerativaReceitas(rest, "chave-falsa", "modelo-teste", 3, 0);
        assertThrows(IAIndisponivelException.class, () -> ia.gerarReceitaAproveitamentoTotal(List.of("arroz")));
    }

    private static final String OK =
            "{\"candidates\":[{\"content\":{\"parts\":[{\"text\":\"Receita pronta\"}]}}]}";

    @Test
    void tentaNovamenteQuandoIaEstaSobrecarregada() {
        RestTemplate rest = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.createServer(rest);
        server.expect(ExpectedCount.once(), anything()).andRespond(withStatus(org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE));
        server.expect(ExpectedCount.once(), anything()).andRespond(withSuccess(OK, MediaType.APPLICATION_JSON));

        IAGenerativaReceitas ia = new IAGenerativaReceitas(rest, "chave-falsa", "modelo-teste", 3, 0);

        assertEquals("Receita pronta", ia.gerarReceitaAproveitamentoTotal(List.of("arroz")));
        server.verify();
    }

    @Test
    void desisteAposTodasAsTentativasComMensagemClara() {
        RestTemplate rest = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.createServer(rest);
        server.expect(ExpectedCount.times(3), anything()).andRespond(withStatus(org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE));

        IAGenerativaReceitas ia = new IAGenerativaReceitas(rest, "chave-falsa", "modelo-teste", 3, 0);

        IAIndisponivelException e = assertThrows(IAIndisponivelException.class,
                () -> ia.gerarReceitaAproveitamentoTotal(List.of("arroz")));
        assertTrue(e.getMessage().contains("sobrecarregada"));
        server.verify();
    }

    @Test
    void naoRepeteQuandoOErroNaoETemporario() {
        RestTemplate rest = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.createServer(rest);
        server.expect(ExpectedCount.once(), anything()).andRespond(withStatus(org.springframework.http.HttpStatus.FORBIDDEN));

        IAGenerativaReceitas ia = new IAGenerativaReceitas(rest, "chave-falsa", "modelo-teste", 3, 0);

        assertThrows(IAIndisponivelException.class, () -> ia.gerarReceitaAproveitamentoTotal(List.of("arroz")));
        server.verify();
    }
}

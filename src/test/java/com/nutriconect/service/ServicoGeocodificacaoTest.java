package com.nutriconect.service;

import com.nutriconect.dto.Coordenadas;
import com.nutriconect.exception.GeocodificacaoIndisponivelException;
import com.nutriconect.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class ServicoGeocodificacaoTest {

    private static final String URL = "https://cep.awesomeapi.com.br/json/";

    private final RestTemplate rest = new RestTemplate();
    private final MockRestServiceServer server = MockRestServiceServer.createServer(rest);
    private final ServicoGeocodificacao servico = new ServicoGeocodificacao(rest, URL);

    @Test
    void converteCepEmCoordenadas() {
        server.expect(requestTo(URL + "01001000")).andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{\"cep\":\"01001000\",\"lat\":\"-23.5502784\",\"lng\":\"-46.6342179\"}",
                        MediaType.APPLICATION_JSON));

        Coordenadas c = servico.buscarCoordenadasPorCep("01001-000");

        assertEquals(-23.5502784, c.latitude(), 0.0000001);
        assertEquals(-46.6342179, c.longitude(), 0.0000001);
        server.verify();
    }

    @Test
    void cepInexistenteViraNaoEncontrado() {
        server.expect(requestTo(URL + "99999999"))
                .andRespond(withStatus(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
                        .body("{\"code\":\"not_found\"}"));
        assertThrows(RecursoNaoEncontradoException.class, () -> servico.buscarCoordenadasPorCep("99999-999"));
    }

    @Test
    void respostaSemCoordenadasViraNaoEncontrado() {
        server.expect(requestTo(URL + "01001000"))
                .andRespond(withSuccess("{\"cep\":\"01001000\"}", MediaType.APPLICATION_JSON));
        assertThrows(RecursoNaoEncontradoException.class, () -> servico.buscarCoordenadasPorCep("01001000"));
    }

    @Test
    void falhaDoServidorViraServicoIndisponivel() {
        server.expect(requestTo(URL + "01001000")).andRespond(withServerError());
        assertThrows(GeocodificacaoIndisponivelException.class, () -> servico.buscarCoordenadasPorCep("01001000"));
    }

    @Test
    void rejeitaCepComFormatoInvalido() {
        assertThrows(IllegalArgumentException.class, () -> servico.buscarCoordenadasPorCep("123"));
    }
}
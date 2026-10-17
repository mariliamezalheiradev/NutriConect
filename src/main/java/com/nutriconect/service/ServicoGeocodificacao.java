package com.nutriconect.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.nutriconect.dto.Coordenadas;
import com.nutriconect.exception.GeocodificacaoIndisponivelException;
import com.nutriconect.exception.RecursoNaoEncontradoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class ServicoGeocodificacao {

    private static final Logger log = LoggerFactory.getLogger(ServicoGeocodificacao.class);

    private final RestTemplate restTemplate;
    private final String urlBase;

    public ServicoGeocodificacao(RestTemplate restTemplate,
                                 @Value("${geocodificacao.url:https://cep.awesomeapi.com.br/json/}") String urlBase) {
        this.restTemplate = restTemplate;
        this.urlBase = urlBase;
    }

    /** Remove traço e espaços e exige 8 números. */
    public static String normalizarCep(String cep) {
        String numeros = cep == null ? "" : cep.replaceAll("\\D", "");
        if (numeros.length() != 8) {
            throw new IllegalArgumentException("CEP inválido. Informe 8 números.");
        }
        return numeros;
    }

    public Coordenadas buscarCoordenadasPorCep(String cep) {
        String numeros = normalizarCep(cep);
        try {
            JsonNode resposta = restTemplate.getForObject(urlBase + numeros, JsonNode.class);
            JsonNode lat = resposta == null ? null : resposta.get("lat");
            JsonNode lng = resposta == null ? null : resposta.get("lng");
            if (lat == null || lng == null || lat.asText().isBlank() || lng.asText().isBlank()) {
                throw new RecursoNaoEncontradoException(
                        "Não foi possível obter as coordenadas do CEP " + numeros + ".");
            }
            return new Coordenadas(Double.parseDouble(lat.asText()), Double.parseDouble(lng.asText()));
        } catch (HttpClientErrorException.NotFound | HttpClientErrorException.BadRequest e) {
            throw new RecursoNaoEncontradoException("CEP não encontrado: " + numeros + ".");
        } catch (RestClientException | NumberFormatException e) {
            log.error("Falha ao consultar a API de CEP", e);
            throw new GeocodificacaoIndisponivelException(
                    "O serviço de CEP está indisponível no momento. Tente novamente em instantes.", e);
        }
    }
}
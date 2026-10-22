package com.nutriconect.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.nutriconect.exception.IAIndisponivelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class IAGenerativaReceitas {

    private static final Logger log = LoggerFactory.getLogger(IAGenerativaReceitas.class);
    private static final String URL_BASE = "https://generativelanguage.googleapis.com/v1beta/models/";

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String modelo;
    private final int maxTentativas;
    private final long esperaInicialMs;

    public IAGenerativaReceitas(RestTemplate restTemplate,
                                @Value("${gemini.api.key:}") String apiKey,
                                @Value("${gemini.model:gemini-flash-latest}") String modelo,
                                @Value("${gemini.tentativas:3}") int maxTentativas,
                                @Value("${gemini.espera-ms:2000}") long esperaInicialMs) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.modelo = modelo;
        this.maxTentativas = Math.max(1, maxTentativas);
        this.esperaInicialMs = Math.max(0, esperaInicialMs);
    }

    public String gerarReceitaAproveitamentoTotal(List<String> ingredientes) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IAIndisponivelException(
                    "Chave da API do Gemini não configurada (variável GEMINI_API_KEY).");
        }

        String prompt = "Você é um chef de cozinha especialista no ODS 2 (Fome Zero) e em aproveitamento total de alimentos. "
                + "Crie uma receita prática, nutritiva e criativa utilizando estritamente os seguintes ingredientes disponíveis "
                + "(você pode assumir o uso de itens básicos como sal, água e óleo): "
                + String.join(", ", ingredientes) + ". "
                + "Retorne a resposta com: Nome do Prato, Tempo de Preparo, Ingredientes Necessários e Modo de Preparo passo a passo.";

        Map<String, Object> corpo = Map.of("contents",
                List.of(Map.of("parts", List.of(Map.of("text", prompt)))));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // A chave vai no cabeçalho para não aparecer em URLs nem em logs.
        headers.set("x-goog-api-key", apiKey);

        HttpEntity<Map<String, Object>> requisicao = new HttpEntity<>(corpo, headers);
        String url = URL_BASE + modelo + ":generateContent";

        for (int tentativa = 1; tentativa <= maxTentativas; tentativa++) {
            try {
                JsonNode resposta = restTemplate.postForObject(url, requisicao, JsonNode.class);
                JsonNode texto = resposta == null ? null
                        : resposta.path("candidates").path(0).path("content").path("parts").path(0).path("text");
                if (texto == null || texto.isMissingNode() || texto.asText().isBlank()) {
                    throw new IAIndisponivelException("A IA não retornou uma receita válida.");
                }
                return texto.asText();
            } catch (RestClientException e) {
                boolean temporario = ehTemporario(e);
                log.warn("Falha ao chamar a API do Gemini (modelo {}, tentativa {}/{}): {}",
                        modelo, tentativa, maxTentativas, e.getMessage());
                if (!temporario) {
                    throw new IAIndisponivelException("Erro ao comunicar com a API de IA.", e);
                }
                if (tentativa == maxTentativas) {
                    throw new IAIndisponivelException(
                            "A IA está sobrecarregada no momento. Tente novamente em instantes.", e);
                }
                esperar(esperaInicialMs * (1L << (tentativa - 1)));
            }
        }
        throw new IAIndisponivelException("Erro ao comunicar com a API de IA.");
    }

    /** 429 (limite de uso), 5xx (instabilidade) e falhas de rede/tempo esgotado valem nova tentativa. */
    private boolean ehTemporario(RestClientException e) {
        if (e instanceof HttpClientErrorException http) {
            return http.getStatusCode().value() == 429;
        }
        if (e instanceof HttpServerErrorException) {
            return true;
        }
        return !(e instanceof org.springframework.web.client.RestClientResponseException);
    }

    private void esperar(long milissegundos) {
        if (milissegundos <= 0) {
            return;
        }
        try {
            Thread.sleep(milissegundos);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new IAIndisponivelException("Requisição interrompida.", ie);
        }
    }
}

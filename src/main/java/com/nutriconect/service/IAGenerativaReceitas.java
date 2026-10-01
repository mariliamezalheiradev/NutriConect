package com.nutriconect.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;

@Service
public class IAGenerativaReceitas {

    // Lê a chave da API definida no application.properties
    @Value("${gemini.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate;

    public IAGenerativaReceitas() {
        this.restTemplate = new RestTemplate();
    }

    public String gerarReceitaAproveitamentoTotal(List<String> ingredientes) {
        try {
            String listaIngredientes = String.join(", ", ingredientes);

            String prompt = "Você é um chef de cozinha especialista no ODS 2 (Fome Zero) e em aproveitamento total de alimentos. "
                    + "Crie uma receita prática, nutritiva e criativa utilizando estritamente os seguintes ingredientes disponíveis "
                    + "(você pode assumir o uso de itens básicos como sal, água e óleo): "
                    + listaIngredientes + ". "
                    + "Retorne a resposta com: Nome do Prato, Tempo de Preparo, Ingredientes Necessários e Modo de Preparo passo a passo.";

            String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> textPart = Map.of("text", prompt);
            Map<String, Object> parts = Map.of("parts", List.of(textPart));
            Map<String, Object> contents = Map.of("contents", List.of(parts));

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(contents, headers);

            JsonNode response = restTemplate.postForObject(url, request, JsonNode.class);

            if (response != null && response.has("candidates")) {
                return response.path("candidates").get(0)
                        .path("content").path("parts").get(0)
                        .path("text").asText();
            } else {
                return "Erro: A Inteligência Artificial não retornou uma receita válida.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao comunicar com a API de Inteligência Artificial: " + e.getMessage();
        }
    }
}

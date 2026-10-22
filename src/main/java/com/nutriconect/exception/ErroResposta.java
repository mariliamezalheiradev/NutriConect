package com.nutriconect.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Formato padrão de TODA resposta de erro da API.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErroResposta(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem,
        String caminho,
        Map<String, String> campos // usado só em erros de validação
) {
    public static ErroResposta of(int status, String erro, String mensagem, String caminho) {
        return new ErroResposta(LocalDateTime.now(), status, erro, mensagem, caminho, null);
    }

    public static ErroResposta comCampos(int status, String erro, String mensagem,
                                         String caminho, Map<String, String> campos) {
        return new ErroResposta(LocalDateTime.now(), status, erro, mensagem, caminho, campos);
    }
}

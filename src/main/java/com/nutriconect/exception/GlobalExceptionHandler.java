package com.nutriconect.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Captura as exceções lançadas por qualquer Controller/Service e devolve um JSON
 * padronizado ({@link ErroResposta}) em vez do erro 500 padrão do servidor.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ---------- 400: dados inválidos ----------

    /** Falha de @Valid no @RequestBody (ex.: @NotBlank "O nome é obrigatório"). */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResposta> tratarValidacao(MethodArgumentNotValidException ex,
                                                        HttpServletRequest req) {
        Map<String, String> campos = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> campos.putIfAbsent(e.getField(), e.getDefaultMessage()));

        return montar(HttpStatus.BAD_REQUEST, "Dados inválidos",
                "Um ou mais campos estão inválidos.", req, campos);
    }

    /** Falha de validação em @RequestParam / @PathVariable. */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroResposta> tratarConstraint(ConstraintViolationException ex,
                                                         HttpServletRequest req) {
        Map<String, String> campos = new LinkedHashMap<>();
        ex.getConstraintViolations()
                .forEach(v -> campos.put(v.getPropertyPath().toString(), v.getMessage()));

        return montar(HttpStatus.BAD_REQUEST, "Dados inválidos",
                "Um ou mais parâmetros estão inválidos.", req, campos);
    }

    /** JSON mal formado ou corpo ausente. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResposta> tratarJsonInvalido(HttpMessageNotReadableException ex,
                                                           HttpServletRequest req) {
        return montar(HttpStatus.BAD_REQUEST, "Requisição inválida",
                "Corpo da requisição ausente ou com formato JSON inválido.", req, null);
    }

    /** Parâmetro obrigatório não enviado. */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErroResposta> tratarParametroAusente(MissingServletRequestParameterException ex,
                                                               HttpServletRequest req) {
        return montar(HttpStatus.BAD_REQUEST, "Parâmetro ausente",
                "O parâmetro '" + ex.getParameterName() + "' é obrigatório.", req, null);
    }

    /** Tipo errado (ex.: /api/ingredientes/abc quando o id é Long). */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResposta> tratarTipoErrado(MethodArgumentTypeMismatchException ex,
                                                         HttpServletRequest req) {
        return montar(HttpStatus.BAD_REQUEST, "Parâmetro inválido",
                "O valor informado para '" + ex.getName() + "' é inválido.", req, null);
    }

    /** Valor inválido detectado nos serviços (ex.: quantidade menor ou igual a zero). */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResposta> tratarArgumentoInvalido(IllegalArgumentException ex,
                                                                HttpServletRequest req) {
        return montar(HttpStatus.BAD_REQUEST, "Dados inválidos", ex.getMessage(), req, null);
    }

    // ---------- 404 / 405 ----------

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResposta> tratarNaoEncontrado(RecursoNaoEncontradoException ex,
                                                            HttpServletRequest req) {
        return montar(HttpStatus.NOT_FOUND, "Não encontrado", ex.getMessage(), req, null);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErroResposta> tratarRotaInexistente(NoResourceFoundException ex,
                                                              HttpServletRequest req) {
        return montar(HttpStatus.NOT_FOUND, "Não encontrado",
                "O endereço solicitado não existe.", req, null);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroResposta> tratarMetodoNaoSuportado(HttpRequestMethodNotSupportedException ex,
                                                                 HttpServletRequest req) {
        return montar(HttpStatus.METHOD_NOT_ALLOWED, "Método não permitido",
                "O método " + ex.getMethod() + " não é aceito neste endereço.", req, null);
    }

    // ---------- 422: regra de negócio ----------

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResposta> tratarRegraNegocio(RegraNegocioException ex,
                                                           HttpServletRequest req) {
        return montar(HttpStatus.UNPROCESSABLE_ENTITY, "Regra de negócio violada",
                ex.getMessage(), req, null);
    }

    // ---------- 503: IA fora do ar ----------

    /** As mensagens de {@link IAIndisponivelException} são escritas pela aplicação e não contêm dados sensíveis. */
    @ExceptionHandler(IAIndisponivelException.class)
    public ResponseEntity<ErroResposta> tratarIAIndisponivel(IAIndisponivelException ex,
                                                             HttpServletRequest req) {
        log.error("Falha ao consultar a API de IA", ex);
        return montar(HttpStatus.SERVICE_UNAVAILABLE, "Serviço de IA indisponível",
                ex.getMessage(), req, null);
    }

    /** Falha de rede/HTTP que escapou dos serviços: mensagem genérica, detalhe só no log. */
    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ErroResposta> tratarFalhaExterna(RestClientException ex, HttpServletRequest req) {
        log.error("Falha ao consultar serviço externo", ex);
        return montar(HttpStatus.SERVICE_UNAVAILABLE, "Serviço externo indisponível",
                "Não foi possível concluir a operação agora. Tente novamente em alguns instantes.",
                req, null);
    }

    // ---------- 500: qualquer outra coisa (rede de segurança) ----------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResposta> tratarGenerico(Exception ex, HttpServletRequest req) {
        log.error("Erro inesperado em {}", req.getRequestURI(), ex); // detalhe só no log
        return montar(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno",
                "Ocorreu um erro inesperado. Tente novamente mais tarde.", req, null);
    }

    // ---------- auxiliar ----------

    private ResponseEntity<ErroResposta> montar(HttpStatus status, String erro, String mensagem,
                                                HttpServletRequest req, Map<String, String> campos) {
        ErroResposta corpo = (campos == null)
                ? ErroResposta.of(status.value(), erro, mensagem, req.getRequestURI())
                : ErroResposta.comCampos(status.value(), erro, mensagem, req.getRequestURI(), campos);
        return ResponseEntity.status(status).body(corpo);
    }
}

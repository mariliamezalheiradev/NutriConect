package com.nutriconect.exception;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

    static class Corpo {
        @NotBlank(message = "O nome é obrigatório")
        private String nome;

        public String getNome() { return nome; }
        public void setNome(String nome) { this.nome = nome; }
    }

    /** Controller de apoio que provoca cada tipo de erro. */
    @RestController
    static class ControllerDeTeste {
        @PostMapping("/valida")
        public String valida(@Valid @RequestBody Corpo corpo) { return "ok"; }

        @GetMapping("/item/{id}")
        public String item(@PathVariable Long id) { return "ok"; }

        @GetMapping("/parametro")
        public String parametro(@RequestParam String nome) { return nome; }

        @GetMapping("/argumento")
        public String argumento() { throw new IllegalArgumentException("A quantidade deve ser maior que zero."); }

        @GetMapping("/regra")
        public String regra() { throw new RegraNegocioException("Já existe um usuário com este e-mail."); }

        @GetMapping("/inexistente")
        public String inexistente() { throw new RecursoNaoEncontradoException("Doador não encontrado: 9"); }

        @GetMapping("/ia")
        public String ia() { throw new IAIndisponivelException("A IA está sobrecarregada no momento."); }

        @GetMapping("/rede")
        public String rede() { throw new RestClientException("detalhe interno: timeout em https://servico.exemplo"); }

        @GetMapping("/login-invalido")
        public String loginInvalido() { throw new CredenciaisInvalidasException("E-mail ou senha inválidos."); }

        @GetMapping("/negado")
        public String negado() { throw new AcessoNegadoException("Você só pode registrar doações em seu próprio nome."); }

        @GetMapping("/negado-spring")
        public String negadoSpring() { throw new org.springframework.security.access.AccessDeniedException("detalhe interno"); }

        @GetMapping("/bug")
        public String bug() { throw new IllegalStateException("senha=segredo123 vazou"); }
    }

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mvc = MockMvcBuilders.standaloneSetup(new ControllerDeTeste())
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void validacaoDevolve400ComOsCampos() throws Exception {
        mvc.perform(post("/valida").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro").value("Dados inválidos"))
                .andExpect(jsonPath("$.campos.nome").value("O nome é obrigatório"))
                .andExpect(jsonPath("$.caminho").value("/valida"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void jsonMalFormadoDevolve400() throws Exception {
        mvc.perform(post("/valida").contentType(MediaType.APPLICATION_JSON).content("{nome:"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Requisição inválida"));
    }

    @Test
    void tipoErradoDevolve400() throws Exception {
        mvc.perform(get("/item/abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Parâmetro inválido"))
                .andExpect(jsonPath("$.mensagem").value("O valor informado para 'id' é inválido."));
    }

    @Test
    void parametroAusenteDevolve400() throws Exception {
        mvc.perform(get("/parametro"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Parâmetro ausente"));
    }

    @Test
    void argumentoInvalidoDevolve400() throws Exception {
        mvc.perform(get("/argumento"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensagem").value("A quantidade deve ser maior que zero."));
    }

    @Test
    void recursoInexistenteDevolve404() throws Exception {
        mvc.perform(get("/inexistente"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.erro").value("Não encontrado"))
                .andExpect(jsonPath("$.mensagem").value("Doador não encontrado: 9"));
    }

    @Test
    void metodoNaoPermitidoDevolve405() throws Exception {
        mvc.perform(post("/item/1"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.erro").value("Método não permitido"));
    }

    @Test
    void regraDeNegocioDevolve422() throws Exception {
        mvc.perform(get("/regra"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.erro").value("Regra de negócio violada"))
                .andExpect(jsonPath("$.mensagem").value("Já existe um usuário com este e-mail."));
    }

    @Test
    void iaIndisponivelDevolve503ComAMensagemDaAplicacao() throws Exception {
        mvc.perform(get("/ia"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.erro").value("Serviço de IA indisponível"))
                .andExpect(jsonPath("$.mensagem").value("A IA está sobrecarregada no momento."));
    }

    @Test
    void falhaExternaDevolve503SemVazarDetalhes() throws Exception {
        mvc.perform(get("/rede"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.mensagem").value(
                        "Não foi possível concluir a operação agora. Tente novamente em alguns instantes."));
    }

    @Test
    void erroInesperadoDevolve500SemVazarDetalhes() throws Exception {
        String corpo = mvc.perform(get("/bug"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.erro").value("Erro interno"))
                .andReturn().getResponse().getContentAsString();
        assertTrue(!corpo.contains("segredo123"), "o detalhe interno não pode aparecer na resposta");
    }

    @Test
    void credenciaisInvalidasDevolve401() throws Exception {
        mvc.perform(get("/login-invalido"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.erro").value("Não autenticado"))
                .andExpect(jsonPath("$.mensagem").value("E-mail ou senha inválidos."));
    }

    @Test
    void acessoNegadoDevolve403() throws Exception {
        mvc.perform(get("/negado"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.erro").value("Acesso negado"))
                .andExpect(jsonPath("$.mensagem").value("Você só pode registrar doações em seu próprio nome."));
    }

    @Test
    void acessoNegadoDoSpringDevolve403SemVazarDetalhes() throws Exception {
        String corpo = mvc.perform(get("/negado-spring"))
                .andExpect(status().isForbidden())
                .andReturn().getResponse().getContentAsString();
        assertTrue(!corpo.contains("detalhe interno"));
    }

    @Test
    void rotaInexistenteDevolve404() {
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/nao-existe");
        ResponseEntity<ErroResposta> resposta = new GlobalExceptionHandler()
                .tratarRotaInexistente(new NoResourceFoundException(HttpMethod.GET, "nao-existe"), req);
        assertEquals(404, resposta.getStatusCode().value());
        assertEquals("/nao-existe", resposta.getBody().caminho());
    }
}

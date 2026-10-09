package com.nutriconect.security;

import com.jayway.jsonpath.JsonPath;
import com.nutriconect.model.Receptor;
import com.nutriconect.repository.ReceptorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.Instant;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Sobe a aplicação inteira (com o filtro de segurança real) e exercita o login pela API. */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "jwt.secret=segredo-so-para-testes-com-mais-de-32-caracteres")
class AuthIntegracaoTest {

    @Autowired private MockMvc mvc;
    @Autowired private ReceptorRepository receptorRepository;
    @Autowired private PasswordEncoder encoder;
    @Autowired private JwtEncoder jwtEncoder;
    @Autowired private JwtDecoder jwtDecoder;

    private String emailUnico() {
        return "u" + UUID.randomUUID().toString().substring(0, 8) + "@teste.com";
    }

    private void cadastrarDoador(String email, String senha) throws Exception {
        mvc.perform(post("/api/doadores").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Mercado\",\"email\":\"" + email + "\",\"senha\":\"" + senha + "\"}"))
                .andExpect(status().isCreated());
    }

    private String login(String email, String senha) throws Exception {
        String corpo = mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"senha\":\"" + senha + "\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(corpo, "$.token");
    }

    private MockHttpServletRequestBuilder comToken(MockHttpServletRequestBuilder req, String token) {
        return req.header("Authorization", "Bearer " + token);
    }

    @Test
    void rotaProtegidaSemTokenDevolve401NoFormatoPadrao() throws Exception {
        mvc.perform(get("/api/ingredientes"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.erro").value("Não autenticado"))
                .andExpect(jsonPath("$.caminho").value("/api/ingredientes"));
    }

    @Test
    void tokenInventadoDevolve401() throws Exception {
        mvc.perform(comToken(get("/api/ingredientes"), "isto.nao.e-um-token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void tokenExpiradoDevolve401() throws Exception {
        Instant antes = Instant.now().minusSeconds(7200);
        JwtClaimsSet claims = JwtClaimsSet.builder().subject("x@x.com")
                .issuedAt(antes).expiresAt(antes.plusSeconds(60))
                .claim("uid", 1L).claim("role", "DOADOR").build();
        String expirado = jwtEncoder.encode(JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();

        mvc.perform(comToken(get("/api/ingredientes"), expirado))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void cadastroDeDoadorELoginSaoPublicos() throws Exception {
        String email = emailUnico();
        cadastrarDoador(email, "senha123");
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"senha\":\"senha123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipo").value("Bearer"))
                .andExpect(jsonPath("$.papel").value("DOADOR"))
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void senhaErradaEEmailInexistenteDevolvem401ComAMesmaMensagem() throws Exception {
        String email = emailUnico();
        cadastrarDoador(email, "senha123");

        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + email + "\",\"senha\":\"errada\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensagem").value("E-mail ou senha inválidos."));
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + emailUnico() + "\",\"senha\":\"qualquer\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.mensagem").value("E-mail ou senha inválidos."));
    }

    @Test
    void loginSemCamposDevolve400() throws Exception {
        mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.email").exists())
                .andExpect(jsonPath("$.campos.senha").exists());
    }

    @Test
    void doadorLogadoRegistraDoacaoEmSeuProprioNome() throws Exception {
        String email = emailUnico();
        cadastrarDoador(email, "senha123");
        String token = login(email, "senha123");

        String ingrediente = mvc.perform(comToken(post("/api/ingredientes"), token)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"nome\":\"Arroz\",\"unidade\":\"kg\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        int ingredienteId = JsonPath.read(ingrediente, "$.id");

        mvc.perform(comToken(post("/api/doacoes"), token).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itens\":[{\"ingredienteId\":" + ingredienteId + ",\"quantidade\":4.5}]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDENTE"))
                .andExpect(jsonPath("$.itens[0].quantidade").value(4.5));
    }

    @Test
    void doadorNaoPodeDoarEmNomeDeOutroDoador() throws Exception {
        String emailA = emailUnico();
        String emailB = emailUnico();
        cadastrarDoador(emailA, "senha123");
        cadastrarDoador(emailB, "senha123");
        String tokenA = login(emailA, "senha123");
        String tokenB = login(emailB, "senha123");

        String ingrediente = mvc.perform(comToken(post("/api/ingredientes"), tokenA)
                        .contentType(MediaType.APPLICATION_JSON).content("{\"nome\":\"Feijão\"}"))
                .andReturn().getResponse().getContentAsString();
        int ingredienteId = JsonPath.read(ingrediente, "$.id");

        // lê o id do doador B no token dele e tenta usá-lo com o token de A
        Object uidB = jwtDecoder.decode(tokenB).getClaims().get("uid");
        String idB = String.valueOf(uidB);
        mvc.perform(comToken(post("/api/doacoes"), tokenA).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"doadorId\":" + idB + ",\"itens\":[{\"ingredienteId\":" + ingredienteId + ",\"quantidade\":1}]}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.mensagem").value("Você só pode registrar doações em seu próprio nome."));
    }

    @Test
    void receptorNaoPodeRegistrarDoacoes() throws Exception {
        String email = emailUnico();
        Receptor r = new Receptor();
        r.setNome("ONG Esperança");
        r.setEmail(email);
        r.setSenha(encoder.encode("senha123"));
        r.setEndereco("Rua A, 1");
        receptorRepository.save(r);

        String token = login(email, "senha123");
        mvc.perform(comToken(post("/api/doacoes"), token).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itens\":[{\"ingredienteId\":1,\"quantidade\":1}]}"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.erro").value("Acesso negado"));
    }
}

package com.nutriconect.service;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nutriconect.dto.LoginRequestDTO;
import com.nutriconect.dto.TokenResponseDTO;
import com.nutriconect.exception.CredenciaisInvalidasException;
import com.nutriconect.model.Doador;
import com.nutriconect.model.Receptor;
import com.nutriconect.model.Usuario;
import com.nutriconect.repository.UsuarioRepository;
import com.nutriconect.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AuthServiceTest {

    private static final SecretKey CHAVE = new SecretKeySpec(
            "chave-de-teste-com-mais-de-32-caracteres!".getBytes(StandardCharsets.UTF_8), "HmacSHA256");

    private UsuarioRepository repository;
    private PasswordEncoder encoder;
    private AuthService service;
    private NimbusJwtDecoder decoder;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(UsuarioRepository.class);
        encoder = new BCryptPasswordEncoder();
        JwtService jwtService = new JwtService(new NimbusJwtEncoder(new ImmutableSecret<>(CHAVE)), 60);
        service = new AuthService(repository, encoder, jwtService);
        decoder = NimbusJwtDecoder.withSecretKey(CHAVE).macAlgorithm(MacAlgorithm.HS256).build();
    }

    private LoginRequestDTO login(String email, String senha) {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setEmail(email);
        dto.setSenha(senha);
        return dto;
    }

    private <T extends Usuario> T usuario(T u, Long id, String email, String senha) {
        u.setId(id);
        u.setNome("Fulano");
        u.setEmail(email);
        u.setSenha(encoder.encode(senha));
        return u;
    }

    @Test
    void loginCorretoDevolveTokenValidoComPapelDeDoador() {
        when(repository.findByEmail("ana@x.com"))
                .thenReturn(Optional.of(usuario(new Doador(), 5L, "ana@x.com", "senha123")));

        TokenResponseDTO resposta = service.login(login("ana@x.com", "senha123"));

        assertEquals("Bearer", resposta.tipo());
        assertEquals("DOADOR", resposta.papel());
        assertEquals(3600, resposta.expiraEmSegundos());
        Jwt jwt = decoder.decode(resposta.token());
        assertEquals("ana@x.com", jwt.getSubject());
        assertEquals("DOADOR", jwt.getClaimAsString("role"));
        assertEquals(5L, ((Number) jwt.getClaim("uid")).longValue());
    }

    @Test
    void receptorRecebePapelDeReceptor() {
        when(repository.findByEmail("ong@x.com"))
                .thenReturn(Optional.of(usuario(new Receptor(), 6L, "ong@x.com", "senha123")));
        assertEquals("RECEPTOR", service.login(login("ong@x.com", "senha123")).papel());
    }

    @Test
    void senhaErradaEEmailInexistenteDevolvemAMesmaMensagem() {
        when(repository.findByEmail("ana@x.com"))
                .thenReturn(Optional.of(usuario(new Doador(), 5L, "ana@x.com", "senha123")));
        when(repository.findByEmail("ninguem@x.com")).thenReturn(Optional.empty());

        CredenciaisInvalidasException senhaErrada = assertThrows(CredenciaisInvalidasException.class,
                () -> service.login(login("ana@x.com", "errada")));
        CredenciaisInvalidasException semUsuario = assertThrows(CredenciaisInvalidasException.class,
                () -> service.login(login("ninguem@x.com", "qualquer")));

        assertEquals(senhaErrada.getMessage(), semUsuario.getMessage());
    }

    @Test
    void ignoraEspacosAoRedorDoEmail() {
        when(repository.findByEmail("ana@x.com"))
                .thenReturn(Optional.of(usuario(new Doador(), 5L, "ana@x.com", "senha123")));
        assertNotNull(service.login(login("  ana@x.com ", "senha123")).token());
    }
}

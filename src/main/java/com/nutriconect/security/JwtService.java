package com.nutriconect.security;

import com.nutriconect.dto.TokenResponseDTO;
import com.nutriconect.model.Doador;
import com.nutriconect.model.Receptor;
import com.nutriconect.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

/** Gera os tokens JWT (assinados com HS256) devolvidos no login. */
@Service
public class JwtService {

    public static final String CLAIM_ID = "uid";
    public static final String CLAIM_PAPEL = "role";

    private final JwtEncoder encoder;
    private final Duration validade;

    public JwtService(JwtEncoder encoder,
                      @Value("${jwt.expiracao-minutos:60}") long expiracaoMinutos) {
        this.encoder = encoder;
        this.validade = Duration.ofMinutes(Math.max(1, expiracaoMinutos));
    }

    public TokenResponseDTO gerar(Usuario usuario) {
        Instant agora = Instant.now();
        String papel = papelDe(usuario);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("nutriconect")
                .subject(usuario.getEmail())
                .issuedAt(agora)
                .expiresAt(agora.plus(validade))
                .claim(CLAIM_ID, usuario.getId())
                .claim(CLAIM_PAPEL, papel)
                .claim("nome", usuario.getNome())
                .build();

        JwsHeader cabecalho = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = encoder.encode(JwtEncoderParameters.from(cabecalho, claims)).getTokenValue();
        return new TokenResponseDTO(token, "Bearer", validade.toSeconds(), papel);
    }

    public static String papelDe(Usuario usuario) {
        if (usuario instanceof Doador) return "DOADOR";
        if (usuario instanceof Receptor) return "RECEPTOR";
        return "USUARIO";
    }
}

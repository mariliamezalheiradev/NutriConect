package com.nutriconect.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    private static final int TAMANHO_MINIMO_SEGREDO = 32; // 256 bits, exigido pelo HS256

    /**
     * Chave de assinatura dos tokens. Vem da variável JWT_SECRET (mínimo de 32 caracteres).
     * Sem ela, é gerada uma chave aleatória a cada início da aplicação: os tokens deixam de
     * valer quando o servidor reinicia. Não existe nenhum segredo padrão no código.
     */
    @Bean
    public SecretKey chaveJwt(@Value("${jwt.secret:}") String segredo) {
        byte[] bytes;
        if (segredo == null || segredo.isBlank()) {
            log.warn("JWT_SECRET não definido: usando uma chave aleatória temporária. "
                    + "Os tokens deixarão de valer quando a aplicação reiniciar.");
            bytes = new byte[TAMANHO_MINIMO_SEGREDO];
            new SecureRandom().nextBytes(bytes);
        } else {
            bytes = segredo.getBytes(StandardCharsets.UTF_8);
            if (bytes.length < TAMANHO_MINIMO_SEGREDO) {
                throw new IllegalStateException(
                        "JWT_SECRET precisa ter pelo menos " + TAMANHO_MINIMO_SEGREDO + " caracteres.");
            }
        }
        return new SecretKeySpec(bytes, "HmacSHA256");
    }

    @Bean
    public JwtEncoder jwtEncoder(SecretKey chaveJwt) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(chaveJwt));
    }

    @Bean
    public JwtDecoder jwtDecoder(SecretKey chaveJwt) {
        return NimbusJwtDecoder.withSecretKey(chaveJwt).macAlgorithm(MacAlgorithm.HS256).build();
    }

    /** Transforma a claim "role" do token (ex.: DOADOR) na autoridade ROLE_DOADOR. */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter autoridades = new JwtGrantedAuthoritiesConverter();
        autoridades.setAuthoritiesClaimName(JwtService.CLAIM_PAPEL);
        autoridades.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter conversor = new JwtAuthenticationConverter();
        conversor.setJwtGrantedAuthoritiesConverter(autoridades);
        return conversor;
    }

    @Bean
    public SecurityFilterChain filtros(HttpSecurity http,
                                       JwtAuthenticationConverter conversor,
                                       RestAuthenticationEntryPoint entryPoint,
                                       RestAccessDeniedHandler accessDeniedHandler) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // API sem sessão nem cookies: CSRF não se aplica
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> a
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/doadores").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/doacoes").hasRole("DOADOR")
                        .requestMatchers("/error").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(o -> o
                        .jwt(j -> j.jwtAuthenticationConverter(conversor))
                        .authenticationEntryPoint(entryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(entryPoint)
                        .accessDeniedHandler(accessDeniedHandler))
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable);
        return http.build();
    }
}

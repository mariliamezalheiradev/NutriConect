package com.nutriconect.service;

import com.nutriconect.dto.LoginRequestDTO;
import com.nutriconect.dto.TokenResponseDTO;
import com.nutriconect.exception.CredenciaisInvalidasException;
import com.nutriconect.model.Usuario;
import com.nutriconect.repository.UsuarioRepository;
import com.nutriconect.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    static final String MENSAGEM_INVALIDA = "E-mail ou senha inválidos.";

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    /** Hash descartável: permite gastar o mesmo tempo quando o e-mail não existe. */
    private final String hashFicticio;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.hashFicticio = passwordEncoder.encode(UUID.randomUUID().toString());
    }

    public TokenResponseDTO login(LoginRequestDTO dto) {
        Optional<Usuario> encontrado = usuarioRepository.findByEmail(dto.getEmail().trim());

        // A mesma mensagem e o mesmo custo para e-mail inexistente e senha errada,
        // para não revelar quais e-mails estão cadastrados.
        String hash = encontrado.map(Usuario::getSenha).orElse(hashFicticio);
        boolean senhaConfere = passwordEncoder.matches(dto.getSenha(), hash);
        if (encontrado.isEmpty() || !senhaConfere) {
            throw new CredenciaisInvalidasException(MENSAGEM_INVALIDA);
        }
        return jwtService.gerar(encontrado.get());
    }
}

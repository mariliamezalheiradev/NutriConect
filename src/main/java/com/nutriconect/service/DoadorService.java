package com.nutriconect.service;

import com.nutriconect.dto.DoadorDTO;
import com.nutriconect.dto.UsuarioResponseDTO;
import com.nutriconect.exception.RegraNegocioException;
import com.nutriconect.model.Doador;
import com.nutriconect.repository.DoadorRepository;
import com.nutriconect.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DoadorService {

    private final DoadorRepository doadorRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DoadorService(DoadorRepository doadorRepository,
                         UsuarioRepository usuarioRepository,
                         PasswordEncoder passwordEncoder) {
        this.doadorRepository = doadorRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponseDTO cadastrar(DoadorDTO dto) {
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RegraNegocioException("Já existe um usuário com este e-mail.");
        }

        Doador doador = new Doador();
        doador.setNome(dto.getNome());
        doador.setEmail(dto.getEmail());
        doador.setSenha(passwordEncoder.encode(dto.getSenha()));
        doador.setTelefone(dto.getTelefone());
        doador.setDocumento(dto.getDocumento());

        Doador salvo = doadorRepository.save(doador);
        return new UsuarioResponseDTO(salvo.getId(), salvo.getNome(), salvo.getEmail());
    }
}

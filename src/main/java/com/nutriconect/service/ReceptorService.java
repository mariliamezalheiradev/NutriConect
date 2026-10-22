package com.nutriconect.service;

import com.nutriconect.dto.Coordenadas;
import com.nutriconect.dto.ReceptorDTO;
import com.nutriconect.dto.ReceptorProximoDTO;
import com.nutriconect.dto.UsuarioResponseDTO;
import com.nutriconect.exception.RegraNegocioException;
import com.nutriconect.model.Receptor;
import com.nutriconect.repository.ReceptorRepository;
import com.nutriconect.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReceptorService {

    private final ReceptorRepository receptorRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ServicoGeocodificacao geocodificacao;
    private final ServicoGeolocalizacao geolocalizacao;

    public ReceptorService(ReceptorRepository receptorRepository,
                           UsuarioRepository usuarioRepository,
                           PasswordEncoder passwordEncoder,
                           ServicoGeocodificacao geocodificacao,
                           ServicoGeolocalizacao geolocalizacao) {
        this.receptorRepository = receptorRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.geocodificacao = geocodificacao;
        this.geolocalizacao = geolocalizacao;
    }

    // Sem @Transactional de propósito: não segura uma conexão do banco durante a chamada HTTP.
    public UsuarioResponseDTO cadastrar(ReceptorDTO dto) {
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RegraNegocioException("Já existe um usuário com este e-mail.");
        }
        Coordenadas coordenadas = geocodificacao.buscarCoordenadasPorCep(dto.getCep());

        Receptor receptor = new Receptor();
        receptor.setNome(dto.getNome());
        receptor.setEmail(dto.getEmail());
        receptor.setSenha(passwordEncoder.encode(dto.getSenha()));
        receptor.setTelefone(dto.getTelefone());
        receptor.setCnpj(dto.getCnpj());
        receptor.setEndereco(dto.getEndereco());
        receptor.setCep(ServicoGeocodificacao.normalizarCep(dto.getCep()));
        receptor.setLatitude(coordenadas.latitude());
        receptor.setLongitude(coordenadas.longitude());

        Receptor salvo = receptorRepository.save(receptor);
        return new UsuarioResponseDTO(salvo.getId(), salvo.getNome(), salvo.getEmail());
    }

    @Transactional(readOnly = true)
    public List<ReceptorProximoDTO> buscarProximos(String cep, double raioKm) {
        if (raioKm <= 0 || raioKm > 100) {
            throw new IllegalArgumentException("O raio deve ser maior que 0 e no máximo 100 km.");
        }
        Coordenadas origem = geocodificacao.buscarCoordenadasPorCep(cep);
        return geolocalizacao.ordenarPorProximidade(origem, receptorRepository.findAll(), raioKm);
    }
}
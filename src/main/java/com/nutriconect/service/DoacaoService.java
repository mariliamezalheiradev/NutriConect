package com.nutriconect.service;

import com.nutriconect.dto.DoacaoDTO;
import com.nutriconect.dto.DoacaoResponseDTO;
import com.nutriconect.exception.RecursoNaoEncontradoException;
import com.nutriconect.model.Doacao;
import com.nutriconect.model.Doador;
import com.nutriconect.model.Ingrediente;
import com.nutriconect.model.Receptor;
import com.nutriconect.repository.DoacaoRepository;
import com.nutriconect.repository.DoadorRepository;
import com.nutriconect.repository.IngredienteRepository;
import com.nutriconect.repository.ReceptorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DoacaoService {

    private final DoacaoRepository doacaoRepository;
    private final DoadorRepository doadorRepository;
    private final ReceptorRepository receptorRepository;
    private final IngredienteRepository ingredienteRepository;

    public DoacaoService(DoacaoRepository doacaoRepository,
                         DoadorRepository doadorRepository,
                         ReceptorRepository receptorRepository,
                         IngredienteRepository ingredienteRepository) {
        this.doacaoRepository = doacaoRepository;
        this.doadorRepository = doadorRepository;
        this.receptorRepository = receptorRepository;
        this.ingredienteRepository = ingredienteRepository;
    }

    @Transactional
    public DoacaoResponseDTO registrar(DoacaoDTO dto) {
        if (dto.getQuantidade() == null || dto.getQuantidade() <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        Doador doador = doadorRepository.findById(dto.getDoadorId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Doador não encontrado: " + dto.getDoadorId()));
        Ingrediente ingrediente = ingredienteRepository.findById(dto.getIngredienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Ingrediente não encontrado: " + dto.getIngredienteId()));

        Doacao doacao = new Doacao();
        doacao.setQuantidade(dto.getQuantidade());
        doacao.setDoador(doador);
        doacao.setIngrediente(ingrediente);

        if (dto.getReceptorId() != null) {
            Receptor receptor = receptorRepository.findById(dto.getReceptorId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Receptor não encontrado: " + dto.getReceptorId()));
            doacao.setReceptor(receptor);
        }

        Doacao salva = doacaoRepository.save(doacao);
        return new DoacaoResponseDTO(
                salva.getId(),
                salva.getQuantidade(),
                salva.getStatus(),
                salva.getDataCriacao(),
                doador.getId(),
                salva.getReceptor() != null ? salva.getReceptor().getId() : null,
                ingrediente.getId());
    }
}

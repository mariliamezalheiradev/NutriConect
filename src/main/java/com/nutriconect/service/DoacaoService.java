package com.nutriconect.service;

import com.nutriconect.dto.DoacaoDTO;
import com.nutriconect.dto.DoacaoResponseDTO;
import com.nutriconect.dto.ItemDoacaoDTO;
import com.nutriconect.exception.RecursoNaoEncontradoException;
import com.nutriconect.exception.RegraNegocioException;
import com.nutriconect.model.*;
import com.nutriconect.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DoacaoService {

    private final DoacaoRepository doacaoRepository;
    private final DoadorRepository doadorRepository;
    private final ReceptorRepository receptorRepository;
    private final IngredienteRepository ingredienteRepository;
    private final StatusDoacaoRepository statusDoacaoRepository;

    public DoacaoService(DoacaoRepository doacaoRepository,
                         DoadorRepository doadorRepository,
                         ReceptorRepository receptorRepository,
                         IngredienteRepository ingredienteRepository,
                         StatusDoacaoRepository statusDoacaoRepository) {
        this.doacaoRepository = doacaoRepository;
        this.doadorRepository = doadorRepository;
        this.receptorRepository = receptorRepository;
        this.ingredienteRepository = ingredienteRepository;
        this.statusDoacaoRepository = statusDoacaoRepository;
    }

    @Transactional
    public DoacaoResponseDTO registrar(DoacaoDTO dto) {
        if (dto.getItens() == null || dto.getItens().isEmpty()) {
            throw new IllegalArgumentException("Informe ao menos um item na doação.");
        }

        Doador doador = doadorRepository.findById(dto.getDoadorId())
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Doador não encontrado: " + dto.getDoadorId()));

        Doacao doacao = new Doacao();
        doacao.setDoador(doador);
        doacao.setStatus(statusInicial());

        if (dto.getReceptorId() != null) {
            Receptor receptor = receptorRepository.findById(dto.getReceptorId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Receptor não encontrado: " + dto.getReceptorId()));
            doacao.setReceptor(receptor);
        }

        Set<Long> jaInformados = new HashSet<>();
        for (ItemDoacaoDTO itemDto : dto.getItens()) {
            if (itemDto.getQuantidade() == null || itemDto.getQuantidade().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
            }
            if (!jaInformados.add(itemDto.getIngredienteId())) {
                throw new RegraNegocioException(
                        "Ingrediente repetido na doação: " + itemDto.getIngredienteId());
            }
            Ingrediente ingrediente = ingredienteRepository.findById(itemDto.getIngredienteId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException(
                            "Ingrediente não encontrado: " + itemDto.getIngredienteId()));
            doacao.adicionarItem(new ItemDoacao(ingrediente, itemDto.getQuantidade()));
        }

        return paraResposta(doacaoRepository.save(doacao));
    }

    private StatusDoacao statusInicial() {
        return statusDoacaoRepository.findByDescricao(StatusDoacao.PENDENTE)
                .orElseGet(() -> statusDoacaoRepository.save(new StatusDoacao(StatusDoacao.PENDENTE)));
    }

    private DoacaoResponseDTO paraResposta(Doacao d) {
        List<DoacaoResponseDTO.Item> itens = d.getItens().stream()
                .map(i -> new DoacaoResponseDTO.Item(i.getIngrediente().getId(), i.getQuantidade()))
                .toList();
        return new DoacaoResponseDTO(
                d.getId(),
                d.getStatus().getDescricao(),
                d.getDataDoacao(),
                d.getDoador().getId(),
                d.getReceptor() != null ? d.getReceptor().getId() : null,
                itens);
    }
}

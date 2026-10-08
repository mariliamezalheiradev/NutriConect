package com.nutriconect.service;

import com.nutriconect.dto.DoacaoDTO;
import com.nutriconect.dto.DoacaoResponseDTO;
import com.nutriconect.exception.RecursoNaoEncontradoException;
import com.nutriconect.model.*;
import com.nutriconect.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DoacaoServiceTest {

    private DoacaoRepository doacaoRepository;
    private DoadorRepository doadorRepository;
    private ReceptorRepository receptorRepository;
    private IngredienteRepository ingredienteRepository;
    private DoacaoService service;

    @BeforeEach
    void setUp() {
        doacaoRepository = Mockito.mock(DoacaoRepository.class);
        doadorRepository = Mockito.mock(DoadorRepository.class);
        receptorRepository = Mockito.mock(ReceptorRepository.class);
        ingredienteRepository = Mockito.mock(IngredienteRepository.class);
        service = new DoacaoService(doacaoRepository, doadorRepository, receptorRepository, ingredienteRepository);
    }

    private DoacaoDTO dto(Double quantidade, Long doadorId, Long ingredienteId) {
        DoacaoDTO dto = new DoacaoDTO();
        dto.setQuantidade(quantidade);
        dto.setDoadorId(doadorId);
        dto.setIngredienteId(ingredienteId);
        return dto;
    }

    @Test
    void registraDoacaoComStatusPendente() {
        Doador doador = new Doador();
        doador.setId(1L);
        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setId(2L);
        when(doadorRepository.findById(1L)).thenReturn(Optional.of(doador));
        when(ingredienteRepository.findById(2L)).thenReturn(Optional.of(ingrediente));
        when(doacaoRepository.save(any(Doacao.class))).thenAnswer(inv -> {
            Doacao d = inv.getArgument(0);
            d.setId(10L);
            return d;
        });

        DoacaoResponseDTO resposta = service.registrar(dto(5.0, 1L, 2L));

        assertEquals(10L, resposta.id());
        assertEquals(StatusDoacao.PENDENTE, resposta.status());
        assertEquals(1L, resposta.doadorId());
        assertEquals(2L, resposta.ingredienteId());
        assertNull(resposta.receptorId());
    }

    @Test
    void rejeitaQuantidadeZeroOuNegativa() {
        assertThrows(IllegalArgumentException.class, () -> service.registrar(dto(0.0, 1L, 2L)));
        verify(doacaoRepository, never()).save(any());
    }

    @Test
    void falhaQuandoDoadorNaoExiste() {
        when(doadorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class, () -> service.registrar(dto(1.0, 1L, 2L)));
        verify(doacaoRepository, never()).save(any());
    }
}

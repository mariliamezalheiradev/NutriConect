package com.nutriconect.service;

import com.nutriconect.dto.DoacaoDTO;
import com.nutriconect.dto.DoacaoResponseDTO;
import com.nutriconect.dto.ItemDoacaoDTO;
import com.nutriconect.exception.AcessoNegadoException;
import com.nutriconect.exception.RecursoNaoEncontradoException;
import com.nutriconect.exception.RegraNegocioException;
import com.nutriconect.model.*;
import com.nutriconect.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DoacaoServiceTest {

    private DoacaoRepository doacaoRepository;
    private DoadorRepository doadorRepository;
    private ReceptorRepository receptorRepository;
    private IngredienteRepository ingredienteRepository;
    private StatusDoacaoRepository statusRepository;
    private DoacaoService service;

    @BeforeEach
    void setUp() {
        doacaoRepository = Mockito.mock(DoacaoRepository.class);
        doadorRepository = Mockito.mock(DoadorRepository.class);
        receptorRepository = Mockito.mock(ReceptorRepository.class);
        ingredienteRepository = Mockito.mock(IngredienteRepository.class);
        statusRepository = Mockito.mock(StatusDoacaoRepository.class);
        service = new DoacaoService(doacaoRepository, doadorRepository, receptorRepository,
                ingredienteRepository, statusRepository);
    }

    /** Registra como se o usuário logado fosse o doador de id 1. */
    private DoacaoResponseDTO registrar(DoacaoDTO dto) {
        return service.registrar(dto, 1L);
    }

    private ItemDoacaoDTO item(Long ingredienteId, String quantidade) {
        ItemDoacaoDTO i = new ItemDoacaoDTO();
        i.setIngredienteId(ingredienteId);
        i.setQuantidade(new BigDecimal(quantidade));
        return i;
    }

    private DoacaoDTO dto(Long doadorId, ItemDoacaoDTO... itens) {
        DoacaoDTO dto = new DoacaoDTO();
        dto.setDoadorId(doadorId);
        dto.setItens(List.of(itens));
        return dto;
    }

    private Doador doador(Long id) {
        Doador d = new Doador();
        d.setId(id);
        return d;
    }

    private Ingrediente ingrediente(Long id) {
        Ingrediente i = new Ingrediente();
        i.setId(id);
        return i;
    }

    @Test
    void registraDoacaoComItensEStatusPendente() {
        when(doadorRepository.findById(1L)).thenReturn(Optional.of(doador(1L)));
        when(ingredienteRepository.findById(2L)).thenReturn(Optional.of(ingrediente(2L)));
        when(ingredienteRepository.findById(3L)).thenReturn(Optional.of(ingrediente(3L)));
        when(statusRepository.findByDescricao(StatusDoacao.PENDENTE))
                .thenReturn(Optional.of(new StatusDoacao(StatusDoacao.PENDENTE)));
        when(doacaoRepository.save(any(Doacao.class))).thenAnswer(inv -> {
            Doacao d = inv.getArgument(0);
            d.setId(10L);
            return d;
        });

        DoacaoResponseDTO resposta = registrar(dto(1L, item(2L, "5"), item(3L, "1.5")));

        assertEquals(10L, resposta.id());
        assertEquals("PENDENTE", resposta.status());
        assertEquals(1L, resposta.doadorId());
        assertNull(resposta.receptorId());
        assertEquals(2, resposta.itens().size());
        assertEquals(new BigDecimal("1.5"), resposta.itens().get(1).quantidade());
    }

    @Test
    void criaOStatusPendenteQuandoAindaNaoExiste() {
        when(doadorRepository.findById(1L)).thenReturn(Optional.of(doador(1L)));
        when(ingredienteRepository.findById(2L)).thenReturn(Optional.of(ingrediente(2L)));
        when(statusRepository.findByDescricao(StatusDoacao.PENDENTE)).thenReturn(Optional.empty());
        when(statusRepository.save(any(StatusDoacao.class))).thenAnswer(inv -> inv.getArgument(0));
        when(doacaoRepository.save(any(Doacao.class))).thenAnswer(inv -> inv.getArgument(0));

        assertEquals("PENDENTE", registrar(dto(1L, item(2L, "1"))).status());
        verify(statusRepository).save(any(StatusDoacao.class));
    }

    @Test
    void rejeitaQuantidadeZeroOuNegativa() {
        when(doadorRepository.findById(1L)).thenReturn(Optional.of(doador(1L)));
        assertThrows(IllegalArgumentException.class, () -> registrar(dto(1L, item(2L, "0"))));
        verify(doacaoRepository, never()).save(any());
    }

    @Test
    void rejeitaIngredienteRepetido() {
        when(doadorRepository.findById(1L)).thenReturn(Optional.of(doador(1L)));
        when(ingredienteRepository.findById(2L)).thenReturn(Optional.of(ingrediente(2L)));
        assertThrows(RegraNegocioException.class,
                () -> registrar(dto(1L, item(2L, "1"), item(2L, "2"))));
        verify(doacaoRepository, never()).save(any());
    }

    @Test
    void falhaQuandoDoadorNaoExiste() {
        when(doadorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class, () -> registrar(dto(1L, item(2L, "1"))));
        verify(doacaoRepository, never()).save(any());
    }

    @Test
    void falhaQuandoIngredienteNaoExiste() {
        when(doadorRepository.findById(1L)).thenReturn(Optional.of(doador(1L)));
        when(ingredienteRepository.findById(2L)).thenReturn(Optional.empty());
        when(statusRepository.findByDescricao(StatusDoacao.PENDENTE))
                .thenReturn(Optional.of(new StatusDoacao(StatusDoacao.PENDENTE)));
        assertThrows(RecursoNaoEncontradoException.class, () -> registrar(dto(1L, item(2L, "1"))));
        verify(doacaoRepository, never()).save(any());
    }

    @Test
    void usaOUsuarioLogadoQuandoOCorpoNaoTrazDoador() {
        when(doadorRepository.findById(1L)).thenReturn(Optional.of(doador(1L)));
        when(ingredienteRepository.findById(2L)).thenReturn(Optional.of(ingrediente(2L)));
        when(statusRepository.findByDescricao(StatusDoacao.PENDENTE))
                .thenReturn(Optional.of(new StatusDoacao(StatusDoacao.PENDENTE)));
        when(doacaoRepository.save(any(Doacao.class))).thenAnswer(inv -> inv.getArgument(0));

        assertEquals(1L, registrar(dto(null, item(2L, "1"))).doadorId());
    }

    @Test
    void impedeRegistrarDoacaoEmNomeDeOutroDoador() {
        assertThrows(AcessoNegadoException.class, () -> service.registrar(dto(7L, item(2L, "1")), 1L));
        verify(doadorRepository, never()).findById(any());
        verify(doacaoRepository, never()).save(any());
    }
}

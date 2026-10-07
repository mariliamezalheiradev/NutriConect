package com.nutriconect.repository;

import com.nutriconect.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DoacaoRepositoryTest {

    @Autowired private DoacaoRepository doacaoRepository;
    @Autowired private DoadorRepository doadorRepository;
    @Autowired private ReceptorRepository receptorRepository;
    @Autowired private IngredienteRepository ingredienteRepository;

    private Doador doador;
    private Receptor receptor;
    private Ingrediente ingrediente;

    @BeforeEach
    void setup() {
        doador = new Doador();
        doador.setNome("Mercado A");
        doador.setEmail("mercado@a.com");
        doador.setSenha("123");
        doador.setDocumento("11111111000111");
        doador = doadorRepository.save(doador);

        receptor = new Receptor();
        receptor.setNome("ONG X");
        receptor.setEmail("ong@x.org");
        receptor.setSenha("123");
        receptor.setCnpj("22222222000122");
        receptor = receptorRepository.save(receptor);

        ingrediente = new Ingrediente();
        ingrediente.setNome("Arroz");
        ingrediente.setCategoria("Grão");
        ingrediente.setUnidadeMedida("kg");
        ingrediente = ingredienteRepository.save(ingrediente);
    }

    private Doacao criarDoacao(StatusDoacao status, LocalDateTime data) {
        Doacao d = new Doacao();
        d.setQuantidade(10.0);
        d.setDataCriacao(data);
        d.setStatus(status);
        d.setDoador(doador);
        d.setIngrediente(ingrediente);
        return d;
    }

    @Test
    @DisplayName("Deve salvar doação e buscar por doador")
    void deveBuscarPorDoador() {
        doacaoRepository.save(criarDoacao(StatusDoacao.PENDENTE, LocalDateTime.now()));
        doacaoRepository.save(criarDoacao(StatusDoacao.CONCLUIDO, LocalDateTime.now()));
        List<Doacao> doacoes = doacaoRepository.findByDoadorId(doador.getId());
        assertThat(doacoes).hasSize(2);
    }

    @Test
    @DisplayName("Deve buscar doações por status")
    void deveBuscarPorStatus() {
        doacaoRepository.save(criarDoacao(StatusDoacao.PENDENTE, LocalDateTime.now()));
        doacaoRepository.save(criarDoacao(StatusDoacao.PENDENTE, LocalDateTime.now()));
        doacaoRepository.save(criarDoacao(StatusDoacao.CONCLUIDO, LocalDateTime.now()));
        List<Doacao> pendentes = doacaoRepository.findByStatus(StatusDoacao.PENDENTE);
        assertThat(pendentes).hasSize(2);
    }

    @Test
    @DisplayName("Deve buscar doações por ingrediente")
    void deveBuscarPorIngrediente() {
        doacaoRepository.save(criarDoacao(StatusDoacao.PENDENTE, LocalDateTime.now()));
        List<Doacao> doacoes = doacaoRepository.findByIngredienteId(ingrediente.getId());
        assertThat(doacoes).hasSize(1);
    }

    @Test
    @DisplayName("Deve buscar doações em intervalo de datas")
    void deveBuscarPorIntervalo() {
        LocalDateTime hoje = LocalDateTime.now();
        doacaoRepository.save(criarDoacao(StatusDoacao.PENDENTE, hoje.minusDays(10)));
        doacaoRepository.save(criarDoacao(StatusDoacao.PENDENTE, hoje.minusDays(2)));
        doacaoRepository.save(criarDoacao(StatusDoacao.PENDENTE, hoje));
        List<Doacao> ultimos7 = doacaoRepository.findByDataCriacaoBetween(
                hoje.minusDays(7), hoje.plusDays(1));
        assertThat(ultimos7).hasSize(2);
    }

    @Test
    @DisplayName("Deve contar doações por doador")
    void deveContarPorDoador() {
        doacaoRepository.save(criarDoacao(StatusDoacao.PENDENTE, LocalDateTime.now()));
        doacaoRepository.save(criarDoacao(StatusDoacao.CONCLUIDO, LocalDateTime.now()));
        long total = doacaoRepository.countByDoadorId(doador.getId());
        assertThat(total).isEqualTo(2);
    }
}
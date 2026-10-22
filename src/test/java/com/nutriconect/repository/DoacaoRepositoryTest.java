package com.nutriconect.repository;

import com.nutriconect.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DoacaoRepositoryTest {

    @Autowired private DoacaoRepository doacaoRepository;
    @Autowired private ItemDoacaoRepository itemDoacaoRepository;
    @Autowired private DoadorRepository doadorRepository;
    @Autowired private ReceptorRepository receptorRepository;
    @Autowired private IngredienteRepository ingredienteRepository;
    @Autowired private StatusDoacaoRepository statusDoacaoRepository;

    private Doador doador;
    private Receptor receptor;
    private Ingrediente arroz;
    private Ingrediente feijao;
    private StatusDoacao pendente;
    private StatusDoacao concluido;

    @BeforeEach
    void setup() {
        doador = new Doador();
        doador.setNome("Mercado");
        doador.setEmail("mercado@email.com");
        doador.setSenha("hash");
        doador = doadorRepository.save(doador);

        receptor = new Receptor();
        receptor.setNome("ONG");
        receptor.setEmail("ong@email.com");
        receptor.setSenha("hash");
        receptor.setEndereco("Rua A, 1");
        receptor = receptorRepository.save(receptor);

        arroz = novoIngrediente("Arroz");
        feijao = novoIngrediente("Feijão");
        pendente = statusDoacaoRepository.save(new StatusDoacao(StatusDoacao.PENDENTE));
        concluido = statusDoacaoRepository.save(new StatusDoacao(StatusDoacao.CONCLUIDO));
    }

    private Ingrediente novoIngrediente(String nome) {
        Ingrediente i = new Ingrediente();
        i.setNome(nome);
        i.setUnidade("kg");
        return ingredienteRepository.save(i);
    }

    private Doacao novaDoacao(StatusDoacao status, Receptor receptor, Ingrediente... ingredientes) {
        Doacao d = new Doacao();
        d.setDoador(doador);
        d.setReceptor(receptor);
        d.setStatus(status);
        for (Ingrediente ing : ingredientes) {
            d.adicionarItem(new ItemDoacao(ing, new BigDecimal("3.50")));
        }
        return doacaoRepository.save(d);
    }

    @Test
    @DisplayName("Deve gravar a doação junto com seus itens")
    void deveGravarItensEmCascata() {
        Doacao salva = novaDoacao(pendente, null, arroz, feijao);
        doacaoRepository.flush();
        assertThat(itemDoacaoRepository.findByDoacaoId(salva.getId())).hasSize(2);
        assertThat(itemDoacaoRepository.findByIngredienteId(arroz.getId()))
                .extracting(ItemDoacao::getQuantidade).containsExactly(new BigDecimal("3.50"));
    }

    @Test
    @DisplayName("Deve buscar doações por doador, receptor e ingrediente")
    void deveBuscarPorRelacionamentos() {
        novaDoacao(pendente, receptor, arroz);
        novaDoacao(pendente, null, feijao);
        assertThat(doacaoRepository.findByDoadorId(doador.getId())).hasSize(2);
        assertThat(doacaoRepository.countByDoadorId(doador.getId())).isEqualTo(2);
        assertThat(doacaoRepository.findByReceptorId(receptor.getId())).hasSize(1);
        assertThat(doacaoRepository.findDistinctByItensIngredienteId(feijao.getId())).hasSize(1);
    }

    @Test
    @DisplayName("Deve buscar doações por status")
    void deveBuscarPorStatus() {
        novaDoacao(pendente, null, arroz);
        novaDoacao(concluido, null, feijao);
        assertThat(doacaoRepository.findByStatusDescricao(StatusDoacao.CONCLUIDO)).hasSize(1);
        assertThat(doacaoRepository.findByStatusDescricao(StatusDoacao.PENDENTE)).hasSize(1);
    }

    @Test
    @DisplayName("Deve buscar doações por período")
    void deveBuscarPorPeriodo() {
        novaDoacao(pendente, null, arroz);
        LocalDate hoje = LocalDate.now();
        assertThat(doacaoRepository.findByDataDoacaoBetween(hoje.minusDays(1), hoje.plusDays(1))).hasSize(1);
        assertThat(doacaoRepository.findByDataDoacaoBetween(hoje.plusDays(1), hoje.plusDays(5))).isEmpty();
    }
}

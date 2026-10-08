package com.nutriconect.repository;

import com.nutriconect.model.Ingrediente;
import com.nutriconect.model.Receita;
import com.nutriconect.model.ReceitaIngrediente;
import com.nutriconect.model.ReceitaIngredienteId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ReceitaRepositoryTest {

    @Autowired private ReceitaRepository receitaRepository;
    @Autowired private IngredienteRepository ingredienteRepository;
    @Autowired private ReceitaIngredienteRepository receitaIngredienteRepository;

    private Receita novaReceita(String nome, String descricao) {
        Receita r = new Receita();
        r.setNome(nome);
        r.setDescricao(descricao);
        r.setModoPreparo("Misture e cozinhe.");
        return r;
    }

    private Ingrediente novoIngrediente(String nome) {
        Ingrediente i = new Ingrediente();
        i.setNome(nome);
        i.setUnidade("kg");
        return ingredienteRepository.save(i);
    }

    @Test
    @DisplayName("Deve buscar receita por nome e por descrição")
    void deveBuscarPorNomeEDescricao() {
        receitaRepository.save(novaReceita("Arroz de Forno", "Aproveita sobras"));
        receitaRepository.save(novaReceita("Sopa", "Legumes variados"));
        assertThat(receitaRepository.findByNomeContainingIgnoreCase("arroz")).hasSize(1);
        assertThat(receitaRepository.findByDescricaoContainingIgnoreCase("legumes")).hasSize(1);
    }

    @Test
    @DisplayName("Deve gravar os ingredientes da receita com chave composta e quantidade")
    void deveGravarIngredientesDaReceita() {
        Ingrediente arroz = novoIngrediente("Arroz");
        Ingrediente frango = novoIngrediente("Frango");
        Receita receita = novaReceita("Arroz com frango", "Prato simples");
        receita.adicionarIngrediente(new ReceitaIngrediente(arroz, new BigDecimal("0.50")));
        receita.adicionarIngrediente(new ReceitaIngrediente(frango, new BigDecimal("1.25")));

        Receita salva = receitaRepository.saveAndFlush(receita);

        assertThat(receitaIngredienteRepository.findByReceitaId(salva.getId())).hasSize(2);
        assertThat(receitaIngredienteRepository.findById(new ReceitaIngredienteId(salva.getId(), frango.getId())))
                .get().extracting(ReceitaIngrediente::getQuantidade).isEqualTo(new BigDecimal("1.25"));
        assertThat(receitaIngredienteRepository.findByIngredienteId(arroz.getId())).hasSize(1);
    }
}

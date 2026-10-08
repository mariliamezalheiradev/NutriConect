package com.nutriconect.repository;

import com.nutriconect.model.Receita;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ReceitaRepositoryTest {

    @Autowired
    private ReceitaRepository receitaRepository;

    private Receita criarReceita(String titulo, String descricao, String modo) {
        Receita r = new Receita();
        r.setTitulo(titulo);
        r.setDescricao(descricao);
        r.setModoPreparo(modo);
        return r;
    }

    @Test
    @DisplayName("Deve buscar receitas por título parcial")
    void deveBuscarPorTitulo() {
        receitaRepository.save(criarReceita("Sopa de legumes", "Sopa nutritiva", "Refogue tudo"));
        receitaRepository.save(criarReceita("Sopa de feijão", "Sopa cremosa", "Bata no mixer"));
        receitaRepository.save(criarReceita("Torta de frango", "Torta salgada", "Asse por 30 min"));
        List<Receita> sopas = receitaRepository.findByTituloContainingIgnoreCase("sopa");
        assertThat(sopas).hasSize(2);
    }

    @Test
    @DisplayName("Deve buscar receitas por descrição parcial")
    void deveBuscarPorDescricao() {
        receitaRepository.save(criarReceita("Bolo", "Bolo de banana madura", "Misture"));
        receitaRepository.save(criarReceita("Suco", "Suco de casca de abacaxi", "Bata"));
        receitaRepository.save(criarReceita("Pão", "Pão caseiro simples", "Sove"));
        List<Receita> comBanana = receitaRepository.findByDescricaoContainingIgnoreCase("banana");
        assertThat(comBanana).hasSize(1);
    }
}
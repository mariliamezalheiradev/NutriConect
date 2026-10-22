package com.nutriconect.repository;

import com.nutriconect.model.Ingrediente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class IngredienteRepositoryTest {

    @Autowired private IngredienteRepository repository;

    private Ingrediente criar(String nome, String categoria, String unidade, LocalDate validade) {
        Ingrediente i = new Ingrediente();
        i.setNome(nome);
        i.setCategoria(categoria);
        i.setUnidade(unidade);
        i.setValidade(validade);
        return repository.save(i);
    }

    @Test
    @DisplayName("Deve buscar por nome ignorando maiúsculas")
    void deveBuscarPorNome() {
        criar("Arroz Integral", "Grão", "kg", null);
        criar("Feijão", "Grão", "kg", null);
        assertThat(repository.findByNomeContainingIgnoreCase("arroz")).hasSize(1);
    }

    @Test
    @DisplayName("Deve buscar por categoria e por unidade")
    void deveBuscarPorCategoriaEUnidade() {
        criar("Arroz", "Grão", "kg", null);
        criar("Leite", "Laticínio", "L", null);
        assertThat(repository.findByCategoriaIgnoreCase("grão")).hasSize(1);
        assertThat(repository.findByUnidadeIgnoreCase("l")).hasSize(1);
    }

    @Test
    @DisplayName("Deve listar ingredientes com validade anterior a uma data")
    void deveBuscarPorValidade() {
        criar("Iogurte", "Laticínio", "un", LocalDate.now().minusDays(1));
        criar("Arroz", "Grão", "kg", LocalDate.now().plusDays(90));
        criar("Sal", "Tempero", "kg", null);
        List<Ingrediente> vencidos = repository.findByValidadeBefore(LocalDate.now());
        assertThat(vencidos).extracting(Ingrediente::getNome).containsExactly("Iogurte");
    }
}

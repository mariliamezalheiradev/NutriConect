package com.nutriconect.repository;

import com.nutriconect.model.Ingrediente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class IngredienteRepositoryTest {

    @Autowired
    private IngredienteRepository ingredienteRepository;

    private Ingrediente criarIngrediente(String nome, String categoria, String unidade) {
        Ingrediente i = new Ingrediente();
        i.setNome(nome);
        i.setCategoria(categoria);
        i.setUnidadeMedida(unidade);
        return i;
    }

    @Test
    @DisplayName("Deve buscar ingrediente por nome parcial")
    void deveBuscarPorNomeParcial() {
        ingredienteRepository.save(criarIngrediente("Arroz", "Grão", "kg"));
        ingredienteRepository.save(criarIngrediente("Arroz Integral", "Grão", "kg"));
        ingredienteRepository.save(criarIngrediente("Feijão", "Grão", "kg"));
        List<Ingrediente> encontrados = ingredienteRepository.findByNomeContainingIgnoreCase("arroz");
        assertThat(encontrados).hasSize(2);
    }

    @Test
    @DisplayName("Deve buscar ingredientes por categoria")
    void deveBuscarPorCategoria() {
        ingredienteRepository.save(criarIngrediente("Alface", "Verdura", "un"));
        ingredienteRepository.save(criarIngrediente("Cenoura", "Legume", "kg"));
        ingredienteRepository.save(criarIngrediente("Rúcula", "Verdura", "un"));
        List<Ingrediente> verduras = ingredienteRepository.findByCategoriaIgnoreCase("verdura");
        assertThat(verduras).hasSize(2);
    }

    @Test
    @DisplayName("Deve buscar ingredientes por unidade de medida")
    void deveBuscarPorUnidadeMedida() {
        ingredienteRepository.save(criarIngrediente("Tomate", "Legume", "kg"));
        ingredienteRepository.save(criarIngrediente("Ovo", "Proteína", "un"));
        ingredienteRepository.save(criarIngrediente("Cebola", "Legume", "kg"));
        List<Ingrediente> porKg = ingredienteRepository.findByUnidadeMedidaIgnoreCase("kg");
        assertThat(porKg).hasSize(2);
    }
}
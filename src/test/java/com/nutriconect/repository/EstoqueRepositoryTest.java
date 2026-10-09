package com.nutriconect.repository;

import com.nutriconect.model.Estoque;
import com.nutriconect.model.Ingrediente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class EstoqueRepositoryTest {

    @Autowired private EstoqueRepository estoqueRepository;
    @Autowired private IngredienteRepository ingredienteRepository;

    private Ingrediente arroz;
    private Ingrediente feijao;

    @BeforeEach
    void setup() {
        arroz = novoIngrediente("Arroz");
        feijao = novoIngrediente("Feijão");
    }

    private Ingrediente novoIngrediente(String nome) {
        Ingrediente i = new Ingrediente();
        i.setNome(nome);
        i.setUnidade("kg");
        return ingredienteRepository.save(i);
    }

    private Estoque novoEstoque(Ingrediente ingrediente, String quantidade) {
        Estoque e = new Estoque();
        e.setIngrediente(ingrediente);
        e.setQuantidade(new BigDecimal(quantidade));
        return e;
    }

    @Test
    @DisplayName("Deve buscar o estoque pelo ingrediente")
    void deveBuscarPorIngrediente() {
        estoqueRepository.save(novoEstoque(arroz, "10.50"));
        assertThat(estoqueRepository.findByIngredienteId(arroz.getId()))
                .get().extracting(Estoque::getQuantidade).isEqualTo(new BigDecimal("10.50"));
        assertThat(estoqueRepository.findByIngredienteId(feijao.getId())).isEmpty();
    }

    @Test
    @DisplayName("Deve listar estoques abaixo de uma quantidade")
    void deveListarEstoqueBaixo() {
        estoqueRepository.save(novoEstoque(arroz, "2.00"));
        estoqueRepository.save(novoEstoque(feijao, "50.00"));
        assertThat(estoqueRepository.findByQuantidadeLessThan(new BigDecimal("5")))
                .extracting(e -> e.getIngrediente().getNome()).containsExactly("Arroz");
    }

    @Test
    @DisplayName("Cada ingrediente só pode ter uma linha de estoque (relação 1:1)")
    void deveImpedirEstoqueDuplicado() {
        estoqueRepository.saveAndFlush(novoEstoque(arroz, "1"));
        assertThatThrownBy(() -> estoqueRepository.saveAndFlush(novoEstoque(arroz, "2")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}

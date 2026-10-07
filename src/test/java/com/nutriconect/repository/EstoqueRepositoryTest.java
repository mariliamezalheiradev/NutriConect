package com.nutriconect.repository;

import com.nutriconect.model.Doador;
import com.nutriconect.model.Estoque;
import com.nutriconect.model.Ingrediente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EstoqueRepositoryTest {

    @Autowired private EstoqueRepository estoqueRepository;
    @Autowired private DoadorRepository doadorRepository;
    @Autowired private IngredienteRepository ingredienteRepository;

    private Doador doador;
    private Ingrediente ingrediente;

    @BeforeEach
    void setup() {
        doador = new Doador();
        doador.setNome("Doador A");
        doador.setEmail("doador@a.com");
        doador.setSenha("123");
        doador.setDocumento("77777777000177");
        doador = doadorRepository.save(doador);

        ingrediente = new Ingrediente();
        ingrediente.setNome("Arroz");
        ingrediente.setCategoria("Grão");
        ingrediente.setUnidadeMedida("kg");
        ingrediente = ingredienteRepository.save(ingrediente);
    }

    private Estoque criarEstoque(Double qtd, LocalDate validade) {
        Estoque e = new Estoque();
        e.setQuantidade(qtd);
        e.setDataValidade(validade);
        e.setDoador(doador);
        e.setIngrediente(ingrediente);
        return e;
    }

    @Test
    @DisplayName("Deve buscar estoque por doador")
    void deveBuscarPorDoador() {
        estoqueRepository.save(criarEstoque(10.0, LocalDate.now().plusDays(30)));
        estoqueRepository.save(criarEstoque(5.0, LocalDate.now().plusDays(60)));
        List<Estoque> estoques = estoqueRepository.findByDoadorId(doador.getId());
        assertThat(estoques).hasSize(2);
    }

    @Test
    @DisplayName("Deve buscar estoque por ingrediente")
    void deveBuscarPorIngrediente() {
        estoqueRepository.save(criarEstoque(10.0, LocalDate.now().plusDays(30)));
        List<Estoque> estoques = estoqueRepository.findByIngredienteId(ingrediente.getId());
        assertThat(estoques).hasSize(1);
    }

    @Test
    @DisplayName("Deve buscar estoques vencidos")
    void deveBuscarVencidos() {
        estoqueRepository.save(criarEstoque(10.0, LocalDate.now().minusDays(5)));
        estoqueRepository.save(criarEstoque(5.0, LocalDate.now().plusDays(10)));
        List<Estoque> vencidos = estoqueRepository.findByDataValidadeBefore(LocalDate.now());
        assertThat(vencidos).hasSize(1);
    }

    @Test
    @DisplayName("Deve buscar estoques ainda válidos")
    void deveBuscarValidos() {
        estoqueRepository.save(criarEstoque(10.0, LocalDate.now().minusDays(5)));
        estoqueRepository.save(criarEstoque(5.0, LocalDate.now().plusDays(10)));
        estoqueRepository.save(criarEstoque(3.0, LocalDate.now().plusDays(20)));
        List<Estoque> validos = estoqueRepository.findByDataValidadeAfter(LocalDate.now());
        assertThat(validos).hasSize(2);
    }
}
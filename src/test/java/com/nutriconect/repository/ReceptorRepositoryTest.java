package com.nutriconect.repository;

import com.nutriconect.model.Receptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ReceptorRepositoryTest {

    @Autowired
    private ReceptorRepository receptorRepository;

    private Receptor criarReceptor(String nome, String email, String cnpj, String endereco) {
        Receptor r = new Receptor();
        r.setNome(nome);
        r.setEmail(email);
        r.setSenha("senha123");
        r.setCnpj(cnpj);
        r.setEndereco(endereco);
        return r;
    }

    @Test
    @DisplayName("Deve salvar receptor e buscar por CNPJ")
    void deveBuscarPorCnpj() {
        receptorRepository.save(criarReceptor("ONG Esperança", "ong@esperanca.org",
                "11111111000111", "Rua A, 100 - São Paulo"));
        Optional<Receptor> encontrado = receptorRepository.findByCnpj("11111111000111");
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("ONG Esperança");
        assertThat(encontrado.get().getEndereco()).contains("São Paulo");
    }

    @Test
    @DisplayName("Deve retornar true quando CNPJ existe")
    void deveRetornarTrueQuandoCnpjExiste() {
        receptorRepository.save(criarReceptor("ONG Luz", "luz@ong.org",
                "22222222000122", "Rua B, 200"));
        assertThat(receptorRepository.existsByCnpj("22222222000122")).isTrue();
        assertThat(receptorRepository.existsByCnpj("00000000000000")).isFalse();
    }

    @Test
    @DisplayName("Deve buscar receptor por email")
    void deveBuscarPorEmail() {
        receptorRepository.save(criarReceptor("Cozinha Comunitária", "cc@x.org",
                "33333333000133", "Rua C, 300"));
        Optional<Receptor> encontrado = receptorRepository.findByEmail("cc@x.org");
        assertThat(encontrado).isPresent();
    }

    @Test
    @DisplayName("Deve buscar receptores por endereço parcial")
    void deveBuscarPorEndereco() {
        receptorRepository.save(criarReceptor("ONG 1", "o1@x.org", "44444444000144", "Rua das Flores, 10"));
        receptorRepository.save(criarReceptor("ONG 2", "o2@x.org", "55555555000155", "Av. Brasil, 500"));
        receptorRepository.save(criarReceptor("ONG 3", "o3@x.org", "66666666000166", "Rua das Flores, 20"));
        List<Receptor> encontrados = receptorRepository.findByEnderecoContainingIgnoreCase("flores");
        assertThat(encontrados).hasSize(2);
    }
}
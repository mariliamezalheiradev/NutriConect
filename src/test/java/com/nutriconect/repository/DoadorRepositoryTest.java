package com.nutriconect.repository;

import com.nutriconect.model.Doador;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class DoadorRepositoryTest {

    @Autowired
    private DoadorRepository doadorRepository;

    private Doador criarDoador(String nome, String email, String doc) {
        Doador d = new Doador();
        d.setNome(nome);
        d.setEmail(email);
        d.setSenha("senha123");
        d.setDocumento(doc);
        return d;
    }

    @Test
    @DisplayName("Deve salvar doador e buscar por documento")
    void deveBuscarPorDocumento() {
        doadorRepository.save(criarDoador("Mercado Bom Preço", "contato@bompreco.com", "11111111000111"));
        Optional<Doador> encontrado = doadorRepository.findByDocumento("11111111000111");
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("Mercado Bom Preço");
    }

    @Test
    @DisplayName("Deve retornar true quando documento existe")
    void deveRetornarTrueQuandoDocumentoExiste() {
        doadorRepository.save(criarDoador("Padaria A", "pada@x.com", "22222222000122"));
        assertThat(doadorRepository.existsByDocumento("22222222000122")).isTrue();
        assertThat(doadorRepository.existsByDocumento("99999999000199")).isFalse();
    }

    @Test
    @DisplayName("Deve buscar doador por email")
    void deveBuscarPorEmail() {
        doadorRepository.save(criarDoador("Restaurante C", "rest@c.com", "33333333000133"));
        Optional<Doador> encontrado = doadorRepository.findByEmail("rest@c.com");
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("Restaurante C");
    }

    @Test
    @DisplayName("Deve buscar doadores por parte do nome")
    void deveBuscarPorNomeParcial() {
        doadorRepository.save(criarDoador("Mercado Alpha", "a@x.com", "44444444000144"));
        doadorRepository.save(criarDoador("Mercado Beta", "b@x.com", "55555555000155"));
        doadorRepository.save(criarDoador("Padaria Gama", "g@x.com", "66666666000166"));
        List<Doador> encontrados = doadorRepository.findByNomeContainingIgnoreCase("mercado");
        assertThat(encontrados).hasSize(2);
    }
}
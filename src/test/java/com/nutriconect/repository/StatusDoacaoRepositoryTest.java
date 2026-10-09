package com.nutriconect.repository;

import com.nutriconect.model.StatusDoacao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class StatusDoacaoRepositoryTest {

    @Autowired private StatusDoacaoRepository repository;

    @Test
    @DisplayName("Deve buscar o status pela descrição")
    void deveBuscarPorDescricao() {
        repository.save(new StatusDoacao(StatusDoacao.PENDENTE));
        assertThat(repository.findByDescricao(StatusDoacao.PENDENTE)).isPresent();
        assertThat(repository.findByDescricao(StatusDoacao.CANCELADO)).isEmpty();
    }

    @Test
    @DisplayName("A descrição do status deve ser única")
    void deveImpedirDescricaoDuplicada() {
        repository.saveAndFlush(new StatusDoacao(StatusDoacao.PENDENTE));
        assertThatThrownBy(() -> repository.saveAndFlush(new StatusDoacao(StatusDoacao.PENDENTE)))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}

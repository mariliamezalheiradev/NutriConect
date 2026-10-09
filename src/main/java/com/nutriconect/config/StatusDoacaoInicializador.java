package com.nutriconect.config;

import com.nutriconect.model.StatusDoacao;
import com.nutriconect.repository.StatusDoacaoRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/** Garante que a tabela de domínio `status_doação` tenha os valores padrão ao iniciar. */
@Component
public class StatusDoacaoInicializador implements ApplicationRunner {

    private final StatusDoacaoRepository repository;

    public StatusDoacaoInicializador(StatusDoacaoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(ApplicationArguments args) {
        List.of(StatusDoacao.PENDENTE, StatusDoacao.EM_ANDAMENTO,
                        StatusDoacao.CONCLUIDO, StatusDoacao.CANCELADO)
                .forEach(descricao -> {
                    if (repository.findByDescricao(descricao).isEmpty()) {
                        repository.save(new StatusDoacao(descricao));
                    }
                });
    }
}

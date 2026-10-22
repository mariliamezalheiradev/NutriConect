package com.nutriconect.repository;

import com.nutriconect.model.StatusDoacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusDoacaoRepository extends JpaRepository<StatusDoacao, Long> {

    Optional<StatusDoacao> findByDescricao(String descricao);
}

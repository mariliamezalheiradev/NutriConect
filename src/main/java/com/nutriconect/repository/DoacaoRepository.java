package com.nutriconect.repository;

import com.nutriconect.model.Doacao;
import com.nutriconect.model.StatusDoacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DoacaoRepository extends JpaRepository<Doacao, Long> {

    List<Doacao> findByDoadorId(Long doadorId);

    List<Doacao> findByReceptorId(Long receptorId);

    List<Doacao> findByIngredienteId(Long ingredienteId);

    List<Doacao> findByStatus(StatusDoacao status);

    List<Doacao> findByDataCriacaoBetween(LocalDateTime inicio, LocalDateTime fim);

    long countByDoadorId(Long doadorId);
}
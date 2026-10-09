package com.nutriconect.repository;

import com.nutriconect.model.Doacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DoacaoRepository extends JpaRepository<Doacao, Long> {

    List<Doacao> findByDoadorId(Long doadorId);

    List<Doacao> findByReceptorId(Long receptorId);

    List<Doacao> findDistinctByItensIngredienteId(Long ingredienteId);

    List<Doacao> findByStatusDescricao(String descricao);

    List<Doacao> findByDataDoacaoBetween(LocalDate inicio, LocalDate fim);

    long countByDoadorId(Long doadorId);
}

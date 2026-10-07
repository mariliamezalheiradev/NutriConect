package com.nutriconect.repository;

import com.nutriconect.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    List<Estoque> findByDoadorId(Long doadorId);

    List<Estoque> findByIngredienteId(Long ingredienteId);

    List<Estoque> findByDataValidadeBefore(LocalDate data);

    List<Estoque> findByDataValidadeAfter(LocalDate data);
}
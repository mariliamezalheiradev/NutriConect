package com.nutriconect.repository;

import com.nutriconect.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, Long> {

    Optional<Estoque> findByIngredienteId(Long ingredienteId);

    List<Estoque> findByQuantidadeLessThan(BigDecimal quantidade);
}

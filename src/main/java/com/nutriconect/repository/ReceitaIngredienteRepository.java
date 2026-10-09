package com.nutriconect.repository;

import com.nutriconect.model.ReceitaIngrediente;
import com.nutriconect.model.ReceitaIngredienteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceitaIngredienteRepository extends JpaRepository<ReceitaIngrediente, ReceitaIngredienteId> {

    List<ReceitaIngrediente> findByReceitaId(Long receitaId);

    List<ReceitaIngrediente> findByIngredienteId(Long ingredienteId);
}

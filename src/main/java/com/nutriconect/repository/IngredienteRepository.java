package com.nutriconect.repository;

import com.nutriconect.model.Ingrediente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {

    List<Ingrediente> findByNomeContainingIgnoreCase(String nome);

    List<Ingrediente> findByCategoriaIgnoreCase(String categoria);

    List<Ingrediente> findByUnidadeIgnoreCase(String unidade);

    List<Ingrediente> findByValidadeBefore(LocalDate data);
}

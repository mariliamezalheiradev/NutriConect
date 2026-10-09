package com.nutriconect.repository;

import com.nutriconect.model.Receita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReceitaRepository extends JpaRepository<Receita, Long> {

    List<Receita> findByNomeContainingIgnoreCase(String nome);

    List<Receita> findByDescricaoContainingIgnoreCase(String termo);
}

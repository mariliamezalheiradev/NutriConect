package com.nutriconect.repository;

import com.nutriconect.model.Doador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoadorRepository extends JpaRepository<Doador, Long> {

    Optional<Doador> findByDocumento(String documento);

    boolean existsByDocumento(String documento);

    Optional<Doador> findByEmail(String email);

    List<Doador> findByNomeContainingIgnoreCase(String nome);
}
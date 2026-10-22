package com.nutriconect.repository;

import com.nutriconect.model.Receptor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReceptorRepository extends JpaRepository<Receptor, Long> {

    Optional<Receptor> findByCnpj(String cnpj);

    boolean existsByCnpj(String cnpj);

    Optional<Receptor> findByEmail(String email);

    List<Receptor> findByEnderecoContainingIgnoreCase(String endereco);
}
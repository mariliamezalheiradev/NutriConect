package com.nutriconect.repository;

import com.nutriconect.model.ItemDoacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemDoacaoRepository extends JpaRepository<ItemDoacao, Long> {

    List<ItemDoacao> findByDoacaoId(Long doacaoId);

    List<ItemDoacao> findByIngredienteId(Long ingredienteId);
}

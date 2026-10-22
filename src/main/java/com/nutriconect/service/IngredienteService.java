package com.nutriconect.service;

import com.nutriconect.dto.IngredienteDTO;
import com.nutriconect.model.Ingrediente;
import com.nutriconect.repository.IngredienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredienteService {

    private final IngredienteRepository ingredienteRepository;

    public IngredienteService(IngredienteRepository ingredienteRepository) {
        this.ingredienteRepository = ingredienteRepository;
    }

    public Ingrediente cadastrar(IngredienteDTO dto) {
        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setNome(dto.getNome());
        ingrediente.setCategoria(dto.getCategoria());
        ingrediente.setUnidade(dto.getUnidade());
        ingrediente.setValidade(dto.getValidade());
        return ingredienteRepository.save(ingrediente);
    }

    public List<Ingrediente> listar() {
        return ingredienteRepository.findAll();
    }
}

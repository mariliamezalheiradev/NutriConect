package com.nutriconect.service;

import com.nutriconect.dto.DoacaoRequestDTO;
import org.springframework.stereotype.Service;

@Service
public class DoacaoService {

    public String processarDoacao(DoacaoRequestDTO dto) {
        if (dto.getIngredientes() == null || dto.getIngredientes().isEmpty()) {
            throw new IllegalArgumentException("A lista de ingredientes não pode estar vazia.");
        }
        return "Doação processada com sucesso para o doador ID: " + dto.getDoadorId();
    }
}
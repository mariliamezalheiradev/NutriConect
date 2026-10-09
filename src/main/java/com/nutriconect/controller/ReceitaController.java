package com.nutriconect.controller;

import com.nutriconect.dto.GerarReceitaRequestDTO;
import com.nutriconect.service.IAGenerativaReceitas;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/receitas")
public class ReceitaController {

    private final IAGenerativaReceitas ia;

    public ReceitaController(IAGenerativaReceitas ia) {
        this.ia = ia;
    }

    @PostMapping("/gerar")
    public ResponseEntity<Map<String, String>> gerar(@Valid @RequestBody GerarReceitaRequestDTO dto) {
        String receita = ia.gerarReceitaAproveitamentoTotal(dto.getIngredientes());
        return ResponseEntity.ok(Map.of("receita", receita));
    }
}

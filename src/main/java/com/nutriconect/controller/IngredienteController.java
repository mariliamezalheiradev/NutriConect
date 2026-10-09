package com.nutriconect.controller;

import com.nutriconect.dto.IngredienteDTO;
import com.nutriconect.model.Ingrediente;
import com.nutriconect.service.IngredienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredientes")
public class IngredienteController {

    private final IngredienteService ingredienteService;

    public IngredienteController(IngredienteService ingredienteService) {
        this.ingredienteService = ingredienteService;
    }

    @PostMapping
    public ResponseEntity<Ingrediente> cadastrar(@Valid @RequestBody IngredienteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ingredienteService.cadastrar(dto));
    }

    @GetMapping
    public List<Ingrediente> listar() {
        return ingredienteService.listar();
    }
}

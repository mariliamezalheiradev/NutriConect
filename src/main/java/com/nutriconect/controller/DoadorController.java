package com.nutriconect.controller;

import com.nutriconect.dto.DoadorDTO;
import com.nutriconect.dto.UsuarioResponseDTO;
import com.nutriconect.service.DoadorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doadores")
public class DoadorController {

    private final DoadorService doadorService;

    public DoadorController(DoadorService doadorService) {
        this.doadorService = doadorService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@Valid @RequestBody DoadorDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doadorService.cadastrar(dto));
    }
}

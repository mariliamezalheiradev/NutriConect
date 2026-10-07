package com.nutriconect.controller;

import com.nutriconect.dto.DoacaoRequestDTO;
import com.nutriconect.service.DoacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doacoes")
public class DoacaoController {

    @Autowired
    private DoacaoService doacaoService;

    @PostMapping
    public ResponseEntity<String> criarDoacao(@Valid @RequestBody DoacaoRequestDTO dto) {
        String resultado = doacaoService.processarDoacao(dto);
        return ResponseEntity.ok(resultado);
    }
}
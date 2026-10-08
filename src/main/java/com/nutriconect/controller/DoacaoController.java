package com.nutriconect.controller;

import com.nutriconect.dto.DoacaoDTO;
import com.nutriconect.dto.DoacaoResponseDTO;
import com.nutriconect.service.DoacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doacoes")
public class DoacaoController {

    private final DoacaoService doacaoService;

    public DoacaoController(DoacaoService doacaoService) {
        this.doacaoService = doacaoService;
    }

    @PostMapping
    public ResponseEntity<DoacaoResponseDTO> criarDoacao(@Valid @RequestBody DoacaoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doacaoService.registrar(dto));
    }
}

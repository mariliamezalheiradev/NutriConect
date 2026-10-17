package com.nutriconect.controller;

import com.nutriconect.dto.ReceptorDTO;
import com.nutriconect.dto.ReceptorProximoDTO;
import com.nutriconect.dto.UsuarioResponseDTO;
import com.nutriconect.service.ReceptorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/receptores")
public class ReceptorController {

    private final ReceptorService receptorService;

    public ReceptorController(ReceptorService receptorService) {
        this.receptorService = receptorService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@Valid @RequestBody ReceptorDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(receptorService.cadastrar(dto));
    }

    @GetMapping("/proximos")
    public List<ReceptorProximoDTO> buscarProximos(@RequestParam String cep,
                                                   @RequestParam(defaultValue = "10") double raioKm) {
        return receptorService.buscarProximos(cep, raioKm);
    }
}
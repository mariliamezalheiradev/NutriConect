package com.nutriconect.controller;

import com.nutriconect.dto.DoacaoDTO;
import com.nutriconect.dto.DoacaoResponseDTO;
import com.nutriconect.security.UsuarioLogado;
import com.nutriconect.service.DoacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doacoes")
public class DoacaoController {

    private final DoacaoService doacaoService;

    public DoacaoController(DoacaoService doacaoService) {
        this.doacaoService = doacaoService;
    }

    /** O doador é sempre o usuário logado; só ele pode registrar doações em seu nome. */
    @PostMapping
    public ResponseEntity<DoacaoResponseDTO> criarDoacao(@Valid @RequestBody DoacaoDTO dto,
                                                         JwtAuthenticationToken autenticacao) {
        Long doadorLogado = UsuarioLogado.id(autenticacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(doacaoService.registrar(dto, doadorLogado));
    }
}

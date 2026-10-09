package com.nutriconect.service;

import com.nutriconect.dto.DoadorDTO;
import com.nutriconect.dto.UsuarioResponseDTO;
import com.nutriconect.exception.RegraNegocioException;
import com.nutriconect.model.Doador;
import com.nutriconect.model.Usuario;
import com.nutriconect.repository.DoadorRepository;
import com.nutriconect.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DoadorServiceTest {

    private final DoadorRepository doadorRepository = Mockito.mock(DoadorRepository.class);
    private final UsuarioRepository usuarioRepository = Mockito.mock(UsuarioRepository.class);
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    private final DoadorService service = new DoadorService(doadorRepository, usuarioRepository, encoder);

    private DoadorDTO dto() {
        DoadorDTO dto = new DoadorDTO();
        dto.setNome("Maria");
        dto.setEmail("maria@email.com");
        dto.setSenha("segredo123");
        return dto;
    }

    @Test
    void salvaSenhaCriptografadaENaoDevolveSenha() {
        when(usuarioRepository.findByEmail("maria@email.com")).thenReturn(Optional.empty());
        when(doadorRepository.save(any(Doador.class))).thenAnswer(inv -> {
            Doador d = inv.getArgument(0);
            d.setId(1L);
            return d;
        });

        UsuarioResponseDTO resposta = service.cadastrar(dto());

        ArgumentCaptor<Doador> captor = ArgumentCaptor.forClass(Doador.class);
        verify(doadorRepository).save(captor.capture());
        String salva = captor.getValue().getSenha();
        assertNotEquals("segredo123", salva);
        assertTrue(encoder.matches("segredo123", salva));
        assertEquals(1L, resposta.id());
        assertEquals("maria@email.com", resposta.email());
    }

    @Test
    void rejeitaEmailDuplicado() {
        when(usuarioRepository.findByEmail("maria@email.com")).thenReturn(Optional.of(new Usuario()));
        assertThrows(RegraNegocioException.class, () -> service.cadastrar(dto()));
        verify(doadorRepository, never()).save(any());
    }
}

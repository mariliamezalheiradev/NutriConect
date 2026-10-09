package com.nutriconect.repository;

import com.nutriconect.model.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private Usuario criarUsuario(String nome, String email, String senha, String telefone) {
        Usuario u = new Usuario();
        u.setNome(nome);
        u.setEmail(email);
        u.setSenha(senha);
        u.setTelefone(telefone);
        return u;
    }

    @Test
    @DisplayName("Deve salvar usuário e buscar por email")
    void deveSalvarEBuscarPorEmail() {
        usuarioRepository.save(criarUsuario("Maria Souza", "maria@nutriconect.org", "senha123", "11999990000"));

        Optional<Usuario> encontrado = usuarioRepository.findByEmail("maria@nutriconect.org");

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("Maria Souza");
    }

    @Test
    @DisplayName("Deve retornar vazio quando email não existe")
    void deveRetornarVazioQuandoEmailNaoExiste() {
        assertThat(usuarioRepository.findByEmail("naoexiste@x.com")).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar true quando email existe")
    void deveRetornarTrueQuandoEmailExiste() {
        usuarioRepository.save(criarUsuario("Ana", "ana@x.com", "abc", "11888887777"));

        assertThat(usuarioRepository.existsByEmail("ana@x.com")).isTrue();
        assertThat(usuarioRepository.existsByEmail("ninguem@x.com")).isFalse();
    }

    @Test
    @DisplayName("Deve buscar usuário por telefone")
    void deveBuscarPorTelefone() {
        usuarioRepository.save(criarUsuario("João", "joao@x.com", "xyz", "11777776666"));

        Optional<Usuario> encontrado = usuarioRepository.findByTelefone("11777776666");
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("João");
    }
}

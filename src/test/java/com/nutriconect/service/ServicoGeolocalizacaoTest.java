package com.nutriconect.service;

import com.nutriconect.dto.Coordenadas;
import com.nutriconect.dto.ReceptorProximoDTO;
import com.nutriconect.model.Receptor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServicoGeolocalizacaoTest {

    private final ServicoGeolocalizacao servico = new ServicoGeolocalizacao();

    private Receptor receptor(long id, String nome, Double lat, Double lon) {
        Receptor r = new Receptor();
        r.setId(id);
        r.setNome(nome);
        r.setLatitude(lat);
        r.setLongitude(lon);
        return r;
    }

    @Test
    void distanciaEntreSaoPauloERioFicaPertoDe360Km() {
        double km = servico.calcularDistanciaHaversine(-23.5505, -46.6333, -22.9068, -43.1729);
        assertEquals(360, km, 8);
    }

    @Test
    void mantemSoQuemEstaNoRaioEOrdenaPorDistancia() {
        Coordenadas origem = new Coordenadas(-23.5505, -46.6333);
        List<Receptor> todos = List.of(
                receptor(2, "Média", -23.5900, -46.6800),
                receptor(1, "Perto", -23.5610, -46.6420),
                receptor(3, "Longe", -22.9068, -43.1729),
                receptor(4, "Sem coordenadas", null, null));

        List<ReceptorProximoDTO> resultado = servico.ordenarPorProximidade(origem, todos, 10);

        assertEquals(2, resultado.size());
        assertEquals("Perto", resultado.get(0).nome());
        assertEquals("Média", resultado.get(1).nome());
        assertTrue(resultado.get(0).distanciaKm() < resultado.get(1).distanciaKm());
    }
}
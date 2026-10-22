package com.nutriconect.service;

import com.nutriconect.dto.Coordenadas;
import com.nutriconect.dto.ReceptorProximoDTO;
import com.nutriconect.model.Receptor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ServicoGeolocalizacao {

    private static final double RAIO_TERRA_KM = 6371.0;

    /** Distância em linha reta, em km, entre dois pontos da Terra (fórmula de Haversine). */
    public double calcularDistanciaHaversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.pow(Math.sin(dLon / 2), 2);
        return 2 * RAIO_TERRA_KM * Math.asin(Math.sqrt(a));
    }

    /** Mantém só os receptores dentro do raio e ordena do mais perto para o mais longe. */
    public List<ReceptorProximoDTO> ordenarPorProximidade(Coordenadas origem,
                                                          List<Receptor> receptores,
                                                          double raioKm) {
        return receptores.stream()
                .filter(r -> r.getLatitude() != null && r.getLongitude() != null)
                .map(r -> new ReceptorProximoDTO(r.getId(), r.getNome(), r.getEndereco(), r.getCep(),
                        arredondar(calcularDistanciaHaversine(origem.latitude(), origem.longitude(),
                                r.getLatitude(), r.getLongitude()))))
                .filter(d -> d.distanciaKm() <= raioKm)
                .sorted(Comparator.comparingDouble(ReceptorProximoDTO::distanciaKm))
                .toList();
    }

    private double arredondar(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}
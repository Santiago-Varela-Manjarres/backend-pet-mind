package com.cesde.petmind.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cesde.petmind.model.entity.GastoCampana;
import com.cesde.petmind.repository.GastoCampanaRepository;
import com.cesde.petmind.service.GastoCampanaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GastoCampanaServiceImpl implements GastoCampanaService {

    private final GastoCampanaRepository gastoCampanaRepository;

    @Override
    public List<GastoCampana> listar() {
        return gastoCampanaRepository.findByEstadoActivoTrue();
    }

    @Override
    public GastoCampana obtenerPorId(Long id) {
        GastoCampana gasto = gastoCampanaRepository.findById(id).orElse(null);

        if (gasto == null || !gasto.getEstadoActivo()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No existe un gasto de campana con id " + id
            );
        }

        return gasto;
    }

    @Override
    public GastoCampana crear(GastoCampana gasto) {
        return gastoCampanaRepository.save(gasto);
    }

    @Override
    public GastoCampana actualizar(Long id, GastoCampana gasto) {
        GastoCampana existente = obtenerPorId(id);

        existente.setConcepto(gasto.getConcepto());
        existente.setMonto(gasto.getMonto());
        existente.setOrden(gasto.getOrden());

        return gastoCampanaRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        GastoCampana gasto = obtenerPorId(id);
        gasto.setEstadoActivo(false);
        gastoCampanaRepository.save(gasto);
    }
}
package com.cesde.petmind.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cesde.petmind.model.entity.CampanaDonacion;
import com.cesde.petmind.repository.CampanaDonacionRepository;
import com.cesde.petmind.service.CampanaDonacionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CampanaDonacionServiceImpl implements CampanaDonacionService {

    private final CampanaDonacionRepository campanaDonacionRepository;

    @Override
    public List<CampanaDonacion> listar() {
        return campanaDonacionRepository.findByEstadoActivoTrue();
    }

    @Override
    public CampanaDonacion obtenerPorId(Long id) {
        CampanaDonacion campana = campanaDonacionRepository.findById(id).orElse(null);

        if (campana == null || !campana.getEstadoActivo()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No existe una campana de donacion con id " + id
            );
        }

        return campana;
    }

    @Override
    public CampanaDonacion crear(CampanaDonacion campana) {
        return campanaDonacionRepository.save(campana);
    }

    @Override
    public CampanaDonacion actualizar(Long id, CampanaDonacion campana) {
        CampanaDonacion existente = obtenerPorId(id);

        existente.setTitulo(campana.getTitulo());
        existente.setDescripcion(campana.getDescripcion());
        existente.setCategoria(campana.getCategoria());
        existente.setMetaMonto(campana.getMetaMonto());
        existente.setMontoRecaudado(campana.getMontoRecaudado());
        existente.setFechaInicio(campana.getFechaInicio());
        existente.setFechaFin(campana.getFechaFin());
        existente.setEstado(campana.getEstado());
        existente.setEsUrgente(campana.getEsUrgente());

        return campanaDonacionRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        CampanaDonacion campana = obtenerPorId(id);
        campana.setEstadoActivo(false);
        campanaDonacionRepository.save(campana);
    }
}
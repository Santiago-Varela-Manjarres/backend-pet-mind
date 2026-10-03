package com.cesde.petmind.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cesde.petmind.model.entity.Actualizacion;
import com.cesde.petmind.model.entity.CampanaDonacion;
import com.cesde.petmind.model.entity.Historia;
import com.cesde.petmind.repository.ActualizacionRepository;
import com.cesde.petmind.repository.CampanaDonacionRepository;
import com.cesde.petmind.repository.HistoriaRepository;
import com.cesde.petmind.service.ActualizacionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActualizacionServiceImpl implements ActualizacionService {

    private final ActualizacionRepository actualizacionRepository;
    private final CampanaDonacionRepository campanaDonacionRepository;
    private final HistoriaRepository historiaRepository;

    @Override
    public List<Actualizacion> listar() {
        return actualizacionRepository.findByEstadoActivoTrue();
    }

    @Override
    public Actualizacion obtenerPorId(Long id) {
        Actualizacion actualizacion = actualizacionRepository.findById(id).orElse(null);

        if (actualizacion == null || !actualizacion.getEstadoActivo()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No existe una actualizacion con id " + id
            );
        }

        return actualizacion;
    }

    @Override
    public Actualizacion crear(Actualizacion actualizacion) {
        validarYAsignarRelacion(actualizacion);

        if (actualizacion.getFechaEvento() == null) {
            actualizacion.setFechaEvento(LocalDateTime.now());
        }

        return actualizacionRepository.save(actualizacion);
    }

    @Override
    public Actualizacion actualizar(Long id, Actualizacion actualizacion) {
        Actualizacion existente = obtenerPorId(id);
        validarYAsignarRelacion(actualizacion);

        existente.setCampana(actualizacion.getCampana());
        existente.setHistoria(actualizacion.getHistoria());
        existente.setTitulo(actualizacion.getTitulo());
        existente.setContenido(actualizacion.getContenido());
        existente.setFechaEvento(actualizacion.getFechaEvento());
        existente.setOrden(actualizacion.getOrden());

        return actualizacionRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        Actualizacion actualizacion = obtenerPorId(id);
        actualizacion.setEstadoActivo(false);
        actualizacionRepository.save(actualizacion);
    }

    private void validarYAsignarRelacion(Actualizacion actualizacion) {
        int relaciones = 0;

        if (actualizacion.getCampana() != null) {
            relaciones++;
        }

        if (actualizacion.getHistoria() != null) {
            relaciones++;
        }

        if (relaciones != 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La actualizacion debe pertenecer a una campana o a una historia"
            );
        }

        if (actualizacion.getCampana() != null) {
            if (actualizacion.getCampana().getId() == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "La campana debe tener un id"
                );
            }

            Long campanaId = actualizacion.getCampana().getId();

            CampanaDonacion campana = campanaDonacionRepository
                    .findById(campanaId)
                    .orElse(null);

            if (campana == null || !campana.getEstadoActivo()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe una campana activa con id " + campanaId
                );
            }

            actualizacion.setCampana(campana);
            actualizacion.setHistoria(null);
        }

        if (actualizacion.getHistoria() != null) {
            if (actualizacion.getHistoria().getId() == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "La historia debe tener un id"
                );
            }

            Long historiaId = actualizacion.getHistoria().getId();

            Historia historia = historiaRepository.findById(historiaId).orElse(null);

            if (historia == null || !historia.getEstadoActivo()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe una historia activa con id " + historiaId
                );
            }

            actualizacion.setHistoria(historia);
            actualizacion.setCampana(null);
        }
    }
}
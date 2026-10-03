package com.cesde.petmind.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cesde.petmind.exceptions.RecursoNoEncontradoException;
import com.cesde.petmind.model.entity.Fundacion;
import com.cesde.petmind.repository.FundacionRepository;
import com.cesde.petmind.service.FundacionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FundacionServiceImpl implements FundacionService {

    private final FundacionRepository fundacionRepository;

    @Override
    public List<Fundacion> listar() {
        return fundacionRepository.findByEstadoActivoTrue();
    }

    @Override
    public Fundacion obtenerPorId(Long id) {
        Fundacion fundacion = fundacionRepository.findById(id).orElse(null);

        // Una fundacion borrada logicamente se trata igual que una que no existe
        if (fundacion == null || !fundacion.getEstadoActivo()) {
            throw new RecursoNoEncontradoException("No existe una fundacion con id " + id);
        }
        return fundacion;
    }

    @Override
    public Fundacion crear(Fundacion fundacion) {
        return fundacionRepository.save(fundacion);
    }

    @Override
    public Fundacion actualizar(Long id, Fundacion fundacion) {
        Fundacion existente = obtenerPorId(id);

        existente.setNombre(fundacion.getNombre());
        existente.setNit(fundacion.getNit());
        existente.setRepresentanteLegal(fundacion.getRepresentanteLegal());
        existente.setDescripcion(fundacion.getDescripcion());
        existente.setDireccion(fundacion.getDireccion());
        existente.setContacto(fundacion.getContacto());
        existente.setUrlLogo(fundacion.getUrlLogo());
        existente.setUrlPortada(fundacion.getUrlPortada());
        existente.setUrlRut(fundacion.getUrlRut());
        existente.setUrlCamaraComercio(fundacion.getUrlCamaraComercio());
        existente.setEstadoVerificacion(fundacion.getEstadoVerificacion());
        existente.setFechaVerificacion(fundacion.getFechaVerificacion());
        existente.setLatitud(fundacion.getLatitud());
        existente.setLongitud(fundacion.getLongitud());
        return fundacionRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        Fundacion fundacion = obtenerPorId(id);

        // Borrado logico: no se borra la fila, solo se desactiva
        fundacion.setEstadoActivo(false);
        fundacionRepository.save(fundacion);
    }
}

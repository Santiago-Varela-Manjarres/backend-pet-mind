package com.cesde.petmind.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cesde.petmind.model.entity.Donacion;
import com.cesde.petmind.model.entity.Usuario;
import com.cesde.petmind.model.enums.EstadoDonacion;
import com.cesde.petmind.repository.DonacionRepository;
import com.cesde.petmind.repository.UsuarioRepository;
import com.cesde.petmind.service.DonacionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DonacionServiceImpl implements DonacionService {

    private final DonacionRepository donacionRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public List<Donacion> listar() {
        return donacionRepository.findByEstadoActivoTrue();
    }

    @Override
    public Donacion obtenerPorId(Long id) {
        Donacion donacion = donacionRepository.findById(id).orElse(null);

        if (donacion == null || !donacion.getEstadoActivo()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No existe una donacion con id " + id
            );
        }

        return donacion;
    }

    @Override
    public Donacion crear(Donacion donacion) {
        validarYAsignarUsuario(donacion);

        if (donacion.getFechaDonacion() == null) {
            donacion.setFechaDonacion(LocalDate.now());
        }

        return donacionRepository.save(donacion);
    }

    @Override
    public Donacion actualizar(Long id, Donacion donacion) {
        Donacion existente = obtenerPorId(id);

        existente.setMonto(donacion.getMonto());
        existente.setValorComision(donacion.getValorComision());
        existente.setMontoTotal(donacion.getMontoTotal());
        existente.setFrecuencia(donacion.getFrecuencia());
        existente.setMetodoPago(donacion.getMetodoPago());
        existente.setEstado(donacion.getEstado());
        existente.setEsAnonima(donacion.getEsAnonima());
        existente.setMensaje(donacion.getMensaje());
        existente.setReferenciaPago(donacion.getReferenciaPago());
        existente.setNombreDonante(donacion.getNombreDonante());
        existente.setCorreoDonante(donacion.getCorreoDonante());
        existente.setDocumentoDonante(donacion.getDocumentoDonante());
        existente.setEstadoRecurrencia(donacion.getEstadoRecurrencia());
        existente.setDiaCobro(donacion.getDiaCobro());
        existente.setFechaProximoCobro(donacion.getFechaProximoCobro());
        existente.setCertificadoCodigo(donacion.getCertificadoCodigo());
        existente.setCertificadoUrl(donacion.getCertificadoUrl());
        existente.setCertificadoFecha(donacion.getCertificadoFecha());

        return donacionRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        Donacion donacion = obtenerPorId(id);
        donacion.setEstadoActivo(false);
        donacionRepository.save(donacion);
    }

    @Override
    public List<Donacion> listarPorUsuario(Long usuarioId) {
        return donacionRepository
                .findByUsuarioIdAndEstadoActivoTrueOrderByFechaDonacionDesc(usuarioId);
    }

    @Override
    public List<Donacion> listarPorCampanaYEstado(
            Long campanaId,
            EstadoDonacion estado
    ) {
        throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "La entidad Donacion aun no tiene la relacion con CampanaDonacion"
        );
    }

    private void validarYAsignarUsuario(Donacion donacion) {
        if (donacion.getUsuario() == null || donacion.getUsuario().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La donacion debe indicar un usuario"
            );
        }

        Long usuarioId = donacion.getUsuario().getId();

        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

        if (usuario == null || !usuario.getEstadoActivo()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No existe un usuario activo con id " + usuarioId
            );
        }

        donacion.setUsuario(usuario);
    }
}
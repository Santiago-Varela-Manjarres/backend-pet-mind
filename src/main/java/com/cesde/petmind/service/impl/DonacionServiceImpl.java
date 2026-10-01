package com.cesde.petmind.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cesde.petmind.model.entity.CampanaDonacion;
import com.cesde.petmind.model.entity.Donacion;
import com.cesde.petmind.model.entity.Usuario;
import com.cesde.petmind.repository.CampanaDonacionRepository;
import com.cesde.petmind.repository.DonacionRepository;
import com.cesde.petmind.repository.UsuarioRepository;
import com.cesde.petmind.model.enums.EstadoDonacion;
import com.cesde.petmind.service.DonacionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DonacionServiceImpl implements DonacionService {

    /*
     * Reglas de negocio:
     * 1. El monto de la donacion debe ser mayor que cero.
     * 2. La donacion debe tener un metodo de pago valido.
     * 3. El usuario y la campana asociados deben existir y estar activos.
     * 4. Las donaciones eliminadas se manejan mediante borrado logico.
     */
    private final DonacionRepository donacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final CampanaDonacionRepository campanaDonacionRepository;

    @Override
    public List<Donacion> listar() {
        return donacionRepository.findByEstadoActivoTrue();
    }

    @Override
    public Donacion obtenerPorId(Long id) {
        Donacion donacion = donacionRepository.findById(id).orElse(null);

        if (donacion == null || !donacion.getEstadoActivo()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No existe una donacion con id " + id);
        }
        return donacion;
    }

    @Override
    public Donacion crear(Donacion donacion) {
        validarDatosDonacion(donacion);
        resolverRelaciones(donacion);
        return donacionRepository.save(donacion);
    }

    @Override
    public Donacion actualizar(Long id, Donacion donacion) {
        validarDatosDonacion(donacion);
        Donacion existente = obtenerPorId(id);

        existente.setMonto(donacion.getMonto());
        existente.setValorComision(donacion.getValorComision());
        existente.setMontoTotal(donacion.getMontoTotal());
        existente.setFrecuencia(donacion.getFrecuencia());
        existente.setFechaDonacion(donacion.getFechaDonacion());
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
        return donacionRepository.findByUsuarioIdAndEstadoActivoTrueOrderByFechaDonacionDesc(usuarioId);
    }

    @Override
    public List<Donacion> listarPorCampanaYEstado(Long campanaId, EstadoDonacion estado) {
        return donacionRepository.findByCampanaIdAndEstadoAndEstadoActivoTrue(campanaId, estado);
    }

    private void validarDatosDonacion(Donacion donacion) {
        if (donacion == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La donacion es obligatoria");
        }

        if (donacion.getMonto() == null || donacion.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El monto de la donacion debe ser mayor que cero");
        }

        if (donacion.getMetodoPago() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La donacion debe tener un metodo de pago valido");
        }
    }

    private void resolverRelaciones(Donacion donacion) {
        if (donacion.getUsuario() != null) {
            Long usuarioId = donacion.getUsuario().getId();
            Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
            if (usuario == null || !usuario.getEstadoActivo()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existe un usuario activo con id " + usuarioId);
            }
            donacion.setUsuario(usuario);
        }

        if (donacion.getCampana() != null) {
            Long campanaId = donacion.getCampana().getId();
            CampanaDonacion campana = campanaDonacionRepository.findById(campanaId).orElse(null);
            if (campana == null || !campana.getEstadoActivo()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existe una campana activa con id " + campanaId);
            }
            donacion.setCampana(campana);
        }
    }
}

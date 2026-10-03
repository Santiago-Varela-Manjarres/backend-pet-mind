package com.cesde.petmind.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cesde.petmind.exceptions.RecursoNoEncontradoException;
import com.cesde.petmind.exceptions.ReglaDeNegocioException;
import com.cesde.petmind.model.entity.CampanaDonacion;
import com.cesde.petmind.model.entity.Fundacion;
import com.cesde.petmind.model.entity.Mascota;
import com.cesde.petmind.model.enums.EstadoCampana;
import com.cesde.petmind.repository.CampanaDonacionRepository;
import com.cesde.petmind.repository.FundacionRepository;
import com.cesde.petmind.repository.MascotaRepository;
import com.cesde.petmind.service.CampanaDonacionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CampanaDonacionServiceImpl implements CampanaDonacionService {

    private final CampanaDonacionRepository campanaDonacionRepository;
    private final FundacionRepository fundacionRepository;
    private final MascotaRepository mascotaRepository;

    @Override
    public List<CampanaDonacion> listar() {
        return campanaDonacionRepository.findByEstadoActivoTrue();
    }

    @Override
    public List<CampanaDonacion> listarPorEstado(EstadoCampana estado) {
        if (estado == null) {
            throw new ReglaDeNegocioException("El estado de la campana es obligatorio");
        }

        return campanaDonacionRepository.findByEstadoAndEstadoActivoTrue(estado);
    }

    @Override
    public CampanaDonacion obtenerPorId(Long id) {
        CampanaDonacion campana = campanaDonacionRepository.findById(id).orElse(null);

        if (campana == null || !campana.getEstadoActivo()) {
            throw new RecursoNoEncontradoException("No existe una campana con id " + id);
        }

        return campana;
    }

    @Override
    public CampanaDonacion crear(CampanaDonacion campana) {
        validarDatosCampana(campana);
        resolverRelaciones(campana);
        return campanaDonacionRepository.save(campana);
    }

    @Override
    public CampanaDonacion actualizar(Long id, CampanaDonacion campana) {
        validarDatosCampana(campana);

        CampanaDonacion existente = obtenerPorId(id);

        if (campana.getFundacion() != null
                && !campana.getFundacion().getId().equals(existente.getFundacion().getId())) {
            throw new ReglaDeNegocioException("La fundacion de una campana no puede cambiarse");
        }

        if (campana.getMascota() != null) {
            validarMascota(campana.getMascota(), existente.getFundacion());
        }

        existente.setTitulo(campana.getTitulo());
        existente.setDescripcion(campana.getDescripcion());
        existente.setCategoria(campana.getCategoria());
        existente.setMetaMonto(campana.getMetaMonto());
        existente.setMontoRecaudado(campana.getMontoRecaudado());
        existente.setFechaInicio(campana.getFechaInicio());
        existente.setFechaFin(campana.getFechaFin());
        existente.setEstado(campana.getEstado());
        existente.setEsUrgente(campana.getEsUrgente());
        existente.setMascota(campana.getMascota());

        return campanaDonacionRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        CampanaDonacion campana = obtenerPorId(id);
        campana.setEstadoActivo(false);
        campanaDonacionRepository.save(campana);
    }

    private void validarDatosCampana(CampanaDonacion campana) {
        if (campana == null) {
            throw new ReglaDeNegocioException("La campana es obligatoria");
        }

        if (campana.getFundacion() == null || campana.getFundacion().getId() == null) {
            throw new ReglaDeNegocioException("La campana debe indicar una fundacion");
        }

        if (campana.getMetaMonto() == null
                || campana.getMetaMonto().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ReglaDeNegocioException("La meta de la campana debe ser mayor que cero");
        }

        if (campana.getMontoRecaudado() != null
                && campana.getMontoRecaudado().compareTo(BigDecimal.ZERO) < 0) {
            throw new ReglaDeNegocioException("El monto recaudado no puede ser negativo");
        }

        if (campana.getFechaInicio() == null) {
            throw new ReglaDeNegocioException("La campana debe indicar una fecha de inicio");
        }

        if (campana.getFechaFin() != null
                && campana.getFechaFin().isBefore(campana.getFechaInicio())) {
            throw new ReglaDeNegocioException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
    }

    private void resolverRelaciones(CampanaDonacion campana) {
        Long fundacionId = campana.getFundacion().getId();

        Fundacion fundacion = fundacionRepository.findById(fundacionId).orElse(null);

        if (fundacion == null || !fundacion.getEstadoActivo()) {
            throw new RecursoNoEncontradoException("No existe una fundacion activa con id " + fundacionId);
        }

        campana.setFundacion(fundacion);

        if (campana.getMascota() != null) {
            validarMascota(campana.getMascota(), fundacion);

            Mascota mascota = mascotaRepository
                    .findById(campana.getMascota().getId())
                    .orElse(null);

            campana.setMascota(mascota);
        }
    }

    private void validarMascota(Mascota referencia, Fundacion fundacion) {
        if (referencia.getId() == null) {
            throw new ReglaDeNegocioException("La mascota asociada debe indicar un id");
        }

        Mascota mascota = mascotaRepository.findById(referencia.getId()).orElse(null);

        if (mascota == null || !mascota.getEstadoActivo()) {
            throw new RecursoNoEncontradoException("No existe una mascota activa con id " + referencia.getId());
        }

        if (mascota.getFundacion() == null
                || !mascota.getFundacion().getId().equals(fundacion.getId())) {
            throw new ReglaDeNegocioException("La mascota debe pertenecer a la fundacion de la campana");
        }
    }
}
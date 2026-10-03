package com.cesde.petmind.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cesde.petmind.model.entity.Fundacion;
import com.cesde.petmind.model.entity.ReporteAnimal;
import com.cesde.petmind.model.entity.ReporteFundacion;
import com.cesde.petmind.repository.FundacionRepository;
import com.cesde.petmind.repository.ReporteAnimalRepository;
import com.cesde.petmind.repository.ReporteFundacionRepository;
import com.cesde.petmind.service.ReporteFundacionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReporteFundacionServiceImpl implements ReporteFundacionService {

    private final ReporteFundacionRepository reporteFundacionRepository;
    private final ReporteAnimalRepository reporteAnimalRepository;
    private final FundacionRepository fundacionRepository;

    @Override
    public List<ReporteFundacion> listar() {
        return reporteFundacionRepository.findByEstadoActivoTrue();
    }

    @Override
    public ReporteFundacion obtenerPorId(Long id) {
        ReporteFundacion reporteFundacion = reporteFundacionRepository.findById(id).orElse(null);

        if (reporteFundacion == null || !reporteFundacion.getEstadoActivo()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No existe un reporte de fundacion con id " + id
            );
        }

        return reporteFundacion;
    }

    @Override
    public ReporteFundacion crear(ReporteFundacion reporteFundacion) {
        validarYAsignarRelaciones(reporteFundacion);

        if (reporteFundacion.getFechaNotificacion() == null) {
            reporteFundacion.setFechaNotificacion(LocalDateTime.now());
        }

        return reporteFundacionRepository.save(reporteFundacion);
    }

    @Override
    public ReporteFundacion actualizar(Long id, ReporteFundacion reporteFundacion) {
        ReporteFundacion existente = obtenerPorId(id);
        validarYAsignarRelaciones(reporteFundacion);

        existente.setReporte(reporteFundacion.getReporte());
        existente.setFundacion(reporteFundacion.getFundacion());
        existente.setDistanciaKm(reporteFundacion.getDistanciaKm());
        existente.setEstadoAtencion(reporteFundacion.getEstadoAtencion());
        existente.setFechaNotificacion(reporteFundacion.getFechaNotificacion());

        return reporteFundacionRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        ReporteFundacion reporteFundacion = obtenerPorId(id);
        reporteFundacion.setEstadoActivo(false);
        reporteFundacionRepository.save(reporteFundacion);
    }

    private void validarYAsignarRelaciones(ReporteFundacion reporteFundacion) {
        if (reporteFundacion.getReporte() == null
                || reporteFundacion.getReporte().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El reporte de fundacion debe indicar un reporte animal"
            );
        }

        if (reporteFundacion.getFundacion() == null
                || reporteFundacion.getFundacion().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El reporte de fundacion debe indicar una fundacion"
            );
        }

        Long reporteId = reporteFundacion.getReporte().getId();

        ReporteAnimal reporte = reporteAnimalRepository.findById(reporteId).orElse(null);

        if (reporte == null || !reporte.getEstadoActivo()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No existe un reporte animal activo con id " + reporteId
            );
        }

        Long fundacionId = reporteFundacion.getFundacion().getId();

        Fundacion fundacion = fundacionRepository.findById(fundacionId).orElse(null);

        if (fundacion == null || !fundacion.getEstadoActivo()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No existe una fundacion activa con id " + fundacionId
            );
        }

        reporteFundacion.setReporte(reporte);
        reporteFundacion.setFundacion(fundacion);
    }
}
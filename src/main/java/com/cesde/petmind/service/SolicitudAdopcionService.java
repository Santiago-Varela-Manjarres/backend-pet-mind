package com.cesde.petmind.service;

import java.util.List;

import com.cesde.petmind.model.entity.SolicitudAdopcion;
import com.cesde.petmind.model.enums.EstadoSolicitud;

public interface SolicitudAdopcionService {
    List<SolicitudAdopcion> listar();
    SolicitudAdopcion obtenerPorId(Long id);
    SolicitudAdopcion crear(SolicitudAdopcion solicitud);
    SolicitudAdopcion actualizar(Long id, SolicitudAdopcion solicitud);
    void eliminar(Long id);

    List<SolicitudAdopcion> listarPorUsuario(Long usuarioId);

    List<SolicitudAdopcion> listarPorFundacionYEstado(
            Long fundacionId,
            EstadoSolicitud estado
    );
}
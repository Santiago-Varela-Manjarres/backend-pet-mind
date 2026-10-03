package com.cesde.petmind.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cesde.petmind.exception.RecursoNoEncontradoException;
import com.cesde.petmind.exception.ReglaNegocioException;
import com.cesde.petmind.model.entity.Mascota;
import com.cesde.petmind.model.entity.SolicitudAdopcion;
import com.cesde.petmind.model.entity.Usuario;
import com.cesde.petmind.model.enums.EstadoAdopcion;
import com.cesde.petmind.model.enums.EstadoSolicitud;
import com.cesde.petmind.repository.MascotaRepository;
import com.cesde.petmind.repository.SolicitudAdopcionRepository;
import com.cesde.petmind.repository.UsuarioRepository;
import com.cesde.petmind.service.SolicitudAdopcionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class SolicitudAdopcionServiceImpl implements SolicitudAdopcionService {

    private final SolicitudAdopcionRepository solicitudAdopcionRepository;
    private final UsuarioRepository usuarioRepository;
    private final MascotaRepository mascotaRepository;

    @Override
    public List<SolicitudAdopcion> listar() {
        return solicitudAdopcionRepository.findByEstadoActivoTrue();
    }

    @Override
    public SolicitudAdopcion obtenerPorId(Long id) {
        SolicitudAdopcion solicitud = solicitudAdopcionRepository.findById(id).orElse(null);

        if (solicitud == null || !solicitud.getEstadoActivo()) {
            throw new RecursoNoEncontradoException("No existe una solicitud de adopcion con id " + id);
        }

        return solicitud;
    }

    @Override
    public SolicitudAdopcion crear(SolicitudAdopcion solicitud) {
        validarYAsignarRelaciones(solicitud);

        if (solicitud.getEstado() == null) {
            solicitud.setEstado(EstadoSolicitud.PENDIENTE);
        }

        if (solicitud.getFechaSolicitud() == null) {
            solicitud.setFechaSolicitud(LocalDate.now());
        }

        return solicitudAdopcionRepository.save(solicitud);
    }

    @Override
    public SolicitudAdopcion actualizar(Long id, SolicitudAdopcion solicitud) {
        SolicitudAdopcion existente = obtenerPorId(id);

        existente.setEstado(solicitud.getEstado());
        existente.setPorcentajeAfinidad(solicitud.getPorcentajeAfinidad());
        existente.setDatosHogar(solicitud.getDatosHogar());
        existente.setCita(solicitud.getCita());
        existente.setNotasInternas(solicitud.getNotasInternas());
        existente.setFechaEnRevision(solicitud.getFechaEnRevision());
        existente.setFechaEntrevista(solicitud.getFechaEntrevista());
        existente.setFechaVisitaHogar(solicitud.getFechaVisitaHogar());
        existente.setFechaDecision(solicitud.getFechaDecision());

        if ((existente.getEstado() == EstadoSolicitud.APROBADA
                || existente.getEstado() == EstadoSolicitud.RECHAZADA)
                && existente.getFechaDecision() == null) {
            existente.setFechaDecision(LocalDateTime.now());
        }

        return solicitudAdopcionRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        SolicitudAdopcion solicitud = obtenerPorId(id);
        solicitud.setEstadoActivo(false);
        solicitudAdopcionRepository.save(solicitud);
    }

    @Override
    public List<SolicitudAdopcion> listarPorUsuario(Long usuarioId) {
        return solicitudAdopcionRepository.findByUsuarioIdAndEstadoActivoTrue(usuarioId);
    }

    @Override
    public List<SolicitudAdopcion> listarPorFundacionYEstado(
            Long fundacionId,
            EstadoSolicitud estado
    ) {
        return solicitudAdopcionRepository
                .findByMascotaFundacionIdAndEstadoAndEstadoActivoTrue(fundacionId, estado);
    }

    private void validarYAsignarRelaciones(SolicitudAdopcion solicitud) {
        if (solicitud.getUsuario() == null || solicitud.getUsuario().getId() == null) {
            throw new ReglaNegocioException("La solicitud debe indicar un usuario");
        }

        if (solicitud.getMascota() == null || solicitud.getMascota().getId() == null) {
            throw new ReglaNegocioException("La solicitud debe indicar una mascota");
        }

        Long usuarioId = solicitud.getUsuario().getId();
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

        if (usuario == null || !usuario.getEstadoActivo()) {
            throw new RecursoNoEncontradoException("No existe un usuario activo con id " + usuarioId);
        }

        Long mascotaId = solicitud.getMascota().getId();
        Mascota mascota = mascotaRepository.findById(mascotaId).orElse(null);

        if (mascota == null || !mascota.getEstadoActivo()) {
            throw new RecursoNoEncontradoException("No existe una mascota activa con id " + mascotaId);
        }

        if (mascota.getEstadoAdopcion() != EstadoAdopcion.DISPONIBLE) {
            throw new ReglaNegocioException("La mascota no esta disponible para adopcion");
        }

        solicitud.setUsuario(usuario);
        solicitud.setMascota(mascota);
    }
}
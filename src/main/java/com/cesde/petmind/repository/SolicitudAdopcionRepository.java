package com.cesde.petmind.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cesde.petmind.model.entity.SolicitudAdopcion;
import com.cesde.petmind.model.enums.EstadoSolicitud;

public interface SolicitudAdopcionRepository extends JpaRepository<SolicitudAdopcion, Long> {

    // Borrado logico: los listados solo traen los registros activos
    List<SolicitudAdopcion> findByEstadoActivoTrue();

    // Panel del usuario
    List<SolicitudAdopcion> findByUsuarioIdAndEstadoActivoTrue(Long usuarioId);

    // Tablero de la fundacion: recorre solicitud -> mascota -> fundacion
    List<SolicitudAdopcion> findByMascotaFundacionIdAndEstadoAndEstadoActivoTrue(
            Long fundacionId,
            EstadoSolicitud estado);
}

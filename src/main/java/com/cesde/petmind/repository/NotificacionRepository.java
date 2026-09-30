package com.cesde.petmind.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cesde.petmind.model.entity.Notificacion;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {

    // Borrado logico: los listados solo traen los registros activos
    List<Notificacion> findByEstadoActivoTrue();

    // Campana del navbar: las que el usuario aun no ha leido
    List<Notificacion> findByUsuarioIdAndLeidaFalseAndEstadoActivoTrue(Long usuarioId);
}

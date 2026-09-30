package com.cesde.petmind.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cesde.petmind.model.entity.Actualizacion;

public interface ActualizacionRepository extends JpaRepository<Actualizacion, Long> {

    // Borrado logico: los listados solo traen los registros activos
    List<Actualizacion> findByEstadoActivoTrue();
}

package com.cesde.petmind.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cesde.petmind.model.entity.Historia;

public interface HistoriaRepository extends JpaRepository<Historia, Long> {

    // Borrado logico: los listados solo traen los registros activos
    List<Historia> findByEstadoActivoTrue();
}

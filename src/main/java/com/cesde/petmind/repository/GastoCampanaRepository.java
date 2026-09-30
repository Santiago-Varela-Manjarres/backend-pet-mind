package com.cesde.petmind.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cesde.petmind.model.entity.GastoCampana;

public interface GastoCampanaRepository extends JpaRepository<GastoCampana, Long> {

    // Borrado logico: los listados solo traen los registros activos
    List<GastoCampana> findByEstadoActivoTrue();
}

package com.cesde.petmind.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cesde.petmind.model.entity.ReporteAnimal;

public interface ReporteAnimalRepository extends JpaRepository<ReporteAnimal, Long> {

    // Borrado logico: los listados solo traen los registros activos
    List<ReporteAnimal> findByEstadoActivoTrue();
}

package com.cesde.petmind.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cesde.petmind.model.entity.ReporteFundacion;

public interface ReporteFundacionRepository extends JpaRepository<ReporteFundacion, Long> {

    // Borrado logico: los listados solo traen los registros activos
    List<ReporteFundacion> findByEstadoActivoTrue();
}

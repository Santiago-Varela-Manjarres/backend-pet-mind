package com.cesde.petmind.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cesde.petmind.model.entity.CampanaDonacion;
import com.cesde.petmind.model.enums.EstadoCampana;

public interface CampanaDonacionRepository extends JpaRepository<CampanaDonacion, Long> {

    // Borrado logico: los listados solo traen los registros activos
    List<CampanaDonacion> findByEstadoActivoTrue();

    List<CampanaDonacion> findByEstadoAndEstadoActivoTrue(EstadoCampana estado);
}

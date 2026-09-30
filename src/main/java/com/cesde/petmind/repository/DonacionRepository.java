package com.cesde.petmind.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cesde.petmind.model.entity.Donacion;
import com.cesde.petmind.model.enums.EstadoDonacion;

public interface DonacionRepository extends JpaRepository<Donacion, Long> {

    // Borrado logico: los listados solo traen los registros activos
    List<Donacion> findByEstadoActivoTrue();

    // Mis donaciones, de la mas reciente a la mas antigua
    List<Donacion> findByUsuarioIdAndEstadoActivoTrueOrderByFechaDonacionDesc(Long usuarioId);

    List<Donacion> findByCampanaIdAndEstadoAndEstadoActivoTrue(Long campanaId, EstadoDonacion estado);
}

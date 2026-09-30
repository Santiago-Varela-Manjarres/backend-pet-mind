package com.cesde.petmind.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cesde.petmind.model.entity.Donacion;

public interface DonacionRepository extends JpaRepository<Donacion, Long> {

    // Borrado logico: los listados solo traen los registros activos
    List<Donacion> findByEstadoActivoTrue();

    // Mis donaciones, de la mas reciente a la mas antigua
    List<Donacion> findByUsuarioIdAndEstadoActivoTrueOrderByFechaDonacionDesc(Long usuarioId);

    // PENDIENTE: descomentar cuando Emmanuel agregue la relacion campana en Donacion.
    // Si se descomenta antes, la app no arranca porque Donacion no tiene el campo campana.
    // List<Donacion> findByCampanaIdAndEstadoAndEstadoActivoTrue(Long campanaId, EstadoDonacion estado);
}

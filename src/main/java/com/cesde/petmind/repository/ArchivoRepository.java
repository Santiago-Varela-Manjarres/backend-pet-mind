package com.cesde.petmind.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cesde.petmind.model.entity.Archivo;

public interface ArchivoRepository extends JpaRepository<Archivo, Long> {

    // Borrado logico: los listados solo traen los registros activos
    List<Archivo> findByEstadoActivoTrue();

    // Galeria de la mascota en el orden en que se muestra
    List<Archivo> findByMascotaIdAndEstadoActivoTrueOrderByOrdenAsc(Long mascotaId);
}

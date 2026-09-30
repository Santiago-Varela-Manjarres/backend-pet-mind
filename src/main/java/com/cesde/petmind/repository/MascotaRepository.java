package com.cesde.petmind.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cesde.petmind.model.entity.Mascota;
import com.cesde.petmind.model.enums.Especie;
import com.cesde.petmind.model.enums.EstadoAdopcion;
import com.cesde.petmind.model.enums.EstadoPublicacion;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {

    // Borrado logico: los listados solo traen los registros activos
    List<Mascota> findByEstadoActivoTrue();

    // Listado con filtros y perfil
    List<Mascota> findByEstadoPublicacionAndEstadoAdopcionAndEstadoActivoTrue(
            EstadoPublicacion estadoPublicacion,
            EstadoAdopcion estadoAdopcion);

    List<Mascota> findByEspecieAndCiudadIgnoreCaseAndEstadoActivoTrue(Especie especie, String ciudad);

    List<Mascota> findByFundacionIdAndEstadoActivoTrue(Long fundacionId);
}

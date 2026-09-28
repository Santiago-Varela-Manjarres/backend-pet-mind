package com.cesde.petmind.service;

import java.util.List;

import com.cesde.petmind.model.entity.Mascota;
import com.cesde.petmind.model.enums.Especie;
import com.cesde.petmind.model.enums.EstadoAdopcion;
import com.cesde.petmind.model.enums.EstadoPublicacion;


public class MascotaService {
    
    List<Mascota> listar();

    Mascota obtenerPorId(Long id);

    Mascota crear(Mascota mascota);

    Mascota actualizar(Long id, Mascota mascota);

    void eliminar(Long id);

    List<Mascota> listarPorEstado(
    EstadoPublicacion estadoPublicacion,
    EstadoAdopcion estadoAdopcion
    );

    List<Mascota> buscarPorEspecieYCiudad(Especie especie, String ciudad);

    List<Mascota> listarPorFundacion(Long fundacionId);

}

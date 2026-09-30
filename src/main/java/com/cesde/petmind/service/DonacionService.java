package com.cesde.petmind.service;

import java.util.List;
import com.cesde.petmind.model.entity.Donacion;
import com.cesde.petmind.model.enums.EstadoDonacion;

public interface DonacionService {
    
    List<Donacion> listar();
    Donacion obtenerPorId(Long id);
    Donacion crear(Donacion donacion);
    Donacion actualizar(Long id, Donacion donacion);
    void eliminar(Long id);

    List<Donacion> listarPorUsuario(Long usuarioId);

    List<Donacion> listarPorCampanaYEstado(
            Long campanaId,
            EstadoDonacion estado
    );
    
}

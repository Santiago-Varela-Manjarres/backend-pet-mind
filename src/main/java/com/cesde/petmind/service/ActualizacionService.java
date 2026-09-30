package com.cesde.petmind.service;

import java.util.List;
import com.cesde.petmind.model.entity.Actualizacion;

public interface ActualizacionService {
    
    List<Actualizacion> listar();
    Actualizacion obtenerPorId(Long id);
    Actualizacion crear(Actualizacion actualizacion);
    Actualizacion actualizar(Long id, Actualizacion actualizacion);
    void eliminar(Long id);

}

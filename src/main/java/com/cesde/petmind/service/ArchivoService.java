package com.cesde.petmind.service;

import java.util.List;
import com.cesde.petmind.model.entity.Archivo;

public class ArchivoService {
   
    List<Archivo> listar();
    Archivo obtenerPorId(Long id);
    Archivo crear(Archivo archivo);
    Archivo actualizar(Long id, Archivo archivo);
    void eliminar(Long id);

    List<Archivo> listarPorMascota(Long mascotaId);
    
}

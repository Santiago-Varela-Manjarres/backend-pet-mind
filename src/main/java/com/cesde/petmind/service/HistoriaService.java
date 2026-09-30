package com.cesde.petmind.service;

import java.util.List;
import com.cesde.petmind.model.entity.Historia;

public interface HistoriaService {
    
    List<Historia> listar();
    Historia obtenerPorId(Long id);
    Historia crear(Historia historia);
    Historia actualizar(Long id, Historia historia);
    void eliminar(Long id);
    
}

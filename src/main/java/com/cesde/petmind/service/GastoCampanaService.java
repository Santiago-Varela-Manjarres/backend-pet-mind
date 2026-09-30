package com.cesde.petmind.service;

import java.util.List;
import com.cesde.petmind.model.entity.GastoCampana;

public interface GastoCampanaService {
    
     List<GastoCampana> listar();
    GastoCampana obtenerPorId(Long id);
    GastoCampana crear(GastoCampana gasto);
    GastoCampana actualizar(Long id, GastoCampana gasto);
    void eliminar(Long id);
    
}

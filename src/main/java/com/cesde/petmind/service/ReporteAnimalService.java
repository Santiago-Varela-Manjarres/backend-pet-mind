package com.cesde.petmind.service;

import java.util.List;
import com.cesde.petmind.model.entity.ReporteAnimal;

public interface ReporteAnimalService {
    List<ReporteAnimal> listar();
    ReporteAnimal obtenerPorId(Long id);
    ReporteAnimal crear(ReporteAnimal reporte);
    ReporteAnimal actualizar(Long id, ReporteAnimal reporte);
    void eliminar(Long id);
}

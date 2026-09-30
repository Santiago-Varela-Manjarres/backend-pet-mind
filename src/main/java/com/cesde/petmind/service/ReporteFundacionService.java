package com.cesde.petmind.service;

import java.util.List;
import com.cesde.petmind.model.entity.ReporteFundacion;

public class ReporteFundacionService {
    
    List<ReporteFundacion> listar();
    ReporteFundacion obtenerPorId(Long id);
    ReporteFundacion crear(ReporteFundacion reporteFundacion);
    ReporteFundacion actualizar(Long id, ReporteFundacion reporteFundacion);
    void eliminar(Long id);
    
}

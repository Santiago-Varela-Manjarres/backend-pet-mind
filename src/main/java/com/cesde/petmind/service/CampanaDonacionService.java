package com.cesde.petmind.service;

import java.util.List;
import com.cesde.petmind.model.entity.CampanaDonacion;

public interface CampanaDonacionService {
    List<CampanaDonacion> listar();
    CampanaDonacion obtenerPorId(Long id);
    CampanaDonacion crear(CampanaDonacion campana);
    CampanaDonacion actualizar(Long id, CampanaDonacion campana);
    void eliminar(Long id);
}
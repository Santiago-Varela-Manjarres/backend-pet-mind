package com.cesde.petmind.service;

import java.util.List;
import com.cesde.petmind.model.entity.CampanaDonacion;
import com.cesde.petmind.model.enums.EstadoCampana;

public interface CampanaDonacionService {
    List<CampanaDonacion> listar();
    List<CampanaDonacion> listarPorEstado(EstadoCampana estado);
    CampanaDonacion obtenerPorId(Long id);
    CampanaDonacion crear(CampanaDonacion campana);
    CampanaDonacion actualizar(Long id, CampanaDonacion campana);
    void eliminar(Long id);
}
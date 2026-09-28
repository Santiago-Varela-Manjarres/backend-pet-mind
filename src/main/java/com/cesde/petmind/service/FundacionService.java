package com.cesde.petmind.service;

import java.util.List;
import com.cesde.petmind.model.entity.Fundacion;

public interface FundacionService {
    List<Fundacion> listar();
    Fundacion obtenerPorId(Long id);
    Fundacion crear(Fundacion fundacion);
    Fundacion actualizar(Long id, Fundacion fundacion);
    void eliminar(Long id);
}
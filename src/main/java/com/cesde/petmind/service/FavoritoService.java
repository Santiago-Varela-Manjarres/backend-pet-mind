package com.cesde.petmind.service;

import java.util.List;
import com.cesde.petmind.model.entity.Favorito;

public class FavoritoService {
    
     List<Favorito> listar();
    Favorito obtenerPorId(Long id);
    Favorito crear(Favorito favorito);
    void eliminar(Long id);

    List<Favorito> listarPorUsuario(Long usuarioId);
    
}

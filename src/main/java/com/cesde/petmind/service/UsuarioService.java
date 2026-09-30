package com.cesde.petmind.service;

import java.util.List;

import com.cesde.petmind.model.entity.Usuario;

public interface UsuarioService {
    List<Usuario> listar();
    Usuario obtenerPorId(Long id);
    Usuario crear(Usuario usuario);
    Usuario actualizar(Long id, Usuario usuario);
    void eliminar(Long id);
}

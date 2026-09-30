package com.cesde.petmind.service;

import java.util.List;
import com.cesde.petmind.model.entity.Notificacion;

public class NotificacionService {
    
    List<Notificacion> listar();
    Notificacion obtenerPorId(Long id);
    Notificacion crear(Notificacion notificacion);
    Notificacion actualizar(Long id, Notificacion notificacion);
    void eliminar(Long id);

    List<Notificacion> listarNoLeidasPorUsuario(Long usuarioId);

    Notificacion marcarComoLeida(Long id);
    
}

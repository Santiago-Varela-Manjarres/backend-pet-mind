package com.cesde.petmind.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cesde.petmind.exception.RecursoDuplicadoException;
import com.cesde.petmind.exception.RecursoNoEncontradoException;
import com.cesde.petmind.exception.ReglaNegocioException;
import com.cesde.petmind.model.entity.Fundacion;
import com.cesde.petmind.model.entity.Usuario;
import com.cesde.petmind.model.enums.EstadoVerificacion;
import com.cesde.petmind.model.enums.RolUsuario;
import com.cesde.petmind.repository.FundacionRepository;
import com.cesde.petmind.repository.UsuarioRepository;
import com.cesde.petmind.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final FundacionRepository fundacionRepository;

    @Override
    public List<Usuario> listar() {
        return usuarioRepository.findByEstadoActivoTrue();
    }

    @Override
    public Usuario obtenerPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);

        // Un usuario borrado logicamente se trata igual que uno que no existe
        if (usuario == null || !usuario.getEstadoActivo()) {
            throw new RecursoNoEncontradoException("No existe un usuario con id " + id);
        }
        return usuario;
    }

    @Override
    public Usuario crear(Usuario usuario) {
        validarCorreoUnico(usuario);
        validarFundacionDelRepresentante(usuario);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizar(Long id, Usuario usuario) {
        Usuario existente = obtenerPorId(id);

        // La regla del correo solo se revisa si el correo cambio
        String correoActual = existente.getContacto().getEmailContacto();
        if (usuario.getContacto() == null || !correoActual.equals(usuario.getContacto().getEmailContacto())) {
            validarCorreoUnico(usuario);
        }
        validarFundacionDelRepresentante(usuario);

        // La contrasena, el token y la verificacion del correo no se cambian por aqui
        existente.setNombre(usuario.getNombre());
        existente.setApellido(usuario.getApellido());
        existente.setContacto(usuario.getContacto());
        existente.setDireccion(usuario.getDireccion());
        existente.setUrlAvatar(usuario.getUrlAvatar());
        existente.setRol(usuario.getRol());
        existente.setFundacion(usuario.getFundacion());
        return usuarioRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        Usuario usuario = obtenerPorId(id);

        // Borrado logico: no se borra la fila, solo se desactiva
        usuario.setEstadoActivo(false);
        usuarioRepository.save(usuario);
    }

    // Regla de negocio 1: el correo es obligatorio y no puede estar registrado por otro usuario
    private void validarCorreoUnico(Usuario usuario) {
        if (usuario.getContacto() == null || usuario.getContacto().getEmailContacto() == null) {
            throw new ReglaNegocioException("El correo del usuario es obligatorio");
        }

        String correo = usuario.getContacto().getEmailContacto();
        if (usuarioRepository.existsByContactoEmailContacto(correo)) {
            throw new RecursoDuplicadoException("Ya existe un usuario registrado con el correo " + correo);
        }
    }

    // Regla de negocio 2: un representante de fundacion debe pertenecer a una fundacion verificada
    private void validarFundacionDelRepresentante(Usuario usuario) {
        // Solo los representantes llevan fundacion; a los demas roles se les quita
        if (usuario.getRol() != RolUsuario.REPRESENTANTE_FUNDACION) {
            usuario.setFundacion(null);
            return;
        }

        if (usuario.getFundacion() == null || usuario.getFundacion().getId() == null) {
            throw new ReglaNegocioException(
                    "Un representante de fundacion debe indicar la fundacion a la que pertenece");
        }

        Long fundacionId = usuario.getFundacion().getId();
        Fundacion fundacion = fundacionRepository.findById(fundacionId).orElse(null);
        if (fundacion == null || !fundacion.getEstadoActivo()) {
            throw new RecursoNoEncontradoException("No existe una fundacion con id " + fundacionId);
        }

        if (fundacion.getEstadoVerificacion() != EstadoVerificacion.VERIFICADA) {
            throw new ReglaNegocioException("La fundacion " + fundacion.getNombre() + " aun no esta verificada");
        }

        // Se guarda la fundacion completa traida de la base, no solo el id que llego en el JSON
        usuario.setFundacion(fundacion);
    }
}

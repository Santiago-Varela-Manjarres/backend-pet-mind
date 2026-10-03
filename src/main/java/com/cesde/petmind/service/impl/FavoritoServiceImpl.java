package com.cesde.petmind.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cesde.petmind.exception.RecursoNoEncontradoException;
import com.cesde.petmind.exception.ReglaNegocioException;
import com.cesde.petmind.model.entity.CampanaDonacion;
import com.cesde.petmind.model.entity.Favorito;
import com.cesde.petmind.model.entity.Fundacion;
import com.cesde.petmind.model.entity.Mascota;
import com.cesde.petmind.model.entity.Usuario;
import com.cesde.petmind.repository.CampanaDonacionRepository;
import com.cesde.petmind.repository.FavoritoRepository;
import com.cesde.petmind.repository.FundacionRepository;
import com.cesde.petmind.repository.MascotaRepository;
import com.cesde.petmind.repository.UsuarioRepository;
import com.cesde.petmind.service.FavoritoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoritoServiceImpl implements FavoritoService {

    private final FavoritoRepository favoritoRepository;

    private final UsuarioRepository usuarioRepository;

    private final MascotaRepository mascotaRepository;

    private final CampanaDonacionRepository campanaDonacionRepository;

    private final FundacionRepository fundacionRepository;

    @Override
    public List<Favorito> listar() {
        return favoritoRepository.findByEstadoActivoTrue();
    }

    @Override
    public Favorito obtenerPorId(Long id) {
        Favorito favorito = favoritoRepository.findById(id).orElse(null);

        // Un favorito borrado logicamente se trata igual que uno que no existe
        if (favorito == null || !favorito.getEstadoActivo()) {
            throw new RecursoNoEncontradoException("No existe un favorito con id " + id);
        }
        return favorito;
    }

    @Override
    public Favorito crear(Favorito favorito) {
        validarUnSoloDestino(favorito);

        if (favorito.getUsuario() == null) {
            throw new ReglaNegocioException("El favorito debe indicar el usuario que lo guarda");
        }

        // En el JSON solo llegan los id; aca se traen los registros completos de la base
        Long usuarioId = favorito.getUsuario().getId();
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null || !usuario.getEstadoActivo()) {
            throw new RecursoNoEncontradoException("No existe un usuario con id " + usuarioId);
        }
        favorito.setUsuario(usuario);

        if (favorito.getMascota() != null) {
            Long mascotaId = favorito.getMascota().getId();
            Mascota mascota = mascotaRepository.findById(mascotaId).orElse(null);
            if (mascota == null || !mascota.getEstadoActivo()) {
                throw new RecursoNoEncontradoException("No existe una mascota con id " + mascotaId);
            }
            favorito.setMascota(mascota);
        }

        if (favorito.getCampana() != null) {
            Long campanaId = favorito.getCampana().getId();
            CampanaDonacion campana = campanaDonacionRepository.findById(campanaId).orElse(null);
            if (campana == null || !campana.getEstadoActivo()) {
                throw new RecursoNoEncontradoException("No existe una campana con id " + campanaId);
            }
            favorito.setCampana(campana);
        }

        if (favorito.getFundacion() != null) {
            Long fundacionId = favorito.getFundacion().getId();
            Fundacion fundacion = fundacionRepository.findById(fundacionId).orElse(null);
            if (fundacion == null || !fundacion.getEstadoActivo()) {
                throw new RecursoNoEncontradoException("No existe una fundacion con id " + fundacionId);
            }
            favorito.setFundacion(fundacion);
        }

        return favoritoRepository.save(favorito);
    }

    @Override
    public void eliminar(Long id) {
        Favorito favorito = obtenerPorId(id);

        // Borrado logico: no se borra la fila, solo se desactiva
        favorito.setEstadoActivo(false);
        favoritoRepository.save(favorito);
    }

    @Override
    public List<Favorito> listarPorUsuario(Long usuarioId) {
        return favoritoRepository.findByUsuarioIdAndEstadoActivoTrue(usuarioId);
    }

    // Regla de negocio 3: un favorito apunta a UNA sola cosa: mascota, campana o fundacion
    private void validarUnSoloDestino(Favorito favorito) {
        int destinos = 0;
        if (favorito.getMascota() != null) {
            destinos++;
        }
        if (favorito.getCampana() != null) {
            destinos++;
        }
        if (favorito.getFundacion() != null) {
            destinos++;
        }

        if (destinos != 1) {
            throw new ReglaNegocioException(
                    "El favorito debe apuntar a una sola cosa: una mascota, una campana o una fundacion");
        }
    }
}

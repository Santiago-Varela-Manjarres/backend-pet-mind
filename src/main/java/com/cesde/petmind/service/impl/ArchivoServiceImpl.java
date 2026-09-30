package com.cesde.petmind.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cesde.petmind.model.entity.Archivo;
import com.cesde.petmind.model.entity.CampanaDonacion;
import com.cesde.petmind.model.entity.Historia;
import com.cesde.petmind.model.entity.Mascota;
import com.cesde.petmind.model.entity.ReporteAnimal;
import com.cesde.petmind.model.entity.SolicitudAdopcion;
import com.cesde.petmind.repository.ArchivoRepository;
import com.cesde.petmind.repository.CampanaDonacionRepository;
import com.cesde.petmind.repository.HistoriaRepository;
import com.cesde.petmind.repository.MascotaRepository;
import com.cesde.petmind.repository.ReporteAnimalRepository;
import com.cesde.petmind.repository.SolicitudAdopcionRepository;
import com.cesde.petmind.service.ArchivoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArchivoServiceImpl implements ArchivoService {

    private final ArchivoRepository archivoRepository;

    private final MascotaRepository mascotaRepository;

    private final SolicitudAdopcionRepository solicitudAdopcionRepository;

    private final CampanaDonacionRepository campanaDonacionRepository;

    private final HistoriaRepository historiaRepository;

    private final ReporteAnimalRepository reporteAnimalRepository;

    @Override
    public List<Archivo> listar() {
        return archivoRepository.findByEstadoActivoTrue();
    }

    @Override
    public Archivo obtenerPorId(Long id) {
        Archivo archivo = archivoRepository.findById(id).orElse(null);

        // Un archivo borrado logicamente se trata igual que uno que no existe
        if (archivo == null || !archivo.getEstadoActivo()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un archivo con id " + id);
        }
        return archivo;
    }

    @Override
    public Archivo crear(Archivo archivo) {
        validarUnSoloDueno(archivo);

        // En el JSON solo llega el id del dueno; aca se trae el registro completo de la base
        if (archivo.getMascota() != null) {
            Long mascotaId = archivo.getMascota().getId();
            Mascota mascota = mascotaRepository.findById(mascotaId).orElse(null);
            if (mascota == null || !mascota.getEstadoActivo()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe una mascota con id " + mascotaId);
            }
            archivo.setMascota(mascota);
        }

        if (archivo.getSolicitud() != null) {
            Long solicitudId = archivo.getSolicitud().getId();
            SolicitudAdopcion solicitud = solicitudAdopcionRepository.findById(solicitudId).orElse(null);
            if (solicitud == null || !solicitud.getEstadoActivo()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe una solicitud con id " + solicitudId);
            }
            archivo.setSolicitud(solicitud);
        }

        if (archivo.getCampana() != null) {
            Long campanaId = archivo.getCampana().getId();
            CampanaDonacion campana = campanaDonacionRepository.findById(campanaId).orElse(null);
            if (campana == null || !campana.getEstadoActivo()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe una campana con id " + campanaId);
            }
            archivo.setCampana(campana);
        }

        if (archivo.getHistoria() != null) {
            Long historiaId = archivo.getHistoria().getId();
            Historia historia = historiaRepository.findById(historiaId).orElse(null);
            if (historia == null || !historia.getEstadoActivo()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe una historia con id " + historiaId);
            }
            archivo.setHistoria(historia);
        }

        if (archivo.getReporte() != null) {
            Long reporteId = archivo.getReporte().getId();
            ReporteAnimal reporte = reporteAnimalRepository.findById(reporteId).orElse(null);
            if (reporte == null || !reporte.getEstadoActivo()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe un reporte con id " + reporteId);
            }
            archivo.setReporte(reporte);
        }

        return archivoRepository.save(archivo);
    }

    @Override
    public Archivo actualizar(Long id, Archivo archivo) {
        Archivo existente = obtenerPorId(id);

        // Solo cambian los datos del archivo; el registro al que pertenece no cambia
        existente.setUrl(archivo.getUrl());
        existente.setTipoMedio(archivo.getTipoMedio());
        existente.setCategoria(archivo.getCategoria());
        existente.setEsPortada(archivo.getEsPortada());
        existente.setOrden(archivo.getOrden());
        return archivoRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        Archivo archivo = obtenerPorId(id);

        // Borrado logico: no se borra la fila, solo se desactiva
        archivo.setEstadoActivo(false);
        archivoRepository.save(archivo);
    }

    @Override
    public List<Archivo> listarPorMascota(Long mascotaId) {
        return archivoRepository.findByMascotaIdAndEstadoActivoTrueOrderByOrdenAsc(mascotaId);
    }

    // Regla de negocio 3: un archivo pertenece a UN solo registro:
    // mascota, solicitud, campana, historia o reporte
    private void validarUnSoloDueno(Archivo archivo) {
        int duenos = 0;
        if (archivo.getMascota() != null) {
            duenos++;
        }
        if (archivo.getSolicitud() != null) {
            duenos++;
        }
        if (archivo.getCampana() != null) {
            duenos++;
        }
        if (archivo.getHistoria() != null) {
            duenos++;
        }
        if (archivo.getReporte() != null) {
            duenos++;
        }

        if (duenos != 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El archivo debe pertenecer a un solo registro: mascota, solicitud, campana, historia o reporte");
        }
    }
}

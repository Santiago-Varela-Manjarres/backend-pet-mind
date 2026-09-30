package com.cesde.petmind.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cesde.petmind.model.entity.Favorito;
import com.cesde.petmind.service.FavoritoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/favoritos")
@RequiredArgsConstructor
public class FavoritoController {

    private final FavoritoService favoritoService;

    @GetMapping
    public ResponseEntity<List<Favorito>> listar() {
        return ResponseEntity.ok(favoritoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Favorito> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(favoritoService.obtenerPorId(id));
    }

    // Ejemplo: GET /api/favoritos/por-usuario?usuarioId=1
    @GetMapping("/por-usuario")
    public ResponseEntity<List<Favorito>> listarPorUsuario(@RequestParam Long usuarioId) {
        return ResponseEntity.ok(favoritoService.listarPorUsuario(usuarioId));
    }

    @PostMapping
    public ResponseEntity<Favorito> crear(@RequestBody Favorito favorito) {
        return ResponseEntity.status(HttpStatus.CREATED).body(favoritoService.crear(favorito));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        favoritoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

package com.cesde.petmind.model.entity;

import com.cesde.petmind.model.base.BaseEntity;
import com.cesde.petmind.model.enums.CategoriaArchivo;
import com.cesde.petmind.model.enums.TipoMedio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)

@Entity
@Table(name = "archivos")
public class Archivo extends BaseEntity {

    // Tabla transversal de medios: las cinco FK son nulas y solo una se llena.
    // La validacion de "solo una llena" va en el servicio, no aca.

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mascota_id")
    private Mascota mascota;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id")
    private SolicitudAdopcion solicitud;

    // PENDIENTE: descomentar cuando Emmanuel suba CampanaDonacion
    // @ToString.Exclude
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "campana_id")
    // private CampanaDonacion campana;

    // PENDIENTE: descomentar cuando Santiago suba Historia
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "historia_id")
    private Historia historia;

    // PENDIENTE: descomentar cuando Emmanuel suba ReporteAnimal
    // @ToString.Exclude
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "reporte_id")
    // private ReporteAnimal reporte;

    @Column(name = "url", nullable = false, length = 500)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_medio", nullable = false)
    private TipoMedio tipoMedio;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false)
    private CategoriaArchivo categoria;

    @Builder.Default
    @Column(name = "es_portada", nullable = false)
    private Boolean esPortada = false;

    // Posicion dentro de la galeria
    @Column(name = "orden")
    private Integer orden;
}

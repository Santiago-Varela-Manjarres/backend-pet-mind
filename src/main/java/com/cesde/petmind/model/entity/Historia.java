package com.cesde.petmind.model.entity;

import java.time.LocalDateTime;

import com.cesde.petmind.model.base.BaseEntity;
import com.cesde.petmind.model.enums.TipoHistoria;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Table(name = "historias")
public class Historia extends BaseEntity {

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fundacion_id", nullable = false)
    private Fundacion fundacion;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mascota_id")
    private Mascota mascota;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "resumen", nullable = false, length = 500)
    private String resumen;

    @Column(name = "contenido", nullable = false, length = 5000)
    private String contenido;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoHistoria tipo;

    @Builder.Default
    @Column(name = "contador_likes", nullable = false)
    private Integer contadorLikes = 0;

    @Builder.Default
    @Column(name = "es_destacada", nullable = false)
    private Boolean esDestacada = false;

    @Column(name = "fecha_publicacion")
    private LocalDateTime fechaPublicacion;
}
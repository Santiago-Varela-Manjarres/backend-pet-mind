package com.cesde.petmind.model.entity;

import java.time.LocalDateTime;

import com.cesde.petmind.model.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "actualizaciones")
public class Actualizacion extends BaseEntity {

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campana_id")
    private CampanaDonacion campana;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "historia_id")
    private Historia historia;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "contenido", nullable = false, length = 5000)
    private String contenido;

    @Column(name = "fecha_evento", nullable = false)
    private LocalDateTime fechaEvento;

    @Column(name = "orden")
    private Integer orden;
}
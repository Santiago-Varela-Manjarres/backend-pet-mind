package com.cesde.petmind.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.cesde.petmind.model.base.BaseEntity;
import com.cesde.petmind.model.enums.CategoriaCampana;
import com.cesde.petmind.model.enums.EstadoCampana;

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
@Table(name = "campanas_donacion")
public class CampanaDonacion extends BaseEntity {

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fundacion_id", nullable = false)
    private Fundacion fundacion;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mascota_id")
    private Mascota mascota;

    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @Column(name = "descripcion", nullable = false, length = 1000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false)
    private CategoriaCampana categoria;

    @Column(name = "meta_monto", nullable = false, precision = 12, scale = 2)
    private BigDecimal metaMonto;

    @Builder.Default
    @Column(name = "monto_recaudado", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoRecaudado = BigDecimal.ZERO;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoCampana estado;

    @Builder.Default
    @Column(name = "es_urgente", nullable = false)
    private Boolean esUrgente = false;
}

package com.cesde.petmind.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.cesde.petmind.model.base.BaseEntity;
import com.cesde.petmind.model.enums.EstadoAtencion;

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
@Table(name = "reporte_fundacion")
public class ReporteFundacion extends BaseEntity {

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporte_id", nullable = false)
    private ReporteAnimal reporte;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fundacion_id", nullable = false)
    private Fundacion fundacion;

    @Column(name = "distancia_km", precision = 10, scale = 2)
    private BigDecimal distanciaKm;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_atencion", nullable = false)
    private EstadoAtencion estadoAtencion;

    @Column(name = "fecha_notificacion")
    private LocalDateTime fechaNotificacion;
}
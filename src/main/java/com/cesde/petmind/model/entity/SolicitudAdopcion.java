package com.cesde.petmind.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.cesde.petmind.model.base.BaseEntity;


import com.cesde.petmind.model.enums.EstadoSolicitud;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
@Table(name = "solicitudes_adopcion")
public class SolicitudAdopcion extends BaseEntity {

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoSolicitud estado;

    @Column(name = "porcentaje_afinidad")
    private Integer porcentajeAfinidad;

    @Embedded
    private DatosHogar datosHogar;

    @Embedded
    private Cita cita;

    @Column(name = "notas_internas", length = 2000)
    private String notasInternas;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDate fechaSolicitud;

    @Column(name = "fecha_en_revision")
    private LocalDateTime fechaEnRevision;

    @Column(name = "fecha_entrevista")
    private LocalDateTime fechaEntrevista;

    @Column(name = "fecha_visita_hogar")
    private LocalDateTime fechaVisitaHogar;

    @Column(name = "fecha_decision")
    private LocalDateTime fechaDecision;
}
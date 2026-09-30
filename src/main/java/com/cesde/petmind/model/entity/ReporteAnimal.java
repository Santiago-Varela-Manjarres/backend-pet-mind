package com.cesde.petmind.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.cesde.petmind.model.base.BaseEntity;
import com.cesde.petmind.model.embeddable.Direccion;
import com.cesde.petmind.model.enums.Especie;
import com.cesde.petmind.model.enums.EstadoReporte;
import com.cesde.petmind.model.enums.NivelUrgencia;
import com.cesde.petmind.model.enums.TipoCaso;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "reportes_animal")
public class ReporteAnimal extends BaseEntity {

    // Relacion pendiente de integrar:
    // - @ManyToOne opcional hacia Usuario mediante usuario_id.

    @Column(name = "codigo_seguimiento", nullable = false, unique = true, length = 50)
    private String codigoSeguimiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_caso", nullable = false)
    private TipoCaso tipoCaso;

    @Enumerated(EnumType.STRING)
    @Column(name = "especie", nullable = false)
    private Especie especie;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_urgencia", nullable = false)
    private NivelUrgencia nivelUrgencia;

    @Embedded
    private Direccion direccion;

    @Column(name = "latitud", precision = 10, scale = 7)
    private BigDecimal latitud;

    @Column(name = "longitud", precision = 10, scale = 7)
    private BigDecimal longitud;

    @Column(name = "descripcion", nullable = false, length = 1000)
    private String descripcion;

    @Column(name = "nombre_contacto", length = 150)
    private String nombreContacto;

    @Column(name = "telefono_contacto", length = 20)
    private String telefonoContacto;

    @Builder.Default
    @Column(name = "es_anonimo", nullable = false)
    private Boolean esAnonimo = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoReporte estado;

    @Column(name = "fecha_reporte", nullable = false)
    private LocalDate fechaReporte;
}

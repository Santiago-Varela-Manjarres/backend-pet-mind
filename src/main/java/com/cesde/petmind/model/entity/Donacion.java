package com.cesde.petmind.model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

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
import lombok.Builder;
import com.cesde.petmind.model.base.BaseEntity;
import com.cesde.petmind.model.enums.EstadoDonacion;
import com.cesde.petmind.model.enums.EstadoRecurrencia;
import com.cesde.petmind.model.enums.Frecuencia;
import com.cesde.petmind.model.enums.MetodoPago;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)

@Entity
@Table(name = "donaciones")
public class Donacion extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "monto", nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Column(name = "valor_comision", precision = 12, scale = 2)
    private BigDecimal valorComision;

    @Column(name = "monto_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal montoTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donacion_origen_id")
    @ToString.Exclude
    private Donacion donacionOrigen;

    @Enumerated(EnumType.STRING)
    @Column(name = "frecuencia", nullable = false)
    private Frecuencia frecuencia;

    @Column(name = "fecha_donacion", nullable = false)
    private LocalDate fechaDonacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false)
    private MetodoPago metodoPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoDonacion estado;

    @Builder.Default
    @Column(name = "es_anonima", nullable = false)
    private Boolean esAnonima = false;

    @Column(name = "mensaje", length = 500)
    private String mensaje;

    @Column(name = "referencia_pago", unique = true, length = 100)
    private String referenciaPago;

    @Column(name = "nombre_donante", length = 150)
    private String nombreDonante;

    @Column(name = "correo_donante", length = 100)
    private String correoDonante;

    @Column(name = "documento_donante", length = 30)
    private String documentoDonante;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_recurrencia")
    private EstadoRecurrencia estadoRecurrencia;

    @Column(name = "dia_cobro")
    private Integer diaCobro;

    @Column(name = "fecha_proximo_cobro")
    private LocalDate fechaProximoCobro;

    @Column(name = "certificado_codigo", unique = true, length = 100)
    private String certificadoCodigo;

    @Column(name = "certificado_url", length = 500)
    private String certificadoUrl;

    @Column(name = "certificado_fecha")
    private LocalDate certificadoFecha;
}

package com.cesde.petmind.model.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import com.cesde.petmind.model.base.BaseEntity;
import com.cesde.petmind.model.embeddable.Direccion;
import com.cesde.petmind.model.embeddable.InformacionContacto;
import com.cesde.petmind.model.enums.EstadoVerificacion;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)

@Entity
@Table(name = "fundaciones")
public class Fundacion extends BaseEntity {

    @Column(name = "nombre", nullable = false, unique = true, length = 150)
    private String nombre;

    @Column(name = "nit", nullable = false, unique = true, length = 20)
    private String nit;

    @Column(name = "representante_legal", nullable = false, length = 150)
    private String representanteLegal;

    @Column(name = "descripcion", length = 1000)
    private String descripcion;

    @Embedded
    private Direccion direccion;

    @Embedded
    private InformacionContacto contacto;

    @Column(name = "url_logo", length = 500)
    private String urlLogo;

    @Column(name = "url_portada", length = 500)
    private String urlPortada;

    // Documentos legales que la fundacion sube para su verificacion
    @Column(name = "url_rut", length = 500)
    private String urlRut;

    @Column(name = "url_camara_comercio", length = 500)
    private String urlCamaraComercio;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_verificacion", nullable = false)
    private EstadoVerificacion estadoVerificacion;

    @Column(name = "fecha_verificacion")
    private LocalDateTime fechaVerificacion;

    // Para ubicar la fundacion en el mapa de las pantallas
    @Column(name = "latitud", precision = 10, scale = 7)
    private BigDecimal latitud;

    @Column(name = "longitud", precision = 10, scale = 7)
    private BigDecimal longitud;
}

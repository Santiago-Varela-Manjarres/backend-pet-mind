package com.cesde.petmind.model.entity;

import java.time.LocalDateTime;

import com.cesde.petmind.model.base.BaseEntity;

import com.cesde.petmind.model.embeddable.CualidadesFisicas;
import com.cesde.petmind.model.enums.Especie;
import com.cesde.petmind.model.enums.EstadoAdopcion;

import com.cesde.petmind.model.enums.SexoMascota;
import com.cesde.petmind.model.enums.TamanoMascota;

import lombok.Builder;
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
@Table(name = "mascotas")
public class Mascota extends BaseEntity {

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fundacion_id", nullable = false)
    private Fundacion fundacion;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "especie", nullable = false)
    private Especie especie;

    @Column(name = "raza", length = 100)
    private String raza;

    @Column(name = "edad_meses")
    private Integer edadMeses;

    @Enumerated(EnumType.STRING)
    @Column(name = "tamano")
    private TamanoMascota tamano;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo")
    private SexoMascota sexo;

    @Embedded
    private CualidadesFisicas cualidadesFisicas;

    @Column(name = "ciudad", length = 100)
    private String ciudad;

    @Column(name = "descripcion", length = 1000)
    private String descripcion;

    @Column(name = "historia", length = 2000)
    private String historia;

    @Column(name = "etiquetas", length = 500)
    private String etiquetas;

    @Column(name = "nivel_energia")
    private Integer nivelEnergia;

    @Embedded
    private Afinidad afinidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_adopcion", nullable = false)
    private EstadoAdopcion estadoAdopcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_publicacion", nullable = false)
    private EstadoPublicacion estadoPublicacion;

    @Builder.Default
    @Column(name = "esterilizado", nullable = false)
    private Boolean esterilizado = false;

    @Builder.Default
    @Column(name = "vacunas_al_dia", nullable = false)
    private Boolean vacunasAlDia = false;

    @Builder.Default
    @Column(name = "desparasitado", nullable = false)
    private Boolean desparasitado = false;

    @Builder.Default
    @Column(name = "tiene_microchip", nullable = false)
    private Boolean tieneMicrochip = false;

    @Builder.Default
    @Column(name = "apto_apadrinamiento", nullable = false)
    private Boolean aptoApadrinamiento = false;

    @Column(name = "fecha_publicacion")
    private LocalDateTime fechaPublicacion;

    @Builder.Default
    @Column(name = "contador_visitas", nullable = false)
    private Integer contadorVisitas = 0;
}
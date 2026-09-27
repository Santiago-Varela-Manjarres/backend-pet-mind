package com.cesde.petmind.model.embeddable;

import com.cesde.petmind.model.enums.TipoVivienda;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class DatosHogar {

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_vivienda")
    private TipoVivienda tipoVivienda;

    @Column(name = "es_propietario")
    private Boolean esPropietario;

    @Column(name = "tiene_patio")
    private Boolean tienePatio;

    @Column(name = "numero_convivientes")
    private Integer numeroConvivientes;

    @Column(name = "tiene_otra_mascota")
    private Boolean tieneOtraMascota;

    @Column(name = "descripcion_otras_mascotas", length = 500)
    private String descripcionOtrasMascotas;

    @Column(name = "experiencia_previa", length = 500)
    private String experienciaPrevia;

    @Column(name = "horas_disponibles_dia")
    private Integer horasDisponiblesDia;

    @Column(name = "motivo_adopcion", length = 500)
    private String motivoAdopcion;

    @Column(name = "acepta_compromisos")
    private Boolean aceptaCompromisos;

    @Column(name = "autoriza_datos")
    private Boolean autorizaDatos;
}

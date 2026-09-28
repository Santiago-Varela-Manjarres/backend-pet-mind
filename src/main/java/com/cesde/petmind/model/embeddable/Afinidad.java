package com.cesde.petmind.model.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Afinidad {

    @Column(name = "nivel_energia")
    private Integer nivelEnergia;

    @Column(name = "afinidad_perros")
    private Integer afinidadPerros;

    @Column(name = "afinidad_gatos")
    private Integer afinidadGatos;

    @Column(name = "afinidad_ninos")
    private Integer afinidadNinos;
}

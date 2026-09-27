package com.cesde.petmind.model.embeddable;

import com.cesde.petmind.model.enums.EstadoCita;
import com.cesde.petmind.model.enums.Modalidad;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Cita {

    @Column(name = "cita_fecha")
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "cita_modalidad")
    private Modalidad modalidad;

    @Column(name = "cita_url_reunion", length = 500)
    private String urlReunion;

    @Enumerated(EnumType.STRING)
    @Column(name = "cita_estado")
    private EstadoCita estado;
}

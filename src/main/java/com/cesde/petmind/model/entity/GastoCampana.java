package com.cesde.petmind.model.entity;

import java.math.BigDecimal;

import com.cesde.petmind.model.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "gastos_campana")
public class GastoCampana extends BaseEntity {

    // Relacion pendiente de integrar:
    // - @ManyToOne hacia CampanaDonacion mediante campana_id.

    @Column(name = "concepto", nullable = false, length = 250)
    private String concepto;

    @Column(name = "monto", nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;

    @Column(name = "orden")
    private Integer orden;
}

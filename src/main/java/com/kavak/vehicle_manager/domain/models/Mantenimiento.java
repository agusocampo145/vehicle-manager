package com.kavak.vehicle_manager.domain.models;

import com.kavak.vehicle_manager.domain.enums.EstadoMantenimiento;
import com.kavak.vehicle_manager.domain.enums.TipoMantenimiento;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "mantenimientos")
public class Mantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Vehiculo vehiculo;

    @Enumerated(EnumType.STRING)
    private TipoMantenimiento tipoMantenimiento;

    private String descripcion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    private EstadoMantenimiento estado;

    @Column(name = "costo_estimado", nullable = false)
    private BigDecimal costoEstimado;

    @Column(name = "costo_final")
    private BigDecimal costoFinal;
}


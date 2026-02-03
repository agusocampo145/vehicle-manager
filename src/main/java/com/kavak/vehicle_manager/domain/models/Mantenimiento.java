package com.kavak.vehicle_manager.domain.models;

import com.kavak.vehicle_manager.domain.enums.EstadoMantenimiento;
import com.kavak.vehicle_manager.domain.enums.TipoMantenimiento;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "mantenimientos")
public class Mantenimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(
            name = "vehiculo_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_mantenimientos_vehiculo")
    )
    private Vehiculo vehiculo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_mantenimiento", nullable = false, length = 30)
    private TipoMantenimiento tipoMantenimiento;

    @Column(columnDefinition = "text")
    private String descripcion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoMantenimiento estado;

    @Column(name = "costo_estimado", nullable = false, precision = 12, scale = 2)
    private BigDecimal costoEstimado;

    @Column(name = "costo_final", precision = 12, scale = 2)
    private BigDecimal costoFinal;

    public Mantenimiento(
            Vehiculo vehiculo,
            TipoMantenimiento tipoMantenimiento,
            String descripcion,
            BigDecimal costoEstimado
    ) {
        this.vehiculo = vehiculo;
        this.tipoMantenimiento = tipoMantenimiento;
        this.descripcion = descripcion;
        this.costoEstimado = costoEstimado;
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoMantenimiento.PENDIENTE;
    }
}

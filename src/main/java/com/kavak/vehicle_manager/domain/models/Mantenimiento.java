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
        if (vehiculo == null) {
            throw new IllegalArgumentException("El vehículo es obligatorio");
        }
        if (tipoMantenimiento == null) {
            throw new IllegalArgumentException("El tipo de mantenimiento es obligatorio");
        }
        if (costoEstimado == null || costoEstimado.signum() < 0) {
            throw new IllegalArgumentException("El costo estimado debe ser cero o positivo");
        }

        this.vehiculo = vehiculo;
        this.tipoMantenimiento = tipoMantenimiento;
        this.descripcion = descripcion;
        this.costoEstimado = costoEstimado;
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoMantenimiento.PENDIENTE;
    }

    public void iniciar() {
        if (this.estado != EstadoMantenimiento.PENDIENTE) {
            throw new IllegalStateException("Solo se puede iniciar un mantenimiento en estado PENDIENTE");
        }
        this.estado = EstadoMantenimiento.EN_PROCESO;
    }

    public void cancelar() {
        if (this.estado == EstadoMantenimiento.COMPLETADO) {
            throw new IllegalStateException("Un mantenimiento COMPLETADO no puede ser cancelado");
        }
        if (this.estado == EstadoMantenimiento.CANCELADO) {
            return;
        }
        this.estado = EstadoMantenimiento.CANCELADO;
    }

    public void completar(BigDecimal costoFinal) {
        if (this.estado != EstadoMantenimiento.EN_PROCESO) {
            throw new IllegalStateException("Solo se puede completar un mantenimiento en estado EN_PROCESO");
        }
        if (costoFinal == null || costoFinal.signum() < 0) {
            throw new IllegalArgumentException("El costo final debe ser cero o positivo");
        }

        this.costoFinal = costoFinal;
        this.estado = EstadoMantenimiento.COMPLETADO;
    }

    public boolean estaActivo() {
        return this.estado == EstadoMantenimiento.PENDIENTE || this.estado == EstadoMantenimiento.EN_PROCESO;
    }
}

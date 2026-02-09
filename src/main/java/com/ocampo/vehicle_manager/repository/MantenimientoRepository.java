package com.ocampo.vehicle_manager.repository;

import com.ocampo.vehicle_manager.domain.enums.EstadoMantenimiento;
import com.ocampo.vehicle_manager.domain.models.Mantenimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface MantenimientoRepository extends JpaRepository<Mantenimiento, Long> {

    List<Mantenimiento> findAllByVehiculoIdOrderByFechaCreacionDesc(Long vehiculoId);

    List<Mantenimiento> findAllByVehiculoIdAndEstadoInOrderByFechaCreacionDesc(
            Long vehiculoId,
            Collection<EstadoMantenimiento> estados
    );

    boolean existsByVehiculoIdAndEstadoIn(Long vehiculoId, Collection<EstadoMantenimiento> estados);
}

package com.ocampo.vehicle_manager.repository;

import com.ocampo.vehicle_manager.domain.models.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    Optional<Vehiculo> findByPatente(String patente);
    boolean existsByPatente(String patente);

}

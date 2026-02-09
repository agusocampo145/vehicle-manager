package com.ocampo.vehicle_manager.service;

import com.ocampo.vehicle_manager.domain.enums.EstadoMantenimiento;
import com.ocampo.vehicle_manager.domain.models.Vehiculo;
import com.ocampo.vehicle_manager.exceptions.RecursoNoEncontradoException;
import com.ocampo.vehicle_manager.exceptions.ReglaDeNegocioException;
import com.ocampo.vehicle_manager.repository.MantenimientoRepository;
import com.ocampo.vehicle_manager.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VehiculoService {

    @Autowired
    private VehiculoRepository vehiculoRepository;
    @Autowired
    private MantenimientoRepository mantenimientoRepository;

    @Transactional
    public Vehiculo registrarVehiculo(Vehiculo vehiculoNuevo) {
        if (vehiculoNuevo == null) {
            throw new IllegalArgumentException("El vehículo no puede ser null");
        }

        if (vehiculoRepository.existsByPatente(vehiculoNuevo.getPatente())) {
            throw new ReglaDeNegocioException("Ya existe un vehículo registrado con la patente: " + vehiculoNuevo.getPatente());
        }

        return vehiculoRepository.save(vehiculoNuevo);
    }

    @Transactional(readOnly = true)
    public Vehiculo obtenerPorId(Long id) {
        return vehiculoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un vehículo con id: " + id));
    }

    @Transactional(readOnly = true)
    public Vehiculo obtenerPorPatente(String patente) {
        return vehiculoRepository.findByPatente(patente)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un vehículo con patente: " + patente));
    }

    @Transactional
    public Vehiculo actualizarKilometraje(Long vehiculoId, Integer nuevoKilometraje) {
        Vehiculo vehiculo = obtenerPorId(vehiculoId);
        vehiculo.actualizarKilometraje(nuevoKilometraje);
        return vehiculoRepository.save(vehiculo);
    }

    @Transactional(readOnly = true)
    public boolean estaDisponible(Long vehiculoId) {
        obtenerPorId(vehiculoId);

        return !mantenimientoRepository.existsByVehiculoIdAndEstadoIn(
                vehiculoId,
                List.of(EstadoMantenimiento.PENDIENTE, EstadoMantenimiento.EN_PROCESO)
        );
    }
}

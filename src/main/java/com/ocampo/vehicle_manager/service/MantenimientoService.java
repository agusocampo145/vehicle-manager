package com.ocampo.vehicle_manager.service;

import com.ocampo.vehicle_manager.domain.enums.EstadoMantenimiento;
import com.ocampo.vehicle_manager.domain.enums.TipoMantenimiento;
import com.ocampo.vehicle_manager.domain.models.Mantenimiento;
import com.ocampo.vehicle_manager.domain.models.Vehiculo;
import com.ocampo.vehicle_manager.exceptions.RecursoNoEncontradoException;
import com.ocampo.vehicle_manager.exceptions.ReglaDeNegocioException;
import com.ocampo.vehicle_manager.repository.MantenimientoRepository;
import com.ocampo.vehicle_manager.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class MantenimientoService {

    @Autowired
    private  MantenimientoRepository mantenimientoRepository;

    @Autowired
    private  VehiculoRepository vehiculoRepository;


    @Transactional
    public Mantenimiento registrarMantenimiento(
            Long vehiculoId,
            TipoMantenimiento tipoMantenimiento,
            String descripcion,
            BigDecimal costoEstimado
    ) {
        Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un vehículo con id: " + vehiculoId));

        if (tipoMantenimiento == null) {
            throw new ReglaDeNegocioException("El tipo de mantenimiento es obligatorio");
        }
        if (costoEstimado == null || costoEstimado.signum() < 0) {
            throw new ReglaDeNegocioException("El costo estimado debe ser cero o positivo");
        }

        Mantenimiento mantenimiento = new Mantenimiento(vehiculo, tipoMantenimiento, descripcion, costoEstimado);
        return mantenimientoRepository.save(mantenimiento);
    }

    @Transactional
    public Mantenimiento cambiarEstado(Long mantenimientoId, EstadoMantenimiento nuevoEstado) {
        if (nuevoEstado == null) {
            throw new ReglaDeNegocioException("Debe proveerse un nuevo estado");
        }

        Mantenimiento mantenimiento = obtenerPorId(mantenimientoId);

        try {
            switch (nuevoEstado) {
                case EN_PROCESO -> mantenimiento.iniciar();
                case CANCELADO -> mantenimiento.cancelar();
                case COMPLETADO -> throw new ReglaDeNegocioException(
                        "Para completar un mantenimiento se debe proveer un costo final"
                );
                case PENDIENTE -> throw new ReglaDeNegocioException(
                        "No se permite volver a PENDIENTE una vez creado el mantenimiento"
                );
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ReglaDeNegocioException(e.getMessage());
        }

        return mantenimientoRepository.save(mantenimiento);
    }

    @Transactional
    public Mantenimiento completarMantenimiento(Long mantenimientoId, BigDecimal costoFinal) {
        if (costoFinal == null || costoFinal.signum() < 0) {
            throw new ReglaDeNegocioException("El costo final debe ser cero o positivo");
        }

        Mantenimiento mantenimiento = obtenerPorId(mantenimientoId);

        try {
            mantenimiento.completar(costoFinal);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ReglaDeNegocioException(e.getMessage());
        }

        return mantenimientoRepository.save(mantenimiento);
    }

    @Transactional
    public Mantenimiento cancelarMantenimiento(Long mantenimientoId) {
        Mantenimiento mantenimiento = obtenerPorId(mantenimientoId);

        try {
            mantenimiento.cancelar();
        } catch (IllegalStateException e) {
            throw new ReglaDeNegocioException(e.getMessage());
        }

        return mantenimientoRepository.save(mantenimiento);
    }

    @Transactional(readOnly = true)
    public List<Mantenimiento> listarPorVehiculo(Long vehiculoId) {
        validarVehiculoExiste(vehiculoId);
        return mantenimientoRepository.findAllByVehiculoIdOrderByFechaCreacionDesc(vehiculoId);
    }

    @Transactional(readOnly = true)
    public List<Mantenimiento> listarActivosPorVehiculo(Long vehiculoId) {
        validarVehiculoExiste(vehiculoId);

        return mantenimientoRepository.findAllByVehiculoIdAndEstadoInOrderByFechaCreacionDesc(
                vehiculoId,
                List.of(EstadoMantenimiento.PENDIENTE, EstadoMantenimiento.EN_PROCESO)
        );
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularCostoTotal(Long vehiculoId) {
        validarVehiculoExiste(vehiculoId);

        List<Mantenimiento> completados = mantenimientoRepository.findAllByVehiculoIdAndEstadoInOrderByFechaCreacionDesc(
                vehiculoId,
                List.of(EstadoMantenimiento.COMPLETADO)
        );

        return completados.stream()
                .map(Mantenimiento::getCostoFinal)
                .filter(costo -> costo != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Mantenimiento obtenerPorId(Long mantenimientoId) {
        return mantenimientoRepository.findById(mantenimientoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un mantenimiento con id: " + mantenimientoId));
    }

    private void validarVehiculoExiste(Long vehiculoId) {
        if (!vehiculoRepository.existsById(vehiculoId)) {
            throw new RecursoNoEncontradoException("No existe un vehículo con id: " + vehiculoId);
        }
    }
}

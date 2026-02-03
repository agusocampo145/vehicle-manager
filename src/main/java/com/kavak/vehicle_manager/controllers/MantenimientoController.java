package com.kavak.vehicle_manager.controllers;

import com.kavak.vehicle_manager.api.dto.mantenimiento.mapper.MantenimientoMapper;
import com.kavak.vehicle_manager.api.dto.mantenimiento.request.MantenimientoCambiarEstadoRequest;
import com.kavak.vehicle_manager.api.dto.mantenimiento.request.MantenimientoCompletarRequest;
import com.kavak.vehicle_manager.api.dto.mantenimiento.request.MantenimientoCrearRequest;
import com.kavak.vehicle_manager.api.dto.mantenimiento.response.CostoTotalResponse;
import com.kavak.vehicle_manager.api.dto.mantenimiento.response.MantenimientoResponse;
import com.kavak.vehicle_manager.domain.models.Mantenimiento;
import com.kavak.vehicle_manager.service.MantenimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Mantenimientos", description = "Operaciones de registro y gestión de mantenimientos asociados a vehículos")
@RestController
@RequestMapping("/api/v1")
public class MantenimientoController {

    private final MantenimientoService mantenimientoService;

    public MantenimientoController(MantenimientoService mantenimientoService) {
        this.mantenimientoService = mantenimientoService;
    }

    @Operation(
            summary = "Registrar mantenimiento para un vehículo",
            description = "Crea un mantenimiento asociado a un vehículo. El mantenimiento inicia en estado PENDIENTE."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Mantenimiento creado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @PostMapping("/vehiculos/{vehiculoId}/mantenimientos")
    @ResponseStatus(HttpStatus.CREATED)
    public MantenimientoResponse registrarMantenimiento(
            @Parameter(description = "Id del vehículo", example = "1")
            @PathVariable Long vehiculoId,
            @Valid @RequestBody MantenimientoCrearRequest request
    ) {
        Mantenimiento creado = mantenimientoService.registrarMantenimiento(
                vehiculoId,
                request.tipoMantenimiento(),
                request.descripcion(),
                request.costoEstimado()
        );
        return MantenimientoMapper.toResponse(creado);
    }

    @Operation(
            summary = "Listar historial de mantenimientos de un vehículo",
            description = "Devuelve todos los mantenimientos asociados al vehículo, ordenados por fecha de creación descendente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @GetMapping("/vehiculos/{vehiculoId}/mantenimientos")
    public List<MantenimientoResponse> listarPorVehiculo(
            @Parameter(description = "Id del vehículo", example = "1")
            @PathVariable Long vehiculoId
    ) {
        return mantenimientoService.listarPorVehiculo(vehiculoId)
                .stream()
                .map(MantenimientoMapper::toResponse)
                .toList();
    }

    @Operation(
            summary = "Listar mantenimientos activos de un vehículo",
            description = "Devuelve mantenimientos en estado PENDIENTE o EN_PROCESO."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @GetMapping("/vehiculos/{vehiculoId}/mantenimientos/activos")
    public List<MantenimientoResponse> listarActivosPorVehiculo(
            @Parameter(description = "Id del vehículo", example = "1")
            @PathVariable Long vehiculoId
    ) {
        return mantenimientoService.listarActivosPorVehiculo(vehiculoId)
                .stream()
                .map(MantenimientoMapper::toResponse)
                .toList();
    }

    @Operation(
            summary = "Cambiar estado de un mantenimiento",
            description = "Permite cambiar el estado del mantenimiento. Para completar, usar el endpoint /completar con costoFinal."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado actualizado"),
            @ApiResponse(responseCode = "400", description = "Transición inválida o datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Mantenimiento no encontrado")
    })
    @PatchMapping("/mantenimientos/{mantenimientoId}/estado")
    public MantenimientoResponse cambiarEstado(
            @Parameter(description = "Id del mantenimiento", example = "10")
            @PathVariable Long mantenimientoId,
            @Valid @RequestBody MantenimientoCambiarEstadoRequest request
    ) {
        Mantenimiento actualizado = mantenimientoService.cambiarEstado(mantenimientoId, request.estado());
        return MantenimientoMapper.toResponse(actualizado);
    }

    @Operation(
            summary = "Completar un mantenimiento",
            description = "Completa un mantenimiento en estado EN_PROCESO y registra el costo final."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mantenimiento completado"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o estado inválido"),
            @ApiResponse(responseCode = "404", description = "Mantenimiento no encontrado")
    })
    @PostMapping("/mantenimientos/{mantenimientoId}/completar")
    public MantenimientoResponse completar(
            @Parameter(description = "Id del mantenimiento", example = "10")
            @PathVariable Long mantenimientoId,
            @Valid @RequestBody MantenimientoCompletarRequest request
    ) {
        Mantenimiento completado = mantenimientoService.completarMantenimiento(mantenimientoId, request.costoFinal());
        return MantenimientoMapper.toResponse(completado);
    }

    @Operation(
            summary = "Calcular costo total de mantenimientos completados de un vehículo",
            description = "Suma el costoFinal de los mantenimientos COMPLETADOS del vehículo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Costo total calculado"),
            @ApiResponse(responseCode = "404", description = "Vehículo no encontrado")
    })
    @GetMapping("/vehiculos/{vehiculoId}/mantenimientos/costo-total")
    public CostoTotalResponse costoTotal(
            @Parameter(description = "Id del vehículo", example = "1")
            @PathVariable Long vehiculoId
    ) {
        BigDecimal total = mantenimientoService.calcularCostoTotal(vehiculoId);
        return new CostoTotalResponse(vehiculoId, total);
    }
}


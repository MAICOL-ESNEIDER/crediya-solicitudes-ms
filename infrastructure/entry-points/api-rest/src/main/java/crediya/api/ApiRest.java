package crediya.api;

import crediya.api.dto.RespuestaError;
import crediya.api.dto.SolicitudRequest;
import crediya.api.dto.SolicitudResponse;
import crediya.model.solicitud.Solicitud;
import crediya.usecase.solicitud.SolicitudUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

/**
 * Controlador REST (entry point): recibe las peticiones HTTP y se las pasa al caso de uso.
 */
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Solicitudes", description = "Registro de solicitudes de préstamo de CrediYa")
public class ApiRest {

    private static final Logger LOG = Logger.getLogger(ApiRest.class.getName());

    private final SolicitudUseCase solicitudUseCase;

    @PostMapping(path = "/solicitud", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Registrar una solicitud de préstamo",
            description = "Valida los datos, verifica que el tipo de préstamo exista y guarda la solicitud "
                    + "con estado Pendiente de revisión.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Solicitud registrada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos",
                    content = @Content(schema = @Schema(implementation = RespuestaError.class))),
            @ApiResponse(responseCode = "404", description = "El tipo de préstamo no existe",
                    content = @Content(schema = @Schema(implementation = RespuestaError.class))),
            @ApiResponse(responseCode = "500", description = "Error inesperado",
                    content = @Content(schema = @Schema(implementation = RespuestaError.class)))
    })
    public ResponseEntity<SolicitudResponse> registrarSolicitud(@RequestBody SolicitudRequest request) {
        LOG.info("Petición recibida: POST /api/v1/solicitud");

        Solicitud registrada = solicitudUseCase.registrarSolicitud(request.toDomain());

        LOG.info(() -> "Respuesta enviada: solicitud creada con id " + registrada.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(SolicitudResponse.fromDomain(registrada));
    }
}

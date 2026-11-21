package crediya.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import crediya.model.solicitud.Solicitud;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

/**
 * DTO de entrada: el JSON que el cliente envía en el POST /api/v1/solicitud.
 */
@Schema(description = "Datos para registrar una solicitud de préstamo")
public record SolicitudRequest(
        @Schema(example = "1000123456") @JsonProperty("documento_identidad") String documentoIdentidad,
        @Schema(example = "5000000") @JsonProperty("monto") BigDecimal monto,
        @Schema(example = "24", description = "Plazo en meses") @JsonProperty("plazo") Integer plazo,
        @Schema(example = "1", description = "Id del tipo de préstamo") @JsonProperty("id_tipo_prestamo") Long idTipoPrestamo,
        @Schema(example = "maicol@correo.com") @JsonProperty("email") String email) {

    public Solicitud toDomain() {
        return Solicitud.builder()
                .documentoIdentidad(documentoIdentidad)
                .monto(monto)
                .plazo(plazo)
                .idTipoPrestamo(idTipoPrestamo)
                .email(email)
                .build();
    }
}

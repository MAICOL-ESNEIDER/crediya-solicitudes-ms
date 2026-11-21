package crediya.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import crediya.model.solicitud.Solicitud;

import java.math.BigDecimal;

/**
 * DTO de salida: el JSON que la API devuelve al registrar una solicitud.
 */
public record SolicitudResponse(
        @JsonProperty("id_solicitud") Long id,
        @JsonProperty("documento_identidad") String documentoIdentidad,
        @JsonProperty("monto") BigDecimal monto,
        @JsonProperty("plazo") Integer plazo,
        @JsonProperty("email") String email,
        @JsonProperty("id_tipo_prestamo") Long idTipoPrestamo,
        @JsonProperty("id_estado") Long idEstado,
        @JsonProperty("estado") String estado) {

    public static SolicitudResponse fromDomain(Solicitud solicitud) {
        return new SolicitudResponse(
                solicitud.getId(),
                solicitud.getDocumentoIdentidad(),
                solicitud.getMonto(),
                solicitud.getPlazo(),
                solicitud.getEmail(),
                solicitud.getIdTipoPrestamo(),
                solicitud.getIdEstado(),
                solicitud.getEstado());
    }
}

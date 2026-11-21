package crediya.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Formato único con el que la API responde cualquier error.
 */
public record RespuestaError(
        @JsonProperty("fecha") LocalDateTime fecha,
        @JsonProperty("estado") int estado,
        @JsonProperty("mensaje") String mensaje,
        @JsonProperty("errores") List<String> errores) {

    public static RespuestaError de(int estado, String mensaje, List<String> errores) {
        return new RespuestaError(LocalDateTime.now(), estado, mensaje, List.copyOf(errores));
    }
}

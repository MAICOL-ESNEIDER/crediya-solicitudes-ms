package crediya.model.solicitud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Modelo del dominio: una solicitud de préstamo de un cliente.
 * No sabe nada de bases de datos ni de la web; solo guarda los datos del negocio.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Solicitud {
    private Long id;
    private BigDecimal monto;
    private Integer plazo;
    private String email;
    private String documentoIdentidad;
    private Long idEstado;
    private Long idTipoPrestamo;
    /** Nombre del estado (por ejemplo "Pendiente de revisión"). Se usa solo para responder; no es una columna de la tabla. */
    private String estado;
}

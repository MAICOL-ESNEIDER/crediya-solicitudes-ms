package crediya.api.exception;

import crediya.api.dto.RespuestaError;
import crediya.model.exceptions.TipoPrestamoNoEncontradoException;
import crediya.model.exceptions.ValidacionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manejador global de errores: atrapa las excepciones de toda la API y responde
 * siempre un JSON claro, para que al usuario nunca le lleguen mensajes inesperados.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionHandler.class.getName());
    private static final String MENSAJE_DATOS_INVALIDOS = "Los datos enviados no son válidos";

    @ExceptionHandler(ValidacionException.class)
    public ResponseEntity<RespuestaError> manejarValidacion(ValidacionException e) {
        LOG.warning(() -> "Error de validación: " + e.getErrores());
        return construir(HttpStatus.BAD_REQUEST, MENSAJE_DATOS_INVALIDOS, e.getErrores());
    }

    @ExceptionHandler(TipoPrestamoNoEncontradoException.class)
    public ResponseEntity<RespuestaError> manejarTipoPrestamoNoEncontrado(TipoPrestamoNoEncontradoException e) {
        LOG.warning(() -> "Tipo de préstamo no encontrado: " + e.getMessage());
        return construir(HttpStatus.NOT_FOUND, e.getMessage(), List.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespuestaError> manejarInesperado(Exception e) {
        LOG.log(Level.SEVERE, "Error inesperado", e);
        return construir(HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrió un error inesperado. Intente de nuevo más tarde", List.of());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        LOG.warning("El cuerpo de la petición no se pudo leer (JSON mal formado o tipo de dato incorrecto)");
        RespuestaError cuerpo = RespuestaError.de(HttpStatus.BAD_REQUEST.value(), MENSAJE_DATOS_INVALIDOS,
                List.of("El JSON no es válido o algún dato tiene un formato incorrecto. "
                        + "Revise que monto, plazo e id_tipo_prestamo sean numéricos"));
        return ResponseEntity.badRequest().body(cuerpo);
    }

    private ResponseEntity<RespuestaError> construir(HttpStatus estado, String mensaje, List<String> errores) {
        return ResponseEntity.status(estado).body(RespuestaError.de(estado.value(), mensaje, errores));
    }
}

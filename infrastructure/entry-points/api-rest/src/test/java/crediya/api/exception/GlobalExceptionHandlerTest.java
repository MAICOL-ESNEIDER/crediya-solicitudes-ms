package crediya.api.exception;

import crediya.api.dto.RespuestaError;
import crediya.model.exceptions.TipoPrestamoNoEncontradoException;
import crediya.model.exceptions.ValidacionException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void validacionRespondeBadRequestConLaListaDeErrores() {
        ResponseEntity<RespuestaError> respuesta =
                handler.manejarValidacion(new ValidacionException(List.of("El campo monto es obligatorio")));

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(List.of("El campo monto es obligatorio"), respuesta.getBody().errores());
    }

    @Test
    void tipoDePrestamoInexistenteRespondeNotFound() {
        ResponseEntity<RespuestaError> respuesta =
                handler.manejarTipoPrestamoNoEncontrado(new TipoPrestamoNoEncontradoException(99L));

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(404, respuesta.getBody().estado());
        assertEquals("El tipo de préstamo con id 99 no existe", respuesta.getBody().mensaje());
    }

    @Test
    void errorInesperadoRespondeInternalServerErrorSinDetallesInternos() {
        ResponseEntity<RespuestaError> respuesta = handler.manejarInesperado(new IllegalStateException("detalle interno"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals("Ocurrió un error inesperado. Intente de nuevo más tarde", respuesta.getBody().mensaje());
    }
}

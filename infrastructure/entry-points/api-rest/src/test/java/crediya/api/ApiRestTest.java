package crediya.api;

import crediya.api.dto.SolicitudRequest;
import crediya.api.dto.SolicitudResponse;
import crediya.model.solicitud.Solicitud;
import crediya.usecase.solicitud.SolicitudUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApiRestTest {

    private final SolicitudUseCase solicitudUseCase = mock(SolicitudUseCase.class);
    private final ApiRest apiRest = new ApiRest(solicitudUseCase);

    @Test
    void registrarSolicitudRespondeCreadoConEstadoPendiente() {
        SolicitudRequest request = new SolicitudRequest("1000123456", new BigDecimal("5000000"), 24, 1L,
                "maicol@correo.com");
        when(solicitudUseCase.registrarSolicitud(any(Solicitud.class)))
                .thenAnswer(invocacion -> ((Solicitud) invocacion.getArgument(0)).toBuilder()
                        .id(1L).idEstado(1L).estado("Pendiente de revisión").build());

        ResponseEntity<SolicitudResponse> respuesta = apiRest.registrarSolicitud(request);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertEquals(1L, respuesta.getBody().id());
        assertEquals("1000123456", respuesta.getBody().documentoIdentidad());
        assertEquals("Pendiente de revisión", respuesta.getBody().estado());
    }
}

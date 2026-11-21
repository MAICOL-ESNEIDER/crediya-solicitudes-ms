package crediya.usecase.solicitud;

import crediya.model.estado.gateways.EstadoRepository;
import crediya.model.exceptions.TipoPrestamoNoEncontradoException;
import crediya.model.exceptions.ValidacionException;
import crediya.model.solicitud.Solicitud;
import crediya.model.solicitud.gateways.SolicitudRepository;
import crediya.model.tipoprestamo.TipoPrestamo;
import crediya.model.tipoprestamo.gateways.TipoPrestamoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SolicitudUseCaseTest {

    private static final Long ID_ESTADO_PENDIENTE = 1L;

    private SolicitudRepository solicitudRepository;
    private TipoPrestamoRepository tipoPrestamoRepository;
    private EstadoRepository estadoRepository;
    private SolicitudUseCase useCase;

    @BeforeEach
    void setUp() {
        solicitudRepository = mock(SolicitudRepository.class);
        tipoPrestamoRepository = mock(TipoPrestamoRepository.class);
        estadoRepository = mock(EstadoRepository.class);
        useCase = new SolicitudUseCase(solicitudRepository, tipoPrestamoRepository, estadoRepository);

        TipoPrestamo libreInversion = TipoPrestamo.builder()
                .id(1L)
                .nombre("Libre inversión")
                .montoMinimo(new BigDecimal("1000000"))
                .montoMaximo(new BigDecimal("50000000"))
                .build();
        when(tipoPrestamoRepository.buscarPorId(1L)).thenReturn(Optional.of(libreInversion));
        when(tipoPrestamoRepository.buscarPorId(99L)).thenReturn(Optional.empty());
        when(estadoRepository.buscarIdPorNombre("Pendiente de revisión")).thenReturn(Optional.of(ID_ESTADO_PENDIENTE));
        when(solicitudRepository.save(any(Solicitud.class)))
                .thenAnswer(invocacion -> ((Solicitud) invocacion.getArgument(0)).toBuilder().id(7L).build());
    }

    private Solicitud solicitudValida() {
        return Solicitud.builder()
                .documentoIdentidad(" 1000123456 ")
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .idTipoPrestamo(1L)
                .email("Maicol@Correo.com")
                .build();
    }

    @Test
    void registraSolicitudValidaConEstadoPendienteDeRevision() {
        Solicitud resultado = useCase.registrarSolicitud(solicitudValida());

        assertEquals(7L, resultado.getId());
        assertEquals(ID_ESTADO_PENDIENTE, resultado.getIdEstado());
        assertEquals("Pendiente de revisión", resultado.getEstado());
        assertEquals("1000123456", resultado.getDocumentoIdentidad());
        assertEquals("maicol@correo.com", resultado.getEmail());
    }

    @Test
    void permiteRegistrarSinEmail() {
        Solicitud sinEmail = solicitudValida().toBuilder().email(null).build();

        Solicitud resultado = useCase.registrarSolicitud(sinEmail);

        assertEquals(7L, resultado.getId());
    }

    @Test
    void rechazaSolicitudNula() {
        assertThrows(ValidacionException.class, () -> useCase.registrarSolicitud(null));
    }

    @Test
    void rechazaSinDocumentoDeIdentidad() {
        Solicitud sinDocumento = solicitudValida().toBuilder().documentoIdentidad(" ").build();

        ValidacionException error = assertThrows(ValidacionException.class,
                () -> useCase.registrarSolicitud(sinDocumento));

        assertTrue(error.getErrores().contains("El campo documento_identidad es obligatorio"));
        verify(solicitudRepository, never()).save(any(Solicitud.class));
    }

    @Test
    void rechazaDocumentoConLetras() {
        Solicitud documentoMalo = solicitudValida().toBuilder().documentoIdentidad("ABC123").build();

        assertThrows(ValidacionException.class, () -> useCase.registrarSolicitud(documentoMalo));
    }

    @Test
    void rechazaMontoYPlazoEnCeroONegativo() {
        Solicitud invalida = solicitudValida().toBuilder().monto(BigDecimal.ZERO).plazo(-3).build();

        ValidacionException error = assertThrows(ValidacionException.class,
                () -> useCase.registrarSolicitud(invalida));

        assertEquals(2, error.getErrores().size());
    }

    @Test
    void rechazaCuandoFaltanMontoPlazoYTipoDePrestamo() {
        Solicitud vacia = Solicitud.builder().documentoIdentidad("1000123456").build();

        ValidacionException error = assertThrows(ValidacionException.class,
                () -> useCase.registrarSolicitud(vacia));

        assertEquals(3, error.getErrores().size());
    }

    @Test
    void rechazaEmailConFormatoInvalido() {
        Solicitud emailMalo = solicitudValida().toBuilder().email("no-es-un-correo").build();

        assertThrows(ValidacionException.class, () -> useCase.registrarSolicitud(emailMalo));
    }

    @Test
    void rechazaTipoDePrestamoQueNoExiste() {
        Solicitud tipoInexistente = solicitudValida().toBuilder().idTipoPrestamo(99L).build();

        assertThrows(TipoPrestamoNoEncontradoException.class, () -> useCase.registrarSolicitud(tipoInexistente));
        verify(solicitudRepository, never()).save(any(Solicitud.class));
    }

    @Test
    void rechazaMontoFueraDelRangoDelTipoDePrestamo() {
        Solicitud montoAlto = solicitudValida().toBuilder().monto(new BigDecimal("90000000")).build();

        ValidacionException error = assertThrows(ValidacionException.class,
                () -> useCase.registrarSolicitud(montoAlto));

        assertTrue(error.getErrores().get(0).contains("Libre inversión"));
        verify(solicitudRepository, never()).save(any(Solicitud.class));
    }

    @Test
    void falla_siNoExisteElEstadoInicial() {
        when(estadoRepository.buscarIdPorNombre("Pendiente de revisión")).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> useCase.registrarSolicitud(solicitudValida()));
    }
}

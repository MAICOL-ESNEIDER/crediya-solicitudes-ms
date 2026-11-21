package crediya.usecase.solicitud;

import crediya.model.estado.gateways.EstadoRepository;
import crediya.model.exceptions.TipoPrestamoNoEncontradoException;
import crediya.model.exceptions.ValidacionException;
import crediya.model.solicitud.Solicitud;
import crediya.model.solicitud.gateways.SolicitudRepository;
import crediya.model.tipoprestamo.TipoPrestamo;
import crediya.model.tipoprestamo.gateways.TipoPrestamoRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Caso de uso de la HU2: registrar una solicitud de préstamo.
 * Aquí viven las reglas del negocio (criterios de aceptación).
 */
@RequiredArgsConstructor
public class SolicitudUseCase {

    private static final Logger LOG = Logger.getLogger(SolicitudUseCase.class.getName());
    private static final Pattern FORMATO_DOCUMENTO = Pattern.compile("^[0-9]{5,20}$");
    private static final Pattern FORMATO_CORREO =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final String ESTADO_INICIAL = "Pendiente de revisión";

    private final SolicitudRepository solicitudRepository;
    private final TipoPrestamoRepository tipoPrestamoRepository;
    private final EstadoRepository estadoRepository;

    public Solicitud registrarSolicitud(Solicitud solicitud) {
        LOG.info("Iniciando el registro de una nueva solicitud de préstamo");

        validarDatos(solicitud);

        TipoPrestamo tipoPrestamo = tipoPrestamoRepository.buscarPorId(solicitud.getIdTipoPrestamo())
                .orElseThrow(() -> {
                    LOG.warning(() -> "Solicitud rechazada: el tipo de préstamo "
                            + solicitud.getIdTipoPrestamo() + " no existe");
                    return new TipoPrestamoNoEncontradoException(solicitud.getIdTipoPrestamo());
                });

        validarMontoSegunTipo(solicitud.getMonto(), tipoPrestamo);

        Long idEstadoInicial = estadoRepository.buscarIdPorNombre(ESTADO_INICIAL)
                .orElseThrow(() -> new IllegalStateException("No existe el estado " + ESTADO_INICIAL));

        Solicitud nueva = solicitud.toBuilder()
                .id(null)
                .documentoIdentidad(solicitud.getDocumentoIdentidad().trim())
                .email(normalizarCorreo(solicitud.getEmail()))
                .idEstado(idEstadoInicial)
                .build();

        Solicitud guardada = solicitudRepository.save(nueva);
        LOG.info(() -> "Solicitud registrada correctamente con id " + guardada.getId());
        return guardada.toBuilder().estado(ESTADO_INICIAL).build();
    }

    private void validarDatos(Solicitud solicitud) {
        if (solicitud == null) {
            throw new ValidacionException(List.of("No se enviaron datos de la solicitud"));
        }

        List<String> errores = new ArrayList<>();

        if (estaVacio(solicitud.getDocumentoIdentidad())) {
            errores.add("El campo documento_identidad es obligatorio");
        } else if (!FORMATO_DOCUMENTO.matcher(solicitud.getDocumentoIdentidad().trim()).matches()) {
            errores.add("El documento_identidad debe tener solo números (entre 5 y 20 dígitos)");
        }

        if (solicitud.getMonto() == null) {
            errores.add("El campo monto es obligatorio");
        } else if (solicitud.getMonto().compareTo(BigDecimal.ZERO) <= 0) {
            errores.add("El monto debe ser mayor que 0");
        }

        if (solicitud.getPlazo() == null) {
            errores.add("El campo plazo es obligatorio");
        } else if (solicitud.getPlazo() <= 0) {
            errores.add("El plazo debe ser mayor que 0 (en meses)");
        }

        if (solicitud.getIdTipoPrestamo() == null) {
            errores.add("El campo id_tipo_prestamo es obligatorio");
        }

        if (!estaVacio(solicitud.getEmail())
                && !FORMATO_CORREO.matcher(solicitud.getEmail().trim()).matches()) {
            errores.add("El email no tiene un formato válido");
        }

        if (!errores.isEmpty()) {
            LOG.warning(() -> "Datos inválidos en la solicitud: " + errores);
            throw new ValidacionException(errores);
        }
    }

    private void validarMontoSegunTipo(BigDecimal monto, TipoPrestamo tipoPrestamo) {
        if (monto.compareTo(tipoPrestamo.getMontoMinimo()) < 0
                || monto.compareTo(tipoPrestamo.getMontoMaximo()) > 0) {
            String mensaje = "El monto debe estar entre " + tipoPrestamo.getMontoMinimo().toPlainString()
                    + " y " + tipoPrestamo.getMontoMaximo().toPlainString()
                    + " para el tipo de préstamo " + tipoPrestamo.getNombre();
            LOG.warning(() -> "Solicitud rechazada: " + mensaje);
            throw new ValidacionException(List.of(mensaje));
        }
    }

    private String normalizarCorreo(String email) {
        return estaVacio(email) ? null : email.trim().toLowerCase(Locale.ROOT);
    }

    private boolean estaVacio(String valor) {
        return valor == null || valor.isBlank();
    }
}

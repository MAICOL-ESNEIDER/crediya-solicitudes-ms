package crediya.model.exceptions;

/**
 * Se lanza cuando el tipo de préstamo enviado no existe.
 */
public class TipoPrestamoNoEncontradoException extends RuntimeException {

    public TipoPrestamoNoEncontradoException(Long idTipoPrestamo) {
        super("El tipo de préstamo con id " + idTipoPrestamo + " no existe");
    }
}

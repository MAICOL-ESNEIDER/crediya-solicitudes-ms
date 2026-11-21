package crediya.model.exceptions;

import java.util.List;

/**
 * Se lanza cuando los datos enviados no cumplen las reglas del negocio.
 * Guarda la lista de todos los errores encontrados.
 */
public class ValidacionException extends RuntimeException {

    private final List<String> errores;

    public ValidacionException(List<String> errores) {
        super("Los datos enviados no son válidos");
        this.errores = List.copyOf(errores);
    }

    public List<String> getErrores() {
        return errores;
    }
}

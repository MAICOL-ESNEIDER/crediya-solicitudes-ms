package crediya.model.estado.gateways;

import java.util.Optional;

/**
 * Puerto (gateway) del dominio para consultar los estados de una solicitud.
 */
public interface EstadoRepository {

    Optional<Long> buscarIdPorNombre(String nombre);
}

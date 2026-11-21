package crediya.model.tipoprestamo.gateways;

import crediya.model.tipoprestamo.TipoPrestamo;

import java.util.Optional;

/**
 * Puerto (gateway) del dominio para consultar los tipos de préstamo.
 */
public interface TipoPrestamoRepository {

    Optional<TipoPrestamo> buscarPorId(Long id);
}

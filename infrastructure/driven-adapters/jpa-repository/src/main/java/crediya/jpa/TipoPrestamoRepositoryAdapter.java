package crediya.jpa;

import crediya.jpa.entity.TipoPrestamoEntity;
import crediya.model.tipoprestamo.TipoPrestamo;
import crediya.model.tipoprestamo.gateways.TipoPrestamoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Adaptador que implementa el puerto TipoPrestamoRepository consultando la tabla "tipo_prestamo".
 */
@Repository
@RequiredArgsConstructor
public class TipoPrestamoRepositoryAdapter implements TipoPrestamoRepository {

    private final TipoPrestamoJPARepository tipoPrestamoJPARepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<TipoPrestamo> buscarPorId(Long id) {
        return tipoPrestamoJPARepository.findById(id).map(this::aDominio);
    }

    private TipoPrestamo aDominio(TipoPrestamoEntity entity) {
        return TipoPrestamo.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .montoMinimo(entity.getMontoMinimo())
                .montoMaximo(entity.getMontoMaximo())
                .tasaInteres(entity.getTasaInteres())
                .validacionAutomatica(entity.getValidacionAutomatica())
                .build();
    }
}

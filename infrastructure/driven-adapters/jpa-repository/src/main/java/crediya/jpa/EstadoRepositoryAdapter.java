package crediya.jpa;

import crediya.jpa.entity.EstadoEntity;
import crediya.model.estado.gateways.EstadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Adaptador que implementa el puerto EstadoRepository consultando la tabla "estados".
 */
@Repository
@RequiredArgsConstructor
public class EstadoRepositoryAdapter implements EstadoRepository {

    private final EstadoJPARepository estadoJPARepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Long> buscarIdPorNombre(String nombre) {
        return estadoJPARepository.findByNombre(nombre).map(EstadoEntity::getId);
    }
}

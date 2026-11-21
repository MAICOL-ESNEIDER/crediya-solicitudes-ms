package crediya.jpa;

import crediya.jpa.entity.EstadoEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Repositorio de Spring Data para la tabla "estados".
 */
public interface EstadoJPARepository extends CrudRepository<EstadoEntity, Long> {

    Optional<EstadoEntity> findByNombre(String nombre);
}

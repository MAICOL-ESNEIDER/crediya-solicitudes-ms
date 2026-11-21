package crediya.jpa;

import crediya.jpa.entity.TipoPrestamoEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Repositorio de Spring Data para la tabla "tipo_prestamo".
 */
public interface TipoPrestamoJPARepository extends CrudRepository<TipoPrestamoEntity, Long> {
}

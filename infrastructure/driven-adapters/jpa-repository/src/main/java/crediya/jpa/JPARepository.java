package crediya.jpa;

import crediya.jpa.entity.SolicitudEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

/**
 * Repositorio de Spring Data para la tabla "solicitud".
 * Spring escribe el SQL solo (save, findById, etc.).
 */
public interface JPARepository extends CrudRepository<SolicitudEntity, Long>, QueryByExampleExecutor<SolicitudEntity> {
}

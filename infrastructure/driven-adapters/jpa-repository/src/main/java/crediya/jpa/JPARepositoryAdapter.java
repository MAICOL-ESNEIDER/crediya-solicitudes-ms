package crediya.jpa;

import crediya.jpa.entity.SolicitudEntity;
import crediya.jpa.helper.AdapterOperations;
import crediya.model.solicitud.Solicitud;
import crediya.model.solicitud.gateways.SolicitudRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

/**
 * Adaptador (driven adapter): implementa el puerto SolicitudRepository usando JPA y PostgreSQL.
 * Traduce entre el modelo del dominio (Solicitud) y la entidad de la base de datos (SolicitudEntity).
 */
@Repository
public class JPARepositoryAdapter extends AdapterOperations<Solicitud, SolicitudEntity, Long, JPARepository>
        implements SolicitudRepository {

    private static final Logger LOG = Logger.getLogger(JPARepositoryAdapter.class.getName());

    public JPARepositoryAdapter(JPARepository repository, ObjectMapper mapper) {
        super(repository, mapper, JPARepositoryAdapter::aDominio);
    }

    @Override
    @Transactional
    public Solicitud save(Solicitud solicitud) {
        Solicitud guardada = super.save(solicitud);
        LOG.info(() -> "Solicitud guardada en la base de datos con id " + guardada.getId());
        return guardada;
    }

    @Override
    protected SolicitudEntity toData(Solicitud solicitud) {
        return SolicitudEntity.builder()
                .id(solicitud.getId())
                .monto(solicitud.getMonto())
                .plazo(solicitud.getPlazo())
                .email(solicitud.getEmail())
                .documentoIdentidad(solicitud.getDocumentoIdentidad())
                .idEstado(solicitud.getIdEstado())
                .idTipoPrestamo(solicitud.getIdTipoPrestamo())
                .build();
    }

    private static Solicitud aDominio(SolicitudEntity entity) {
        return Solicitud.builder()
                .id(entity.getId())
                .monto(entity.getMonto())
                .plazo(entity.getPlazo())
                .email(entity.getEmail())
                .documentoIdentidad(entity.getDocumentoIdentidad())
                .idEstado(entity.getIdEstado())
                .idTipoPrestamo(entity.getIdTipoPrestamo())
                .build();
    }
}

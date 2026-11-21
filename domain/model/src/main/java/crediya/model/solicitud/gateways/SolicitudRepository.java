package crediya.model.solicitud.gateways;

import crediya.model.solicitud.Solicitud;

/**
 * Puerto (gateway) del dominio: dice QUÉ se puede hacer con las solicitudes guardadas,
 * pero no CÓMO. El "cómo" lo implementa el adaptador de base de datos (infraestructura).
 */
public interface SolicitudRepository {

    Solicitud save(Solicitud solicitud);
}

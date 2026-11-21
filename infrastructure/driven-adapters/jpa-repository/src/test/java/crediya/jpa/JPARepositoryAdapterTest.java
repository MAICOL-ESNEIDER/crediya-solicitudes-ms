package crediya.jpa;

import crediya.jpa.entity.SolicitudEntity;
import crediya.model.solicitud.Solicitud;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivecommons.utils.ObjectMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JPARepositoryAdapterTest {

    private JPARepository repository;
    private JPARepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        repository = mock(JPARepository.class);
        adapter = new JPARepositoryAdapter(repository, mock(ObjectMapper.class));
    }

    @Test
    void guardaLaSolicitudYDevuelveElIdGenerado() {
        when(repository.save(any(SolicitudEntity.class))).thenAnswer(invocacion -> {
            SolicitudEntity entidad = invocacion.getArgument(0);
            entidad.setId(10L);
            return entidad;
        });
        Solicitud solicitud = Solicitud.builder()
                .documentoIdentidad("1000123456")
                .monto(new BigDecimal("5000000"))
                .plazo(24)
                .idEstado(1L)
                .idTipoPrestamo(2L)
                .build();

        Solicitud guardada = adapter.save(solicitud);

        assertEquals(10L, guardada.getId());
        assertEquals("1000123456", guardada.getDocumentoIdentidad());
        assertEquals(1L, guardada.getIdEstado());
        assertEquals(2L, guardada.getIdTipoPrestamo());
    }

    @Test
    void buscaPorIdYConvierteAlModeloDelDominio() {
        SolicitudEntity entidad = SolicitudEntity.builder()
                .id(5L)
                .documentoIdentidad("1000123456")
                .monto(new BigDecimal("3000000"))
                .plazo(12)
                .idEstado(1L)
                .idTipoPrestamo(1L)
                .build();
        when(repository.findById(5L)).thenReturn(Optional.of(entidad));

        Solicitud solicitud = adapter.findById(5L);

        assertEquals(5L, solicitud.getId());
        assertEquals(12, solicitud.getPlazo());
    }

    @Test
    void buscaPorIdInexistenteDevuelveNulo() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertNull(adapter.findById(99L));
    }
}

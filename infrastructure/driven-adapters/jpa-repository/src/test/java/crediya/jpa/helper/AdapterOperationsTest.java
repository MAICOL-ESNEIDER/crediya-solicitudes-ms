package crediya.jpa.helper;

import crediya.jpa.JPARepository;
import crediya.jpa.JPARepositoryAdapter;
import crediya.jpa.entity.SolicitudEntity;
import crediya.model.solicitud.Solicitud;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivecommons.utils.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AdapterOperationsTest {

    private JPARepository repository;
    private JPARepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        repository = mock(JPARepository.class);
        adapter = new JPARepositoryAdapter(repository, mock(ObjectMapper.class));
    }

    @Test
    void findAllConvierteTodasLasEntidadesAlDominio() {
        SolicitudEntity primera = SolicitudEntity.builder().id(1L).plazo(6).build();
        SolicitudEntity segunda = SolicitudEntity.builder().id(2L).plazo(12).build();
        when(repository.findAll()).thenReturn(List.of(primera, segunda));

        List<Solicitud> solicitudes = adapter.findAll();

        assertEquals(2, solicitudes.size());
        assertEquals(12, solicitudes.get(1).getPlazo());
    }
}

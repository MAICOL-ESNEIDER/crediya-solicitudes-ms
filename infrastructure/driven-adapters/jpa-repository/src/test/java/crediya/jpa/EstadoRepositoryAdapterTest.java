package crediya.jpa;

import crediya.jpa.entity.EstadoEntity;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EstadoRepositoryAdapterTest {

    private final EstadoJPARepository jpaRepository = mock(EstadoJPARepository.class);
    private final EstadoRepositoryAdapter adapter = new EstadoRepositoryAdapter(jpaRepository);

    @Test
    void devuelveElIdDelEstadoCuandoExiste() {
        when(jpaRepository.findByNombre("Pendiente de revisión"))
                .thenReturn(Optional.of(new EstadoEntity(1L, "Pendiente de revisión", "Solicitud recién creada")));

        assertEquals(Optional.of(1L), adapter.buscarIdPorNombre("Pendiente de revisión"));
    }

    @Test
    void devuelveVacioCuandoElEstadoNoExiste() {
        when(jpaRepository.findByNombre("Otro")).thenReturn(Optional.empty());

        assertTrue(adapter.buscarIdPorNombre("Otro").isEmpty());
    }
}

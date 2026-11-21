package crediya.jpa;

import crediya.jpa.entity.TipoPrestamoEntity;
import crediya.model.tipoprestamo.TipoPrestamo;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TipoPrestamoRepositoryAdapterTest {

    private final TipoPrestamoJPARepository jpaRepository = mock(TipoPrestamoJPARepository.class);
    private final TipoPrestamoRepositoryAdapter adapter = new TipoPrestamoRepositoryAdapter(jpaRepository);

    @Test
    void devuelveElTipoDePrestamoCuandoExiste() {
        when(jpaRepository.findById(1L)).thenReturn(Optional.of(new TipoPrestamoEntity(
                1L, "Libre inversión", new BigDecimal("1000000"), new BigDecimal("50000000"),
                new BigDecimal("1.80"), false)));

        Optional<TipoPrestamo> resultado = adapter.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Libre inversión", resultado.get().getNombre());
        assertEquals(new BigDecimal("50000000"), resultado.get().getMontoMaximo());
    }

    @Test
    void devuelveVacioCuandoNoExiste() {
        when(jpaRepository.findById(99L)).thenReturn(Optional.empty());

        assertTrue(adapter.buscarPorId(99L).isEmpty());
    }
}

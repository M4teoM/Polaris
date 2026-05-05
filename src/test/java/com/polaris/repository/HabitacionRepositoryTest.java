package com.polaris.repository;

import com.polaris.model.Habitacion;
import com.polaris.model.TipoHabitacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Pruebas del Repositorio de Habitacion")
class HabitacionRepositoryTest {

    @Autowired
    private IHabitacionRepository habitacionRepository;

    @Autowired
    private ITipoHabitacionRepository tipoHabitacionRepository;

    private TipoHabitacion tipoHabitacionTest;

    @BeforeEach
    void setUp() {
        tipoHabitacionTest = TipoHabitacion.builder()
                .nombre("Suite Ejecutiva")
                .descripcion("Habitación amplia con amenidades premium")
                .precioPorNoche(250.00)
                .imagenUrl("https://ejemplo.com/suite.jpg")
                .metrosCuadrados(45)
                .capacidad(2)
                .tipoCama("King")
                .build();

        tipoHabitacionTest = tipoHabitacionRepository.save(tipoHabitacionTest);
    }

    @Test
    @DisplayName("Debe buscar habitaciones disponibles por tipo usando SQL nativo")
    void testBuscarHabitacionesDisponiblesPorTipo() {
        // Arrange
                // Dos habitaciones disponibles y una ocupada para comprobar el filtro nativo.
        Habitacion habitacionDisponible1 = Habitacion.builder()
                .numero("101")
                .piso(1)
                .estado("Disponible")
                .tipoHabitacion(tipoHabitacionTest)
                .build();

        Habitacion habitacionDisponible2 = Habitacion.builder()
                .numero("102")
                .piso(1)
                .estado("Disponible")
                .tipoHabitacion(tipoHabitacionTest)
                .build();

        Habitacion habitacionOcupada = Habitacion.builder()
                .numero("103")
                .piso(1)
                .estado("Ocupada")
                .tipoHabitacion(tipoHabitacionTest)
                .build();

        habitacionRepository.saveAll(List.of(habitacionDisponible1, habitacionDisponible2, habitacionOcupada));

        // Act
        List<Habitacion> habitacionesEncontradas = habitacionRepository.findDisponiblesByTipoId(tipoHabitacionTest.getId());

        // Assert
        assertThat(habitacionesEncontradas).hasSize(2);
        assertThat(habitacionesEncontradas).allMatch(habitacion -> habitacion.getEstado().equals("Disponible"));
        assertThat(habitacionesEncontradas).allMatch(habitacion -> habitacion.getTipoHabitacion().getId().equals(tipoHabitacionTest.getId()));
    }

    @Test
    @DisplayName("Debe contar las habitaciones de un piso usando SQL nativo")
    void testContarHabitacionesPorPiso() {
        // Arrange
                // Se insertan tres habitaciones para verificar el conteo exacto del piso 2.
        Habitacion habitacion1 = Habitacion.builder()
                .numero("201")
                .piso(2)
                .estado("Disponible")
                .tipoHabitacion(tipoHabitacionTest)
                .build();

        Habitacion habitacion2 = Habitacion.builder()
                .numero("202")
                .piso(2)
                .estado("Disponible")
                .tipoHabitacion(tipoHabitacionTest)
                .build();

        Habitacion habitacion3 = Habitacion.builder()
                .numero("301")
                .piso(3)
                .estado("Disponible")
                .tipoHabitacion(tipoHabitacionTest)
                .build();

        habitacionRepository.saveAll(List.of(habitacion1, habitacion2, habitacion3));

        // Act
        long totalHabitacionesPiso2 = habitacionRepository.countByPiso(2);

        // Assert
        assertThat(totalHabitacionesPiso2).isEqualTo(2);
    }
}
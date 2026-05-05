package com.polaris.repository;

import com.polaris.model.Cliente;
import com.polaris.model.Habitacion;
import com.polaris.model.ReservaHabitacion;
import com.polaris.model.TipoHabitacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Pruebas del Repositorio de ReservaHabitacion")
class ReservaHabitacionRepositoryTest {

    @Autowired
    private IReservaHabitacionRepository reservaHabitacionRepository;

    @Autowired
    private IHabitacionRepository habitacionRepository;

    @Autowired
    private ITipoHabitacionRepository tipoHabitacionRepository;

    @Autowired
    private IClienteRepository clienteRepository;

    private Cliente clienteBusqueda;
    private Habitacion habitacion1;
    private Habitacion habitacion2;

    @BeforeEach
    void setUp() {
                // Se preparan cliente, tipo de habitacion y reservas para probar el JOIN por nombre.
        TipoHabitacion tipoHabitacion = tipoHabitacionRepository.save(TipoHabitacion.builder()
                .nombre("Doble Estándar")
                .descripcion("Habitación doble con servicios esenciales")
                .precioPorNoche(120.00)
                .imagenUrl("https://ejemplo.com/doble.jpg")
                .metrosCuadrados(30)
                .capacidad(2)
                .tipoCama("Queen")
                .build());

        clienteBusqueda = clienteRepository.save(Cliente.builder()
                .nombre("Juan")
                .apellido("Pérez")
                .correo("juan.perez@ejemplo.com")
                .contrasena("secreta")
                .cedula("1002003001")
                .telefono("0999999999")
                .build());

        Cliente clienteNoCoincide = clienteRepository.save(Cliente.builder()
                .nombre("María")
                .apellido("Gómez")
                .correo("maria.gomez@ejemplo.com")
                .contrasena("secreta")
                .cedula("1002003002")
                .telefono("0888888888")
                .build());

        habitacion1 = habitacionRepository.save(Habitacion.builder()
                .numero("401")
                .piso(4)
                .estado("Disponible")
                .tipoHabitacion(tipoHabitacion)
                .build());

        habitacion2 = habitacionRepository.save(Habitacion.builder()
                .numero("402")
                .piso(4)
                .estado("Disponible")
                .tipoHabitacion(tipoHabitacion)
                .build());

        ReservaHabitacion reserva1 = ReservaHabitacion.builder()
                .fechaCheckIn(LocalDate.of(2026, 5, 10))
                .fechaCheckOut(LocalDate.of(2026, 5, 12))
                .estado("Activa")
                .numeroHuespedes(2)
                .cliente(clienteBusqueda)
                .habitacion(habitacion1)
                .build();

        ReservaHabitacion reserva2 = ReservaHabitacion.builder()
                .fechaCheckIn(LocalDate.of(2026, 6, 1))
                .fechaCheckOut(LocalDate.of(2026, 6, 3))
                .estado("Confirmada")
                .numeroHuespedes(2)
                .cliente(clienteBusqueda)
                .habitacion(habitacion2)
                .build();

        ReservaHabitacion reserva3 = ReservaHabitacion.builder()
                .fechaCheckIn(LocalDate.of(2026, 7, 1))
                .fechaCheckOut(LocalDate.of(2026, 7, 4))
                .estado("Activa")
                .numeroHuespedes(1)
                .cliente(clienteNoCoincide)
                .habitacion(habitacion1)
                .build();

        reservaHabitacionRepository.saveAll(List.of(reserva1, reserva2, reserva3));
    }

    @Test
    @DisplayName("Debe buscar reservas por nombre de cliente usando JOIN JPQL")
    void testBuscarReservasPorNombreDeCliente() {
        // Arrange
                // El filtro debe devolver las reservas del cliente Juan y excluir el resto.
        String criterioBusqueda = "Juan";

        // Act
        List<ReservaHabitacion> reservasEncontradas = reservaHabitacionRepository.findByNombreCliente(criterioBusqueda);

        // Assert
        assertThat(reservasEncontradas).hasSize(2);
        assertThat(reservasEncontradas).allMatch(reserva -> reserva.getCliente().getNombre().contains("Juan"));
        assertThat(reservasEncontradas).allMatch(reserva -> reserva.getHabitacion().getNumero().startsWith("4"));
    }
}
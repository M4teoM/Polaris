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
import java.util.Optional;
import org.springframework.dao.DataIntegrityViolationException;

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

    @Test
    @DisplayName("CRUD positivo: guardar, buscar por ID y eliminar ReservaHabitacion")
    void testCrudPositivoGuardarBuscarEliminar() {
        // Arrange
        ReservaHabitacion nuevaReserva = ReservaHabitacion.builder()
                .fechaCheckIn(LocalDate.of(2026, 8, 1))
                .fechaCheckOut(LocalDate.of(2026, 8, 5))
                .estado("Pendiente")
                .numeroHuespedes(2)
                .cliente(clienteBusqueda)
                .habitacion(habitacion1)
                .build();

        // Act - Guardar
        ReservaHabitacion guardada = reservaHabitacionRepository.save(nuevaReserva);

        // Assert - Guardada y buscada por ID
        assertThat(guardada.getId()).isNotNull();
        Optional<ReservaHabitacion> buscada = reservaHabitacionRepository.findById(guardada.getId());
        assertThat(buscada).isPresent();
        assertThat(buscada.get().getCliente().getId()).isEqualTo(clienteBusqueda.getId());

        // Act - Eliminar
        reservaHabitacionRepository.deleteById(guardada.getId());

        // Assert - ya no existe
        Optional<ReservaHabitacion> despuesEliminacion = reservaHabitacionRepository.findById(guardada.getId());
        assertThat(despuesEliminacion).isEmpty();
    }

    @Test
    @DisplayName("Negativo: buscar ID inexistente devuelve Optional vacío")
    void testBuscarIdInexistenteDevuelveOptionalVacio() {
        // Act
        Optional<ReservaHabitacion> resultado = reservaHabitacionRepository.findById(999999L);

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("Negativo: guardar reserva sin cliente o sin habitación lanza DataIntegrityViolationException")
    void testGuardarSinCampoObligatorioLanzaExcepcion() {
        // Reserva sin cliente
        ReservaHabitacion sinCliente = ReservaHabitacion.builder()
                .fechaCheckIn(LocalDate.of(2026, 9, 1))
                .fechaCheckOut(LocalDate.of(2026, 9, 3))
                .estado("Pendiente")
                .numeroHuespedes(1)
                .habitacion(habitacion1)
                .cliente(null)
                .build();

        // Reserva sin habitación
        ReservaHabitacion sinHabitacion = ReservaHabitacion.builder()
                .fechaCheckIn(LocalDate.of(2026, 9, 10))
                .fechaCheckOut(LocalDate.of(2026, 9, 12))
                .estado("Pendiente")
                .numeroHuespedes(1)
                .cliente(clienteBusqueda)
                .habitacion(null)
                .build();

        // Assert - al intentar persistir debe lanzarse excepción de integridad
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> reservaHabitacionRepository.saveAndFlush(sinCliente))
                .isInstanceOf(DataIntegrityViolationException.class);

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> reservaHabitacionRepository.saveAndFlush(sinHabitacion))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
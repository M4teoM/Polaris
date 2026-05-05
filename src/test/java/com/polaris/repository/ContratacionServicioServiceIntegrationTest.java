package com.polaris.repository;

import com.polaris.service.ContratacionServicioService;
import com.polaris.model.Cliente;
import com.polaris.model.Habitacion;
import com.polaris.model.ReservaHabitacion;
import com.polaris.model.Servicio;
import com.polaris.model.TipoHabitacion;
import com.polaris.repository.IClienteRepository;
import com.polaris.repository.ICuentaRepository;
import com.polaris.repository.IHabitacionRepository;
import com.polaris.repository.IItemCuentaRepository;
import com.polaris.repository.IReservaHabitacionRepository;
import com.polaris.repository.IServicioRepository;
import com.polaris.repository.ITipoHabitacionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

// Pruebas de integración de ContratacionServicioService sin mocks.
// @DataJpaTest levanta solo la capa de persistencia con H2 en memoria.
// @Import inyecta el servicio real para probar el flujo completo con BD.
@DataJpaTest
@Import(ContratacionServicioService.class)
@DisplayName("Integración – ContratacionServicioService")
class ContratacionServicioServiceIntegrationTest {

    @Autowired private IClienteRepository          clienteRepo;
    @Autowired private ITipoHabitacionRepository   tipoHabRepo;
    @Autowired private IHabitacionRepository        habitacionRepo;
    @Autowired private IReservaHabitacionRepository reservaRepo;
    @Autowired private IServicioRepository          servicioRepo;
    @Autowired private ICuentaRepository            cuentaRepo;
    @Autowired private IItemCuentaRepository        itemRepo;
    @Autowired private ContratacionServicioService  contratacionService;

    private String numeroHabitacion;
    private Long   servicioId;

    // Crea el escenario base antes de cada prueba:
    // un cliente con una reserva activa en la habitación 305 y un servicio disponible.
    // @DataJpaTest hace rollback automático al terminar cada test, así cada uno parte limpio.
    @BeforeEach
    void prepararEscenario() {
        Cliente cliente = clienteRepo.save(Cliente.builder()
                .nombre("Ana").apellido("López")
                .correo("ana.lopez@polaris.com").contrasena("clave123")
                .cedula("9001002001").telefono("3001234567")
                .build());

        TipoHabitacion tipo = tipoHabRepo.save(TipoHabitacion.builder()
                .nombre("Doble Estándar").descripcion("Habitación doble con vista al jardín")
                .precioPorNoche(180.00).metrosCuadrados(28).capacidad(2).tipoCama("Queen")
                .build());

        numeroHabitacion = "305";
        Habitacion habitacion = habitacionRepo.save(Habitacion.builder()
                .numero(numeroHabitacion).piso(3).estado("Disponible").tipoHabitacion(tipo)
                .build());

        reservaRepo.save(ReservaHabitacion.builder()
                .fechaCheckIn(LocalDate.now().minusDays(1))
                .fechaCheckOut(LocalDate.now().plusDays(3))
                .estado("Activa").numeroHuespedes(2)
                .cliente(cliente).habitacion(habitacion)
                .build());

        servicioId = servicioRepo.save(Servicio.builder()
                .nombre("Desayuno en la habitación")
                .descripcion("Servicio de desayuno continental a domicilio")
                .precio(45.00).categoria("Gastronomía")
                .build()).getId();
    }

    // Verifica que al buscar por número de habitación se retornen los datos
    // correctos del cliente y que aún no tenga cuenta ni ítems asociados.
    @Test
    @DisplayName("Retorna datos del cliente si hay reserva activa en la habitación")
    void obtenerInfo_debeRetornarDatosDelCliente() {
        ContratacionServicioService.ContratacionInfo info =
                contratacionService.obtenerInfoPorNumeroHabitacion(numeroHabitacion);

        assertThat(info.habitacionNumero()).isEqualTo(numeroHabitacion);
        assertThat(info.clienteNombre()).isEqualTo("Ana");
        assertThat(info.totalCuenta()).isEqualTo(0.0);
        assertThat(info.cuentaId()).isNull();
    }

    // Verifica que al contratar un servicio se cree la cuenta automáticamente
    // y se registre el ítem con el precio correcto en la BD.
    @Test
    @DisplayName("Crea la cuenta y agrega el servicio como ítem al contratar")
    void contratarServicio_debePersistirCuentaEItem() {
        ContratacionServicioService.ContratacionResultado resultado =
                contratacionService.contratarServicio(numeroHabitacion, servicioId);

        assertThat(resultado.servicioPrecio()).isEqualTo(45.00);
        assertThat(resultado.totalCuenta()).isEqualTo(45.00);
        assertThat(cuentaRepo.findById(resultado.cuentaId())).isPresent();
        assertThat(itemRepo.findByCuentaId(resultado.cuentaId())).hasSize(1);
    }

    // Verifica que al pagar la cuenta tanto ella como todos sus ítems
    // queden marcados como pagados en la BD.
    @Test
    @DisplayName("Marca la cuenta y todos sus ítems como pagados")
    void pagarCuenta_debeCambiarEstadoAPagado() {
        Long cuentaId = contratacionService.contratarServicio(numeroHabitacion, servicioId).cuentaId();
        contratacionService.pagarCuenta(cuentaId);

        assertThat(cuentaRepo.findById(cuentaId))
                .hasValueSatisfying(c -> assertThat(c.getPagada()).isTrue());
        assertThat(itemRepo.findByCuentaId(cuentaId))
                .allSatisfy(item -> assertThat(item.getPagado()).isTrue());
    }

    // Verifica que buscar una habitación sin reserva activa lanza excepción
    // con el mensaje adecuado para informar al operador.
    @Test
    @DisplayName("Lanza excepción si no hay reserva activa en esa habitación")
    void obtenerInfo_debeRechazarHabitacionSinReservaActiva() {
        assertThatThrownBy(() -> contratacionService.obtenerInfoPorNumeroHabitacion("999"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("reserva activa");
    }

    // Verifica que no se pueda pagar dos veces la misma cuenta,
    // protegiendo contra cobros duplicados.
    @Test
    @DisplayName("Lanza excepción al intentar pagar una cuenta ya pagada")
    void pagarCuenta_debeRechazarDoblePago() {
        Long cuentaId = contratacionService.contratarServicio(numeroHabitacion, servicioId).cuentaId();
        contratacionService.pagarCuenta(cuentaId);

        assertThatThrownBy(() -> contratacionService.pagarCuenta(cuentaId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ya está completamente pagada");
    }
}
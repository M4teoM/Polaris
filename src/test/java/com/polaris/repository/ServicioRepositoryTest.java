package com.polaris.repository;

import com.polaris.model.Servicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Pruebas del Repositorio de Servicio")
class ServicioRepositoryTest {

    @Autowired
    private IServicioRepository servicioRepository;

    private Servicio servicioTest;

    @BeforeEach
    void setUp() {
        // Inicializar un objeto de prueba usando el patrón Builder
        servicioTest = Servicio.builder()
                .nombre("Masaje Relajante")
                .descripcion("Masaje corporal de relajación total")
                .descripcionDetallada("Masaje profesional de 60 minutos con técnicas de relajación profunda")
                .precio(150.00)
                .imagenUrl("https://ejemplo.com/masaje.jpg")
                .categoria("Bienestar")
                .duracion("60 minutos")
                .horario("8:00 AM - 10:00 PM")
                .incluye("Toalla|Aceite aromático|Música relajante")
                .destacados("Relajación total|Alivia tensiones")
                .build();
    }

    @Test
    @DisplayName("Debe guardar un servicio exitosamente y generar un ID")
    void testCrearServicio() {
        // Arrange
        assertThat(servicioTest.getId()).isNull();

        // Act
        Servicio servicioGuardado = servicioRepository.save(servicioTest);

        // Assert
        assertThat(servicioGuardado).isNotNull();
        assertThat(servicioGuardado.getId()).isNotNull();
        assertThat(servicioGuardado.getId()).isGreaterThan(0);
        assertThat(servicioGuardado.getNombre()).isEqualTo("Masaje Relajante");
        assertThat(servicioGuardado.getPrecio()).isEqualTo(150.00);
    }

    @Test
    @DisplayName("Debe recuperar un servicio por ID y verificar que coincidan los datos")
    void testLeerServicio() {
        // Arrange
        Servicio servicioGuardado = servicioRepository.save(servicioTest);
        Long idGuardado = servicioGuardado.getId();

        // Act
        Optional<Servicio> servicioRecuperado = servicioRepository.findById(idGuardado);

        // Assert
        assertThat(servicioRecuperado).isPresent();
        assertThat(servicioRecuperado.get().getId()).isEqualTo(idGuardado);
        assertThat(servicioRecuperado.get().getNombre()).isEqualTo("Masaje Relajante");
        assertThat(servicioRecuperado.get().getDescripcion())
                .isEqualTo("Masaje corporal de relajación total");
        assertThat(servicioRecuperado.get().getPrecio()).isEqualTo(150.00);
        assertThat(servicioRecuperado.get().getCategoria()).isEqualTo("Bienestar");
    }

    @Test
    @DisplayName("Debe actualizar un servicio existente y verificar los cambios")
    void testActualizarServicio() {
        // Arrange
        Servicio servicioGuardado = servicioRepository.save(servicioTest);
        Long idGuardado = servicioGuardado.getId();

        // Act
        servicioGuardado.setNombre("Masaje Descontracturante");
        servicioGuardado.setPrecio(180.00);
        servicioGuardado.setDescripcion("Masaje terapéutico especializado");
        Servicio servicioActualizado = servicioRepository.save(servicioGuardado);

        // Assert
        assertThat(servicioActualizado.getId()).isEqualTo(idGuardado);
        assertThat(servicioActualizado.getNombre()).isEqualTo("Masaje Descontracturante");
        assertThat(servicioActualizado.getPrecio()).isEqualTo(180.00);
        assertThat(servicioActualizado.getDescripcion())
                .isEqualTo("Masaje terapéutico especializado");
    }

    @Test
    @DisplayName("Debe eliminar un servicio y confirmar que ya no existe")
    void testEliminarServicio() {
        // Arrange
        Servicio servicioGuardado = servicioRepository.save(servicioTest);
        Long idGuardado = servicioGuardado.getId();

        // Verificar que el servicio existe antes de eliminarlo
        assertThat(servicioRepository.findById(idGuardado)).isPresent();

        // Act
        servicioRepository.deleteById(idGuardado);

        // Assert
        Optional<Servicio> servicioEliminado = servicioRepository.findById(idGuardado);
        assertThat(servicioEliminado).isEmpty();
    }

        @Test
        @DisplayName("Debe buscar servicios por rango de precios usando @Query JPQL")
        void testBuscarServiciosPorRangoDePrecios() {
        // Arrange
            // Se crean tres servicios para validar que solo uno quede dentro del rango.
        Servicio servicioEconomico = Servicio.builder()
            .nombre("Desayuno Continental")
            .descripcion("Desayuno básico para huéspedes")
            .descripcionDetallada("Incluye café, pan y fruta fresca")
            .precio(50.00)
            .categoria("Alimentos")
            .build();

        Servicio servicioIntermedio = Servicio.builder()
            .nombre("Spa Premium")
            .descripcion("Acceso al spa del hotel")
            .descripcionDetallada("Incluye sauna y jacuzzi")
            .precio(180.00)
            .categoria("Bienestar")
            .build();

        Servicio servicioExclusivo = Servicio.builder()
            .nombre("Cena Romántica")
            .descripcion("Cena privada en terraza")
            .descripcionDetallada("Menú de tres tiempos con vino")
            .precio(320.00)
            .categoria("Gastronomía")
            .build();

        servicioRepository.saveAll(List.of(servicioEconomico, servicioIntermedio, servicioExclusivo));

        // Act
        List<Servicio> serviciosEncontrados = servicioRepository.findByPrecioBetweenCustom(100.00, 250.00);

        // Assert
        assertThat(serviciosEncontrados).hasSize(1);
        assertThat(serviciosEncontrados.get(0).getNombre()).isEqualTo("Spa Premium");
        assertThat(serviciosEncontrados.get(0).getPrecio()).isEqualTo(180.00);
        }

        @Test
        @DisplayName("Debe filtrar servicios por categoría usando @Query JPQL")
        void testFiltrarServiciosPorCategoria() {
        // Arrange
            // Solo uno de los servicios debe coincidir con la palabra clave de la categoria.
        Servicio servicioBienestar = Servicio.builder()
            .nombre("Masaje Relajante")
            .descripcion("Masaje corporal de relajación total")
            .descripcionDetallada("Masaje profesional de 60 minutos con técnicas de relajación profunda")
            .precio(150.00)
            .categoria("Bienestar")
            .build();

        Servicio servicioGastronomia = Servicio.builder()
            .nombre("Brunch Ejecutivo")
            .descripcion("Brunch para reuniones de negocios")
            .descripcionDetallada("Incluye café, jugo y selección gourmet")
            .precio(95.00)
            .categoria("Gastronomía Premium")
            .build();

        servicioRepository.saveAll(List.of(servicioBienestar, servicioGastronomia));

        // Act
        List<Servicio> serviciosEncontrados = servicioRepository.findByCategoriaContainingIgnoreCaseCustom("bien");

        // Assert
        assertThat(serviciosEncontrados).hasSize(1);
        assertThat(serviciosEncontrados.get(0).getCategoria()).isEqualTo("Bienestar");
        }
}

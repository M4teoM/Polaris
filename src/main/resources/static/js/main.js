/* =========================================
   HOTEL POLARIS - JAVASCRIPT PRINCIPAL
   ========================================= */

// ===== VARIABLES GLOBALES =====
let currentTestimonial = 0;
const testimonials = document.querySelectorAll(".testimonial-card");

// ===== INICIALIZACIÓN =====
document.addEventListener("DOMContentLoaded", function () {
  initExploreInView();
  initNavbar();
  initSearchForm();
  initTestimonialsSlider();
  initScrollAnimations();
  initStarsBackground();
  setMinDateForSearch();
});

// ===== EXPLORE: ANIMACIÓN AL ENTRAR EN PANTALLA =====
function initExploreInView() {
  const card = document.querySelector(".explore-card");
  if (!card) return;

  const observer = new IntersectionObserver(
    (entries) => {
      entries.forEach((entry) => {
        if (entry.isIntersecting) {
          card.classList.add("in-view");
          observer.unobserve(card);
        }
      });
    },
    { threshold: 0.2 }
  );

  observer.observe(card);
}

// ===== NAVBAR =====
function initNavbar() {
  const header = document.getElementById("header");
  const navToggle = document.getElementById("navToggle");
  const navMenu = document.getElementById("navMenu");

  // Cambiar estilo del navbar al hacer scroll
  window.addEventListener("scroll", () => {
    if (window.scrollY > 100) {
      header.classList.add("scrolled");
    } else {
      header.classList.remove("scrolled");
    }
  });

  // Toggle menú móvil
  if (navToggle) {
    navToggle.addEventListener("click", () => {
      navMenu.classList.toggle("active");
    });
  }

  // Cerrar menú al hacer clic en un link
  const navLinks = document.querySelectorAll(".nav-link");
  navLinks.forEach((link) => {
    link.addEventListener("click", () => {
      navMenu.classList.remove("active");

      // Actualizar link activo
      navLinks.forEach((l) => l.classList.remove("active"));
      link.classList.add("active");
    });
  });

  // Smooth scroll para links internos
  document.querySelectorAll('a[href^="#"]').forEach((anchor) => {
    anchor.addEventListener("click", function (e) {
      e.preventDefault();
      const target = document.querySelector(this.getAttribute("href"));
      if (target) {
        const headerHeight = header.offsetHeight;
        const targetPosition = target.offsetTop - headerHeight;

        window.scrollTo({
          top: targetPosition,
          behavior: "smooth",
        });
      }
    });
  });
}

// ===== FORMULARIO DE BÚSQUEDA =====
function initSearchForm() {
  const searchForm = document.getElementById("searchForm");

  if (searchForm) {
    searchForm.addEventListener("submit", function (e) {
      e.preventDefault();

      const checkIn = document.getElementById("checkIn").value;
      const checkOut = document.getElementById("checkOut").value;
      const guests = document.getElementById("guests").value;
      const roomType = document.getElementById("roomType").value;

      // Validaciones
      if (!checkIn || !checkOut) {
        showNotification("Por favor seleccione las fechas de entrada y salida", "error");
        return;
      }

      const checkInDate = new Date(checkIn);
      const checkOutDate = new Date(checkOut);

      if (checkOutDate <= checkInDate) {
        showNotification("La fecha de salida debe ser posterior a la fecha de entrada", "error");
        return;
      }

      // Calcular noches
      const nights = Math.ceil((checkOutDate - checkInDate) / (1000 * 60 * 60 * 24));

      // Mostrar resultados (simulado)
      showNotification(
        `Buscando habitaciones disponibles del ${formatDate(checkInDate)} al ${formatDate(checkOutDate)} (${nights} noche${nights > 1 ? "s" : ""}) para ${guests} huésped${guests > 1 ? "es" : ""}...`,
        "success",
      );

      // Aquí iría la lógica real de búsqueda con el backend
      setTimeout(() => {
        // Scroll a la sección de habitaciones
        const roomsSection = document.getElementById("habitaciones");
        if (roomsSection) {
          const headerHeight = document.getElementById("header").offsetHeight;
          window.scrollTo({
            top: roomsSection.offsetTop - headerHeight,
            behavior: "smooth",
          });
        }
      }, 1000);
    });
  }
}

// ===== ESTABLECER FECHA MÍNIMA PARA BÚSQUEDA =====
function setMinDateForSearch() {
  const checkInInput = document.getElementById("checkIn");
  const checkOutInput = document.getElementById("checkOut");

  if (checkInInput && checkOutInput) {
    const today = new Date();
    const tomorrow = new Date(today);
    tomorrow.setDate(tomorrow.getDate() + 1);

    checkInInput.min = today.toISOString().split("T")[0];
    checkOutInput.min = tomorrow.toISOString().split("T")[0];

    // Actualizar mínimo de checkout cuando cambia checkin
    checkInInput.addEventListener("change", function () {
      const selectedDate = new Date(this.value);
      const nextDay = new Date(selectedDate);
      nextDay.setDate(nextDay.getDate() + 1);
      checkOutInput.min = nextDay.toISOString().split("T")[0];

      // Si checkout es antes que checkin + 1 día, actualizarlo
      if (checkOutInput.value && new Date(checkOutInput.value) <= selectedDate) {
        checkOutInput.value = nextDay.toISOString().split("T")[0];
      }
    });
  }
}

// ===== SLIDER DE TESTIMONIOS =====
function initTestimonialsSlider() {
  const track = document.getElementById("testimonialTrack");
  const prevBtn = document.getElementById("prevBtn");
  const nextBtn = document.getElementById("nextBtn");
  const dotsContainer = document.getElementById("sliderDots");

  if (!track || testimonials.length === 0) return;

  // Crear dots
  testimonials.forEach((_, index) => {
    const dot = document.createElement("div");
    dot.classList.add("slider-dot");
    if (index === 0) dot.classList.add("active");
    dot.addEventListener("click", () => goToTestimonial(index));
    dotsContainer.appendChild(dot);
  });

  const dots = document.querySelectorAll(".slider-dot");

  // Función para ir a un testimonial específico
  function goToTestimonial(index) {
    currentTestimonial = index;
    updateSlider();
  }

  // Actualizar el slider
  function updateSlider() {
    const count = testimonials.length;
    const offsetPercent = count > 0 ? -currentTestimonial * (100 / count) : 0;
    track.style.transform = `translateX(${offsetPercent}%)`;

    // Actualizar dots
    dots.forEach((dot, index) => {
      if (index === currentTestimonial) {
        dot.classList.add("active");
      } else {
        dot.classList.remove("active");
      }
    });
  }

  // Botón anterior
  if (prevBtn) {
    prevBtn.addEventListener("click", () => {
      currentTestimonial = (currentTestimonial - 1 + testimonials.length) % testimonials.length;
      updateSlider();
    });
  }

  // Botón siguiente
  if (nextBtn) {
    nextBtn.addEventListener("click", () => {
      currentTestimonial = (currentTestimonial + 1) % testimonials.length;
      updateSlider();
    });
  }

  // Auto-play (opcional)
  let autoplayInterval = setInterval(() => {
    currentTestimonial = (currentTestimonial + 1) % testimonials.length;
    updateSlider();
  }, 6000);

  // Pausar autoplay al hover
  track.addEventListener("mouseenter", () => {
    clearInterval(autoplayInterval);
  });

  track.addEventListener("mouseleave", () => {
    autoplayInterval = setInterval(() => {
      currentTestimonial = (currentTestimonial + 1) % testimonials.length;
      updateSlider();
    }, 6000);
  });

  // Soporte para swipe en móvil
  let touchStartX = 0;
  let touchEndX = 0;

  track.addEventListener("touchstart", (e) => {
    touchStartX = e.changedTouches[0].screenX;
  });

  track.addEventListener("touchend", (e) => {
    touchEndX = e.changedTouches[0].screenX;
    handleSwipe();
  });

  function handleSwipe() {
    if (touchEndX < touchStartX - 50) {
      // Swipe left
      currentTestimonial = (currentTestimonial + 1) % testimonials.length;
      updateSlider();
    }
    if (touchEndX > touchStartX + 50) {
      // Swipe right
      currentTestimonial = (currentTestimonial - 1 + testimonials.length) % testimonials.length;
      updateSlider();
    }
  }
}

// ===== ANIMACIONES DE SCROLL =====
function initScrollAnimations() {
  const animatedElements = document.querySelectorAll("[data-aos]");

  const observerOptions = {
    threshold: 0.1,
    rootMargin: "0px 0px -100px 0px",
  };

  const observer = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        entry.target.classList.add("aos-animate");
      }
    });
  }, observerOptions);

  animatedElements.forEach((element) => {
    observer.observe(element);
  });
}

// ===== FONDO DE ESTRELLAS ANIMADO =====
function initStarsBackground() {
  const canvas = document.getElementById("starsCanvas");
  if (!canvas) return;

  // Crear canvas si no existe
  const existingCanvas = canvas.querySelector("canvas");
  if (existingCanvas) return;

  const canvasEl = document.createElement("canvas");
  canvas.appendChild(canvasEl);
  const ctx = canvasEl.getContext("2d");

  // Configurar canvas
  function resizeCanvas() {
    canvasEl.width = window.innerWidth;
    canvasEl.height = window.innerHeight;
  }
  resizeCanvas();
  window.addEventListener("resize", resizeCanvas);

  // Crear estrellas
  const stars = [];
  const numStars = 150;

  for (let i = 0; i < numStars; i++) {
    stars.push({
      x: Math.random() * canvasEl.width,
      y: Math.random() * canvasEl.height,
      radius: Math.random() * 1.5,
      alpha: Math.random(),
      speed: Math.random() * 0.5,
    });
  }

  // Animar estrellas
  function animateStars() {
    ctx.clearRect(0, 0, canvasEl.width, canvasEl.height);

    stars.forEach((star) => {
      ctx.beginPath();
      ctx.arc(star.x, star.y, star.radius, 0, Math.PI * 2);
      ctx.fillStyle = `rgba(46, 196, 182, ${star.alpha})`;
      ctx.fill();

      // Efecto de parpadeo
      star.alpha += star.speed;
      if (star.alpha > 1 || star.alpha < 0) {
        star.speed *= -1;
      }
    });

    requestAnimationFrame(animateStars);
  }

  animateStars();
}

// ===== MOSTRAR DETALLES DE HABITACIÓN =====
function showRoomDetails(roomType) {
  showNotification(`Viendo detalles de habitación: ${roomType}`, "info");
}

// ===== SISTEMA DE NOTIFICACIONES =====
function showNotification(message, type = "info") {
  // Crear elemento de notificación
  const notification = document.createElement("div");
  notification.className = `notification notification-${type}`;
  notification.textContent = message;

  // Estilos inline para la notificación
  Object.assign(notification.style, {
    position: "fixed",
    top: "100px",
    right: "20px",
    padding: "1rem 1.5rem",
    borderRadius: "8px",
    boxShadow: "0 4px 16px rgba(0,0,0,0.2)",
    zIndex: "9999",
    maxWidth: "400px",
    fontSize: "0.95rem",
    fontWeight: "500",
    animation: "slideInRight 0.3s ease",
    backgroundColor:
      type === "success" ? "#10b981" : type === "error" ? "#ef4444" : type === "warning" ? "#f59e0b" : "#3b82f6",
    color: "white",
  });

  // Agregar al body
  document.body.appendChild(notification);

  // Remover después de 4 segundos
  setTimeout(() => {
    notification.style.animation = "slideOutRight 0.3s ease";
    setTimeout(() => {
      document.body.removeChild(notification);
    }, 300);
  }, 4000);
}

// Agregar estilos de animación para notificaciones
const style = document.createElement("style");
style.textContent = `
    @keyframes slideInRight {
        from {
            transform: translateX(400px);
            opacity: 0;
        }
        to {
            transform: translateX(0);
            opacity: 1;
        }
    }
    
    @keyframes slideOutRight {
        from {
            transform: translateX(0);
            opacity: 1;
        }
        to {
            transform: translateX(400px);
            opacity: 0;
        }
    }
`;
document.head.appendChild(style);

// ===== UTILIDADES =====
function formatDate(date) {
  const options = { year: "numeric", month: "long", day: "numeric" };
  return date.toLocaleDateString("es-ES", options);
}

function formatPrice(price) {
  return new Intl.NumberFormat("es-CO", {
    style: "currency",
    currency: "COP",
    minimumFractionDigits: 0,
  }).format(price);
}

// ===== VALIDACIONES =====
function validateEmail(email) {
  const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return re.test(email);
}

function validatePhone(phone) {
  const re = /^[0-9]{10}$/;
  return re.test(phone.replace(/\s/g, ""));
}

// ===== MANEJO DE ERRORES GLOBAL =====
window.addEventListener("error", function (e) {
  console.error("Error global:", e.error);
});

// ===== PREVENIR COMPORTAMIENTO POR DEFECTO EN DESARROLLO =====
if (window.location.hostname === "localhost" || window.location.hostname === "127.0.0.1") {
  console.log("🌟 Hotel Polaris - Modo Desarrollo");
  console.log("📱 Para mejores resultados, prueba en diferentes tamaños de pantalla");
}

// ===== EXPORTAR FUNCIONES PARA USO GLOBAL =====
window.HotelPolaris = {
  showRoomDetails,
  showNotification,
  formatDate,
  formatPrice,
  validateEmail,
  validatePhone,
};
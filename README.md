# Fercementos Spring — Módulo web con framework (Spring Boot)

Evidencia: **Codificación de módulos del software aplicando un framework**.

Este proyecto es la evolución del módulo web de la ferretería **Fercementos**.
En la evidencia anterior (GA7-220501096-AA2-EV01) el módulo se codificó con
Servlets y JSP "a mano"; en esta evidencia el mismo dominio se implementa
aplicando el framework **Spring Boot**, lo que permite comparar directamente
qué aporta el framework frente al desarrollo manual.

## 1. Framework seleccionado y justificación

**Spring Boot 3.2** (Java 17), con los siguientes módulos del ecosistema Spring:

| Módulo del framework | Qué reemplaza de la versión manual |
|---|---|
| **Spring MVC** (`@Controller`, `@GetMapping`, `@PostMapping`) | Los Servlets con `doGet`/`doPost` y el mapeo manual de URLs |
| **Spring Data JPA** (Hibernate) | Los DAO con JDBC: `Connection`, `PreparedStatement`, `ResultSet` y el SQL escrito a mano |
| **Thymeleaf** | Los JSP con JSTL |
| **Spring Security** | El `AuthFilter` manual, el manejo de sesión y el hash de contraseñas (ahora BCrypt) |
| **Bean Validation** (`@NotBlank`, `@Min`, `@Email`) | Las validaciones `if/else` dentro de los servlets |
| **Tomcat embebido** | La instalación y despliegue manual del `.war` en Tomcat |

Se eligió Spring Boot porque todo el proyecto Fercementos está construido en
**Java + MySQL** (aplicación de escritorio NetBeans, módulo web con Servlets),
de modo que el framework aprovecha el mismo lenguaje, la misma base de datos
y los mismos artefactos de diseño sin reescribir el dominio en otra tecnología.

## 2. Trazabilidad con los artefactos del ciclo de vida

| Artefacto previo | Cómo se refleja en este módulo |
|---|---|
| **Script SQL oficial** (Evidencia GA6-220501096-AA2-EV03) | Las entidades JPA (`Categoria`, `Cliente`, `Proveedor`, `Producto`, `Venta`, `DetalleVenta`) están mapeadas columna por columna a las tablas reales: `categorias`, `clientes`, `proveedores`, `productos`, `ventas`, `detalle_venta`. `spring.jpa.hibernate.ddl-auto=none` garantiza que el framework respete ese esquema. |
| **Modelo Entidad-Relación** (Evidencia GA6-220501096-AA1-EV02) | Las relaciones `@ManyToOne` / `@OneToMany` implementan las FK del MER (producto→categoría, producto→proveedor, venta→cliente, detalle→venta/producto). |
| **Prototipo Balsamiq** (`prototipo_inicio_sesión_de_usuarios.bmpr`) | Las vistas `login.html` y `registro.html` replican las pantallas "Iniciar sesión" y "Crear cuenta" con sus campos exactos: usuario, contraseña, correo, número de identificación, fecha de nacimiento y checkbox de términos. |
| **Diagrama de funcionalidades** | La validación "¿Usuario válido?" la realiza Spring Security (`UsuarioService implements UserDetailsService`); los mensajes de error del diagrama ("usuario o contraseña incorrectos", "campos incompletos o inválidos") aparecen en el login y en las validaciones de formularios. |
| **Diagrama de modelo de dominio** | El flujo "Compras se hace → Pedido" y "se actualiza → Inventario" se refleja en el módulo de ventas: al agregar una línea de detalle se descuenta el stock del producto (`VentaController.agregarDetalle`, transaccional). |
| **Diagrama de paquetes** | La organización en paquetes `modelo`, `repositorio`, `servicio`, `controlador`, `config` sigue la separación por responsabilidades del diagrama. |
| **Diagrama de flujo de aplicación web** | El ciclo navegador → servidor → procesamiento → respuesta corresponde al ciclo petición/respuesta de Spring MVC. |
| **Casos de uso** (Evidencia GA2-220501093-AA1-EV02) | El CU-001 "Ingreso al sistema" está implementado en el módulo de autenticación. Los mensajes de error de la aplicación usan la redacción literal de sus excepciones: "Los datos ingresados son incorrectos" (login) y "El usuario ya se encuentra registrado en la base de datos" (registro duplicado). |
| **Historias de usuario** (Evidencia GA2-220501093-AA1-EV03) | Los requerimientos funcionales implementados: registro con usuario y contraseña, actualización del inventario con cada venta y gestión de la información del negocio. Los requerimientos no funcionales guían decisiones técnicas: proteger la información del cliente (BCrypt + Spring Security), compatibilidad con navegadores (HTML estándar), facilidad de uso (interfaz uniforme entre módulos). |
| **Informe técnico de plan de trabajo** (Evidencia GA7-220501096-AA1-EV01) | El plan define Git como herramienta de versionamiento con estrategia de ramas main / develop / feature y commits descriptivos frecuentes: este repositorio implementa exactamente ese flujo (ver sección 7). También confirma el alcance del sistema: inventario, ventas, clientes, proveedores y control de usuarios — los módulos aquí codificados. |
| **Interfaces gráficas HTML** (Evidencia GA6-220501096-AA3-EV03) | Las pantallas estáticas de esa evidencia (login, registro de usuario, catálogo por categorías) evolucionan aquí a vistas dinámicas Thymeleaf conectadas a la base de datos real. |

> Los artefactos originales están en la carpeta `docs/` del repositorio.

**Nota de alcance** (documentada también en la evidencia anterior): los
diagramas conceptuales describen funcionalidades de e-commerce (pedidos,
pagos, entregas, beneficios) que no llegaron a la base de datos oficial del
proyecto. Este módulo implementa lo que existe en el esquema real, más el
módulo de usuarios definido en el prototipo, cuya tabla se agrega con el
script `sql/01_tabla_usuarios.sql`.

## 3. Estándares de codificación aplicados

- **Convenciones Java**: clases en `PascalCase`, métodos y atributos en
  `camelCase`, paquetes en minúscula, constantes en `MAYÚSCULA_CON_GUION`.
- **Arquitectura en capas** (patrón MVC + repositorio): `modelo` (entidades),
  `repositorio` (acceso a datos), `servicio` (lógica de negocio),
  `controlador` (web), `config` (configuración del framework).
- **Inyección de dependencias por constructor** (recomendación oficial de
  Spring), sin `@Autowired` sobre campos.
- **Comentarios Javadoc** en todas las clases y en los métodos no triviales,
  explicando además la trazabilidad con los artefactos de diseño.
- **Convención uniforme de rutas** en todos los módulos CRUD:
  `GET /{modulo}`, `GET /{modulo}/nuevo`, `GET /{modulo}/editar/{id}`,
  `POST /{modulo}/guardar`, `GET /{modulo}/eliminar/{id}`.
- **Seguridad**: contraseñas con hash BCrypt, protección CSRF activa
  (Thymeleaf añade el token automáticamente en los formularios POST),
  rutas internas protegidas por Spring Security.
- **Transaccionalidad**: las operaciones que tocan varias tablas (agregar
  línea de venta + descontar stock) están marcadas con `@Transactional`.

## 4. Estructura del proyecto

```
fercementos-spring/
├── pom.xml
├── sql/
│   ├── 01_tabla_usuarios.sql        # complemento al esquema oficial
│   └── 02_datos_prueba.sql          # datos opcionales de prueba
├── docs/                            # artefactos del ciclo de vida
└── src/main/
    ├── java/co/fercementos/
    │   ├── FercementosApplication.java
    │   ├── config/SecurityConfig.java
    │   ├── modelo/          # entidades JPA (mapean el script SQL oficial)
    │   ├── repositorio/     # interfaces Spring Data JPA
    │   ├── servicio/        # UsuarioService (login + registro)
    │   └── controlador/     # controladores Spring MVC
    └── resources/
        ├── application.properties
        ├── static/css/estilos.css
        └── templates/       # vistas Thymeleaf (login, registro, index,
                             #  categorias/, proveedores/, clientes/,
                             #  productos/, ventas/)
```

## 5. Rutas de la aplicación

| Módulo | Listar | Crear | Editar | Guardar (POST) | Eliminar |
|---|---|---|---|---|---|
| Categorías | `/categorias` | `/categorias/nueva` | `/categorias/editar/{id}` | `/categorias/guardar` | `/categorias/eliminar/{id}` |
| Proveedores | `/proveedores` | `/proveedores/nuevo` | `/proveedores/editar/{id}` | `/proveedores/guardar` | `/proveedores/eliminar/{id}` |
| Clientes | `/clientes` | `/clientes/nuevo` | `/clientes/editar/{id}` | `/clientes/guardar` | `/clientes/eliminar/{id}` |
| Productos | `/productos` (+ búsqueda `?q=`) | `/productos/nuevo` | `/productos/editar/{id}` | `/productos/guardar` | `/productos/eliminar/{id}` |
| Ventas | `/ventas` | `/ventas/nueva` | `/ventas/detalle/{id}` | `/ventas/guardar`, `/ventas/detalle/{id}/agregar` | `/ventas/eliminar/{id}` |

Autenticación: `GET /login`, `POST /login` (Spring Security), `GET|POST /registro`, `GET /logout`.

## 6. Cómo ejecutar el proyecto

1. **Base de datos**: tener MySQL con la BD `ferreteria` creada a partir del
   script oficial de la Evidencia GA6-220501096-AA2-EV03, y luego ejecutar
   `sql/01_tabla_usuarios.sql` (y opcionalmente `02_datos_prueba.sql`).
2. **Credenciales**: si tu usuario/clave de MySQL no son `root`/`9604`,
   ajústalos en `src/main/resources/application.properties`.
3. **Requisitos**: JDK 17 y Maven (o el Maven embebido de NetBeans/IntelliJ).
4. **Ejecutar**:
   ```bash
   mvn spring-boot:run
   ```
   o desde NetBeans/IntelliJ: abrir el proyecto Maven y ejecutar la clase
   `FercementosApplication`.
5. **Probar**: abrir `http://localhost:8080/` → redirige al login → crear
   una cuenta en "Crear cuenta" → iniciar sesión → usar los módulos.

## 7. Control de versiones

El proyecto se creó con Git desde el inicio siguiendo la **estrategia de
ramas definida en el Informe técnico de plan de trabajo (GA7-220501096-AA1-EV01)**:

- **main**: versión final y estable del sistema.
- **develop**: rama de integración del desarrollo.
- **feature/***: una rama por funcionalidad (`feature/entidades-jpa`,
  `feature/seguridad`, `feature/modulos-crud`, `feature/vistas`), que se
  integran a `develop` y, una vez estable, a `main`.

Los commits son frecuentes y descriptivos (convención `tipo(alcance): descripción`),
tal como exige el plan. El historial (`git log --graph --all`) evidencia el
proceso completo de codificación. Para publicarlo en GitHub (plataforma
seleccionada en el plan):

```bash
git remote add origin <URL-de-tu-repositorio>
git branch -M main
git push -u origin main
```

**Importante**: antes de subir a un repositorio público, externaliza las
credenciales de la base de datos (variables de entorno o un
`application-local.properties` ignorado por Git).

## 8. Diferencia con la evidencia anterior (Servlets/JSP)

| Aspecto | Versión Servlets/JSP | Versión Spring Boot |
|---|---|---|
| Acceso a datos | SQL manual en cada DAO (~90 líneas por entidad) | Interfaz de 3 líneas por entidad (Spring Data JPA) |
| Login/sesión | Servlet + filtro manuales, hash SHA-256 propio | Spring Security + BCrypt |
| Validaciones | `if/else` en el servlet | Anotaciones en la entidad + `BindingResult` |
| Vistas | JSP + JSTL | Thymeleaf con fragmentos tipados |
| Despliegue | `.war` sobre Tomcat instalado | `mvn spring-boot:run` (Tomcat embebido) |

Nota: los usuarios creados en la versión anterior no son compatibles con esta
(el hash de contraseña cambió de SHA-256 a BCrypt); deben registrarse de nuevo.

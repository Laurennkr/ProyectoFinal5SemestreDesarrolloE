# ProyectoFinal5SemestreDesarrolloE
Proyecto final del quinto semestre de Ingeniería de sistemas con el evaluador Javier Charry

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen)
![Spring Security](https://img.shields.io/badge/Security-JWT-red)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![License](https://img.shields.io/badge/Status-Académico-success)

## 📖 Descripción

**Groccy** es una plataforma web desarrollada para administrar de forma centralizada el proceso operativo de una empresa dedicada a la fabricación y comercialización de chaquetas de invierno.

El sistema permite gestionar proveedores, insumos, producción, inventario, ventas, alertas y reportes empresariales mediante una arquitectura robusta basada en **Java 21** y **Spring Boot**.

Su característica diferenciadora es la integración directa entre producción e inventario mediante un panel especializado para costureros, permitiendo registrar avances de fabricación y reportar faltantes de materiales en tiempo real.

---

## 🎯 Objetivo del Proyecto

Centralizar la gestión empresarial de una tienda con producción propia y múltiples puntos de venta, permitiendo:

- Control de inventario central y local.
- Gestión de proveedores e insumos.
- Administración de producción.
- Registro y control de ventas.
- Alertas automáticas de stock.
- Métricas y reportes para la toma de decisiones.
- Seguridad basada en roles mediante JWT.

---

# 👥 Equipo de Desarrollo

| Integrante | Rol Principal |
|------------|---------------|
| **Lauren Castro** | Base de datos y lógica de inventario |
| **Jerónimo Ramos** | Backend, arquitectura y seguridad |
| **Alisson Romero** | Documentación y pruebas |
| **Valentina García** | Diseño visual y frontend |

---

# 🏗️ Arquitectura del Sistema

El proyecto implementa una arquitectura en capas siguiendo las buenas prácticas de Spring Boot:

```text
Frontend
    │
    ▼
Controllers REST
    │
    ▼
Services
    │
    ▼
Repositories
    │
    ▼
Base de Datos
```

### Componentes principales

- Controllers REST
- Services (lógica de negocio)
- Repositories (Spring Data JPA)
- DTOs
- Entidades JPA
- Seguridad JWT
- Manejo global de excepciones
- Validaciones con Bean Validation

---

# 🔐 Roles del Sistema

## 👑 Administrador

Tiene acceso completo al sistema.

Funciones:

- Gestionar proveedores
- Gestionar insumos
- Gestionar usuarios
- Crear fichas de producción
- Controlar inventario central
- Distribuir productos a locales
- Consultar ventas
- Consultar ganancias
- Revisar alertas

---

## 🏪 Vendedor

Opera únicamente sobre su local asignado.

Funciones:

- Consultar stock del local
- Registrar ventas
- Consultar productos más vendidos
- Actualización automática del inventario

---

## ✂️ Costurero / Satélite

Responsable de la producción.

Funciones:

- Consultar fichas asignadas
- Visualizar imágenes de referencia
- Consultar materiales requeridos
- Reportar producción realizada
- Reportar faltantes de insumos

---

# 🚀 Funcionalidades Principales

## 🔑 Autenticación

- Inicio de sesión
- JWT Authentication
- Autorización por roles
- Protección de endpoints

## 📦 Gestión de Inventario

- Inventario central
- Inventario por local
- Control de stock mínimo
- Stock crítico
- Movimientos de inventario

## 🧵 Producción

- Fichas de producción
- Materiales requeridos
- Seguimiento de fabricación
- Reporte de producción
- Reporte de faltantes

## 💰 Ventas

- Registro de ventas
- Validación de stock
- Descuento automático de inventario
- Historial de ventas

## 📊 Reportes

- Ventas por local
- Top productos vendidos
- Ganancias semanales
- Dashboard administrativo

## 🚨 Alertas

- Bajo stock
- Stock crítico
- Faltantes de materiales
- Producción finalizada

---

# 🗄️ Modelo de Datos

Principales entidades del sistema:

- Usuario
- Rol
- Proveedor
- Insumo
- Producto
- EtiquetaProducto
- FichaProduccion
- MaterialRequerido
- Produccion
- Local
- StockCentral
- StockLocal
- Venta
- DetalleVenta
- Alerta
- MovimientoInventario

---

# 🛠️ Tecnologías Utilizadas

## Backend

- Java 21 LTS
- Spring Boot 3.x
- Spring Security
- JWT
- Spring Data JPA
- Hibernate
- MapStruct
- Jakarta Bean Validation
- OpenAPI / Swagger

## Base de Datos

- PostgreSQL 16
- MySQL 8

## Frontend

- HTML5
- CSS3
- JavaScript
- React (opcional)

## Herramientas

- Maven
- Git
- GitHub
- Postman
- IntelliJ IDEA

---

# 📡 API REST

Todos los endpoints utilizan el prefijo:

```http
/api/v1
```

### Módulos disponibles

#### Autenticación

```http
POST /auth/login
GET  /auth/me
```

#### Usuarios

```http
POST /usuarios
GET  /usuarios
PATCH /usuarios/{id}/estado
```

#### Proveedores

```http
POST /proveedores
GET  /proveedores
PUT  /proveedores/{id}
```

#### Producción

```http
POST /fichas-produccion
GET  /fichas-produccion
GET  /fichas-produccion/mis-fichas
POST /producciones
```

#### Inventario

```http
GET  /stock-central
POST /distribuciones
GET  /stock-locales/{id}
```

#### Ventas

```http
POST /ventas
GET  /ventas
```

#### Reportes

```http
GET /reportes/ventas-por-local
GET /reportes/top-productos
GET /reportes/ganancias-semanales
```

---

# ⚙️ Instalación y Ejecución

## Clonar el repositorio

```bash
git clone https://github.com/usuario/groccy.git
cd groccy
```

## Compilar el proyecto

```bash
mvn clean install
```

## Ejecutar la aplicación

```bash
mvn spring-boot:run
```

---

# 🔧 Variables de Entorno

Crear un archivo `.env` o configurar:

```env
DB_URL=
DB_USERNAME=
DB_PASSWORD=

JWT_SECRET=
JWT_EXPIRATION=
```

---

# 📋 Flujo de Prueba Recomendado

### 1️⃣ Administrador

- Inicia sesión
- Registra proveedor
- Registra insumos
- Crea ficha de producción

### 2️⃣ Costurero

- Consulta ficha asignada
- Reporta unidades realizadas
- Reporta faltantes

### 3️⃣ Administrador

- Valida producción
- Recibe alertas
- Distribuye inventario

### 4️⃣ Vendedor

- Registra venta
- Verifica actualización automática de stock

### 5️⃣ Dashboard

- Consulta ventas
- Consulta top productos
- Consulta ganancias

---

# 🎨 Identidad Visual

Paleta corporativa:

| Color | Código |
|---------|----------|
| Rojo Principal | `#C8102E` |
| Negro Fondo | `#111111` |
| Blanco Texto | `#FFFFFF` |
| Rojo Oscuro Alertas | `#1F0505` |

---

# 📚 Documentación

La documentación de la API puede consultarse mediante Swagger:

```http
http://localhost:8080/swagger-ui.html
```

o

```http
http://localhost:8080/swagger-ui/index.html
```

---

# 📌 Estado del Proyecto

🚧 Proyecto académico desarrollado para la asignatura **Desarrollo Empresarial** de la **Universidad Santo Tomás**.

Versión actual:

```text
v1.0
```

---

# 📄 Licencia

Proyecto desarrollado con fines académicos.

© 2026 - Equipo Groccy

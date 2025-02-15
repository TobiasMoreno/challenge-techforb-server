# Challenge Técnico 2025 - Techforb 🚀

## 📌 Descripción
Este proyecto es una aplicación **full-stack** creada con **Spring Boot** en el backend y **Angular** en el frontend. Está diseñada para gestionar sensores, lecturas y alertas en plantas industriales, con un enfoque en seguridad y usabilidad. Implementé autenticación con JWT para proteger las rutas y asegurar el acceso controlado a la información.

He buscado crear una solución robusta y eficiente, utilizando tecnologías modernas que garantizan un buen rendimiento y una experiencia de usuario fluida. Este proyecto refleja mi capacidad para desarrollar aplicaciones escalables y bien estructuradas.

Soy **Tobias Moreno**, un desarrollador web con experiencia en **Angular** y **Spring Boot**. Mi objetivo es crear aplicaciones eficientes y escalables, enfocadas en ofrecer la mejor experiencia al usuario. Este proyecto es solo una muestra de mi enfoque hacia el desarrollo de software de alta calidad.

---

## 🛠️ Tecnologías Utilizadas
### Backend (Spring Boot)
- **Spring Boot 3**
- **Spring Security con JWT**
- **Spring Data JPA**
- **Hibernate**
- **MySQL + Docker**
- **Swagger + SpringDoc**

### Frontend (Angular)
- **Angular Material**
- **Bootstrap**

### Infraestructura
- **Netlify** (Frontend)
- **Railway** (Backend)

---

## Características del Proyecto
- **Autenticación y seguridad** con JWT, validación de correo y contraseña, cifrado de la contraseña, y manejo de token de seguridad.
- **Protección de endpoints**: Solo los usuarios autenticados pueden acceder a los datos sensibles.
- **Obtención dinámica de datos** para el frontend:
  - Obtener lecturas, alertas medias, alertas rojas, sensores deshabilitados y plantas con sus datos.
- **Gestión de sensores y lecturas**:
  - CRUD completo para sensores y lecturas.
  - Modificación de registros de lecturas y alertas para cada sensor.
- **Gestión de plantas**: Crear, editar y eliminar plantas.
- **Tabla principal**: Los datos de lecturas y alertas de cada planta corresponden a la sumatoria de las cifras para todos los sensores de esa planta.
- **Creación y edición de plantas** y sus datos asociados.
- **Validaciones y manejo de errores**: Validación de entrada, como formato de correo y longitud de la contraseña, y mensajes de error claros y detallados.

---

## Instalación y Configuración

### 1- Clonar el Repositorio
```bash
git clone https://github.com/TobiasMoreno/challenge-techforb-server.git
cd challenge-techforb-server
```

# Servicio BFF - Historial Médico

## Descripción
Este proyecto es un Backend-For-Frontend (BFF) que actúa como una capa intermedia entre el frontend y múltiples microservicios. Su principal función es agregar y transformar datos de diferentes servicios para proporcionar una API unificada y optimizada para el cliente.

## Tecnologías Utilizadas
- Java 17
- Spring Boot 3.x
- Spring Cloud OpenFeign (para comunicación entre microservicios)
- iText PDF (para generación de reportes)
- Maven (gestión de dependencias)

## Requisitos Previos
- JDK 17 o superior
- Maven 3.6 o superior
- Acceso a los siguientes microservicios:
  - Servicio de Pacientes
  - Servicio de Citas
  - Servicio de Consultas
  - Servicio de Pagos

## Configuración del Entorno
1. Clonar el repositorio
2. Configurar las URLs de los microservicios en `application.properties`:
```properties
services.pacientes.url=https://microserviciospacientes-production.up.railway.app
services.citas.url=https://microserviciocitas-production.up.railway.app
services.consultas.url=https://microservicioconsultas-production.up.railway.app
services.pagos.url=https://microserviciopagos-production.up.railway.app
```

## Iniciar el Servicio
1. Navegar al directorio del proyecto:
```bash
cd tallerfinalpoo
```

2. Compilar el proyecto:
```bash
mvn clean install
```

3. Ejecutar el servicio:
```bash
mvn spring-boot:run
```

El servicio estará disponible en `http://localhost:8080`

## Estructura del Proyecto
```
tallerfinalpoo/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/market/
│   │   │       ├── client/           # Clientes Feign para microservicios
│   │   │       ├── controller/       # Controladores REST
│   │   │       ├── domain/           # DTOs y lógica de negocio
│   │   │       ├── infraestructure/  # Implementaciones de repositorios
│   │   │       └── util/             # Utilidades (generación PDF)
│   │   └── resources/
│   │       └── application.properties # Configuración
└── pom.xml
```

## Endpoints Principales

### Historial Médico
- `GET /historial/paciente/{id}`: Obtiene un resumen básico del paciente
- `GET /historial/paciente/{id}/completo`: Obtiene el historial médico completo
- `GET /historial/paciente/{id}/pdf`: Genera un PDF con el historial médico

## Características del Servicio

### 1. Agregación de Datos
- Combina información de múltiples microservicios
- Proporciona una vista unificada del historial médico
- Calcula estadísticas y totales

### 2. Transformación de Datos
- Convierte formatos de datos entre servicios
- Agrega cálculos y derivaciones
- Optimiza la respuesta para el cliente

### 3. Generación de Reportes
- Crea PDFs profesionales del historial médico
- Incluye estadísticas y resúmenes
- Formato estructurado y fácil de leer

### 4. Manejo de Errores
- Gestión centralizada de errores
- Respuestas consistentes
- Logging detallado

## Ejemplos de Uso

### Obtener Resumen del Paciente
```bash
curl http://localhost:8080/historial/paciente/1
```

### Generar PDF del Historial
```bash
curl http://localhost:8080/historial/paciente/1/pdf -o historial.pdf
```

## Contribución
1. Fork el repositorio
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

## Licencia
Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles. 
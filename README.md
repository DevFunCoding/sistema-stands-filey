# Sistema Administrativo de Stands - FILEY

## 1. Requisitos Funcionales Implementados

1. **Gestión de Estados de Stands:**
   - `DISPONIBLE`: Abierto a reservación general.
   - `BLOQUEADO`: Reservación especial por el administrador. Permite la transición manual a `DISPONIBLE` o la asignación directa a `RESERVADO`.
   - `RESERVADO`: Asignado a una entidad editorial. Incluye validación de tiempos límite de reserva y aplicación de pronto pago.
   - `OCUPADO`: Transición de estado automatizada al registrarse un pago que cubra, como mínimo, el 15% del costo total del stand.
2. **Cálculo de Costos:** Se determina mediante la fórmula: Dimensiones ($m^2$) $\times$ Valor por $m^2$.
3. **Motor de Notificaciones:** Sistema de evaluación  de fechas límite para la emisión de alertas relacionadas con el vencimiento de reservas, caducidad de pronto pago y fecha máxima de liquidación total.

---

## 2. Arquitectura del Sistema (Diseño por Paquetes)

- `com.filey.datos`: Modelos del dominio (`Stand`, `Editorial`, `EstadoStand`).
- `com.filey.logica`: Definición de contratos e implementación de las reglas de negocio (`IGestorOperaciones`, `GestorOperacionesImpl`).
- `com.filey.notificaciones`: Componentes del patrón Observer para el manejo de eventos y alertas (`IObservadorAlerta`, `ObservadorConsola`, `MotorNotificaciones`).
- `com.filey.main`: Interfaz de usuario interactiva basada en línea de comandos (CLI).

---

## 3. Diagrama de Clases UML

```mermaid
classDiagram
    namespace Datos {
        class EstadoStand {
            <<enumeration>>
            DISPONIBLE
            RESERVADO
            OCUPADO
            BLOQUEADO
        }
        class Editorial {
            -String id
            -String nombre
            -String emailContacto
        }
        class Stand {
            -String id
            -double metrosCuadrados
            -double valorMetroCuadrado
            -EstadoStand estado
            -Editorial editorialAsignada
            -double montoPagado
            -LocalDateTime limiteReserva
            -LocalDateTime limiteProntoPago
            -LocalDateTime limitePagoTotal
        }
    }

    namespace Logica {
        class IGestorOperaciones {
            <<interface>>
            +calcularCostoTotal(Stand) double
            +registrarPago(Stand, double)
            +asignarReservaEspecial(Stand, Editorial)
            +liberarStandBloqueado(Stand)
        }
        class GestorOperacionesImpl {
            +calcularCostoTotal(Stand) double
            +registrarPago(Stand, double)
        }
    }

    namespace Notificaciones {
        class IObservadorAlerta {
            <<interface>>
            +notificar(Editorial, String)
        }
        class ObservadorConsola {
            +notificar(Editorial, String)
        }
        class MotorNotificaciones {
            -List~IObservadorAlerta~ observadores
            +suscribir(IObservadorAlerta)
            +desuscribir(IObservadorAlerta)
            +procesarAlertas(List~Stand~)
        }
    }

    Stand --> EstadoStand
    Stand --> Editorial
    GestorOperacionesImpl ..|> IGestorOperaciones
    ObservadorConsola ..|> IObservadorAlerta
    MotorNotificaciones --> IObservadorAlerta : notifica
```

---

## 4. Instrucciones de Compilación y Ejecución

Para garantizar la compatibilidad con entornos de ejecución Java 8 o versiones posteriores, es imperativo utilizar el parámetro de compilación correspondiente.

```bash
# 1. Compilación del código fuente especificando la compatibilidad con Java 8
javac --release 8 src/com/filey/datos/*.java src/com/filey/logica/*.java src/com/filey/notificaciones/*.java src/com/filey/main/Main.java -d out/

# 2. Ejecución del sistema interactivo
java -cp out com.filey.main.Main
```
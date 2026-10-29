package com.filey.main;

import com.filey.datos.Editorial;
import com.filey.datos.EstadoStand;
import com.filey.datos.Stand;
import com.filey.logica.GestorOperacionesImpl;
import com.filey.logica.IGestorOperaciones;
import com.filey.notificaciones.IObservadorAlerta;
import com.filey.notificaciones.MotorNotificaciones;
import com.filey.notificaciones.ObservadorConsola;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // 1. Configuración inicial del sistema
        MotorNotificaciones motor = new MotorNotificaciones();
        IObservadorAlerta consola = new ObservadorConsola();
        motor.suscribir(consola); 

        IGestorOperaciones gestor = new GestorOperacionesImpl();
        List<Stand> standsEnSistema = inicializarStandsPrueba();
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;

        System.out.println("===========================================");
        System.out.println("  SISTEMA ADMINISTRATIVO FILEY INICIADO");
        System.out.println("===========================================");

        // 2. Bucle del Menú Interactivo
        while (!salir) {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1. Ver estado de los Stands");
            System.out.println("2. Reservar un Stand (Simulación)");
            System.out.println("3. Registrar Pago a un Stand");
            System.out.println("4. Ejecutar Barrido de Notificaciones");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    mostrarStands(standsEnSistema, gestor);
                    break;
                case "2":
                    reservarStandMenu(scanner, standsEnSistema);
                    break;
                case "3":
                    registrarPagoMenu(scanner, standsEnSistema, gestor);
                    break;
                case "4":
                    System.out.println("\n--- EJECUTANDO MOTOR DE NOTIFICACIONES ---");
                    motor.procesarAlertas(standsEnSistema);
                    System.out.println("------------------------------------------");
                    break;
                case "5":
                    salir = true;
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
        scanner.close();
    }

    // --- MÉTODOS DE APOYO PARA EL MENÚ ---

    private static List<Stand> inicializarStandsPrueba() {
        List<Stand> lista = new ArrayList<>();
        // Creamos un par de stands disponibles por defecto
        lista.add(new Stand("A1", 10.0, 1000.0, EstadoStand.DISPONIBLE));
        lista.add(new Stand("B2", 15.0, 1200.0, EstadoStand.DISPONIBLE));
        lista.add(new Stand("C3", 20.0, 1000.0, EstadoStand.BLOQUEADO)); // Uno bloqueado para pruebas
        return lista;
    }

    private static void mostrarStands(List<Stand> stands, IGestorOperaciones gestor) {
        System.out.println("\n--- LISTADO DE STANDS ---");
        for (Stand s : stands) {
            System.out.printf("Stand: %s | Estado: %-10s | Costo Total: $%.2f | Pagado: $%.2f", 
                    s.getId(), s.getEstado(), gestor.calcularCostoTotal(s), s.getMontoPagado());
            if (s.getEditorialAsignada() != null) {
                System.out.print(" | Editorial: " + s.getEditorialAsignada().getNombre());
            }
            System.out.println();
        }
    }

    private static void reservarStandMenu(Scanner scanner, List<Stand> stands) {
        System.out.print("Ingrese el ID del stand a reservar (Ej. A1): ");
        String idStand = scanner.nextLine();
        
        Stand standEncontrado = buscarStand(idStand, stands);
        if (standEncontrado == null) {
            System.out.println("Stand no encontrado.");
            return;
        }

        if (standEncontrado.getEstado() != EstadoStand.DISPONIBLE) {
            System.out.println("El stand no está disponible (Actualmente: " + standEncontrado.getEstado() + ").");
            return;
        }

        System.out.print("Nombre de la editorial: ");
        String nombre = scanner.nextLine();
        System.out.print("Email de contacto: ");
        String email = scanner.nextLine();

        Editorial nuevaEditorial = new Editorial("E-" + System.currentTimeMillis(), nombre, email);
        standEncontrado.setEditorialAsignada(nuevaEditorial);
        standEncontrado.setEstado(EstadoStand.RESERVADO);
        
        // Configuramos fechas simuladas para que el motor lance alertas al probarlo
        standEncontrado.setLimiteReserva(LocalDateTime.now().plusHours(12)); // Vence pronto
        standEncontrado.setLimiteProntoPago(LocalDateTime.now().plusDays(1)); // Vence pronto

        System.out.println("¡Stand reservado exitosamente!");
    }

    private static void registrarPagoMenu(Scanner scanner, List<Stand> stands, IGestorOperaciones gestor) {
        System.out.print("Ingrese el ID del stand a pagar: ");
        String idStand = scanner.nextLine();
        
        Stand standEncontrado = buscarStand(idStand, stands);
        if (standEncontrado == null) {
            System.out.println("Stand no encontrado.");
            return;
        }

        System.out.print("Monto a pagar: $");
        try {
            double monto = Double.parseDouble(scanner.nextLine());
            gestor.registrarPago(standEncontrado, monto);
            System.out.println("Pago registrado. Estado actual del stand: " + standEncontrado.getEstado());
        } catch (NumberFormatException e) {
            System.out.println("Monto inválido. Ingrese solo números.");
        }
    }

    private static Stand buscarStand(String id, List<Stand> stands) {
        for (Stand s : stands) {
            if (s.getId().equalsIgnoreCase(id)) {
                return s;
            }
        }
        return null;
    }
}
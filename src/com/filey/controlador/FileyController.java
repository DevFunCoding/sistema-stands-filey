package com.filey.controlador;

import com.filey.datos.Editorial;
import com.filey.datos.EstadoStand;
import com.filey.datos.Reservacion;
import com.filey.datos.Stand;
import com.filey.logica.IGestorOperaciones;
import com.filey.notificaciones.IObservadorAlerta;
import com.filey.notificaciones.MotorNotificaciones;
import com.filey.persistencia.IEditorialDAO;
import com.filey.persistencia.IReservacionDAO;
import com.filey.persistencia.IStandDAO;
import com.filey.vista.FileyView;

import java.util.List;

public class FileyController {
    private final FileyView view;
    private final IGestorOperaciones gestor;
    private final IStandDAO standDAO;
    private final IEditorialDAO editorialDAO;
    private final IReservacionDAO reservacionDAO;
    private final MotorNotificaciones motorNotificaciones;

    public FileyController(FileyView view, IGestorOperaciones gestor,
                           IStandDAO standDAO, IEditorialDAO editorialDAO,
                           IReservacionDAO reservacionDAO, MotorNotificaciones motorNotificaciones) {
        this.view = view;
        this.gestor = gestor;
        this.standDAO = standDAO;
        this.editorialDAO = editorialDAO;
        this.reservacionDAO = reservacionDAO;
        this.motorNotificaciones = motorNotificaciones;

        this.view.addActualizarListener(e -> cargarTablaStands());
        this.view.addReservarListener(e -> reservarStand());
        this.view.addPagarListener(e -> registrarPago());
        this.view.addNotificacionesListener(e -> ejecutarNotificaciones());
        this.view.addEditorialesListener(e -> listarEditoriales());

        cargarTablaStands();
    }

    public void cargarTablaStands() {
        view.limpiarTabla();
        List<Stand> stands = standDAO.listarTodos();
        for (Stand s : stands) {
            Reservacion res = reservacionDAO.buscarActivaPorStand(s.getId());
            String nombreEditorial = (res != null && res.getEditorial() != null) ? res.getEditorial().getNombre() : "—";
            double pagado = (res != null) ? res.getMontoPagado() : 0.0;

            view.agregarFilaStand(new Object[]{
                s.getId(),
                s.getMetrosCuadrados() + " m²",
                String.format("$%.2f", gestor.calcularCostoTotal(s)),
                s.getEstado().name(),
                nombreEditorial,
                String.format("$%.2f", pagado)
            });
        }
    }

    private void reservarStand() {
        String idStand = view.getIdStandSeleccionado();
        if (idStand == null) {
            view.mostrarError("Selecciona primero un stand de la tabla.");
            return;
        }

        Stand stand = standDAO.buscarPorId(idStand);
        if (stand == null || stand.getEstado() != EstadoStand.DISPONIBLE) {
            view.mostrarError("El stand " + idStand + " no está disponible para reserva.");
            return;
        }

        String[] datos = view.pedirDatosEditorial(idStand);
        if (datos == null) return;

        if (datos[0].isEmpty() || datos[1].isEmpty()) {
            view.mostrarError("Debes ingresar nombre y correo de la editorial.");
            return;
        }

        Editorial nuevaEditorial = new Editorial("E-" + System.currentTimeMillis(), datos[0], datos[1]);
        if (gestor.reservarStand(stand, nuevaEditorial)) {
            view.mostrarInfo("Éxito", "Stand " + idStand + " reservado para " + datos[0] + ".");
            cargarTablaStands();
        } else {
            view.mostrarError("Ocurrió un error al guardar la reservación en la base de datos.");
        }
    }

    private void registrarPago() {
        String idStand = view.getIdStandSeleccionado();
        if (idStand == null) {
            view.mostrarError("Selecciona un stand de la tabla para registrar su pago.");
            return;
        }

        Reservacion reserva = reservacionDAO.buscarActivaPorStand(idStand);
        if (reserva == null) {
            view.mostrarError("El stand " + idStand + " no tiene una reservación activa.");
            return;
        }

        String input = view.pedirMontoPago(idStand);
        if (input == null || input.trim().isEmpty()) return;

        try {
            double monto = Double.parseDouble(input.trim());
            if (monto <= 0) {
                view.mostrarError("El monto debe ser mayor a cero.");
                return;
            }
            if (gestor.registrarPago(reserva, monto)) {
                view.mostrarInfo("Pago Registrado", "Abono aplicado. Estado actual del stand: " + reserva.getStand().getEstado());
                cargarTablaStands();
            } else {
                view.mostrarError("No se pudo actualizar el pago en la base de datos.");
            }
        } catch (NumberFormatException ex) {
            view.mostrarError("Monto inválido. Ingresa un valor numérico.");
        }
    }

    private void ejecutarNotificaciones() {
        StringBuilder reporte = new StringBuilder();
        IObservadorAlerta observadorGUI = (editorial, mensaje) ->
            reporte.append("• Para: ").append(editorial.getNombre())
                   .append(" (").append(editorial.getEmailContacto()).append(")\n  ")
                   .append(mensaje).append("\n\n");

        motorNotificaciones.suscribir(observadorGUI);
        motorNotificaciones.procesarAlertas(reservacionDAO.listarActivas());
        motorNotificaciones.desuscribir(observadorGUI);

        if (reporte.length() == 0) {
            view.mostrarInfo("Notificaciones", "No hay alertas pendientes por vencer en este momento.");
        } else {
            view.mostrarInfo("Alertas Generadas", reporte.toString());
        }
    }

    private void listarEditoriales() {
        List<Editorial> lista = editorialDAO.listarTodas();
        if (lista.isEmpty()) {
            view.mostrarInfo("Editoriales", "Aún no hay editoriales registradas en la base de datos.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Editorial e : lista) {
            sb.append("• ").append(e.getNombre()).append(" — ").append(e.getEmailContacto()).append("\n");
        }
        view.mostrarInfo("Editoriales Registradas", sb.toString());
    }
}
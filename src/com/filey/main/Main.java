package com.filey.main;

import com.filey.controlador.FileyController;
import com.filey.logica.GestorOperacionesImpl;
import com.filey.logica.IGestorOperaciones;
import com.filey.notificaciones.MotorNotificaciones;
import com.filey.notificaciones.ObservadorConsola;
import com.filey.persistencia.*;
import com.filey.vista.FileyView;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            IStandDAO standDAO = new StandDAOImpl();
            IEditorialDAO editorialDAO = new EditorialDAOImpl();
            IReservacionDAO reservacionDAO = new ReservacionDAOImpl();

            IGestorOperaciones gestor = new GestorOperacionesImpl(standDAO, editorialDAO, reservacionDAO);
            MotorNotificaciones motor = new MotorNotificaciones();
            motor.suscribir(new ObservadorConsola());

            FileyView view = new FileyView();
            new FileyController(view, gestor, standDAO, editorialDAO, reservacionDAO, motor);
            view.setVisible(true);
        });
    }
}
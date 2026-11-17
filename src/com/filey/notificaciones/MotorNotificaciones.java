package com.filey.notificaciones;

import com.filey.datos.Editorial;
import com.filey.datos.EstadoStand;
import com.filey.datos.Reservacion;
import com.filey.datos.Stand;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MotorNotificaciones {

    private final List<IObservadorAlerta> observadores;

    public MotorNotificaciones() {
        this.observadores = new ArrayList<>();
    }

    public void suscribir(IObservadorAlerta observador) {
        this.observadores.add(observador);
    }

    public void desuscribir(IObservadorAlerta observador) {
        this.observadores.remove(observador);
    }

    private void notificarATodos(Editorial editorial, String mensaje) {
        for (IObservadorAlerta observador : observadores) {
            observador.notificar(editorial, mensaje);
        }
    }

    public void procesarAlertas(List<Reservacion> reservaciones) {
        LocalDateTime ahora = LocalDateTime.now();

        for (Reservacion reserva : reservaciones) {
            if (!reserva.isActiva() || reserva.getEditorial() == null) continue;

            Stand stand = reserva.getStand();
            Editorial editorial = reserva.getEditorial();

            if (stand.getEstado() == EstadoStand.RESERVADO && reserva.getLimiteReserva() != null) {
                if (ahora.isAfter(reserva.getLimiteReserva())) {
                    notificarATodos(editorial,
                        "URGENTE: La reserva del stand " + stand.getId() + " ha vencido.");
                } else if (ahora.plusDays(1).isAfter(reserva.getLimiteReserva())) {
                    notificarATodos(editorial,
                        "ALERTA: Su reserva del stand " + stand.getId() + " vence en menos de 24 horas.");
                }
            }

            if (reserva.getLimiteProntoPago() != null && stand.getEstado() != EstadoStand.DISPONIBLE) {
                if (ahora.plusDays(2).isAfter(reserva.getLimiteProntoPago()) && ahora.isBefore(reserva.getLimiteProntoPago())) {
                    notificarATodos(editorial,
                        "RECORDATORIO: El descuento por pronto pago del stand " + stand.getId() + " vence pronto.");
                }
            }

            if ((stand.getEstado() == EstadoStand.OCUPADO || stand.getEstado() == EstadoStand.RESERVADO)
                    && reserva.getLimitePagoTotal() != null) {
                if (ahora.plusDays(3).isAfter(reserva.getLimitePagoTotal()) && ahora.isBefore(reserva.getLimitePagoTotal())) {
                    notificarATodos(editorial,
                        "AVISO: La fecha máxima para cubrir el costo total del stand " + stand.getId() + " está por vencer.");
                }
            }
        }
    }
}
package com.filey.notificaciones;

import com.filey.datos.EstadoStand;
import com.filey.datos.Stand;
import com.filey.datos.Editorial;
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

    public void procesarAlertas(List<Stand> stands) {
        LocalDateTime ahora = LocalDateTime.now();

        for (Stand stand : stands) {
            if (stand.getEditorialAsignada() == null) continue;

            if (stand.getEstado() == EstadoStand.RESERVADO && stand.getLimiteReserva() != null) {
                if (ahora.isAfter(stand.getLimiteReserva())) {
                    notificarATodos(stand.getEditorialAsignada(), 
                        "URGENTE: La reserva del stand " + stand.getId() + " ha vencido.");
                } else if (ahora.plusDays(1).isAfter(stand.getLimiteReserva())) {
                    notificarATodos(stand.getEditorialAsignada(), 
                        "ALERTA: Su reserva del stand " + stand.getId() + " vence en menos de 24 horas.");
                }
            }

            if (stand.getLimiteProntoPago() != null && stand.getEstado() != EstadoStand.DISPONIBLE) {
                if (ahora.plusDays(2).isAfter(stand.getLimiteProntoPago()) && ahora.isBefore(stand.getLimiteProntoPago())) {
                    notificarATodos(stand.getEditorialAsignada(), 
                        "RECORDATORIO: El descuento por pronto pago del stand " + stand.getId() + " vence pronto.");
                }
            }

            if ((stand.getEstado() == EstadoStand.OCUPADO || stand.getEstado() == EstadoStand.RESERVADO) 
                    && stand.getLimitePagoTotal() != null) {
                if (ahora.plusDays(3).isAfter(stand.getLimitePagoTotal()) && ahora.isBefore(stand.getLimitePagoTotal())) {
                    notificarATodos(stand.getEditorialAsignada(), 
                        "AVISO: La fecha máxima para cubrir el costo total del stand " + stand.getId() + " está por vencer.");
                }
            }
        }
    }
}
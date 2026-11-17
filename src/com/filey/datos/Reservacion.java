package com.filey.datos;

import java.time.LocalDateTime;

public class Reservacion {
    private int idReservacion;
    private Stand stand;
    private Editorial editorial;
    private double montoPagado;
    private LocalDateTime limiteReserva;
    private LocalDateTime limiteProntoPago;
    private LocalDateTime limitePagoTotal;
    private boolean activa;

    public Reservacion(int idReservacion, Stand stand, Editorial editorial, double montoPagado,
                       LocalDateTime limiteReserva, LocalDateTime limiteProntoPago,
                       LocalDateTime limitePagoTotal, boolean activa) {
        this.idReservacion = idReservacion;
        this.stand = stand;
        this.editorial = editorial;
        this.montoPagado = montoPagado;
        this.limiteReserva = limiteReserva;
        this.limiteProntoPago = limiteProntoPago;
        this.limitePagoTotal = limitePagoTotal;
        this.activa = activa;
    }

    public int getIdReservacion() { return idReservacion; }
    public void setIdReservacion(int idReservacion) { this.idReservacion = idReservacion; }
    public Stand getStand() { return stand; }
    public Editorial getEditorial() { return editorial; }
    public double getMontoPagado() { return montoPagado; }
    public void setMontoPagado(double montoPagado) { this.montoPagado = montoPagado; }
    public LocalDateTime getLimiteReserva() { return limiteReserva; }
    public void setLimiteReserva(LocalDateTime limiteReserva) { this.limiteReserva = limiteReserva; }
    public LocalDateTime getLimiteProntoPago() { return limiteProntoPago; }
    public void setLimiteProntoPago(LocalDateTime limiteProntoPago) { this.limiteProntoPago = limiteProntoPago; }
    public LocalDateTime getLimitePagoTotal() { return limitePagoTotal; }
    public void setLimitePagoTotal(LocalDateTime limitePagoTotal) { this.limitePagoTotal = limitePagoTotal; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
}
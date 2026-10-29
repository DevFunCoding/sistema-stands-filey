package com.filey.datos;

import java.time.LocalDateTime;

public class Stand {
    private String id;
    private double metrosCuadrados;
    private double valorMetroCuadrado;
    private EstadoStand estado;
    private Editorial editorialAsignada;
    private double montoPagado;

    private LocalDateTime limiteReserva;
    private LocalDateTime limiteProntoPago;
    private LocalDateTime limitePagoTotal;

    public Stand(String id, double metrosCuadrados, double valorMetroCuadrado, EstadoStand estadoInicial) {
        this.id = id;
        this.metrosCuadrados = metrosCuadrados;
        this.valorMetroCuadrado = valorMetroCuadrado;
        this.estado = estadoInicial;
        this.montoPagado = 0.0;
    }

    // Getters
    public String getId() { return id; }
    public double getMetrosCuadrados() { return metrosCuadrados; }
    public double getValorMetroCuadrado() { return valorMetroCuadrado; }
    public EstadoStand getEstado() { return estado; }
    public Editorial getEditorialAsignada() { return editorialAsignada; }
    public double getMontoPagado() { return montoPagado; }
    public LocalDateTime getLimiteReserva() { return limiteReserva; }
    public LocalDateTime getLimiteProntoPago() { return limiteProntoPago; }
    public LocalDateTime getLimitePagoTotal() { return limitePagoTotal; }

    // Setters
    public void setEstado(EstadoStand estado) { this.estado = estado; }
    public void setEditorialAsignada(Editorial editorial) { this.editorialAsignada = editorial; }
    public void setMontoPagado(double montoPagado) { this.montoPagado = montoPagado; }
    public void setLimiteReserva(LocalDateTime limiteReserva) { this.limiteReserva = limiteReserva; }
    public void setLimiteProntoPago(LocalDateTime limiteProntoPago) { this.limiteProntoPago = limiteProntoPago; }
    public void setLimitePagoTotal(LocalDateTime limitePagoTotal) { this.limitePagoTotal = limitePagoTotal; }
}
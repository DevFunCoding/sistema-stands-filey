package com.filey.datos;

public class Stand {
    private String id;
    private double metrosCuadrados;
    private double valorMetroCuadrado;
    private EstadoStand estado;

    public Stand(String id, double metrosCuadrados, double valorMetroCuadrado, EstadoStand estadoInicial) {
        this.id = id;
        this.metrosCuadrados = metrosCuadrados;
        this.valorMetroCuadrado = valorMetroCuadrado;
        this.estado = estadoInicial;
    }

    public String getId() { return id; }
    public double getMetrosCuadrados() { return metrosCuadrados; }
    public double getValorMetroCuadrado() { return valorMetroCuadrado; }
    public EstadoStand getEstado() { return estado; }
    public void setEstado(EstadoStand estado) { this.estado = estado; }
}
package com.filey.logica;

import com.filey.datos.Editorial;
import com.filey.datos.EstadoStand;
import com.filey.datos.Stand;

public class GestorOperacionesImpl implements IGestorOperaciones {
    
    private static final double PORCENTAJE_PARA_OCUPADO = 0.15;

    @Override
    public double calcularCostoTotal(Stand stand) {
        return stand.getMetrosCuadrados() * stand.getValorMetroCuadrado();
    }

    @Override
    public void registrarPago(Stand stand, double montoPago) {
        double montoActualizado = stand.getMontoPagado() + montoPago;
        stand.setMontoPagado(montoActualizado);

        double costoTotal = calcularCostoTotal(stand);
        double porcentajePagado = montoActualizado / costoTotal;

        if (stand.getEstado() == EstadoStand.RESERVADO && porcentajePagado >= PORCENTAJE_PARA_OCUPADO) {
            stand.setEstado(EstadoStand.OCUPADO);
        }
    }

    @Override
    public void asignarReservaEspecial(Stand stand, Editorial editorial) {
        if (stand.getEstado() == EstadoStand.BLOQUEADO) {
            stand.setEstado(EstadoStand.RESERVADO);
            stand.setEditorialAsignada(editorial);
        }
    }

    @Override
    public void liberarStandBloqueado(Stand stand) {
        if (stand.getEstado() == EstadoStand.BLOQUEADO) {
            stand.setEstado(EstadoStand.DISPONIBLE);
        }
    }
}
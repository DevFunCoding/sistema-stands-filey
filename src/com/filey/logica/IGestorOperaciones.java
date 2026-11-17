package com.filey.logica;

import com.filey.datos.Editorial;
import com.filey.datos.Reservacion;
import com.filey.datos.Stand;

public interface IGestorOperaciones {
    double calcularCostoTotal(Stand stand);
    boolean reservarStand(Stand stand, Editorial editorial);
    boolean registrarPago(Reservacion reservacion, double montoPago);
    boolean asignarReservaEspecial(Stand stand, Editorial editorial);
    boolean liberarStandBloqueado(Stand stand);
}
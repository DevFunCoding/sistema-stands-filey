package com.filey.logica;

import com.filey.datos.Editorial;
import com.filey.datos.Stand;

public interface IGestorOperaciones {
    double calcularCostoTotal(Stand stand);
    void registrarPago(Stand stand, double montoPago);
    
    void asignarReservaEspecial(Stand stand, Editorial editorial);
    
    void liberarStandBloqueado(Stand stand);
}
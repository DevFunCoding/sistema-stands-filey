package com.filey.persistencia;

import com.filey.datos.Reservacion;
import java.util.List;

public interface IReservacionDAO {
    boolean registrarReservacion(Reservacion reservacion);
    Reservacion buscarActivaPorStand(String idStand);
    List<Reservacion> listarActivas();
    boolean actualizarPago(int idReservacion, double nuevoMonto);
}
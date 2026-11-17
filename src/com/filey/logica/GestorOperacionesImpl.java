package com.filey.logica;

import com.filey.datos.Editorial;
import com.filey.datos.EstadoStand;
import com.filey.datos.Reservacion;
import com.filey.datos.Stand;
import com.filey.persistencia.IEditorialDAO;
import com.filey.persistencia.IReservacionDAO;
import com.filey.persistencia.IStandDAO;
import java.time.LocalDateTime;

public class GestorOperacionesImpl implements IGestorOperaciones {

    private static final double PORCENTAJE_PARA_OCUPADO = 0.15;

    private final IStandDAO standDAO;
    private final IEditorialDAO editorialDAO;
    private final IReservacionDAO reservacionDAO;

    public GestorOperacionesImpl(IStandDAO standDAO, IEditorialDAO editorialDAO, IReservacionDAO reservacionDAO) {
        this.standDAO = standDAO;
        this.editorialDAO = editorialDAO;
        this.reservacionDAO = reservacionDAO;
    }

    @Override
    public double calcularCostoTotal(Stand stand) {
        return stand.getMetrosCuadrados() * stand.getValorMetroCuadrado();
    }

    @Override
    public boolean reservarStand(Stand stand, Editorial editorial) {
        if (stand.getEstado() != EstadoStand.DISPONIBLE) {
            return false;
        }
        if (!editorialDAO.insertar(editorial)) {
            return false;
        }
        Reservacion nuevaReserva = new Reservacion(
            0,
            stand,
            editorial,
            0.0,
            LocalDateTime.now().plusHours(12),
            LocalDateTime.now().plusDays(1),
            LocalDateTime.now().plusDays(5),
            true
        );
        return reservacionDAO.registrarReservacion(nuevaReserva);
    }

    @Override
    public boolean registrarPago(Reservacion reservacion, double montoPago) {
        double montoActualizado = reservacion.getMontoPagado() + montoPago;
        if (!reservacionDAO.actualizarPago(reservacion.getIdReservacion(), montoActualizado)) {
            return false;
        }
        reservacion.setMontoPagado(montoActualizado);

        Stand stand = reservacion.getStand();
        double costoTotal = calcularCostoTotal(stand);
        double porcentajePagado = montoActualizado / costoTotal;

        if (stand.getEstado() == EstadoStand.RESERVADO && porcentajePagado >= PORCENTAJE_PARA_OCUPADO) {
            if (standDAO.actualizarEstado(stand.getId(), EstadoStand.OCUPADO)) {
                stand.setEstado(EstadoStand.OCUPADO);
            }
        }
        return true;
    }

    @Override
    public boolean asignarReservaEspecial(Stand stand, Editorial editorial) {
        if (stand.getEstado() == EstadoStand.BLOQUEADO) {
            if (!editorialDAO.insertar(editorial)) return false;
            Reservacion especial = new Reservacion(
                0, stand, editorial, 0.0,
                LocalDateTime.now().plusHours(24),
                LocalDateTime.now().plusDays(2),
                LocalDateTime.now().plusDays(7),
                true
            );
            return reservacionDAO.registrarReservacion(especial);
        }
        return false;
    }

    @Override
    public boolean liberarStandBloqueado(Stand stand) {
        if (stand.getEstado() == EstadoStand.BLOQUEADO) {
            if (standDAO.actualizarEstado(stand.getId(), EstadoStand.DISPONIBLE)) {
                stand.setEstado(EstadoStand.DISPONIBLE);
                return true;
            }
        }
        return false;
    }
}
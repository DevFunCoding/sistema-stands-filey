package com.filey.persistencia;

import com.filey.datos.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservacionDAOImpl implements IReservacionDAO {

    @Override
    public boolean registrarReservacion(Reservacion r) {
        String sqlInsert = "INSERT INTO reservaciones (id_stand, id_editorial, monto_pagado, limite_reserva, limite_pronto_pago, limite_pago_total, activa) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlUpdateStand = "UPDATE stands SET estado = ? WHERE id = ?";
        Connection conn = null;

        try {
            conn = ConexionDB.getConexion();
            conn.setAutoCommit(false);

            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                psInsert.setString(1, r.getStand().getId());
                psInsert.setString(2, r.getEditorial().getId());
                psInsert.setDouble(3, r.getMontoPagado());
                psInsert.setTimestamp(4, r.getLimiteReserva() != null ? Timestamp.valueOf(r.getLimiteReserva()) : null);
                psInsert.setTimestamp(5, r.getLimiteProntoPago() != null ? Timestamp.valueOf(r.getLimiteProntoPago()) : null);
                psInsert.setTimestamp(6, r.getLimitePagoTotal() != null ? Timestamp.valueOf(r.getLimitePagoTotal()) : null);
                psInsert.setBoolean(7, true);
                psInsert.executeUpdate();

                try (ResultSet keys = psInsert.getGeneratedKeys()) {
                    if (keys.next()) r.setIdReservacion(keys.getInt(1));
                }
            }

            try (PreparedStatement psStand = conn.prepareStatement(sqlUpdateStand)) {
                psStand.setString(1, EstadoStand.RESERVADO.name());
                psStand.setString(2, r.getStand().getId());
                psStand.executeUpdate();
            }

            conn.commit();
            r.getStand().setEstado(EstadoStand.RESERVADO);
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            System.err.println("Error transaccional al registrar reservación: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }

    @Override
    public Reservacion buscarActivaPorStand(String idStand) {
        String sql = "SELECT r.*, s.metros_cuadrados, s.valor_metro_cuadrado, s.estado, e.nombre, e.email_contacto " +
                     "FROM reservaciones r " +
                     "JOIN stands s ON r.id_stand = s.id " +
                     "JOIN editoriales e ON r.id_editorial = e.id " +
                     "WHERE r.id_stand = ? AND r.activa = TRUE";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setString(1, idStand);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapearReservacion(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar reservación activa: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Reservacion> listarActivas() {
        List<Reservacion> lista = new ArrayList<>();
        String sql = "SELECT r.*, s.metros_cuadrados, s.valor_metro_cuadrado, s.estado, e.nombre, e.email_contacto " +
                     "FROM reservaciones r " +
                     "JOIN stands s ON r.id_stand = s.id " +
                     "JOIN editoriales e ON r.id_editorial = e.id " +
                     "WHERE r.activa = TRUE";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(mapearReservacion(rs));
        } catch (SQLException e) {
            System.err.println("Error al listar reservaciones activas: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean actualizarPago(int idReservacion, double nuevoMonto) {
        String sql = "UPDATE reservaciones SET monto_pagado = ? WHERE id_reservacion = ?";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setDouble(1, nuevoMonto);
            ps.setInt(2, idReservacion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar pago: " + e.getMessage());
            return false;
        }
    }

    private Reservacion mapearReservacion(ResultSet rs) throws SQLException {
        Stand s = new Stand(
            rs.getString("id_stand"),
            rs.getDouble("metros_cuadrados"),
            rs.getDouble("valor_metro_cuadrado"),
            EstadoStand.valueOf(rs.getString("estado"))
        );
        Editorial e = new Editorial(
            rs.getString("id_editorial"),
            rs.getString("nombre"),
            rs.getString("email_contacto")
        );
        Timestamp tRes = rs.getTimestamp("limite_reserva");
        Timestamp tPronto = rs.getTimestamp("limite_pronto_pago");
        Timestamp tTotal = rs.getTimestamp("limite_pago_total");

        return new Reservacion(
            rs.getInt("id_reservacion"),
            s, e,
            rs.getDouble("monto_pagado"),
            tRes != null ? tRes.toLocalDateTime() : null,
            tPronto != null ? tPronto.toLocalDateTime() : null,
            tTotal != null ? tTotal.toLocalDateTime() : null,
            rs.getBoolean("activa")
        );
    }
}
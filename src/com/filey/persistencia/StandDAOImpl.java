package com.filey.persistencia;

import com.filey.datos.EstadoStand;
import com.filey.datos.Stand;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StandDAOImpl implements IStandDAO {

    @Override
    public List<Stand> listarTodos() {
        List<Stand> lista = new ArrayList<>();
        String sql = "SELECT id, metros_cuadrados, valor_metro_cuadrado, estado FROM stands";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Stand(
                    rs.getString("id"),
                    rs.getDouble("metros_cuadrados"),
                    rs.getDouble("valor_metro_cuadrado"),
                    EstadoStand.valueOf(rs.getString("estado"))
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar stands: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public Stand buscarPorId(String id) {
        String sql = "SELECT id, metros_cuadrados, valor_metro_cuadrado, estado FROM stands WHERE id = ?";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Stand(
                        rs.getString("id"),
                        rs.getDouble("metros_cuadrados"),
                        rs.getDouble("valor_metro_cuadrado"),
                        EstadoStand.valueOf(rs.getString("estado"))
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar stand: " + e.getMessage());
        }
        return null;
    }

    @Override
    public boolean actualizarEstado(String idStand, EstadoStand nuevoEstado) {
        String sql = "UPDATE stands SET estado = ? WHERE id = ?";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setString(1, nuevoEstado.name());
            ps.setString(2, idStand);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar estado del stand: " + e.getMessage());
            return false;
        }
    }
}
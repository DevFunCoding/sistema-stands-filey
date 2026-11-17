package com.filey.persistencia;

import com.filey.datos.Editorial;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EditorialDAOImpl implements IEditorialDAO {

    @Override
    public boolean insertar(Editorial editorial) {
        String sql = "INSERT INTO editoriales (id, nombre, email_contacto) VALUES (?, ?, ?)";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setString(1, editorial.getId());
            ps.setString(2, editorial.getNombre());
            ps.setString(3, editorial.getEmailContacto());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar editorial: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Editorial buscarPorId(String id) {
        String sql = "SELECT id, nombre, email_contacto FROM editoriales WHERE id = ?";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Editorial(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("email_contacto")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar editorial: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Editorial> listarTodas() {
        List<Editorial> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, email_contacto FROM editoriales";
        try (PreparedStatement ps = ConexionDB.getConexion().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Editorial(
                    rs.getString("id"),
                    rs.getString("nombre"),
                    rs.getString("email_contacto")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar editoriales: " + e.getMessage());
        }
        return lista;
    }
}
package com.filey.persistencia;

import com.filey.datos.EstadoStand;
import com.filey.datos.Stand;
import java.util.List;

public interface IStandDAO {
    List<Stand> listarTodos();
    Stand buscarPorId(String id);
    boolean actualizarEstado(String idStand, EstadoStand nuevoEstado);
}
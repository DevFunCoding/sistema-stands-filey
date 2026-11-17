package com.filey.persistencia;

import com.filey.datos.Editorial;
import java.util.List;

public interface IEditorialDAO {
    boolean insertar(Editorial editorial);
    Editorial buscarPorId(String id);
    List<Editorial> listarTodas();
}
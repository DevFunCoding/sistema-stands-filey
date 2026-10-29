package com.filey.notificaciones;

import com.filey.datos.Editorial;

public interface IObservadorAlerta {
    void notificar(Editorial editorial, String mensaje);
}
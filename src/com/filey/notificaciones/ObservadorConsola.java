package com.filey.notificaciones;

import com.filey.datos.Editorial;

public class ObservadorConsola implements IObservadorAlerta {
    
    @Override
    public void notificar(Editorial editorial, String mensaje) {
        System.out.println("-> [SISTEMA ENVIANDO EMAIL A: " + editorial.getEmailContacto() + "]");
        System.out.println("   Asunto/Cuerpo: " + mensaje + "\n");
    }
}
package com.filey.datos;

public class Editorial {
    private String id;
    private String nombre;
    private String emailContacto;

    public Editorial(String id, String nombre, String emailContacto) {
        this.id = id;
        this.nombre = nombre;
        this.emailContacto = emailContacto;
    }

    public String getNombre() { return nombre; }
    public String getEmailContacto() { return emailContacto; }
}
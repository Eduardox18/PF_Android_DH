package com.rockola.rsx.ws.pojos;

public class Color {
    private Integer idColor;
    private String nombre;

    public Color() {
    }

    public Color(Integer idColor, String nombre) {
        this.idColor = idColor;
        this.nombre = nombre;
    }

    public Integer getIdColor() {
        return idColor;
    }

    public void setIdColor(Integer idColor) {
        this.idColor = idColor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}

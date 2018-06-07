package com.rockola.rsx.ws.pojos;

public class Aseguradora {
    private Integer idAseguradora;
    private String nombre;

    public Aseguradora() {
    }

    public Aseguradora(Integer idAseguradora, String nombre) {
        this.idAseguradora = idAseguradora;
        this.nombre = nombre;
    }

    public Integer getIdAseguradora() {
        return idAseguradora;
    }

    public void setIdAseguradora(Integer idAseguradora) {
        this.idAseguradora = idAseguradora;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}


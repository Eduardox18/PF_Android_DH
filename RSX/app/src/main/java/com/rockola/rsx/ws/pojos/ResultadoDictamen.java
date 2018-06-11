package com.rockola.rsx.ws.pojos;

public class ResultadoDictamen {
    private Integer folio;
    private String descripcion;
    private String fechaHora;
    private String nombre;
    private String apPaterno;

    public ResultadoDictamen() {
    }

    public ResultadoDictamen(Integer folio, String descripcion, String fechaHora, String nombre, String apPaterno) {
        this.folio = folio;
        this.descripcion = descripcion;
        this.fechaHora = fechaHora;
        this.nombre = nombre;
        this.apPaterno = apPaterno;
    }

    public Integer getFolio() {
        return folio;
    }

    public void setFolio(Integer folio) {
        this.folio = folio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApPaterno() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
    }
}

package com.rockola.rsx.ws.pojos;

public class Reporte {
    private String idReporte;
    private String latitud;
    private String longitud;
    private String nombreSiniestro;
    private String apPaternoSiniestro;
    private String apMaternoSiniestro;
    private String fechaHora;
    private String idConductor;
    private String idVehiculoConductor;
    private String idVehiculoSiniestro;

    public Reporte() {
    }

    public Reporte(String latitud, String longitud, String nombreSiniestro, String apPaternoSiniestro, String apMaternoSiniestro, String idConductor, String idVehiculoConductor, String idVehiculoSiniestro) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombreSiniestro = nombreSiniestro;
        this.apPaternoSiniestro = apPaternoSiniestro;
        this.apMaternoSiniestro = apMaternoSiniestro;
        this.idConductor = idConductor;
        this.idVehiculoConductor = idVehiculoConductor;
        this.idVehiculoSiniestro = idVehiculoSiniestro;
    }

    public Reporte(String latitud, String longitud, String nombreSiniestro, String apPaternoSiniestro, String apMaternoSiniestro, String idConductor, String idVehiculoConductor) {
        this.latitud = latitud;
        this.longitud = longitud;
        this.nombreSiniestro = nombreSiniestro;
        this.apPaternoSiniestro = apPaternoSiniestro;
        this.apMaternoSiniestro = apMaternoSiniestro;
        this.idConductor = idConductor;
        this.idVehiculoConductor = idVehiculoConductor;
    }

    public String getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(String idReporte) {
        this.idReporte = idReporte;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getNombreSiniestro() {
        return nombreSiniestro;
    }

    public void setNombreSiniestro(String nombreSiniestro) {
        this.nombreSiniestro = nombreSiniestro;
    }

    public String getApPaternoSiniestro() {
        return apPaternoSiniestro;
    }

    public void setApPaternoSiniestro(String apPaternoSiniestro) {
        this.apPaternoSiniestro = apPaternoSiniestro;
    }

    public String getApMaternoSiniestro() {
        return apMaternoSiniestro;
    }

    public void setApMaternoSiniestro(String apMaternoSiniestro) {
        this.apMaternoSiniestro = apMaternoSiniestro;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getIdConductor() {
        return idConductor;
    }

    public void setIdConductor(String idConductor) {
        this.idConductor = idConductor;
    }

    public String getIdVehiculoConductor() {
        return idVehiculoConductor;
    }

    public void setIdVehiculoConductor(String idVehiculoConductor) {
        this.idVehiculoConductor = idVehiculoConductor;
    }

    public String getIdVehiculoSiniestro() {
        return idVehiculoSiniestro;
    }

    public void setIdVehiculoSiniestro(String idVehiculoSiniestro) {
        this.idVehiculoSiniestro = idVehiculoSiniestro;
    }
}
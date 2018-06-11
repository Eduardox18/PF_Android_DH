package com.rockola.rsx.ws.pojos;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResultadoReporte {
    private String idReporte;
    private String noPlaca;
    private String nombreSiniestro;
    private String apPaternoSiniestro;
    private String fechaHora;

    public ResultadoReporte() {
    }

    public ResultadoReporte(String noPlaca, String nombreSiniestro, String apPaternoSiniestro, String fechaHora) {
        this.noPlaca = noPlaca;
        this.nombreSiniestro = nombreSiniestro;
        this.apPaternoSiniestro = apPaternoSiniestro;
        this.fechaHora = fechaHora;
    }

    public String getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(String idReporte) {
        this.idReporte = idReporte;
    }

    public String getNoPlaca() {
        return noPlaca;
    }

    public void setNoPlaca(String noPlaca) {
        this.noPlaca = noPlaca;
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

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    @Override
    public String toString() {
        String fecha = getFechaHora();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        Date date = null;
        try {
            date = format.parse(fecha);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return "Placa: " + getNoPlaca() + "\nNombre: " +
        getNombreSiniestro() + " " + getApPaternoSiniestro() + "\nFecha: "
                + DateFormat.getDateInstance(DateFormat.SHORT).format(date) +
                "\nHora: " + DateFormat.getTimeInstance(DateFormat.SHORT).format(date);
    }
}

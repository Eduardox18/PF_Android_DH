package com.rockola.rsx.ws.pojos;

public class Conductor {
    private Integer idConductor;
    private String nombre;
    private String apPaterno;
    private String apMaterno;
    private String fechaNacimiento;
    private String noLicencia;
    private String telCelular;
    private String password;
    private Integer codigoVerificacion;
    private boolean status;

    public Conductor() {
    }

    public Conductor(String nombre, String apPaterno, String apMaterno,
                     String fechaNacimiento, String noLicencia, String telCelular, String password) {
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.fechaNacimiento = fechaNacimiento;
        this.noLicencia = noLicencia;
        this.telCelular = telCelular;
        this.password = password;
    }

    public Conductor(String nombre, String apPaterno, String apMaterno, String fechaNacimiento,
                     String noLicencia, String telCelular, String password, Integer codigoVerificacion,
                     boolean status) {
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.fechaNacimiento = fechaNacimiento;
        this.noLicencia = noLicencia;
        this.telCelular = telCelular;
        this.password = password;
        this.codigoVerificacion = codigoVerificacion;
        this.status = status;
    }

    public Integer getIdConductor() {
        return idConductor;
    }

    public void setIdConductor(Integer idConductor) {
        this.idConductor = idConductor;
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

    public String getApMaterno() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNoLicencia() {
        return noLicencia;
    }

    public void setNoLicencia(String noLicencia) {
        this.noLicencia = noLicencia;
    }

    public String getTelCelular() {
        return telCelular;
    }

    public void setTelCelular(String telCelular) {
        this.telCelular = telCelular;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getCodigoVerificacion() {
        return codigoVerificacion;
    }

    public void setCodigoVerificacion(Integer codigoVerificacion) {
        this.codigoVerificacion = codigoVerificacion;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

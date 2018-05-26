package com.rockola.rsx.ws.pojos;

public class Mensaje {
    private String mensaje;
    /**
     * Significado de statusMensaje:
     * 1 es que ocurrió un error
     * 0 es que no ocurrió un error
     * 250 es que el usuario existe y su contraseña es correcta
     * 251 es que los datos son incorrectos
     * 252 es que el usuario no ha activado su cuenta
     * 253 es que el usuario no existe
     */
    private Integer statusMensaje;
    private String smsCode;
    private Conductor conductor;

    public Mensaje(){}

    public Mensaje(String mensaje, Integer statusMensaje) {
        this.mensaje = mensaje;
        this.statusMensaje = statusMensaje;
    }

    public Mensaje(String mensaje, Integer statusMensaje, String smsCode) {
        this.mensaje = mensaje;
        this.statusMensaje = statusMensaje;
        this.smsCode = smsCode;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Integer getStatusMensaje() {
        return statusMensaje;
    }

    public void setStatusMensaje(Integer statusMensaje) {
        this.statusMensaje = statusMensaje;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public Conductor getConductor() {
        return conductor;
    }

    public void setUsuario(Conductor conductor) {
        this.conductor = conductor;
    }
}

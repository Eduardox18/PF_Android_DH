package com.rockola.rsx.ws.pojos;

public class Vehiculo {
    private Integer idVehiculo;
    private String noPlaca;
    private String modelo;
    private String anio;
    private String noPolizaSeguro;
    private String idMarca;
    private String idAseguradora;
    private String idColor;
    private String idConductor;

    public Vehiculo() {
    }

    public Vehiculo(String noPlaca, String modelo, String anio, String noPolizaSeguro, String idMarca, String idAseguradora, String idColor, String idConductor) {
        this.noPlaca = noPlaca;
        this.modelo = modelo;
        this.anio = anio;
        this.noPolizaSeguro = noPolizaSeguro;
        this.idMarca = idMarca;
        this.idAseguradora = idAseguradora;
        this.idColor = idColor;
        this.idConductor = idConductor;
    }

    public Vehiculo(String noPlaca, String modelo, String anio, String noPolizaSeguro, String idMarca, String idAseguradora, String idColor) {
        this.noPlaca = noPlaca;
        this.modelo = modelo;
        this.anio = anio;
        this.noPolizaSeguro = noPolizaSeguro;
        this.idMarca = idMarca;
        this.idAseguradora = idAseguradora;
        this.idColor = idColor;
    }

    public Integer getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Integer idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getNoPlaca() {
        return noPlaca;
    }

    public void setNoPlaca(String noPlaca) {
        this.noPlaca = noPlaca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getNoPolizaSeguro() {
        return noPolizaSeguro;
    }

    public void setNoPolizaSeguro(String noPolizaSeguro) {
        this.noPolizaSeguro = noPolizaSeguro;
    }

    public String getIdMarca() {
        return idMarca;
    }

    public void setIdMarca(String idMarca) {
        this.idMarca = idMarca;
    }

    public String getIdAseguradora() {
        return idAseguradora;
    }

    public void setIdAseguradora(String idAseguradora) {
        this.idAseguradora = idAseguradora;
    }

    public String getIdColor() {
        return idColor;
    }

    public void setIdColor(String idColor) {
        this.idColor = idColor;
    }

    public String getIdConductor() {
        return idConductor;
    }

    public void setIdConductor(String idConductor) {
        this.idConductor = idConductor;
    }

    @Override
    public String toString() {
        return getNoPlaca();
    }
}

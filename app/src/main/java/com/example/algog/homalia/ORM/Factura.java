package com.example.algog.homalia.ORM;

/**
 * Created by algog on 29/05/2017.
 */

public class Factura {

    // ATRIBUTOS
    private double importe;
    private String fechaCargo;
    private String id;
    private String servicio;

    // CONSTRUCTORES
    // Constructor vacio
    public Factura() {
        this.importe = 0.0;
        this.fechaCargo = "";
        this.id = id;
        this.servicio = servicio;
    }

    // Constructor con parametros
    public Factura(double importe, String fechaCargo, String id, String servicio) {
        this.importe = importe;
        this.fechaCargo = fechaCargo;
        this.id = id;
        this.servicio = servicio;
    }

    // GETTERS Y SETTERS
    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public String getFechaCargo() {
        return fechaCargo;
    }

    public void setFechaCargo(String fechaCargo) {
        this.fechaCargo = fechaCargo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }
}

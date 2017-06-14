package com.example.algog.homalia.ORM;

/**
 * Created by algog on 29/05/2017.
 */

public class Producto {

    // ATRIBUTOS
    private String id;
    private String nombre;
    private int categoria;
    private String anyadidoPor;
    private String fechaHoraCreacion;
    private boolean prioritario;

    // CONSTRUCTORES
    // Constructor vacio
    public Producto() {
        this.id = "";
        this.nombre = "";
        this.categoria = 0;
        this.anyadidoPor = "";
        this.prioritario = false;
    }

    // Costructor con parametros
    public Producto(String id, String nombre, int categoria, String anyadidoPor, String fechaHoraCreacion, boolean prioritario) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.anyadidoPor = anyadidoPor;
        this.fechaHoraCreacion = fechaHoraCreacion;
        this.prioritario = prioritario;
    }

    // GETTERS Y SETTERS
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public String getAnyadidoPor() {
        return anyadidoPor;
    }

    public void setAnyadidoPor(String anyadidoPor) {
        this.anyadidoPor = anyadidoPor;
    }

    public boolean isPrioritario() {
        return prioritario;
    }

    public void setPrioritario(boolean prioritario) {
        this.prioritario = prioritario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFechaHoraCreacion() {
        return fechaHoraCreacion;
    }

    public void setFechaHoraCreacion(String fechaHoraCreacion) {
        this.fechaHoraCreacion = fechaHoraCreacion;
    }
}

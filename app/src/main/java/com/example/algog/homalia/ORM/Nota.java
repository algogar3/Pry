package com.example.algog.homalia.ORM;

/**
 * Created by algog on 29/05/2017.
 */

public class Nota {

    // ATRIBUTOS
    private String fechaHoraCreacion;
    private String mensaje;
    private String nicknameCreador;
    private String id;

    // CONSTRUCTORES
    // Constructor vacio
    public Nota() {
        this.fechaHoraCreacion = "";
        this.mensaje = "";
        this.nicknameCreador = "";
        this.id = id;
    }

    // Constructor con parametros
    public Nota(String fechaHoraCreacion, String mensaje, String nicknameCreador, String id) {
        this.fechaHoraCreacion = fechaHoraCreacion;
        this.mensaje = mensaje;
        this.nicknameCreador = nicknameCreador;
        this.id = id;
    }

    // GETTERS Y SETTERS
    public String getFechaHoraCreacion() {
        return fechaHoraCreacion;
    }

    public void setFechaHoraCreacion(String fechaHoraCreacion) {
        this.fechaHoraCreacion = fechaHoraCreacion;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getNicknameCreador() {
        return nicknameCreador;
    }

    public void setNicknameCreador(String nicknameCreador) {
        this.nicknameCreador = nicknameCreador;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

package com.example.algog.homalia.ORM;

/**
 * Created by algog on 29/05/2017.
 */

public class Contrato {

    // ATRIBUTOS
    String titular;
    String dni;
    String numeroContrato;

    // CONSTRUCTORES
    // Constructor vacio
    public Contrato() {
        this.titular = "";
        this.dni = "";
        this.numeroContrato = "";
    }

    // Constructor con parametros
    public Contrato(String titular, String dni, String numeroContrato) {
        this.titular = titular;
        this.dni = dni;
        this.numeroContrato = numeroContrato;
    }

    // GETTERS Y SETTERS
    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNumeroContrato() {
        return numeroContrato;
    }

    public void setNumeroContrato(String numeroContrato) {
        this.numeroContrato = numeroContrato;
    }

}

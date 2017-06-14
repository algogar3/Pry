package com.example.algog.homalia.ORM;

import java.util.ArrayList;

/**
 * Created by algog on 29/05/2017.
 */

public class Tarea {
    // CONSTANTES
    public final static int ID_DIARIO = 1;
    public final static int ID_DOS_VECES_SEMANA = 2;
    public final static int ID_SEMANAL = 3;
    public final static int ID_QUINCENAL = 4;
    public final static int ID_MENSUAL = 5;

    // ATRIBUTOS
    private Integer id;
    private String nombre;
    private int frecuencia;
    private ArrayList<Companyero> companyerosElegibles;

    // CONSTRUCTORES
    // Constructor vacio
    public Tarea() {
    	this.id = allocateTaskId();
        this.nombre = "";
        this.frecuencia = 0;
        this.companyerosElegibles = new ArrayList<Companyero>();
    }

    // Constructor con parametros
    public Tarea(String nombre, int frecuencia, ArrayList<Companyero> companyerosElegibles) {
    	this.id = allocateTaskId();
        this.nombre = nombre;
        this.frecuencia = frecuencia;
        this.companyerosElegibles = companyerosElegibles;
    }

    // METODOS
    // Metodo allocateTaskId()
    private Integer allocateTaskId(){
        Integer id = null;

        /*
        ** Insertar codigo que llame a la bbdd y asigne el id libre de menor valor
         */

        return id;
    }
    
    // Metodo anyadirCompanyeroElegible(Companyero companyero)
    public ArrayList<Companyero> anyadirCompanyeroElegible(Companyero companyero){
        companyerosElegibles.add(companyero);
        return companyerosElegibles;
    }

    // Metodo eliminarCompanyeroElegible(Companyero companyero)
    public ArrayList<Companyero> eliminarCompanyeroElegible(Companyero companyero){
        companyerosElegibles.remove(companyero);
        // quita de la lista al companyero pasado como parametro. Este companyero ya no será
        // responsable de esta tarea
        return companyerosElegibles;
    }

    // GETTERS Y SETTERS


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(int frecuencia) {
        this.frecuencia = frecuencia;
    }
}

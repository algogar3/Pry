package com.example.algog.homalia.ORM;

import com.example.algog.homalia.R;

import java.util.ArrayList;

/**
 * Created by algog on 29/05/2017.
 */

public class Servicio {

    // CONSTANTES PARA LOS SERVICIOS BÁSICOS INCLUIDOS EN TODAS LAS CASAS
    public final static int ID_ELECTRICITY = 0;
    public final static int ID_WATER = 1;
    public final static int ID_GAS = 2;
    public final static int ID_PHONE_INTERNET = 3;

    public final static String KEY_ELECTRICITY = "Electricidad";
    public final static String KEY_WATER = "Agua";
    public final static String KEY_GAS = "Gas";
    public final static String KEY_PHONE_INTERNET = "Teléfono e Internet";

    // ATRIBUTOS
    private int id;
    private String nombre;
    private String imagen;
    private ArrayList<Factura> facturas;
    private Contrato contrato;

    // CONSTRUCTORES
    // Constructor vacio
    public Servicio() {
        this.id = getServiceId();
        this.nombre = "";
        this.imagen = "imagen_servicio_generico.png";
        this.facturas = new ArrayList<Factura>();
        this.contrato = new Contrato();
    }

    // Constructor con parametros
    public Servicio(int id, String nombre, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.imagen = imagen;
        this.facturas = new ArrayList<Factura>();
        //this.contrato = new Contrato(text_titular, text_dni, combo_fecha, text_permanencia); // preparar por si no estan inicializados los text
    }

    // METODOS
    // Metodo getServiceId()
    private int getServiceId(){
        int id = -1;

        /*
        ** Insertar código que llame a la bbdd y asigne el id libre de menor valor
         */

        return id;
    }

    // Metodo introducirNuevaFactura(Factura factura)
    public void introducirNuevaFactura(Factura factura){
        facturas.add(factura);
    }

    // Metodo borrarFactura(Factura factura)
    public void borrarFactura(Factura factura){
        factura.setImporte(0.0);
        // pone el importe de la factura a 0 para que la gráfica no se desfigure
        // las facturas con importe 0 no se mostrarán en la lista de facturas, solo
        // se usarán para pintar la gráfica
    }

    // GETTERS Y SETTERS
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Contrato getContrato() {
        return contrato;
    }

    public void setContrato(Contrato contrato) {
        this.contrato = contrato;
    }
}

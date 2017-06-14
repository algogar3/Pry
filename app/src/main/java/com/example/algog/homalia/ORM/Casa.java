package com.example.algog.homalia.ORM;

import java.util.ArrayList;

/**
 * Created by algog on 29/05/2017.
 */

public class Casa {

    // ATRIBUTOS
    private String nombre;
    private String password;
    private String id;
    private String imagen;
    private ArrayList<Companyero> companyeros;
    private ArrayList<Tarea> tareas;
    private ArrayList<Producto> productos;
    private ArrayList<Nota> notas;
    private ArrayList<Servicio> servicios;
    
    // CONSTRUCTORES
    // Constructor vacio
    public Casa() {
        this.nombre = "";
        this.password = "";
        this.id = "";
        this.imagen = "imagen_casa_generica.jpg";
        this.companyeros = new ArrayList<Companyero>();
        this.tareas = new ArrayList<Tarea>();
        this.productos = new ArrayList<Producto>();
        this.notas = new ArrayList<Nota>();
        this.servicios = new ArrayList<Servicio>();
    }

    // Constructor con parametros
    public Casa(String nombre, String password, String id, ArrayList<Companyero> companyeros, ArrayList<Servicio> servicios) {
        this.nombre = nombre;
        this.password = password;
        this.id = id;
        this.imagen = "imagen_casa_generica.jpg";
        this.companyeros = companyeros;
        this.tareas = new ArrayList<Tarea>();
        this.productos = new ArrayList<Producto>();
        this.notas = new ArrayList<Nota>();
        this.servicios = servicios;
    }
    
    // Metodo anyadirCompanyero(Companyero companyero)
    public ArrayList<Companyero> anyadirCompanyero(Companyero companyero){
        companyeros.add(companyero);
        return companyeros;
    }

    // Metodo eliminarCompanyero(Companyero companyero)
    public ArrayList<Companyero> eliminarCompanyero(Companyero companyero){
        companyeros.remove(companyero);
        // quita de la lista al companyero pasado como parametro.
        return companyeros;
    }
    
    // Metodo anyadirTarea(Tarea tarea)
    public ArrayList<Tarea> anyadirTarea(Tarea tarea){
        tareas.add(tarea);
        return tareas;
    }

    // Metodo eliminarTarea(Tarea tarea)
    public ArrayList<Tarea> eliminarTarea(Tarea tarea){
        tareas.remove(tarea);
        // quita de la lista la tarea pasada como parametro.
        return tareas;
    }
    
    // Metodo anyadirProducto(Producto producto)
    public ArrayList<Producto> anyadirProducto(Producto producto){
        productos.add(producto);
        return productos;
    }

    // Metodo eliminarProducto(Producto producto)
    public ArrayList<Producto> eliminarProducto(Producto producto){
        productos.remove(producto);
        // quita de la lista el producto pasado como parametro.
        return productos;
    }
    
    // Metodo anyadirNota(Nota nota)
    public ArrayList<Nota> anyadirNota(Nota nota){
        notas.add(nota);
        return notas;
    }

    // Metodo eliminarNota(Nota nota)
    public ArrayList<Nota> eliminarNota(Nota nota){
        notas.remove(nota);
        // quita de la lista la nota pasada como parametro.
        return notas;
    }
    
    // Metodo anyadirServicio(Servicio servicio)
    public ArrayList<Servicio> anyadirServicio(Servicio servicio){
        servicios.add(servicio);
        return servicios;
    }

    // Metodo eliminarServicio(Servicio servicio)
    public ArrayList<Servicio> eliminarServicio(Servicio servicio){
        servicios.remove(servicio);
        // quita de la lista al companyero pasado como parametro. Este companyero ya no será
        // responsable de esta tarea
        return servicios;
    }

    // GETTERS Y SETTERS
    public String getId() {
    	return id;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}

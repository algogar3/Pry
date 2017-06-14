package com.example.algog.homalia.ORM;

/**
 * Created by algog on 29/05/2017.
 */

public class CategoriaProducto {

    // ATRIBUTOS
    private Integer id;
    private String nombre;
    private String imagen;

    // CONSTRUCTORES
    // Constructor vacio
    public CategoriaProducto() {
        this.id = allocateProductId();
        this.nombre = "";
        this.imagen = "imagen_producto_generico.png";
    }

    // Costructor con parametros
    public CategoriaProducto(int id, String nombre, String imagen) {
        this.id = allocateProductId();
        this.nombre = nombre;
        this.imagen = imagen;
    }

    // METODOS
    private Integer allocateProductId(){
        Integer id = null;

        /*
        ** Insertar codigo que llame a la bbdd y asigne el id libre de menor valor
         */

        return id;
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
}

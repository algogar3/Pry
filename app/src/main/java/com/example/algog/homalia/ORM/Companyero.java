package com.example.algog.homalia.ORM;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by algog on 29/05/2017.
 */

public class Companyero implements Parcelable {

    // ATRIBUTOS
    private String id;
    private String nombre;
    private String apellidos;
    private String nick;
    private String fechaNacimiento;
    private String telefono;
    private String imagen;
    private String descripcion;
    private ArrayList<Integer> idTareas;
    private double gasto;
    private String idCasa;
    
    // CONSTRUCTORES
    // Constructor vacio
    public Companyero(){
        this.id = "";
        this.nombre = "";
        this.apellidos = "";
        nick = "";
        this.fechaNacimiento = "";
        this.telefono = "";
        this.imagen = "imagen_companyero_generico.png";
        this.descripcion = "";
        this.idTareas = new ArrayList<Integer>();
        this.gasto = 0.0;
        this.idCasa = "";
    }

    // Constructor con parametros
    public Companyero(String id, String nombre, String apellidos, String nick, String fechaNacimiento, String telefono, String imagen, String descripcion){
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.nick = nick;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.idTareas = new ArrayList<Integer>();
        this.gasto = 0.0;
        this.idCasa = "";
    }

    // METODOS
    // Metodo asignarTarea(Tarea tarea)
    public ArrayList<Integer> asignarTarea(Tarea tarea){
    	idTareas.add(tarea.getId());
    	return idTareas;
    }

    // Metodo desasignarTarea(Tarea tarea)
    public ArrayList<Integer> desasignarTarea(Tarea tarea){
    	idTareas.remove(tarea.getId());
        // elimina la tarea de la lista de tareas del companyero porque este deja de ser elegible para dicha tarea
    	return idTareas;
    }
    
    // Metodo prepararSalidaCompanyero()
    public Companyero prepararSalidaCompanyero(Companyero companyero){
    	companyero.idTareas.clear();
    	companyero.gasto = 0.0;
    	companyero.idCasa = null;
    	
    	return companyero;
    }

    // GETTERS Y SETTERS
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ArrayList<Integer> getIdTareas() {
        return idTareas;
    }

    public void setIdTareas(ArrayList<Integer> idTareas) {
        this.idTareas = idTareas;
    }

    public double getGasto() {
        return gasto;
    }

    public void setGasto(double gasto) {
        this.gasto = gasto;
    }

    public String getIdCasa() {
        return idCasa;
    }

    public void setIdCasa(String idCasa) {
        this.idCasa = idCasa;
    }

    //Este método pertenece a la interfaz Parceable (Ésta nos permite pasar el objeto en un Bundle)
    public Companyero(Parcel in) {
        this();
        nombre = in.readString();
        apellidos = in.readString();
        fechaNacimiento = in.readString();
        nick = in.readString();
        telefono = in.readString();
        imagen = in.readString();
        descripcion = in.readString();
        gasto = in.readDouble();
        idCasa = in.readString();
        ArrayList<Integer> listaTareas = new ArrayList<Integer>();
        //idTareas = in.readTypedList(listaTareas, );
    }

    //Este método pertenece a la interfaz Parceable (Ésta nos permite pasar el objeto en un Bundle)
    public static final Creator<Companyero> CREATOR = new Creator<Companyero>() {
        @Override
        public Companyero createFromParcel(Parcel in) {
            return new Companyero(in);
        }

        @Override
        public Companyero[] newArray(int size) {
            return new Companyero[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nombre);
        parcel.writeString(apellidos);
        parcel.writeString(fechaNacimiento);
        parcel.writeString(nick);
        parcel.writeString(telefono);
        parcel.writeString(imagen);
        parcel.writeString(descripcion);
        parcel.writeString(idCasa);
        parcel.writeDouble(gasto);
        //parcel.writeList(idTareas);
    }
}

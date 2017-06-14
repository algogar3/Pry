package com.example.algog.homalia.API;

import android.content.Context;
import android.widget.EditText;

import com.example.algog.homalia.ORM.Companyero;
import com.example.algog.homalia.ORM.Servicio;
import com.example.algog.homalia.R;
import com.example.algog.homalia.act.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by algog on 02/06/2017.
 */

public class Api {

    // Método editTextIsEmpty
    /*
    * Devuelve true si el widget de tipo EditText pasado como parámertro
    * no tiene texto introducido. En caso contrario devuelve false
    * */
    public static boolean editTextIsEmpty(EditText editText){
        return (editText.getText().toString().isEmpty())? true:false;
    }

    // Método validPassword
    /*
    * Devuelve true si la constraseña introducida cumple con las normas establecidas.
    * En caso contrario devuelve false
    * */
    public static boolean validPassword(EditText pass){
        return (pass.getText().toString().length()>5)? true:false;
    }

    // Método validEmail
    /*
    * Devuelve true si la dirección de correo introducida es válida. En caso contrario
    * devuelve false
    * */
    public static boolean validEmail(EditText email){
        return (email.getText().toString().contains("@") && email.getText().toString().contains("."))? true:false;
    }

    // Método passwordsMatch
    /*
    * Devuelve true si la contraseña introducida por el usuario coincide en ambos EditText. De esta manera
    * se evita que el usuario introduzca una contraseña diferente a la que cree por error. Devuelve false
    * si las contraseñas no coinciden
    * */
    public static boolean passwordsMatch(EditText pass1, EditText pass2){
        return (pass1.getText().toString().equals(pass2.getText().toString()))? true:false;
    }

    // Método completarrCompanyero
    /*
    * Comprueba que campos no obligatorios ha rellenado el usuario al registrarse en la aplicación.
    * Asigna al objeto companyero los campos que el usuario haya rellenado, en caso contrario asigna
    * una cadena vacía, puesto que todos los campos opcionales son de tipo String
    * */
    public static Companyero completarCompanyero(Companyero companyeroTemp){

        // Nombre
        try{
            companyeroTemp.getNombre();
        } catch (Exception e){
            companyeroTemp.setNombre("");
        }

        // Apellidos
        try{
            companyeroTemp.getApellidos();
        } catch (Exception e){
            companyeroTemp.setApellidos("");
        }

        // Teléfono
        try{
            companyeroTemp.getTelefono();
        } catch (Exception e){
            companyeroTemp.setTelefono("");
        }

        // Descripción
        try{
            companyeroTemp.getDescripcion();
        } catch (Exception e){
            companyeroTemp.setDescripcion("");
        }

        // Imgaen de perfil
        try{
            companyeroTemp.getImagen();
        } catch (Exception e){
            companyeroTemp.setImagen("");
        }

        return companyeroTemp;
    }

    // Método crearServiciosBasicos
    /*
    * CGenera una lista con los servicios básicos que se incluyen cuando se crea una nueva casa
    * */
    public static ArrayList<Servicio> crearServiciosBasicos(){

        ArrayList<Servicio> serviciosBasicos = new ArrayList<Servicio>();

        // Se crea un objeto de tipo Servicio para ir asignándole un servicio básico
        Servicio servicioElecticidad = new Servicio(Servicio.ID_ELECTRICITY, Servicio.KEY_ELECTRICITY, "imagen_generica_elecrticidad.jpg");
        Servicio servicioAgua = new Servicio(Servicio.ID_WATER, Servicio.KEY_WATER, "imagen_generica_agua.jpg");
        Servicio servicioGas = new Servicio(Servicio.ID_GAS, Servicio.KEY_GAS, "imagen_generica_gas.jpg");
        Servicio servicioTelefonoInternet = new Servicio(Servicio.ID_PHONE_INTERNET, Servicio.KEY_PHONE_INTERNET, "imagen_generica_telefono_internet.jpg");

        // Posteriormente se añade a la lista que devuelve el método
        serviciosBasicos.add(servicioElecticidad);
        serviciosBasicos.add(servicioAgua);
        serviciosBasicos.add(servicioGas);
        serviciosBasicos.add(servicioTelefonoInternet);

        return serviciosBasicos;
    }

    // Método getCategoriasProducto
    /*
    * Genera una lista con los nombres de las categorías de los productos
    * */
    public static ArrayList<String> getCategoriasProducto(Context context){

        ArrayList<String> categoriasProducto = new ArrayList<String>();

        categoriasProducto.add(context.getString(R.string.product_cat_1));
        categoriasProducto.add(context.getString(R.string.product_cat_2));
        categoriasProducto.add(context.getString(R.string.product_cat_3));

        return categoriasProducto;
    }

}

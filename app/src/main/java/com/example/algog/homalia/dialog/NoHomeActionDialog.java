package com.example.algog.homalia.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.algog.homalia.R;


/**
 * Created by algog on 05/06/2017.
 */

public class NoHomeActionDialog extends DialogFragment implements DialogInterface.OnClickListener{
    // Variables
    private OnRealizarAccion escuchador;

    // Constructor artificial
    public static NoHomeActionDialog newInstance(){
        NoHomeActionDialog dialogo = new NoHomeActionDialog();
        return dialogo;
    }

    // Ciclo de vida del dialogo

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Se construye el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.no_home_action_dialog_title))
                .setMessage(getString(R.string.no_home_action_dialog_message))
                .setPositiveButton(getString(R.string.no_home_action_dialog_positive), this)
                .setNegativeButton(getString(R.string.no_home_action_dialog_negative), this)
                .setNeutralButton(getString(R.string.btn_cancel),this)
                .setCancelable(false);
        // Se devuelve el diálogo
        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    // Desarrollo de los métodos de la interfaz DialogInterface.OnClickListener
    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if(i == DialogInterface.BUTTON_POSITIVE){
            // Se ha seleccionado crear una nueva casa
            // Se llama al método de nuestra interfaz para que haga sus cosas
            escuchador.newHome();
            dismiss();
        }
        else if(i == DialogInterface.BUTTON_NEGATIVE){
            // Se ha seleccionado unirse a una casa ya existente
            // Se llama al método de nuestra interfaz para que haga sus cosas
            escuchador.joinHome();
            dismiss();
        }
        else if(i == DialogInterface.BUTTON_NEUTRAL){
            // Se ha cancelado la operación
            escuchador.cancel();
            dismiss();
        }
    }

    // Declaración de nuestra interfaz
    public interface OnRealizarAccion{
        void newHome();
        void joinHome();
        void cancel();
    }

    // Método para que nuestra actividad se pueda suscribir a la interfaz
    public void setOnRealizarAccionListener(OnRealizarAccion escuchador){
        this.escuchador = escuchador;
    }
}

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
 * Created by algog on 10/06/2017.
 */

public class ConfirmDeleteInvoiceDialog extends DialogFragment implements DialogInterface.OnClickListener{
    // Constantes
    public static final int KEY_CANCEL_DELETE = 0;
    public static final int KEY_CONFIRM_DELETE = 1;
    public static final String KEY_POSITION = "key_position";

    // Variables
    private ConfirmDeleteInvoiceDialog.OnRealizarAccion escuchador;
    private int position;

    // Constructor artificial
    public static ConfirmDeleteInvoiceDialog newInstance(int position){
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        ConfirmDeleteInvoiceDialog dialogo = new ConfirmDeleteInvoiceDialog();
        dialogo.setArguments(args);
        return dialogo;
    }

    // Ciclo de vida del dialogo

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Recupero la factura
        position = getArguments().getInt(KEY_POSITION);

        // Se construye el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.confirm_delete_invoice_dialog_title))
                .setPositiveButton(getString(R.string.confirm_delete_note_dialog_positive), this)
                .setNegativeButton(getString(R.string.confirm_delete_note_dialog_negative), this)
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

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if(i == DialogInterface.BUTTON_POSITIVE){
            // Se ha seleccionado eliminar la factura
            // Se llama al método de nuestra interfaz para que realizce la acción
            escuchador.action(KEY_CONFIRM_DELETE, position);
            dismiss();
        }
        else if(i == DialogInterface.BUTTON_NEGATIVE){
            // Se ha seleccionado no eliminar la factura
            // Se llama al método de nuestra interfaz para que realizce la acción
            escuchador.action(KEY_CANCEL_DELETE, position);
            dismiss();
        }

    }

    // Declaración de nuestra interfaz
    public interface OnRealizarAccion{
        void action(int action, int position);
    }

    // Método para que nuestra actividad se pueda suscribir a la interfaz
    public void setOnRealizarAccionListener(ConfirmDeleteInvoiceDialog.OnRealizarAccion escuchador){
        this.escuchador = escuchador;
    }
}

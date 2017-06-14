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
import com.example.algog.homalia.act.MainActivity;

/**
 * Created by algog on 09/06/2017.
 */

public class LeaveHouseDialog extends DialogFragment  {
    // Constantes
    public static final int KEY_ACCION_PERMANECER_CASA = 0;
    public static final int KEY_ACCION_ABANCONAR_CASA = 1;

    // Variables
    private LeaveHouseDialog.OnRealizarAccion escuchador;
    private String houseId;
    private String userId;

    // Constructor artificial
    public static LeaveHouseDialog newInstance(String houseId, String userId){
        Bundle args = new Bundle();
        args.putString(MainActivity.KEY_HOME_ID, houseId);
        args.putString(MainActivity.KEY_USER_UID, userId);
        LeaveHouseDialog dialogo = new LeaveHouseDialog();
        dialogo.setArguments(args);
        return dialogo;
    }

    // Ciclo de vida del dialogo

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Recupero los datos
        houseId = getArguments().getString(MainActivity.KEY_HOME_ID);
        userId = getArguments().getString(MainActivity.KEY_USER_UID);

        // Se construye el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.leave_house))
                .setMessage(R.string.leave_house_text)
                .setPositiveButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        escuchador.action(KEY_ACCION_ABANCONAR_CASA);
                    }
                })
                .setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        escuchador.action(KEY_ACCION_PERMANECER_CASA);
                    }
                })
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

    // Declaración de nuestra interfaz
    public interface OnRealizarAccion{
        void action(int opcion);
    }

    // Método para que nuestra actividad se pueda suscribir a la interfaz
    public void setOnRealizarAccionListener(LeaveHouseDialog.OnRealizarAccion escuchador){
        this.escuchador = escuchador;
    }
}

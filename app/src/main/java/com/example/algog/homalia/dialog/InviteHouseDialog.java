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

public class InviteHouseDialog extends DialogFragment implements DialogInterface.OnClickListener {
    // Variables
    private String houseId;
    private String password;

    // Constructor artificial
    public static InviteHouseDialog newInstance(String houseId, String password){
        Bundle args = new Bundle();
        args.putString(MainActivity.KEY_HOME_ID, houseId);
        args.putString(MainActivity.KEY_PASSWORD, password);
        InviteHouseDialog dialogo = new InviteHouseDialog();
        dialogo.setArguments(args);

        return dialogo;
    }

    // Ciclo de vida del dialogo

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Recupero los valores
        houseId = getArguments().getString(MainActivity.KEY_HOME_ID);
        password = getArguments().getString(MainActivity.KEY_PASSWORD);

        // Se construye el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.share_house))
                .setMessage(getString(R.string.share_house_text) + "\n\n" +
                getString(R.string.home_id) + ": " + houseId + "\n" +
                getText(R.string.password) + ": " + password)
                .setPositiveButton(getString(R.string.btn_ok), this)
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
        dismiss();
    }
}

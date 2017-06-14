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
 * Created by algog on 09/06/2017.
 */

public class AboutUsDialog extends DialogFragment implements DialogInterface.OnClickListener{
    // Constructor artificial
    public static AboutUsDialog newInstance(){
        AboutUsDialog dialogo = new AboutUsDialog();
        return dialogo;
    }

    // Ciclo de vida del dialogo

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Se construye el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.about))
                .setMessage(R.string.about_description)
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

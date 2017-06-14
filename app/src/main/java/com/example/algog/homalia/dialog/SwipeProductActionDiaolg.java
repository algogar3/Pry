package com.example.algog.homalia.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.example.algog.homalia.API.Api;
import com.example.algog.homalia.R;
import com.example.algog.homalia.act.ShoppingListActivity;

public class SwipeProductActionDiaolg extends DialogFragment implements TextWatcher, DialogInterface.OnShowListener,
        RadioGroup.OnCheckedChangeListener{
    // Constantes
    public final static int KEY_CANCELED = 0;
    public final static int KEY_DELETE = 1;
    public final static int KEY_BUY = 2;

    // Variables
    private RadioGroup radioGroup;
    private RadioButton radioButtonDelete;
    private RadioButton radioButtonBuy;
    private ImageView imageProductCost;
    private EditText productCost;
    private Button positiveButton;
    private AlertDialog dialog;
    private int position;

    private OnOptionSelected escuchador;

    //Como el constructor debe ser vacío obligatoriamente, este método
    //estático nos permite pasarle parámetros al fragmento
    public static SwipeProductActionDiaolg newInstance(int position) {
        Bundle args = new Bundle();
        //Añado la posición del producto al bundle que recibirá onCreateDialog()
        args.putInt(ShoppingListActivity.KEY_POSITION, position);
        SwipeProductActionDiaolg fragment = new SwipeProductActionDiaolg();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //Recupero el companyero
        position = getArguments().getInt(ShoppingListActivity.KEY_POSITION);

        //Construyo y muestro el diálogo
        //Primero genero un constructor de diálogos de alerta
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Seteo el titulo con el nombre del alumno
        builder.setTitle(R.string.product_swipe_options);

        //Inflo el layout del dialogo
        View layoutDialogo = getActivity().getLayoutInflater().inflate(R.layout.activity_swipe_product_action_dialog, null);

        //Obtengo los widgets que necesito consultar
        radioGroup = (RadioGroup) layoutDialogo.findViewById(R.id.radio_group);
        radioButtonDelete = (RadioButton) layoutDialogo.findViewById(R.id.radioButtonDelete);
        radioButtonBuy = (RadioButton) layoutDialogo.findViewById(R.id.radioButtonBuy);
        imageProductCost = (ImageView) layoutDialogo.findViewById(R.id.image_product_cost);
        productCost = (EditText)layoutDialogo.findViewById(R.id.product_cost);

        // Escuchadores
        productCost.addTextChangedListener(this);
        radioGroup.setOnCheckedChangeListener(this);

        // Inserto la imagen en el ViewImage haciendo uso de la librería Glide
        Glide.with(this)
                .load(R.drawable.ic_euro_symbol_black_24dp)
                .override(120, 120)
                .into(imageProductCost);


        //Seteo el layout en el diálogo
        builder.setView(layoutDialogo);

        // Botón aceptar
        builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Se ve que opción ha seleccionado el usuario
                switch(radioGroup.getCheckedRadioButtonId()){
                    case R.id.radioButtonDelete:{
                        // OPCIÓN BORRAR SELECCIONADA
                        // Se introduce el parámetro correspondiente en el método de la interfaz
                        escuchador.onOptionSelected(KEY_DELETE, position, 0);
                    };break;

                    case R.id.radioButtonBuy:{
                        // OPCIÓN COMPRAR SELECCIONADA
                        // Se introduce el parámetro correspondiente en el método de la interfaz
                        escuchador.onOptionSelected(KEY_BUY, position, Double.valueOf(productCost.getText().toString()));
                    };break;
                }

            }
        });

        // Botón cancelar
        builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Se introduce el parámetro correspondiente en el método de la interfaz
                escuchador.onOptionSelected(KEY_CANCELED, position, 0);
            }
        });

        // Se crea el diálogo
        dialog = builder.create();

        // Escuchador para detectar cuando se ha mostrado el diálogo y así poder recoger
        // los objetos de sus botones
        dialog.setOnShowListener(this);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        setCancelable(false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //Método que permite suscribirse a las actividades a nuestro evento
    public void setOnOptionSelectedListener (OnOptionSelected listener){
        this.escuchador = listener;
    }

    // INICIO INTERFAZ TEXTWATCHER
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Vacío
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(Api.editTextIsEmpty(productCost) && radioGroup.getCheckedRadioButtonId() == R.id.radioButtonBuy){
            // Se desactiva el botón aceptar cuando cambia el texto
            positiveButton.setEnabled(false);
        } else {
            positiveButton.setEnabled(true);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // Vacío
    }
    // FIN INTERFAZ TEXT WATCHER

    // INICIO INTERFAZ ONSHOWLISTENER
    @Override
    public void onShow(DialogInterface dialogInterface) {
        // Una vez mostrado el diálogo puedo recoger el objeto possitivebutton
        positiveButton = dialog
                .getButton(AlertDialog.BUTTON_POSITIVE);

        // Se fija a que por defecto aparezca como seleccionada la opción de borrar producto
        radioGroup.check(R.id.radioButtonDelete);
    }
    // FIN INTERFAZ ONSHOWLISTENER

    // INICIO INTERFAZ ONCHECKEDCHANGED
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        // Se ve que opción ha seleccionado el usuario
        switch(radioGroup.getCheckedRadioButtonId()) {
            case R.id.radioButtonDelete: {
                // OPCIÓN BORRAR SELECCIONADA
                // Actualización de la UI
                imageProductCost.setVisibility(View.GONE);
                productCost.setVisibility(View.GONE);
                positiveButton.setEnabled(true);
            };break;

            case R.id.radioButtonBuy: {
                // OPCIÓN COMPRAR SELECCIONADA
                // Actualización de la UI
                imageProductCost.setVisibility(View.VISIBLE);
                productCost.setVisibility(View.VISIBLE);
                if(Api.editTextIsEmpty(productCost)){
                    positiveButton.setEnabled(false);
                } else{
                    positiveButton.setEnabled(true);
                }
            };break;
        }
    }
    // FIN INTERFAZ ONCHECKEDCHANGED

    //Construyo una interface que defina la callback OnOptionSelected()
    public interface OnOptionSelected{
        void onOptionSelected (int option, int position, double cost);
    }
}

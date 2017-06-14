package com.example.algog.homalia.act;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.algog.homalia.ORM.Contrato;
import com.example.algog.homalia.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ContractInformationActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    // Variables
    private ActionBar actionBar;
    private ImageView serviceImage;
    private Button btnEditName;
    private Button btnEditId;
    private Button btnEditContractId;
    private Button btnOk;
    private Button btnCancel;
    private EditText name;
    private EditText id;
    private EditText contractId;

    private Intent intent;
    private String service;
    private String homeId;
    private boolean textChaged;

    private DatabaseReference mDatabase;
    private DatabaseReference contratoReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_information);

        // Modificación de la action bar
        actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.contract_info);

        // Se recogen los datos
        intent = getIntent();
        service = intent.getExtras().getString(ExpensesActivity.KEY_SERVICE);
        homeId = intent.getExtras().getString(MainActivity.KEY_HOME_ID);

        // Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        contratoReference = mDatabase.child("casas").child(homeId).child("servicios").child(service);

        // Inflado de los widgets
        serviceImage = (ImageView) findViewById(R.id.service_image);
        btnEditName = (Button) findViewById(R.id.btn_edit_name);
        btnEditId = (Button) findViewById(R.id.btn_edit_id);
        btnEditContractId = (Button) findViewById(R.id.btn_edit_contract_id);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        name = (EditText) findViewById(R.id.name_text);
        id = (EditText) findViewById(R.id.id_text);
        contractId = (EditText) findViewById(R.id.contract_text);

        // Escuchadores
        btnEditName.setOnClickListener(this);
        btnEditId.setOnClickListener(this);
        btnEditContractId.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        name.addTextChangedListener(this);
        id.addTextChangedListener(this);
        contractId.addTextChangedListener(this);

        // Estado inicial de la UI
        name.setEnabled(false);
        id.setEnabled(false);
        contractId.setEnabled(false);
        textChaged = false;

        if(service.equals(ExpensesActivity.KEY_ELECTRICITY)){
            Glide.with(this)
                    .load(R.drawable.electricity_service_2)
                    .override(400, 400)
                    .into(serviceImage);
        }
        else if(service.equals(ExpensesActivity.KEY_WATER)){
            Glide.with(this)
                    .load(R.drawable.water_service_2)
                    .override(400, 400)
                    .into(serviceImage);
        }
        else if(service.equals(ExpensesActivity.KEY_GAS)){
            Glide.with(this)
                    .load(R.drawable.gas_service_2)
                    .override(400, 400)
                    .into(serviceImage);
        }
        else if(service.equals(ExpensesActivity.KEY_PHONE_AND_INTERNET)){
            Glide.with(this)
                    .load(R.drawable.phone_service_2)
                    .override(400, 400)
                    .into(serviceImage);
        }

        // Se debe comprobar si existe un contrato almacenado en base de datos para el servicio actual
        // Si es así, se recuperan sus datos y se muestran
        contratoReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("contrato")){
                    // Existe un contrato en base de datos, se recupera la información
                    name.setText(String.valueOf(dataSnapshot.child("contrato").child("titular").getValue()));
                    id.setText(String.valueOf(dataSnapshot.child("contrato").child("dni").getValue()));
                    contractId.setText(String.valueOf(dataSnapshot.child("contrato").child("numeroContrato").getValue()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_edit_name:{
                if(name.isEnabled()){
                    // Cambios en la UI
                    btnEditName.setText(R.string.edit);
                    name.setEnabled(false);
                } else{
                    btnEditName.setText(R.string.btn_ok);
                    name.setEnabled(true);
                }
            }; break;

            case R.id.btn_edit_id:{
                if(id.isEnabled()){
                    // Cambios en la UI
                    btnEditId.setText(R.string.edit);
                    id.setEnabled(false);
                } else{
                    btnEditId.setText(R.string.btn_ok);
                    id.setEnabled(true);
                }
            }; break;

            case R.id.btn_edit_contract_id:{
                if(contractId.isEnabled()){
                    // Cambios en la UI
                    btnEditContractId.setText(R.string.edit);
                    contractId.setEnabled(false);
                } else{
                    btnEditContractId.setText(R.string.btn_ok);
                    contractId.setEnabled(true);
                }
            }; break;

            case R.id.btn_cancel:{
                // Se devuelve el control a la actividad llamante
                setResult(RESULT_CANCELED);
                finish();
            }

            case R.id.btn_ok:{
                if(textChaged){
                    // Ha habido cambios en el texto
                    // Se comprueba que se han aceptado los cambios antes de modificar la base de datos
                    if(name.isEnabled() || id.isEnabled() || contractId.isEnabled()){
                        // Hay cambios sin guardar
                        Toast.makeText(this, R.string.accept_changes, Toast.LENGTH_SHORT).show();
                    } else{
                        // No hay cambios sin guardar
                        // Se debe guardar en base de datos la información del contrato
                        Contrato contrato = new Contrato();
                        contrato.setTitular(name.getText().toString());
                        contrato.setDni(id.getText().toString());
                        contrato.setNumeroContrato(contractId.getText().toString());

                        // Se introduce la información del contrato en la base de datos de Firebase
                        Map<String, Object> updatecontract = new HashMap<>();
                        updatecontract.put("/casas/" + homeId + "/servicios/" + service + "/contrato", contrato);
                        mDatabase.updateChildren(updatecontract);

                        // Se devuelve el control a la actividad llamante
                        setResult(RESULT_OK);
                        finish();
                    }
                } else{
                    // Sin cambios, se devuelve el control a la actividad llamante
                    setResult(RESULT_CANCELED);
                    finish();
                }
            }
        }
    }

    // INICIO IMPLEMENTACIÓN DE LOS MÉTODOS DE LA INTERFAZ TEXTWATCHER
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Vacío
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // Se cambia la variable de control de texto cambiado
        textChaged = true;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // Vacío
    }
    // FIN IMPLEMENTACIÓN DE LOS MÉTODOS DE LA INTERFAZ TEXTWATCHER
}

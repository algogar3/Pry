package com.example.algog.homalia.act;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.algog.homalia.API.Api;
import com.example.algog.homalia.ORM.Factura;
import com.example.algog.homalia.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateInvoiceActivity extends AppCompatActivity implements View.OnClickListener {
    // Variables
    private EditText cost;
    private DatePicker datePicker;
    private ImageView serviceImage;
    private Button btn_ok;
    private Button btn_cancel;
    private Intent intent;
    private String homeId;
    private String service;
    private String fechaCargo;
    private Factura factura;
    private Calendar calendar;
    private ActionBar actionBar;

    private DatabaseReference mDatabase;
    private String key;
    private double importe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invoice);

        // Modificación de la action bar
        actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.new_invoice);

        // Se obtiene la información pasada en el intent
        intent = this.getIntent();
        homeId = intent.getExtras().getString(MainActivity.KEY_HOME_ID);
        service = intent.getExtras().getString(ExpensesActivity.KEY_SERVICE);

        // Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        key = mDatabase.child("casas").child(homeId).child("servicios").child(service).child("facturas").push().getKey();

        // Inflado de los widgets
        serviceImage = (ImageView) findViewById(R.id.image_service);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        cost = (EditText) findViewById(R.id.cost);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        // Escuchadores
        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        // Se muestra la imagen del servicio recibido
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

        // Se fija la fecha igual a la actual
        calendar = Calendar.getInstance();
        datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.setMaxDate(new Date().getTime());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_ok: {
                // BOTÓN ACEPTAR
                // Se comprueba que el valor del campo importe de la factura no se encuentra vacío
                if(Api.editTextIsEmpty(cost)){
                    // NO SE HA INTRODUCIDO IMPORTE DE LA FACTURA
                    Toast.makeText(this, R.string.content_amount_empty, Toast.LENGTH_SHORT).show();
                } else{
                    // SE HA INTRODUCIDO IMPORTE DE FACTURA
                    // Se recogen los atributos necesarios para almacenar una nueva factura en la base de datos
                    fechaCargo =  datePicker.getYear() + "/" + (datePicker.getMonth()+1) +"/" + datePicker.getDayOfMonth();
                    importe = Double.valueOf(cost.getText().toString());

                    // Se genera la nueva factura
                    factura = new Factura(importe, fechaCargo, key, service);

                    // Se introduce la nueva factura en la base de datos de Firebase
                    Map<String, Object> updateFacturas = new HashMap<>();
                    updateFacturas.put("/casas/" + homeId + "/servicios/" + service + "/facturas/" + key, factura);
                    mDatabase.updateChildren(updateFacturas);

                    // Se devuelve el control a NotesActivity
                    setResult(RESULT_OK);
                    finish();
                }
            }; break;

            case R.id.btn_cancel: {
                // SE PRESIONA EL BOTÓN CANCELAR
                // Se devuelve el control a NotesActivity
                setResult(RESULT_CANCELED);
                finish();
            }; break;
        }

    }
}

package com.example.algog.homalia.act;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import com.example.algog.homalia.R;

public class ExpensesActivity extends AppCompatActivity implements View.OnClickListener {
    // Constantes
    public static final String KEY_ELECTRICITY = "Electricidad";
    public static final String KEY_GAS = "Gas";
    public static final String KEY_PHONE_AND_INTERNET = "Teléfono e Internet";
    public static final String KEY_WATER = "Agua";
    public static final String KEY_SERVICE = "key_service";

    // Variables
    private ActionBar actionBar;
    private ImageView electricityService;
    private ImageView waterService;
    private ImageView gasService;
    private ImageView phoneService;

    private Intent intent;
    private String homeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        // Modificación de la action bar
        actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.btn_expenses);

        // Se recogen los datos enviados desde la actividad llamante
        intent = getIntent();
        homeId = intent.getExtras().getString(MainActivity.KEY_HOME_ID);

        // Inflado de los widgets
        electricityService = (ImageView) findViewById(R.id.electricity_image);
        waterService = (ImageView) findViewById(R.id.water_image);
        phoneService = (ImageView) findViewById(R.id.phone_image);
        gasService = (ImageView) findViewById(R.id.gas_image);



        // Escuchadores
        electricityService.setOnClickListener(this);
        waterService.setOnClickListener(this);
        phoneService.setOnClickListener(this);
        gasService.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.electricity_image:{
                // Electricidad
                intent = new Intent(this, IndividualServiceActivity.class);
                intent.putExtra(MainActivity.KEY_HOME_ID, homeId);
                intent.putExtra(KEY_SERVICE, KEY_ELECTRICITY);
                startActivity(intent);
            };break;

            case R.id.water_image:{
                // Agua
                intent = new Intent(this, IndividualServiceActivity.class);
                intent.putExtra(MainActivity.KEY_HOME_ID, homeId);
                intent.putExtra(KEY_SERVICE, KEY_WATER);
                startActivity(intent);
            };break;

            case R.id.gas_image:{
                // Gas
                intent = new Intent(this, IndividualServiceActivity.class);
                intent.putExtra(MainActivity.KEY_HOME_ID, homeId);
                intent.putExtra(KEY_SERVICE, KEY_GAS);
                startActivity(intent);
            };break;

            case R.id.phone_image:{
                // Teléfono
                intent = new Intent(this, IndividualServiceActivity.class);
                intent.putExtra(MainActivity.KEY_HOME_ID, homeId);
                intent.putExtra(KEY_SERVICE, KEY_PHONE_AND_INTERNET);
                startActivity(intent);
            };break;
        }

    }

}

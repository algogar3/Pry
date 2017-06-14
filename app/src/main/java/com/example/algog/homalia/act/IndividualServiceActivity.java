package com.example.algog.homalia.act;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.algog.homalia.ORM.Factura;
import com.example.algog.homalia.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class IndividualServiceActivity extends AppCompatActivity implements View.OnClickListener {
    // Constantes
    public static final int KEY_CONTRACT_INFO = 0;
    public static final int KEY_SEE_INVOICES = 1;

    // Variables
    private ActionBar actionBar;
    private Intent intent;
    private Button btnContractInfo;
    private Button btnInvoices;

    private String homeId;
    private String service;

    private RelativeLayout graphLayout;
    private DatabaseReference mDatabase;
    private DatabaseReference facturaReference;
    private ArrayList<Factura> listaFacturas;
    private List<PointValue> values;
    private List<AxisValue> axisValues;
    private AxisValue axisValue;
    private List<Line> lines;
    private LineChartData data;
    private Axis axisX;
    private Axis axisY;
    private LineChartView chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_service);

        // Se recogen los datos enviados desde la actividad llamante
        intent = getIntent();
        homeId = intent.getExtras().getString(MainActivity.KEY_HOME_ID);
        service = intent.getExtras().getString(ExpensesActivity.KEY_SERVICE);

        // Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        facturaReference = mDatabase.child("casas").child(homeId).child("servicios").child(service).child("facturas");

        // Modificación de la action bar en función del servicio
        actionBar = getSupportActionBar();
        if(service.equals(ExpensesActivity.KEY_ELECTRICITY)){
            actionBar.setTitle(R.string.electricity);
        }
        else if(service.equals(ExpensesActivity.KEY_WATER)){
            actionBar.setTitle(R.string.water);
        }
        else if(service.equals(ExpensesActivity.KEY_GAS)){
            actionBar.setTitle(R.string.gas);
        }
        else if(service.equals(ExpensesActivity.KEY_PHONE_AND_INTERNET)){
            actionBar.setTitle(R.string.phone_internet);
        }

        // Inflado de los widgets
        btnContractInfo = (Button) findViewById(R.id.btn_contract_info);
        btnInvoices = (Button) findViewById(R.id.btn_invoices);
        graphLayout = (RelativeLayout) findViewById(R.id.graph_layout);

        // Escuchadores
        btnContractInfo.setOnClickListener(this);
        btnInvoices.setOnClickListener(this);

        // Instanciación de los objetos relativos a la gráfica
        listaFacturas = new ArrayList<Factura>();
        values = new ArrayList<PointValue>();
        axisValues = new ArrayList<AxisValue>();
        lines = new ArrayList<Line>();

        // Escuchador para el cambio de información en las facturas
        facturaReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Borrado de datos previos
                listaFacturas.clear();
                values.clear();
                axisValues.clear();
                lines.clear();
                graphLayout.removeAllViews();

                // Se recogen en un arraylist todas las facturas
                for(DataSnapshot facturas : dataSnapshot.getChildren()){
                    listaFacturas.add(facturas.getValue(Factura.class));
                }

                // Se ordenan las facturas por fecha
                Collections.sort(listaFacturas, new Comparator<Factura>() {
                    @Override
                    public int compare(Factura factura1, Factura factura2) {
                        return factura2.getFechaCargo().compareTo(factura1.getFechaCargo());
                    }
                });

                /************ INICIO GRÁFICA ************/
                for(int i=0; i<listaFacturas.size(); i++){
                    values.add(new PointValue(i, (float) listaFacturas.get(i).getImporte()));
                }

                for (int i = 0; i < listaFacturas.size(); i++) {
                    axisValue = new AxisValue(i);
                    axisValue.setLabel(listaFacturas.get(i).getFechaCargo());
                    axisValues.add(axisValue);
                }

                Line line = new Line(values);
                // Se cambia el color de la gráfica en función del servicio
                if(service.equals(ExpensesActivity.KEY_ELECTRICITY)){
                    line.setColor(Color.rgb(229, 170, 23));
                }
                else if(service.equals(ExpensesActivity.KEY_WATER)){
                    line.setColor(Color.rgb(119, 179, 212));
                }
                else if(service.equals(ExpensesActivity.KEY_GAS)){
                    line.setColor(Color.rgb(255, 111, 0));
                }
                else if(service.equals(ExpensesActivity.KEY_PHONE_AND_INTERNET)){
                    line.setColor(Color.rgb(117, 183, 59));
                }
                line.setCubic(true);
                line.setFilled(true);
                line.setHasLabelsOnlyForSelected(true);
                lines.add(line);

                data = new LineChartData();
                data.setLines(lines);

                axisX = new Axis();
                axisX.setAutoGenerated(true);
                axisX.setValues(axisValues);
                axisX.setName("Facturas");
                data.setAxisXBottom(axisX);

                axisY = new Axis();
                axisY.setAutoGenerated(true);
                axisY.setName("Importe");
                data.setAxisYLeft(axisY);

                chart = new LineChartView(IndividualServiceActivity.this);
                chart.setLineChartData(data);
                graphLayout.addView(chart);
                /************ FIN GRÁFICA ************/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_invoices: {
                // El usuario desea mostrar las facturas del actual servicio
                intent = new Intent(this, InvoicesActivity.class);
                // Se pasa a la actividad el servicio actual y el id de la casa para hacer modificaciones en la base de datos
                intent.putExtra(ExpensesActivity.KEY_SERVICE, service);
                intent.putExtra(MainActivity.KEY_HOME_ID, homeId);
                // Se lanza la actividad
                startActivityForResult(intent, KEY_SEE_INVOICES);
            }; break;

            case R.id.btn_contract_info: {
                // El usuario desea mostrar la información del contrato
                intent = new Intent(this, ContractInformationActivity.class);
                // Se pasa a la actividad el servicio actual y el id de la casa para hacer modificaciones en la base de datos
                intent.putExtra(ExpensesActivity.KEY_SERVICE, service);
                intent.putExtra(MainActivity.KEY_HOME_ID, homeId);
                // Se lanza la actividad
                startActivityForResult(intent, KEY_CONTRACT_INFO);
            }; break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case KEY_SEE_INVOICES:{
                if(resultCode == RESULT_OK){

                } else{

                }
            }; break;

            case KEY_CONTRACT_INFO:{
                if(resultCode == RESULT_OK){
                    Toast.makeText(this, R.string.contract_updated, Toast.LENGTH_SHORT).show();
                }
            }; break;
        }

    }
}

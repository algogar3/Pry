package com.example.algog.homalia.act;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.algog.homalia.API.Api;
import com.example.algog.homalia.ORM.Producto;
import com.example.algog.homalia.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateProductActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    // Constantes
    public final static int KEY_ALIMENTACION = 0;
    public final static int KEY_LIMPIEZA = 1;
    public final static int KEY_OTROS = 2;

    // Variables
    private ImageView imageProductCategory;
    private EditText productName;
    private Spinner spinnerProductCategory;
    private Switch productPriority;
    private Button btnOk;
    private Button btnCancel;
    private ActionBar actionBar;

    private Intent intent;
    private String homeId;
    private String categoriaProducto;
    private Calendar calendar;
    private SimpleDateFormat df;
    private String fechaHoraCreacion;
    private Producto producto;
    private int categoria;
    private boolean isPrioritary;
    private String nickName;

    private DatabaseReference mDatabase;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        // Modificación de la action bar
        actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.new_product);

        // Se obtiene la información pasada en el intent
        intent = this.getIntent();
        homeId = intent.getExtras().getString(MainActivity.KEY_HOME_ID);
        nickName = intent.getExtras().getString(MainActivity.KEY_NICKNAME);

        // Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        key = mDatabase.child("casas").child(homeId).child("productos").push().getKey();

        // Inflado de los widgets
        imageProductCategory = (ImageView) findViewById(R.id.image_product_category);
        productName = (EditText) findViewById(R.id.product_name);
        spinnerProductCategory = (Spinner) findViewById(R.id.spinner_product_category);
        productPriority = (Switch) findViewById(R.id.switch_priority);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        // Se crea un arraylist con los nombres de las categorías de los productos
        ArrayList<String> arrayCategorias = Api.getCategoriasProducto(getApplicationContext());
        // Creo un arrayAdapter para un array de alumnos y elijo que modo gráfico voy a elegir y
        //le paso un array de alumnos. En vez de pasarle android.R.layot.simple_spinner_item le paso uno customizado
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.product_category_spinner, arrayCategorias);
        // Me erijo como escuchador del callback del Spinner (tengo que implementar la interfaz)
        spinnerProductCategory.setOnItemSelectedListener(this);
        // Fijo el adaptador al Spinner
        spinnerProductCategory.setAdapter(arrayAdapter);

        // Por defecto aparecerá seleccionada la categoría de alimentación
        Glide.with(this)
                .load(R.drawable.ic_local_dining_black_48dp)
                .override(500, 500)
                .into(imageProductCategory);

        // Por defecto el producto no es prioritario
        isPrioritary = false;

        // Escuchadores
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        productPriority.setOnCheckedChangeListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //Obtengo el nombre de la categoría del producto
        categoriaProducto = (String) adapterView.getItemAtPosition(i);

        // Se cambia la imagen mostrada en función de la categoría seleccionada por el usuario
        if (categoriaProducto.equals("Alimentación")) {
            Glide.with(this)
                    .load(R.drawable.ic_local_dining_black_48dp)
                    .override(500, 500)
                    .into(imageProductCategory);

            categoria = KEY_ALIMENTACION;
        } else if (categoriaProducto.equals("Limpieza")) {
            Glide.with(this)
                    .load(R.drawable.ic_format_paint_black_48dp)
                    .override(500, 500)
                    .into(imageProductCategory);
            categoria = KEY_LIMPIEZA;
        } else {
            Glide.with(this)
                    .load(R.drawable.ic_shopping_cart_black_48dp)
                    .override(500, 500)
                    .into(imageProductCategory);

            categoria = KEY_OTROS;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Vacío
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok: {
                // BOTON ACEPTAR
                // Se comprueba si se ha introducido texto
                if (Api.editTextIsEmpty(productName)) {
                    // NO SE HA INTRODUCIDO TEXTO
                    Toast.makeText(this, R.string.content_empty, Toast.LENGTH_SHORT).show();
                } else {
                    // SE HA INTRODUCIDO TEXTO
                    // Se obtiene la fecha y la hora de creación de la nota en el formato adecuado
                    calendar = Calendar.getInstance();
                    df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    fechaHoraCreacion = df.format(calendar.getTime());

                    // Se genera la nueva nota
                    producto = new Producto(key, productName.getText().toString(), categoria, nickName, fechaHoraCreacion, isPrioritary);

                    // Se introduce el nuevo producto en la base de datos de Firebase
                    Map<String, Object> updateProducto = new HashMap<>();
                    updateProducto.put("/casas/" + homeId + "/productos/" + key, producto);
                    mDatabase.updateChildren(updateProducto);

                    // Se devuelve el control a NotesActivity
                    setResult(RESULT_OK);
                    finish();
                }
            };break;

            case R.id.btn_cancel: {
                // BOTON CANCELAR
                // Se devuelve el control a ShoppingListActivity
                setResult(RESULT_CANCELED);
                finish();
            };break;


        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        isPrioritary = b;
    }
}

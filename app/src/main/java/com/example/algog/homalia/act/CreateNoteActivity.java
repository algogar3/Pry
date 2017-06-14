package com.example.algog.homalia.act;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.algog.homalia.API.Api;
import com.example.algog.homalia.ORM.Nota;
import com.example.algog.homalia.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CreateNoteActivity extends AppCompatActivity implements View.OnClickListener {

    // Variables
    private EditText content;
    private Button btn_ok;
    private Button btn_cancel;
    private Intent intent;
    private String nickname;
    private String homeId;
    private String fechaHoraCreacion;
    private Nota nota;
    private Calendar calendar;
    private SimpleDateFormat df;
    private ActionBar actionBar;

    private DatabaseReference mDatabase;
    private String key;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        // Modificación de la action bar
        actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.new_note);

        // Se obtiene la información pasada en el intent
        intent = this.getIntent();
        nickname = intent.getExtras().getString(MainActivity.KEY_NICKNAME);
        homeId = intent.getExtras().getString(MainActivity.KEY_HOME_ID);

        // Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        key = mDatabase.child("casas").child(homeId).child("notas").push().getKey();

        // Inflado de los widgets
        content = (EditText) findViewById(R.id.content);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);

        // Escuchadores
        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok:{
                // SE PRESIONA EL BOTÓN ACEPTAR
                // Se comprueba si se ha introducido texto
                if(Api.editTextIsEmpty(content)){
                    // NO SE HA INTRODUCIDO TEXTO
                    Toast.makeText(this, R.string.content_empty, Toast.LENGTH_SHORT).show();

                } else{
                    // SE HA INTRODUCIDO TEXTO
                    // Se obtiene la fecha y la hora de creación de la nota en el formato adecuado
                    calendar = Calendar.getInstance();
                    df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    fechaHoraCreacion = df.format(calendar.getTime());
                    id = key;

                    // Se genera la nueva nota
                    nota = new Nota(fechaHoraCreacion, content.getText().toString(), nickname, id);

                    // Se introduce la nueva nota en la base de datos de Firebase
                    Map<String, Object> updateNotas = new HashMap<>();
                    updateNotas.put("/casas/" + homeId + "/notas/" + key, nota);
                    mDatabase.updateChildren(updateNotas);

                    // Se devuelve el control a NotesActivity
                    setResult(RESULT_OK);
                    finish();
                }
            };break;

            case R.id.btn_cancel:{
                // SE PRESIONA EL BOTÓN CANCELAR
                // Se devuelve el control a NotesActivity
                setResult(RESULT_CANCELED);
                finish();
            };break;
        }
    }
}

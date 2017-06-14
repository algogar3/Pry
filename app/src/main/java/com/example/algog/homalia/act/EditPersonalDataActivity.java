package com.example.algog.homalia.act;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.algog.homalia.ORM.Companyero;
import com.example.algog.homalia.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class EditPersonalDataActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, CompoundButton.OnCheckedChangeListener {
    // Variables
    private ActionBar actionBar;
    private Button profileImage;
    private Button btnEditName;
    private Button btnEditLastName;
    private Button btnEditPhone;
    private Button btnEditDescription;
    private DatePicker birthDate;
    private Switch switchBirthDate;
    private Button btnOk;
    private Button btnCancel;
    private EditText name;
    private EditText lastName;
    private EditText phone;
    private EditText description;

    private Intent intent;
    private String homeId;
    private String userId;
    private boolean textChanged;
    private boolean dateChanged;

    private DatabaseReference mDatabase;
    private DatabaseReference companyeroReference;

    private String fechaCumpelanyos;
    private int[] elementosFecha = new int[3];
    private double gasto;
    private String idCasa;
    private String id;
    private String imagen;
    private String nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_data);

        // Modificación de la action bar
        actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.personal_data);

        // Se recogen los datos
        intent = getIntent();
        userId = intent.getExtras().getString(MainActivity.KEY_USER_UID);
        homeId = intent.getExtras().getString(MainActivity.KEY_HOME_ID);

        // Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        companyeroReference = mDatabase.child("companyeros").child(userId);

        // Inflado de los widgets
        profileImage = (Button) findViewById(R.id.profile_image);
        btnEditName = (Button) findViewById(R.id.btn_edit_name);
        btnEditLastName = (Button) findViewById(R.id.btn_edit_last_name);
        btnEditPhone = (Button) findViewById(R.id.btn_edit_phone);
        btnEditDescription = (Button) findViewById(R.id.btn_edit_description);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        name = (EditText) findViewById(R.id.name_text);
        lastName = (EditText) findViewById(R.id.last_name_text);
        phone = (EditText) findViewById(R.id.phone_text);
        description = (EditText) findViewById(R.id.description_text);
        switchBirthDate = (Switch) findViewById(R.id.switch_birth_date);
        birthDate = (DatePicker) findViewById(R.id.birth_date);


        // Escuchadores
        profileImage.setOnClickListener(this);
        btnEditName.setOnClickListener(this);
        btnEditLastName.setOnClickListener(this);
        btnEditPhone.setOnClickListener(this);
        btnEditDescription.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        name.addTextChangedListener(this);
        lastName.addTextChangedListener(this);
        phone.addTextChangedListener(this);
        description.addTextChangedListener(this);
        switchBirthDate.setOnCheckedChangeListener(this);

        // Estado inicial de la UI
        name.setEnabled(false);
        lastName.setEnabled(false);
        phone.setEnabled(false);
        description.setEnabled(false);
        birthDate.setEnabled(false);
        textChanged = false;
        dateChanged = false;

        // Se fija una fecha por defecto razonable y se setea un escuchador para detectar el cambio de fecha
        birthDate.init(1990,1,1, new DatePicker.OnDateChangedListener(){

            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                fechaCumpelanyos = i + "/" + (i1+1) +"/" + i2;
            }
        });

        // Se debe comprobar si existe información almacenada en base de datos del companyero
        // Si es así, se recuperan sus datos y se muestran
        companyeroReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name.setText(String.valueOf(dataSnapshot.child("nombre").getValue()));
                lastName.setText(String.valueOf(dataSnapshot.child("apellidos").getValue()));
                phone.setText(String.valueOf(dataSnapshot.child("telefono").getValue()));
                description.setText(String.valueOf(dataSnapshot.child("descripcion").getValue()));
                fechaCumpelanyos = String.valueOf(dataSnapshot.child("fechaNacimiento").getValue());
                gasto = Double.valueOf(dataSnapshot.child("gasto").getValue().toString());
                id = String.valueOf(dataSnapshot.child("id").getValue());
                idCasa = String.valueOf(dataSnapshot.child("idCasa").getValue());
                imagen = String.valueOf(dataSnapshot.child("imagen").getValue());
                nick = String.valueOf(dataSnapshot.child("nick").getValue());
                // Fecha de cumpleaños
                if(!fechaCumpelanyos.equals("")){

                    // Se actualiza el valor del datepicker
                    StringTokenizer stringTokenizer = new StringTokenizer(fechaCumpelanyos,"/");
                    int i = 0;
                    while (stringTokenizer.hasMoreTokens()){
                        elementosFecha[i] = Integer.valueOf(stringTokenizer.nextToken());
                        i++;
                    }

                    birthDate.updateDate(elementosFecha[0], elementosFecha[1], elementosFecha[2]);
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
            case R.id.profile_image: {
                Toast.makeText(this, R.string.profile_picture_functionality_not_activated, Toast.LENGTH_SHORT).show();
            }

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

            case R.id.btn_edit_last_name:{
                if(lastName.isEnabled()){
                    // Cambios en la UI
                    btnEditLastName.setText(R.string.edit);
                    lastName.setEnabled(false);
                } else{
                    btnEditLastName.setText(R.string.btn_ok);
                    lastName.setEnabled(true);
                }
            }; break;

            case R.id.btn_edit_phone:{
                if(phone.isEnabled()){
                    // Cambios en la UI
                    btnEditPhone.setText(R.string.edit);
                    phone.setEnabled(false);
                } else{
                    btnEditPhone.setText(R.string.btn_ok);
                    phone.setEnabled(true);
                }
            }; break;

            case R.id.btn_edit_description:{
                if(description.isEnabled()){
                    // Cambios en la UI
                    btnEditDescription.setText(R.string.edit);
                    description.setEnabled(false);
                } else{
                    btnEditDescription.setText(R.string.btn_ok);
                    description.setEnabled(true);
                }
            }; break;

            case R.id.btn_cancel:{
                // Se devuelve el control a la actividad llamante
                setResult(RESULT_CANCELED);
                finish();
            }

            case R.id.btn_ok:{
                if(textChanged || dateChanged){
                    // Ha habido cambios en el texto o en la fecha de cumpleaños
                    // Se comprueba que se han aceptado los cambios antes de modificar la base de datos
                    if(name.isEnabled() || lastName.isEnabled() || phone.isEnabled() || description.isEnabled()){
                        // Hay cambios sin guardar
                        Toast.makeText(this, R.string.accept_changes, Toast.LENGTH_SHORT).show();
                    } else{
                        // No hay cambios sin guardar
                        // Se debe guardar en base de datos la información del usuario
                        Companyero companyero = new Companyero();
                        companyero.setNombre(name.getText().toString());
                        companyero.setApellidos(lastName.getText().toString());
                        companyero.setTelefono(phone.getText().toString());
                        companyero.setDescripcion(description.getText().toString());
                        companyero.setFechaNacimiento(fechaCumpelanyos);
                        companyero.setIdCasa(idCasa);
                        companyero.setId(id);
                        companyero.setNick(nick);
                        companyero.setGasto(gasto);
                        companyero.setImagen(imagen);

                        // Se introduce la información del usuario en la base de datos de Firebase (Companyero individual)
                        Map<String, Object> updateCompanyero = new HashMap<>();
                        updateCompanyero.put("/companyeros/" + userId, companyero);
                        mDatabase.updateChildren(updateCompanyero);

                        // Se introduce la información del usuario en la base de datos de Firebase (Companyero dentro de casa)
                        Map<String, Object> updateCompanyeroCasa = new HashMap<>();
                        updateCompanyeroCasa.put("/casas/" + homeId + "/companyeros/" + userId, companyero);
                        mDatabase.updateChildren(updateCompanyeroCasa);

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
        textChanged = true;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        // Vacío
    }
    // FIN IMPLEMENTACIÓN DE LOS MÉTODOS DE LA INTERFAZ TEXTWATCHER

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        dateChanged = b;
        if(b){
            // El switch está activado
            birthDate.setEnabled(b);
        } else {
            // El switch está desactivado
            birthDate.setEnabled(b);
        }
    }

}

package com.example.algog.homalia.act;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.algog.homalia.API.Api;
import com.example.algog.homalia.ORM.Casa;
import com.example.algog.homalia.ORM.Companyero;
import com.example.algog.homalia.ORM.Servicio;
import com.example.algog.homalia.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreateOrJoinHomeActivity extends AppCompatActivity implements View.OnClickListener{

    // VARIABLES
    private Button btnProfileImage;
    private EditText houseId;
    private EditText houseName;
    private EditText password;
    private EditText repeatPassword;
    private ImageView imageHouseId;
    private ImageView imageHouseName;
    private ImageView imagePassNewHome;
    private ImageView imageRepeatPassNewHome;
    private Button btnOk;
    private Button btnCancel;

    private Intent intent;
    private Casa casa;
    private Companyero companyero;
    private String userUid;
    private String key;
    private ArrayList<Servicio> serviciosBasicos;
    private ArrayList<Companyero> companyeros = new ArrayList<Companyero>();

    private DatabaseReference mDatabase;
    private Query casaReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_join_home);

        // Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        key = mDatabase.child("casas").push().getKey();

        // Inflado de los widgets
        btnProfileImage = (Button) findViewById(R.id.btn_profile_image);
        houseId = (EditText) findViewById(R.id.house_id);
        houseName = (EditText) findViewById(R.id.house_name);
        password = (EditText) findViewById(R.id.password);
        repeatPassword = (EditText) findViewById(R.id.repeat_password);
        imageHouseId = (ImageView) findViewById(R.id.image_house_id);
        imageHouseName = (ImageView) findViewById(R.id.image_house_name);
        imagePassNewHome = (ImageView) findViewById(R.id.image_pass_new_home);
        imageRepeatPassNewHome = (ImageView) findViewById(R.id.image_repeat_pass_new_home);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        // Se insertan las imagenes deseadas en los ImageView haciendo uso de la librería Glide
        Glide.with(this)
                .load(R.drawable.id_icon_96)
                .override(120, 120)
                .into(imageHouseId);

        Glide.with(this)
                .load(R.drawable.ic_home_black_24dp)
                .override(120, 120)
                .into(imageHouseName);

        Glide.with(this)
                .load(R.drawable.ic_lock_black_48dp)
                .override(120, 120)
                .into(imagePassNewHome);

        Glide.with(this)
                .load(R.drawable.ic_lock_black_48dp)
                .override(120, 120)
                .into(imageRepeatPassNewHome);

        // Escuchadores de los widgets
        btnProfileImage.setOnClickListener(this);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        // Se recoge el intent que originó la llamada a la actividad
        intent = this.getIntent();

        // Se recogen los datos necesarios comunes a todas las operaciones
        companyero = intent.getParcelableExtra(MainActivity.KEY_COMPANYERO);
        userUid = intent.getExtras().getString(MainActivity.KEY_USER_UID);

        // Se obtine la finalidad con la que se llamó a la actividad para modificar la UI
        if(intent.getExtras().getInt(MainActivity.KEY_REQUEST_CODE) == MainActivity.KEY_CREATE_HOME){
            // OPCIÓN CREAR UNA NUEVA CASA
            // Modificación de la UI
            houseId.setVisibility(View.GONE);
            imageHouseId.setVisibility(View.GONE);

            // Obtención de los datos necesarios
            serviciosBasicos = Api.crearServiciosBasicos();
        }
        else if(intent.getExtras().getInt(MainActivity.KEY_REQUEST_CODE) == MainActivity.KEY_JOIN_HOME){
            // OPCIÓN UNIRSE A UNA CASA EXISTENTE
            // Modificación de la UI
            houseName.setVisibility(View.GONE);
            imageHouseName.setVisibility(View.GONE);
            repeatPassword.setVisibility(View.GONE);
            imageRepeatPassNewHome.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            // Botón de imagen de perfil de la casa
            case (R.id.btn_profile_image):{
                Toast.makeText(this, R.string.profile_picture_functionality_not_activated,Toast.LENGTH_SHORT).show();
            };break;

            // Botón OK
            case (R.id.btn_ok):{
                // CASO CREAR UNA NUEVA CASA
                if(intent.getExtras().getInt(MainActivity.KEY_REQUEST_CODE) == MainActivity.KEY_CREATE_HOME) {
                    // Comprobación que el usuario ha rellenado los campos obligatorios
                    if (!Api.editTextIsEmpty(houseName) && !Api.editTextIsEmpty(password) && !Api.editTextIsEmpty(repeatPassword)) {
                        // Comprobación de que la contraseña introducida es válida
                        if (Api.validPassword(password)) {
                            // Comprobación de que las contraseñas introducidas por el usuario coinciden
                            if (Api.passwordsMatch(password, repeatPassword)) {
                                // DATOS CORRECTOS
                                // Se modifica el atributo de pertenencia a casa del usuario antes de introducirlo en la nueva casa
                                companyero.setIdCasa(key);
                                companyeros.add(companyero);

                                // Modificación del usuario en la base de datos
                                mDatabase.child("companyeros").child(userUid).child("idCasa").setValue(key);

                                // Creación del objeto casa
                                casa = new Casa(houseName.getText().toString(), password.getText().toString(), key,
                                        companyeros, serviciosBasicos);

                                // Introuducción de la casa en la base de datos

                                Map<String, Object> updateHome = new HashMap<>();
                                updateHome.put("/casas/"  + key, casa);
                                mDatabase.updateChildren(updateHome);

                                // INTRODUCCIÓN DE LOS ARRAYLIST DE LA CASA EN LA BASE DE DATOS
                                // Companyeros
                                Map<String, Object> updateCompanyeros = new HashMap<>();
                                updateCompanyeros.put("/casas/" + key + "/companyeros/" + userUid, companyero);
                                mDatabase.updateChildren(updateCompanyeros);

                                // Servicios
                                Map<String, Object> updateServices = new HashMap<>();
                                for(Servicio servicio : serviciosBasicos){
                                    updateServices.put("/casas/" + key + "/servicios/" + servicio.getNombre().toString(), servicio);
                                }
                                mDatabase.updateChildren(updateServices);

                                // Finalización de la actividad
                                setResult(RESULT_OK);
                                finish();

                            } else {
                                Toast.makeText(this, R.string.passwords_dont_match, Toast.LENGTH_SHORT).show();
                                password.setText("");
                                repeatPassword.setText("");
                            }
                        } else {
                            Toast.makeText(this, R.string.password_length, Toast.LENGTH_SHORT).show();
                            password.setText("");
                            repeatPassword.setText("");
                        }
                    } else {
                        Toast.makeText(this, R.string.mandatory_fileds_empty, Toast.LENGTH_SHORT).show();
                    }
                }
                // CASO UNIRSE A UNA CASA EXISTENTE
                else if(intent.getExtras().getInt(MainActivity.KEY_REQUEST_CODE) == MainActivity.KEY_JOIN_HOME){
                    // Comprobación que el usuario ha rellenado los campos obligatorios
                    if(!Api.editTextIsEmpty(houseId) && !Api.editTextIsEmpty(password)){
                        // Comprobación de que los datos introducidos pertenecen a una casa existente
                        casaReference = mDatabase.child("casas").orderByChild("id").equalTo(houseId.getText().toString());

                        casaReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // El id proporcionado por el usuario corresponde con un id de una casa existente
                                    for (DataSnapshot casas : dataSnapshot.getChildren()) {
                                        casa = casas.getValue(Casa.class);
                                        if(password.getText().toString().equals(casa.getPassword())){
                                            // DATOS CORRECTOS

                                            // MODIFICACIÓN DE LA BASE DE DATOS
                                            // Modificación del usuario en la base de datos
                                            mDatabase.child("companyeros").child(userUid).child("idCasa").setValue(casa.getId());
                                            companyero.setIdCasa(casa.getId());

                                            // Modificación de la casa en la base de datos
                                            Map<String, Object> updateCompanyeros = new HashMap<>();
                                            updateCompanyeros.put("/casas/" + casa.getId() + "/companyeros/" + userUid, companyero);
                                            mDatabase.updateChildren(updateCompanyeros);

                                            // Finalización de la actividad
                                            setResult(RESULT_OK);
                                            finish();
                                        }else{
                                            // La contraseña no coincide con el id de casa introducido por el usuario
                                            Toast.makeText(CreateOrJoinHomeActivity.this, R.string.check_your_data ,Toast.LENGTH_SHORT).show();
                                            password.setText("");
                                            houseId.setText("");
                                        }
                                    }
                                }else{
                                    // No existe ninguna casa con el id introducido por el usuario
                                    Toast.makeText(CreateOrJoinHomeActivity.this, R.string.check_your_data ,Toast.LENGTH_SHORT).show();
                                    password.setText("");
                                    houseId.setText("");
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }
            };break;

            // Botón Cancelar
            case (R.id.btn_cancel):{
                // El usuario ha cancelado la operación. Se finaliza la actividad
                setResult(RESULT_CANCELED);
                houseName.setText("");
                houseId.setText("");
                password.setText("");
                repeatPassword.setText("");
                finish();
            };break;
        }
    }
}

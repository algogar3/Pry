package com.example.algog.homalia.act;

import android.support.annotation.NonNull;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.algog.homalia.API.Api;
import com.example.algog.homalia.ORM.Companyero;
import com.example.algog.homalia.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrarseActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    // CONSTANTES DE CLASE
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASS = "pass";

    // VARIABLES
    private Intent intencion;
    private EditText email;
    private EditText password;
    private EditText repeatPassword;
    private EditText nickName;
    private EditText firstName;
    private EditText lastName;
    private EditText phone;
    private EditText description;
    private DatePicker birthDate;
    private Switch switchBirthDate;
    private Button btn_profile_picture;
    private Button btn_ok;
    private Button btn_cancel;
    private ImageView image_email_register;
    private ImageView image_pass_register;
    private ImageView image_repeat_pass_register;
    private ImageView image_nick;
    private ImageView image_first_name;
    private ImageView image_last_name;
    private ImageView image_phone;
    private ImageView image_description;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private DatabaseReference dbCreateUser;
    private String newCompFirstName;
    private String newCompLastName;
    private String newCompPhone;
    private String newCompDesription;
    private String newCompBirthDate;
    private String newCompProfilePicture;

    /*** CICLO DE VIDA DE LA ACTIVIDAD ***/

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        // Instancia de FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Escuchador de Firebase
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
            }
        };


        // Inflado de los widgets  de la actividad
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        repeatPassword = (EditText) findViewById(R.id.repeat_password);
        nickName = (EditText) findViewById(R.id.nick);
        firstName = (EditText) findViewById(R.id.first_name);
        lastName = (EditText) findViewById(R.id.last_name);
        phone = (EditText) findViewById(R.id.phone);
        description = (EditText) findViewById(R.id.description);
        switchBirthDate = (Switch) findViewById(R.id.switch_birth_date);
        birthDate = (DatePicker) findViewById(R.id.birth_date);
        btn_profile_picture = (Button) findViewById(R.id.btn_profile_image);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        image_email_register = (ImageView) findViewById(R.id.image_email_register);
        image_pass_register = (ImageView) findViewById(R.id.image_pass_register);
        image_repeat_pass_register = (ImageView) findViewById(R.id.image_repeat_pass_register);
        image_nick = (ImageView) findViewById(R.id.image_nick);
        image_first_name = (ImageView) findViewById(R.id.image_first_name);
        image_last_name = (ImageView) findViewById(R.id.image_last_name);
        image_phone = (ImageView) findViewById(R.id.image_phone);
        image_description = (ImageView) findViewById(R.id.image_description);

        // Se fija una fecha por defecto razonable y se setea un escuchador para detectar el cambio de fecha
        birthDate.init(1990,1,1, new DatePicker.OnDateChangedListener(){

            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                newCompBirthDate = i + "/" + (i1+1) +"/" + i2;
            }
        });

        // La selección de fecha de cumpleaños está desactivada por defecto
        birthDate.setEnabled(false);
        newCompBirthDate = "";

        // Inserción de las imágenes en los ImageViews haciendo uso de la librería Glide
        Glide.with(this)
                .load(R.drawable.ic_markunread_black_48dp)
                .override(120, 120)
                .into(image_email_register);

        Glide.with(this)
                .load(R.drawable.ic_lock_black_48dp)
                .override(120, 120)
                .into(image_pass_register);

        Glide.with(this)
                .load(R.drawable.ic_lock_black_48dp)
                .override(120, 120)
                .into(image_repeat_pass_register);

        Glide.with(this)
                .load(R.drawable.ic_supervisor_account_black_48dp)
                .override(120, 120)
                .into(image_nick);

        Glide.with(this)
                .load(R.drawable.ic_supervisor_account_black_48dp)
                .override(120, 120)
                .into(image_first_name);

        Glide.with(this)
                .load(R.drawable.ic_supervisor_account_black_48dp)
                .override(120, 120)
                .into(image_last_name);

        Glide.with(this)
                .load(R.drawable.ic_phone_black_48dp)
                .override(120, 120)
                .into(image_phone);

        Glide.with(this)
                .load(R.drawable.ic_library_books_black_48dp)
                .override(120, 120)
                .into(image_description);

        // Escuchadores de los widgets
        btn_profile_picture.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        switchBirthDate.setOnCheckedChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            // BOTÓN ACEPTAR
            case R.id.btn_ok: {
                // Comprobación de que los campos obligatorios se ha rellenado
                if(!Api.editTextIsEmpty(email) && !Api.editTextIsEmpty(password) && !Api.editTextIsEmpty(repeatPassword) && !Api.editTextIsEmpty(nickName)){
                    // Comprobación de que el usuario ha introducido un email válido
                    if(Api.validEmail(email)) {
                        // Comprobación de que la contraseña introducida es válida
                        if(Api.validPassword(password)){
                            // Comprobación de que las contraseñas introducidas por el usuario coinciden
                            if(Api.passwordsMatch(password, repeatPassword)){
                                // Datos correctos
                                intencion = new Intent(this, LoginActivity.class);
                                mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            // Si el registro se ha realizado con éxito, se introduce al nuevo companyero en la base de datos

                                            // Se crea y se prepara el objeto nuevoCompanyero
                                            Companyero nuevoCompanyero;

                                            newCompFirstName = (!Api.editTextIsEmpty(firstName))? firstName.getText().toString():"";
                                            newCompLastName = (!Api.editTextIsEmpty(lastName))? lastName.getText().toString():"";
                                            newCompPhone = (!Api.editTextIsEmpty(phone))? phone.getText().toString():"";
                                            newCompDesription = (!Api.editTextIsEmpty(description))? description.getText().toString():"";
                                            newCompProfilePicture = "";

                                            nuevoCompanyero = new Companyero(user.getUid(), newCompFirstName, newCompLastName, nickName.getText().toString(),
                                                    newCompBirthDate, newCompPhone, newCompProfilePicture, newCompDesription);

                                            // Se introduce el nuevoCompanyero en la base de datos, asignandole con id el UID generado por Firebase Auth
                                            dbCreateUser = FirebaseDatabase.getInstance().getReference().child("companyeros");
                                            dbCreateUser.child(user.getUid()).setValue(nuevoCompanyero);

                                            // Se pasa a LoginActivity el email y la contraseña para que el logeo sea transparente al usuario
                                            intencion.putExtra(KEY_EMAIL, email.getText().toString());
                                            intencion.putExtra(KEY_PASS, password.getText().toString());

                                            // Se marca un resultado no satisfactorio
                                            setResult(RESULT_OK, intencion);

                                            // Se finaliza la actividad y se devuelve el control a la LoginActivity
                                            finish();
                                        }
                                        else{
                                            try{
                                                throw task.getException();
                                            }

                                            catch(FirebaseAuthUserCollisionException e) {
                                                Toast.makeText(RegistrarseActivity.this,"El email introducido ya esta en uso", Toast.LENGTH_SHORT).show();
                                            }

                                            catch (Exception e){
                                                Toast.makeText(RegistrarseActivity.this,"Error al registrar el usuario", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });



                            } else{
                                Toast.makeText(this, R.string.passwords_dont_match, Toast.LENGTH_SHORT).show();
                                password.setText("");
                                repeatPassword.setText("");
                            }
                        } else{
                            Toast.makeText(this, R.string.password_length, Toast.LENGTH_SHORT).show();
                            password.setText("");
                            repeatPassword.setText("");
                        }
                    } else{
                        Toast.makeText(this, R.string.invalid_email, Toast.LENGTH_SHORT).show();
                        email.setText("");
                    }
                } else{
                    Toast.makeText(this, R.string.mandatory_fileds_empty, Toast.LENGTH_SHORT).show();
                }

            };break;

            // BOTON CANCELAR
            case R.id.btn_cancel: {
                // Se marca un resultado no satisfactorio
                setResult(RESULT_CANCELED, intencion);

                // Se finaliza la actividad y se devuelve el control a la LoginActivity
                finish();
            };break;

            // IMAGE VIEW
            case R.id.btn_profile_image: {
                Toast.makeText(this, R.string.profile_picture_functionality_not_activated, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Implementación del método de la interfaz del escuchador del estado del switch
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(b){
            // El switch está activado
            birthDate.setEnabled(b);
        } else {
            birthDate.setEnabled(b);
            newCompBirthDate = "";
        }
    }
}

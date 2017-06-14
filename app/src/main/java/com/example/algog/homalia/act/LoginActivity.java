package com.example.algog.homalia.act;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.bumptech.glide.Glide;
import com.example.algog.homalia.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.example.algog.homalia.API.Api;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    // CONSTANTES
    public static final int KEY_REGISTRARSE = 0;
    public static final int KEY_RECUPERAR_PASS = 1;

    // VARIABLES
    private ImageView image_login;
    private ImageView image_email;
    private ImageView image_pass;
    private EditText email;
    private EditText password;
    private Button btn_login;
    private Button btn_signup;
    private TextView pass_forgotten;
    private Intent intencion;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    /*** CICLO DE VIDA DE LA ACTIVIDAD ***/

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Instancia de FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Escuchador de Firebase
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };

        // Inflado de los widgets de la actividad
        image_login = (ImageView) findViewById(R.id.image_login);
        image_email = (ImageView) findViewById(R.id.image_email);
        image_pass = (ImageView) findViewById(R.id.image_pass);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        pass_forgotten = (TextView) findViewById(R.id.pass_forgotten);

        // Escuchadores
        btn_login.setOnClickListener(this);
        btn_signup.setOnClickListener(this);
        pass_forgotten.setOnClickListener(this);

        // Se insertan las imagenes deseadas en los ImageView haciendo uso de la librería Glide
        Glide.with(this)
                .load(R.drawable.logo_homalia)
                .override(600, 600)
                .into(image_login);

        Glide.with(this)
                .load(R.drawable.ic_markunread_black_48dp)
                .override(120, 120)
                .into(image_email);

        Glide.with(this)
                .load(R.drawable.ic_lock_black_48dp)
                .override(120, 120)
                .into(image_pass);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    // EVENTOS CLICK DE LOS WIDGETS
    @Override
    public void onClick(View v) {

        switch(v.getId()){

            // Botón ENTRAR
            case R.id.btn_login:{
                if(!Api.editTextIsEmpty(email) && !Api.editTextIsEmpty(password)
                        && Api.validEmail(email) && Api.validPassword(password)) {
                    intencion = new Intent(LoginActivity.this, MainActivity.class);
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        // Login incorrecto
                                        Toast.makeText(LoginActivity.this, R.string.login_failed, Toast.LENGTH_LONG).show();
                                        email.setText("");
                                        password.setText("");
                                    } else {
                                        // Login correcto
                                        startActivity(intencion);
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(this, R.string.check_your_data, Toast.LENGTH_SHORT).show();
                    password.setText("");
                    email.setText("");
                }
            };break;

            // Botón REGISTRARSE
            case R.id.btn_signup:{
                // Se borran los campos
                email.setText("");
                password.setText("");

                // Se llama a la actividad de registro
                Intent intencion = new Intent(LoginActivity.this, RegistrarseActivity.class);
                startActivityForResult(intencion, KEY_REGISTRARSE);
            };break;

            // Contraseña olvidada
            case R.id.pass_forgotten:{
                Intent intencion = new Intent(LoginActivity.this, RecoverPasswordActivity.class);
                intencion.putExtra(MainActivity.KEY_EMAIL, email.getText().toString());
                startActivityForResult(intencion, KEY_RECUPERAR_PASS);
                //Toast.makeText(LoginActivity.this, "Te jodes", Toast.LENGTH_LONG).show();
            }
        }
    }

    // ON ACTIVITY RESULT

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case KEY_REGISTRARSE: {
                if(resultCode == Activity.RESULT_OK){
                    // El usuario se ha registrado correctamente
                    Toast.makeText(LoginActivity.this, R.string.signup_ok, Toast.LENGTH_SHORT).show();
                    email.setText(data.getExtras().getString(RegistrarseActivity.KEY_EMAIL));
                    password.setText(data.getExtras().getString(RegistrarseActivity.KEY_PASS));

                    // Logeo transparente para el usuario recien registrado
                    btn_login.performClick();

                } else if(resultCode == Activity.RESULT_CANCELED){
                    // El usuario ha cancelado el registro
                    Toast.makeText(LoginActivity.this, R.string.signup_canceled, Toast.LENGTH_SHORT).show();
                }
            }; break;

            case KEY_RECUPERAR_PASS: {
                if(resultCode == Activity.RESULT_OK){
                    // El email introducido por el usuario es correcto y se ha enviado su contraseña
                    Toast.makeText(LoginActivity.this, R.string.email_sent, Toast.LENGTH_LONG).show();
                }
            }; break;
        }


    }
}

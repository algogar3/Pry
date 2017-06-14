package com.example.algog.homalia.act;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.algog.homalia.API.Api;
import com.example.algog.homalia.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class RecoverPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    // Variables
    private EditText editText;
    private String email;
    private Button btnOk;
    private Button btnCancel;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        // Se recuperan los datos
        Intent intent = getIntent();
        email = intent.getExtras().getString(MainActivity.KEY_EMAIL);

        // Inflado de los widgets
        editText = (EditText) findViewById(R.id.email);
        btnOk = (Button) findViewById(R.id.btn_ok);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        // Si el usuario había introducido algún correo en LoginActivity, se recupera
        editText.setText(email);

        // Escuchadores
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        // Instancias de firebase
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ok: {
                // Se comprueba si el campo del email está vacío
                if(!Api.editTextIsEmpty(editText)){
                    auth.sendPasswordResetEmail(editText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                try{
                                    throw task.getException();
                                }

                                catch(FirebaseAuthInvalidUserException e) {
                                    Toast.makeText(RecoverPasswordActivity.this,R.string.invalid_email, Toast.LENGTH_SHORT).show();
                                }

                                catch (Exception e){}
                                setResult(RESULT_CANCELED);
                            }
                        }
                    });
                } else{
                    Toast.makeText(RecoverPasswordActivity.this, R.string.mandatory_fileds_empty, Toast.LENGTH_SHORT).show();
                }

            };break;

            case R.id.btn_cancel: {
                setResult(RESULT_CANCELED);
                finish();
            };break;
        }
    }
}

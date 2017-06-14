package com.example.algog.homalia.act;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.algog.homalia.API.Api;
import com.example.algog.homalia.ORM.Companyero;
import com.example.algog.homalia.dialog.AboutUsDialog;
import com.example.algog.homalia.dialog.InviteHouseDialog;
import com.example.algog.homalia.dialog.LeaveHouseDialog;
import com.example.algog.homalia.dialog.NoHomeActionDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.example.algog.homalia.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NoHomeActionDialog.OnRealizarAccion, LeaveHouseDialog.OnRealizarAccion {

    // CONSTANTES DE CLASE
    public static final int KEY_CREATE_HOME = 0;
    public static final int KEY_JOIN_HOME = 1;
    public static final int KEY_EDIT_PERSONAL_DATA = 2;
    public static final String KEY_COMPANYERO = "key_companyero";
    public static final String KEY_REQUEST_CODE = "key_request_code";
    public static final String KEY_USER_UID = "key_user";
    public static final String KEY_HOME_ID = "key_home_id";
    public static final String KEY_NICKNAME = "key_nickname";
    public static final String KEY_PASSWORD = "key_password";
    public static final String KEY_EMAIL = "key_email";


    // VARIABLES
    private Intent intent;
    private ImageView image_home;
    private Button btn_flatmates;
    private Button btn_tasks;
    private Button btn_shopping_list;
    private Button btn_notes;
    private Button btn_expenses;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private DatabaseReference companyeroReference;
    private DatabaseReference casaReference;

    private String idCasaUsuarioLogeado;
    private String passwordCasaUsuarioLogeado;
    private Companyero companyero;
    private ActionBar actionBar;
    private ProgressDialog progressDialog;
    private NoHomeActionDialog dialogo;

    /*** CICLO DE VIDA DE LA ACTIVIDAD ***/

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Progress dialog
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle(getString(R.string.title_progress_dialog));
        progressDialog.setMessage(getString(R.string.message_progress_dialog));
        progressDialog.show();

        // Diálogo
        dialogo = NoHomeActionDialog.newInstance();

        // Instancia de FirebaseAuth
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        // Escuchador de Firebase para posibles cambios de usuraio
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
            }
        };

        // Instancia de FirebaseDatabase
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Se inflan los widgets del layout
        image_home = (ImageView) findViewById(R.id.image_home);
        btn_flatmates = (Button) findViewById(R.id.btn_flatmates);
        btn_tasks = (Button) findViewById(R.id.btn_tasks);
        btn_shopping_list = (Button) findViewById(R.id.btn_shopping_list);
        btn_notes = (Button) findViewById(R.id.btn_notes);
        btn_expenses = (Button) findViewById(R.id.btn_expenses);
        actionBar = getSupportActionBar();
        actionBar.hide();

        // Se desactivan todas las funcionalidades hasta que se obtenga la información del usuario logeado
        image_home.setEnabled(false);
        btn_flatmates.setEnabled(false);
        btn_tasks.setEnabled(false);
        btn_shopping_list.setEnabled(false);
        btn_notes.setEnabled(false);
        btn_expenses.setEnabled(false);

        // Escuchadores de los widgets
        image_home.setOnClickListener(this);
        btn_flatmates.setOnClickListener(this);
        btn_tasks.setOnClickListener(this);
        btn_shopping_list.setOnClickListener(this);
        btn_notes.setOnClickListener(this);
        btn_expenses.setOnClickListener(this);

        // Instancias a la base de datos
        companyeroReference = mDatabase.child("companyeros").child(user.getUid());
        casaReference = mDatabase.child("casas");

        // VALUE EVENT LISTENERS
        // Companyero listener
        companyeroReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Companyero companyeroTemp = dataSnapshot.getValue(Companyero.class);
                companyero = Api.completarCompanyero(companyeroTemp);

                if (!companyero.getIdCasa().equals("")){
                    // CASO: El usuario logeado ya pertenece a una casa

                    progressDialog.hide();
                    if(dialogo.isAdded() && dialogo.isVisible() && dialogo != null){
                        dialogo.dismiss();
                    }

                    // Se recupera el id de la casa a la que pertenece el usuario
                    idCasaUsuarioLogeado = companyero.getIdCasa();

                    // Se habilitan las funcionalidades de la casa
                    image_home.setEnabled(true);
                    btn_flatmates.setEnabled(true);
                    btn_tasks.setEnabled(true);
                    btn_shopping_list.setEnabled(true);
                    btn_notes.setEnabled(true);
                    btn_expenses.setEnabled(true);

                    // Se muestra la action bar
                    actionBar.show();

                    casaReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            actionBar.setTitle(String.valueOf(dataSnapshot.child(idCasaUsuarioLogeado).child("nombre").getValue()));
                            passwordCasaUsuarioLogeado = String.valueOf(dataSnapshot.child(idCasaUsuarioLogeado).child("password").getValue());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    // CASO: El usuario logeado no pertenece a ninguna casa

                    // Se desactivan todas las funcionalidades ya que el usuario no pertenece a ninguna casa
                    image_home.setEnabled(false);
                    btn_flatmates.setEnabled(false);
                    btn_tasks.setEnabled(false);
                    btn_shopping_list.setEnabled(false);
                    btn_notes.setEnabled(false);
                    btn_expenses.setEnabled(false);

                    // Se oculta la action bar
                    actionBar.hide();

                    // Tratamiento del diálogo
                    // Me suscribo a los eventos del fragmento
                    dialogo.setOnRealizarAccionListener(MainActivity.this);
                    // Se hace que el fragmento sea modal
                    dialogo.setCancelable(false);
                    //Muestro el fragmento diálogo
                    progressDialog.hide();
                    if(dialogo.isAdded()){
                        return;
                    }else{
                        dialogo.show(getFragmentManager(), null);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

        switch(v.getId()){
            case R.id.image_home:{
                Toast.makeText(this, R.string.profile_picture_functionality_not_activated, Toast.LENGTH_SHORT).show();
            };break;

            case R.id.btn_flatmates:{
                intent = new Intent (this, FlatMatesActivity.class);
                intent.putExtra(KEY_HOME_ID, idCasaUsuarioLogeado);
                startActivity(intent);
            }break;

            case R.id.btn_tasks:{
                intent = new Intent (this, TasksActivity.class);
                intent.putExtra(KEY_HOME_ID, idCasaUsuarioLogeado);
                startActivity(intent);
            }break;

            case R.id.btn_shopping_list:{
                intent = new Intent (this, ShoppingListActivity.class);
                intent.putExtra(KEY_HOME_ID, idCasaUsuarioLogeado);
                intent.putExtra(KEY_USER_UID, user.getUid());
                intent.putExtra(KEY_NICKNAME, companyero.getNick());
                startActivity(intent);
            }break;

            case R.id.btn_notes:{
                intent = new Intent (this, NotesActivity.class);
                intent.putExtra(KEY_HOME_ID, idCasaUsuarioLogeado);
                intent.putExtra(KEY_NICKNAME, companyero.getNick());
                startActivity(intent);
            }break;

            case R.id.btn_expenses:{
                intent = new Intent (this, ExpensesActivity.class);
                intent.putExtra(KEY_HOME_ID, idCasaUsuarioLogeado);
                startActivity(intent);
            }break;
        }

    }

    // Metodos de la interfaz OnRealizarAccion
    @Override
    public void newHome() {
        intent = new Intent(this, CreateOrJoinHomeActivity.class);
        intent.putExtra(KEY_COMPANYERO, companyero);
        intent.putExtra(KEY_REQUEST_CODE, KEY_CREATE_HOME);
        intent.putExtra(KEY_USER_UID, user.getUid());
        startActivityForResult(intent, KEY_CREATE_HOME);
    }

    @Override
    public void joinHome() {
        intent = new Intent(this, CreateOrJoinHomeActivity.class);
        intent.putExtra(KEY_COMPANYERO, companyero);
        intent.putExtra(KEY_REQUEST_CODE, KEY_JOIN_HOME);
        intent.putExtra(KEY_USER_UID, user.getUid());
        startActivityForResult(intent,KEY_JOIN_HOME);
    }

    @Override
    public void cancel() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case KEY_CREATE_HOME: {
                if(requestCode == RESULT_OK){
                    // Casa creada con éxito
                    Toast.makeText(this, R.string.home_created ,Toast.LENGTH_SHORT).show();
                }else{
                    // El usuario sigue sin pertenecer a ninguna casa
                    // Se vuelve a llamar a la actividad para que lance de nuevo el diálogo
                    intent = new Intent(this,MainActivity.class);
                    startActivity(intent);
                }
            }; break;

            case KEY_JOIN_HOME: {
                if(requestCode == RESULT_OK){
                    // Unión a una casa realizada con éxito
                    Toast.makeText(this, R.string.home_joined ,Toast.LENGTH_SHORT).show();
                }else{
                    // El usuario sigue sin pertenecer a ninguna casa
                    // Se vuelve a llamar a la actividad para que lance de nuevo el diálogo
                    intent = new Intent(this,MainActivity.class);
                    startActivity(intent);
                }
            }; break;

            case KEY_EDIT_PERSONAL_DATA: {
                if(requestCode == RESULT_OK){
                    Toast.makeText(this, R.string.personal_data_updated ,Toast.LENGTH_SHORT).show();
                }else{

                }
            }; break;

        }
    }

    // INICIO MÉTODOS ACTIONBAR
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.personal_data:
                Intent intent = new Intent(this, EditPersonalDataActivity.class);
                intent.putExtra(KEY_HOME_ID, idCasaUsuarioLogeado);
                intent.putExtra(KEY_USER_UID, user.getUid());
                startActivityForResult(intent, KEY_EDIT_PERSONAL_DATA);
                return true;
            case R.id.share_house:
                // Share house
                InviteHouseDialog inviteHouseDialog = InviteHouseDialog.newInstance(idCasaUsuarioLogeado, passwordCasaUsuarioLogeado);
                //Muestro el diálogo
                inviteHouseDialog.show(getFragmentManager(), null);
                return true;
            case R.id.leave_house:
                // Leave house
                LeaveHouseDialog leaveHouseDialog = LeaveHouseDialog.newInstance(idCasaUsuarioLogeado, user.getUid());
                //Me suscribo al evento onRealizarAccion del fragmento y lo implemento
                leaveHouseDialog.setOnRealizarAccionListener(this);
                //Muestro el diálogo
                leaveHouseDialog.show(getFragmentManager(), null);
                return true;
            case R.id.about:
                // About us
                AboutUsDialog aboutUsDialog = AboutUsDialog.newInstance();
                //Muestro el diálogo
                aboutUsDialog.show(getFragmentManager(), null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // FIN MÉTODOS ACTIONBAR

    @Override
    public void action(int opcion) {
        switch(opcion){
            case LeaveHouseDialog.KEY_ACCION_PERMANECER_CASA: {
                Toast.makeText(this, R.string.leave_house_cancelled, Toast.LENGTH_SHORT).show();
            };break;

            case LeaveHouseDialog.KEY_ACCION_ABANCONAR_CASA: {
                // Se borra al usuario de la casa
                casaReference.child(idCasaUsuarioLogeado).child("companyeros").child(user.getUid()).removeValue();

                // Se acutualiza el valor de pertenencia a casa del usuario como objeto independiente
                companyeroReference.child("idCasa").setValue("");

                Toast.makeText(this, R.string.leave_house_accepted, Toast.LENGTH_SHORT).show();
            };break;
        }
    }

}

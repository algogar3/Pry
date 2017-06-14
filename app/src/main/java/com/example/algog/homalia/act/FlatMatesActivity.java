package com.example.algog.homalia.act;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.algog.homalia.ORM.Companyero;
import com.example.algog.homalia.R;
import com.example.algog.homalia.adapters.FlatmatesAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FlatMatesActivity extends AppCompatActivity {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    List<Companyero> items;
    private ActionBar actionBar;

    private Intent intent;
    private String homeId;

    private DatabaseReference mDatabase;
    private Query companyeroReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flat_mates);

        // Modificación de la action bar
        actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.btn_flatmates);

        // Se obtiene el intent por el cual se llamó a la actividad
        intent = this.getIntent();

        // Se obtienen los datos pasados en el intent
        homeId = intent.getExtras().getString(MainActivity.KEY_HOME_ID);

        // Inicializar Companyeros
        items = new ArrayList<>();

        // Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        companyeroReference = mDatabase.child("casas").child(homeId).child("companyeros");

        // Se obtienen todos los companyeros de la casa con el id pasado en el intent
        companyeroReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Se borra el contenido del arraylist para que no se dupliquen datos
                items.clear();

                for(DataSnapshot companyeros : dataSnapshot.getChildren()){
                    items.add(companyeros.getValue(Companyero.class));
                }

                // Se obtiene el RecyclerView
                recycler = (RecyclerView) findViewById(R.id.recycler_view_flatmates);
                recycler.setHasFixedSize(true);

                // USe usa un administrador para LinearLayout
                lManager = new LinearLayoutManager(FlatMatesActivity.this);
                recycler.setLayoutManager(lManager);

                // Se crea el adapter
                adapter = new FlatmatesAdapter(items, getApplicationContext());
                recycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}

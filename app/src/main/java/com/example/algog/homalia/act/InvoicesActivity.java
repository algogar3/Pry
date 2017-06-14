package com.example.algog.homalia.act;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.algog.homalia.ORM.Factura;
import com.example.algog.homalia.R;
import com.example.algog.homalia.adapters.InvoicesAdapter;
import com.example.algog.homalia.dialog.ConfirmDeleteInvoiceDialog;
import com.example.algog.homalia.dialog.ConfirmDeleteNoteDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InvoicesActivity extends AppCompatActivity implements View.OnClickListener, ConfirmDeleteInvoiceDialog.OnRealizarAccion {
    // Variables
    private ActionBar actionBar;
    private Intent intent;
    private String homeId;
    private String service;

    List<Factura> items;
    private RecyclerView recycler;
    private TextView empty_view;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private FloatingActionButton fab;

    private DatabaseReference mDatabase;
    private DatabaseReference invoicesReference;

    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoices);

        // Modificación de la action bar
        actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.invoices);

        // Se obtiene el intent por el cual se llamó a la actividad
        intent = this.getIntent();

        // Se obtienen los datos pasados en el intent
        homeId = intent.getExtras().getString(MainActivity.KEY_HOME_ID);
        service = intent.getExtras().getString(ExpensesActivity.KEY_SERVICE);

        // Inicializar Companyeros
        items = new ArrayList<>();

        // Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        invoicesReference = mDatabase.child("casas").child(homeId).child("servicios").child(service).child("facturas");

        // INICIO FUNCIONALIDAD SWIPE CARDS
        ItemTouchHelper.SimpleCallback simpleCallbackItemTouchHelper = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                // Se llama al diálogo de confirmación para eliminar la factura
                // Al deslizar la factura, se preguntará al usuario si realmente desea eliminar la nota
                ConfirmDeleteInvoiceDialog confirmDeleteInvoiceDialog = ConfirmDeleteInvoiceDialog.newInstance(position);
                //Me suscribo al evento onRealizarAccion del fragmento y lo implemento
                confirmDeleteInvoiceDialog.setOnRealizarAccionListener(InvoicesActivity.this);
                //Muestro el diálogo
                confirmDeleteInvoiceDialog.show(getFragmentManager(), null);


            }
        };

        itemTouchHelper = new ItemTouchHelper(simpleCallbackItemTouchHelper);
        // FIN FUNCIONALIDAD SWIPE CARDS

        // Se obtienen todos las facturas de la casa con el id pasado en el intent
        invoicesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Se inflan los elementos de la UI
                recycler = (RecyclerView) findViewById(R.id.recycler_view_invoices);
                recycler.setHasFixedSize(true);
                empty_view = (TextView) findViewById(R.id.empty_view);
                fab = (FloatingActionButton) findViewById(R.id.fab);

                // Escuchadores
                fab.setOnClickListener(InvoicesActivity.this);

                // Se borra el contenido del arraylist para que no se dupliquen datos
                items.clear();

                // Se rellena el dataset
                for(DataSnapshot facturas : dataSnapshot.getChildren()){
                    items.add(facturas.getValue(Factura.class));
                }

                // Se ordena el arraylist de facturas para mostrar las notas más recientes en la parte superior
                Collections.sort(items, new Comparator<Factura>() {
                    @Override
                    public int compare(Factura factura1, Factura factura2) {
                        return factura2.getFechaCargo().compareTo(factura1.getFechaCargo());
                    }
                });


                // Se comprueba si existen facturas a mostrar y se modifica la UI
                if(!items.isEmpty()){
                    recycler.setVisibility(View.VISIBLE);
                    empty_view.setVisibility(View.GONE);
                } else{
                    recycler.setVisibility(View.GONE);
                    empty_view.setVisibility(View.VISIBLE);
                }

                // Se usa un administrador para LinearLayout
                lManager = new LinearLayoutManager(InvoicesActivity.this);
                recycler.setLayoutManager(lManager);

                // Se crea el adapter
                adapter = new InvoicesAdapter(items, getApplicationContext());
                recycler.setAdapter(adapter);
                itemTouchHelper.attachToRecyclerView(recycler);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fab:{
                // El usuario quiere crear una nueva factura
                // Se pasa en el intent el nick del usuario activo
                intent = new Intent(this, CreateInvoiceActivity.class);
                intent.putExtra(ExpensesActivity.KEY_SERVICE, service);
                intent.putExtra(MainActivity.KEY_HOME_ID, homeId);
                // Se utiliza como requestCode 0, ya que siempre se llamará a la actividad con la misma finalidad
                startActivityForResult(intent, 0);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Toast.makeText(this, R.string.invoice_created, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.invoice_canceled, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void action(int action, int position) {
        switch(action){
            case ConfirmDeleteInvoiceDialog.KEY_CONFIRM_DELETE:{
                // El usuario ha confirmado la eliminación de la nota
                // Actualización de la base de datos de Firebase
                invoicesReference.child(items.get(position).getId()).removeValue();

                // Actualización de la UI
                items.remove(position);
                adapter.notifyDataSetChanged();

                Toast.makeText(this, R.string.confirmed_delete_invoice, Toast.LENGTH_SHORT).show();
            }; break;

            case ConfirmDeleteNoteDialog.KEY_CANCEL_DELETE: {
                // Se ha cancelado la eliminación de la factura
                Toast.makeText(this, R.string.canceled_delete_note, Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
        }
    }
}

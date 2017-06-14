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

import com.example.algog.homalia.ORM.Producto;
import com.example.algog.homalia.R;
import com.example.algog.homalia.adapters.ProductAdapter;
import com.example.algog.homalia.dialog.SwipeProductActionDiaolg;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity implements View.OnClickListener, SwipeProductActionDiaolg.OnOptionSelected {
    // Constantes
    public static final String KEY_POSITION = "key_position";

    // Variables
    private RecyclerView recycler;
    private TextView empty_view;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private FloatingActionButton fab;
    List<Producto> items;
    private ActionBar actionBar;

    private Intent intent;
    private String homeId;
    private String userUid;
    private String nickName;

    private DatabaseReference mDatabase;
    private DatabaseReference productsReference;
    private DatabaseReference companyeroReference;
    private DatabaseReference casaReference;

    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        // Modificación de la action bar
        actionBar = getSupportActionBar();
        actionBar.setTitle(R.string.btn_shopping_list);

        // Se obtiene el intent por el cual se llamó a la actividad
        intent = this.getIntent();

        // Se obtienen los datos pasados en el intent
        homeId = intent.getExtras().getString(MainActivity.KEY_HOME_ID);
        userUid = intent.getExtras().getString(MainActivity.KEY_USER_UID);
        nickName = intent.getExtras().getString(MainActivity.KEY_NICKNAME);

        // Inicializar Productos
        items = new ArrayList<>();

        // Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();
        productsReference = mDatabase.child("casas").child(homeId).child("productos");
        companyeroReference = mDatabase.child("companyeros").child(userUid);
        casaReference = mDatabase.child("casas").child(homeId).child("companyeros").child(userUid);

        // INICIO FUNCIONALIDAD SWIPE CARDS
        ItemTouchHelper.SimpleCallback simpleCallbackItemTouchHelper = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT){

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                // Al deslizar el producto, el usuario podrá eliminar el producto de la lista o indicar que lo ha comprado
                SwipeProductActionDiaolg swipeProductActionDiaolg = SwipeProductActionDiaolg.newInstance(position);
                //Me suscribo al evento onLogin del fragmento y lo implemento
                swipeProductActionDiaolg.setOnOptionSelectedListener(ShoppingListActivity.this);
                //Muestro el diálogo
                swipeProductActionDiaolg.show(getFragmentManager(), null);
            }
        };

        itemTouchHelper = new ItemTouchHelper(simpleCallbackItemTouchHelper);
        // FIN FUNCIONALIDAD SWIPE CARDS

        // Se obtienen todos los companyeros de la casa con el id pasado en el intent
        productsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Se inflan los elementos de la UI
                recycler = (RecyclerView) findViewById(R.id.recycler_view_products);
                recycler.setHasFixedSize(true);
                empty_view = (TextView) findViewById(R.id.empty_view);
                fab = (FloatingActionButton) findViewById(R.id.fab);

                // Escuchadores
                fab.setOnClickListener(ShoppingListActivity.this);

                // Se borra el contenido del arraylist para que no se dupliquen datos
                items.clear();

                // Se rellena el dataset
                for(DataSnapshot productos : dataSnapshot.getChildren()){
                    items.add(productos.getValue(Producto.class));
                }

                // Se ordena el arraylist de notas para mostrar las notas más recientes en la parte superior
                Collections.sort(items, new Comparator<Producto>() {
                        @Override
                        public int compare(Producto producto1, Producto producto2) {
                        return producto2.getFechaHoraCreacion().compareTo(producto1.getFechaHoraCreacion());
                    }
                });


                // Se comprueba si existen productos a mostrar y se modifica la UI
                if(!items.isEmpty()){
                    recycler.setVisibility(View.VISIBLE);
                    empty_view.setVisibility(View.GONE);
                } else{
                    recycler.setVisibility(View.GONE);
                    empty_view.setVisibility(View.VISIBLE);
                }

                // Se usa un administrador para LinearLayout
                lManager = new LinearLayoutManager(ShoppingListActivity.this);
                recycler.setLayoutManager(lManager);

                // Se crea el adapter
                adapter = new ProductAdapter(items, getApplicationContext());
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
                // El usuario quiere añadir un nuevo producto a la lista
                // Se pasa en el intent el nick del usuario activo
                intent = new Intent(this, CreateProductActivity.class);
                intent.putExtra(MainActivity.KEY_USER_UID, userUid);
                intent.putExtra(MainActivity.KEY_HOME_ID, homeId);
                intent.putExtra(MainActivity.KEY_NICKNAME, nickName);
                // Se utiliza como requestCode 0, ya que siempre se llamará a la actividad con la misma finalidad
                startActivityForResult(intent, 0);
            }
        }
    }

    @Override
    public void onOptionSelected(int option, int position, final double cost) {
        // Acciones dependiendo de la opción que haya seleccionado el usuario
        switch(option){
            case SwipeProductActionDiaolg.KEY_CANCELED: {
                // El USUARIO HA CANCELADO LA ACCIÓN
                // Actualización de la UI
                adapter.notifyDataSetChanged();
            }; break;

            case SwipeProductActionDiaolg.KEY_DELETE: {
                // El USUARIO QUIERE BORRAR EL PRODUCTO DE LA LISTA
                // Actualización de la base de datos de Firebase
                productsReference.child(items.get(position).getId()).removeValue();

                // Actualización de la UI
                items.remove(position);
                adapter.notifyDataSetChanged();

                // Se notifica al usuario que el producto ha sido borrado
                Toast.makeText(ShoppingListActivity.this, R.string.product_deleted , Toast.LENGTH_SHORT).show();
            }; break;

            case SwipeProductActionDiaolg.KEY_BUY: {
                // El USUARIO HA COMPRADO EL PRODUCTO
                // Actualización de la base de datos de Firebase
                productsReference.child(items.get(position).getId()).removeValue();

                // Actualización de la UI
                items.remove(position);
                adapter.notifyDataSetChanged();

                // ACTUALIZACIÓN DE LA BASE DE DATOS DE FIREBASE
                // Actualización del objeto independiente companyero
                companyeroReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Se recupera el gasto acutal
                        double costeAux = Double.valueOf(dataSnapshot.child("gasto").getValue().toString());

                        // Se suma el coste del producto comprado
                        costeAux += cost;

                        // Se actualiza el gasto con su nuevo valor
                        companyeroReference.child("gasto").setValue(costeAux);

                        // Se notifica al usuario que el producto ha sido comprado
                        Toast.makeText(ShoppingListActivity.this, R.string.product_purchased , Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                // Actualización del objeto companyero dentro de la casa
                casaReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Se recupera el gasto acutal
                        double costeAux = Double.valueOf(dataSnapshot.child("gasto").getValue().toString());

                        // Se suma el coste del producto comprado
                        costeAux += cost;

                        // Se actualiza el gasto con su nuevo valor
                        casaReference.child("gasto").setValue(costeAux);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }; break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if(resultCode == RESULT_OK){
            Toast.makeText(this, R.string.product_created, Toast.LENGTH_SHORT).show();
         } else {
            Toast.makeText(this, R.string.product_canceled, Toast.LENGTH_SHORT).show();
         }
    }
}


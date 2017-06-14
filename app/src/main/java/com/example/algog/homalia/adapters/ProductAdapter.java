package com.example.algog.homalia.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.algog.homalia.ORM.Producto;
import com.example.algog.homalia.R;

import java.util.List;

/**
 * Created by algog on 07/06/2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    // Constantes
    private final static int KEY_ALIMENTACION = 0;
    private final static int KEY_LIMPIEZA = 1;
    private final static int KEY_OTROS = 2;

    // Variables
    private List<Producto> items;
    private Context contexto;

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView productCategoryImage;
        public TextView name;
        public TextView addedBy;
        public TextView fechaHoraCreacion;
        public boolean prioritario;
        public CardView cv;


        public ProductViewHolder(View v) {
            super(v);
            productCategoryImage = (ImageView) v.findViewById(R.id.product_category_image);
            name = (TextView) v.findViewById(R.id.name);
            addedBy = (TextView) v.findViewById(R.id.addedBy);
            fechaHoraCreacion = (TextView) v.findViewById(R.id.dateAdded);
            cv = (CardView) v.findViewById(R.id.cv);

            CardView cv = (CardView)itemView.findViewById(R.id.cv);
        }
    }

    public ProductAdapter(List<Producto> items, Context contexto) {
        this.items = items;
        this.contexto = contexto;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_card, viewGroup, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder viewHolder, int i) {
        viewHolder.name.setText(items.get(i).getNombre());
        viewHolder.addedBy.setText(contexto.getString(R.string.added_by) + items.get(i).getAnyadidoPor());
        viewHolder.fechaHoraCreacion.setText(items.get(i).getFechaHoraCreacion());

        switch (items.get(i).getCategoria()){
            case KEY_ALIMENTACION:{
                viewHolder.productCategoryImage.setImageResource(R.drawable.ic_local_dining_black_48dp);
            };break;

            case KEY_LIMPIEZA:{
                viewHolder.productCategoryImage.setImageResource(R.drawable.ic_format_paint_black_48dp);
            };break;

            case KEY_OTROS:{
                viewHolder.productCategoryImage.setImageResource(R.drawable.ic_shopping_cart_black_48dp);
            };break;
        }

        // Asignación de color de la tarjeta en función de la categoría y de la prioridad del producto
        switch (items.get(i).getCategoria()){
            case KEY_ALIMENTACION:{
                if(items.get(i).isPrioritario()){
                    // Prioritario
                    viewHolder.cv.setCardBackgroundColor(Color.rgb(243, 247, 129));
                } else{
                    // No prioritario
                    viewHolder.cv.setCardBackgroundColor(Color.rgb(245, 246, 206));
                }
            };break;

            case KEY_LIMPIEZA:{
                if(items.get(i).isPrioritario()){
                    // Prioritario
                    viewHolder.cv.setCardBackgroundColor(Color.rgb(159, 247, 129));
                } else{
                    // No prioritario
                    viewHolder.cv.setCardBackgroundColor(Color.rgb(216, 246, 206));
                }
            };break;

            case KEY_OTROS:{
                if(items.get(i).isPrioritario()){
                    // Prioritario
                    viewHolder.cv.setCardBackgroundColor(Color.rgb(129, 218, 245));
                } else{
                    // No prioritario
                    viewHolder.cv.setCardBackgroundColor(Color.rgb(206, 236, 245));
                }
            };break;
        }
    }
}

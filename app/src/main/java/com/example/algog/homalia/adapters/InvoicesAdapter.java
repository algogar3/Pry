package com.example.algog.homalia.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.algog.homalia.ORM.Factura;
import com.example.algog.homalia.R;
import com.example.algog.homalia.act.ExpensesActivity;

import java.util.List;

/**
 * Created by algog on 10/06/2017.
 */

public class InvoicesAdapter extends RecyclerView.Adapter<InvoicesAdapter.InvoicesViewHolder> {

    // Variables
    private List<Factura> items;
    private Context contexto;

    public static class InvoicesViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView importe;
        public TextView fechaCargo;
        public TextView servicio;
        public ImageView serviceImage;
        public CardView cv;

        public InvoicesViewHolder(View v) {
            super(v);
            importe = (TextView) v.findViewById(R.id.cost);
            fechaCargo = (TextView) v.findViewById(R.id.date_charge);
            servicio = (TextView) v.findViewById(R.id.service);
            serviceImage = (ImageView) v.findViewById(R.id.service_image);
            cv = (CardView) v.findViewById(R.id.cv);
        }
    }

    public InvoicesAdapter(List<Factura> items, Context contexto) {
        this.items = items;
        this.contexto = contexto;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public InvoicesAdapter.InvoicesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.invoice_card, viewGroup, false);
        return new InvoicesAdapter.InvoicesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(InvoicesAdapter.InvoicesViewHolder viewHolder, int i) {
        viewHolder.importe.setText(contexto.getString(R.string.amount_card) + String.valueOf(items.get(i).getImporte() + " €"));
        viewHolder.fechaCargo.setText(contexto.getString(R.string.invoice_date) + items.get(i).getFechaCargo());

        // Asignación de de título y de color e imagen de la tarjeta
        if(items.get(i).getServicio().equals(ExpensesActivity.KEY_ELECTRICITY)){
            viewHolder.servicio.setText(contexto.getString(R.string.electricity));
            viewHolder.serviceImage.setImageResource(R.drawable.electricity_service_3);
            viewHolder.cv.setCardBackgroundColor(Color.rgb(229, 170, 23));
        }
        else if(items.get(i).getServicio().equals(ExpensesActivity.KEY_WATER)){
            viewHolder.servicio.setText(contexto.getString(R.string.water));
            viewHolder.serviceImage.setImageResource(R.drawable.water_service_3);
            viewHolder.cv.setCardBackgroundColor(Color.rgb(119, 179, 212));
        }
        else if(items.get(i).getServicio().equals(ExpensesActivity.KEY_GAS)){
            viewHolder.servicio.setText(contexto.getString(R.string.gas));
            viewHolder.serviceImage.setImageResource(R.drawable.gas_service_3);
            viewHolder.cv.setCardBackgroundColor(Color.rgb(255, 111, 0));
        }
        else if(items.get(i).getServicio().equals(ExpensesActivity.KEY_PHONE_AND_INTERNET)){
            viewHolder.servicio.setText(contexto.getString(R.string.phone_internet));
            viewHolder.serviceImage.setImageResource(R.drawable.phone_service_3);
            viewHolder.cv.setCardBackgroundColor(Color.rgb(117, 183, 59));
        }
    }
}

package com.example.algog.homalia.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.algog.homalia.ORM.Companyero;
import com.example.algog.homalia.R;

import java.util.List;

/**
 * Created by algog on 06/06/2017.
 */

public class FlatmatesAdapter extends RecyclerView.Adapter<FlatmatesAdapter.FlatmatesViewHolder> {

    // Variables
    private List<Companyero> items;
    private Context contexto;

    public static class FlatmatesViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView nickname;
        public TextView nameSurname;
        public TextView phoneNumber;
        public TextView expense;
        public TextView description;


        public FlatmatesViewHolder(View v) {
            super(v);
            nickname = (TextView) v.findViewById(R.id.nickname);
            nameSurname = (TextView) v.findViewById(R.id.name_surname);
            phoneNumber = (TextView) v.findViewById(R.id.phone);
            expense = (TextView) v.findViewById(R.id.expense);
            description = (TextView) v.findViewById(R.id.description);
        }
    }

    public FlatmatesAdapter(List<Companyero> items, Context contexto) {
        this.items = items;
        this.contexto = contexto;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public FlatmatesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.flatmate_card, viewGroup, false);
        return new FlatmatesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FlatmatesViewHolder viewHolder, int i) {
        viewHolder.nickname.setText(items.get(i).getNick());
        viewHolder.nameSurname.setText(contexto.getString(R.string.name) + items.get(i).getNombre() + " " + items.get(i).getApellidos());
        viewHolder.phoneNumber.setText(contexto.getString(R.string.phone_number) + items.get(i).getTelefono());
        viewHolder.expense.setText(contexto.getString(R.string.accumulated_expenses) + String.valueOf(items.get(i).getGasto()) + " €");
        viewHolder.description.setText(contexto.getString(R.string.description_flatmate) + items.get(i).getDescripcion());
    }
}



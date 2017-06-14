package com.example.algog.homalia.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.algog.homalia.ORM.Nota;
import com.example.algog.homalia.R;

import java.util.List;

/**
 * Created by algog on 06/06/2017.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    // Variables
    private List<Nota> items;
    private Context contexto;

    public static class NotesViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView nickname;
        public TextView dateTime;
        public TextView content;

        public NotesViewHolder(View v) {
            super(v);
            nickname = (TextView) v.findViewById(R.id.nickname);
            dateTime = (TextView) v.findViewById(R.id.date_time);
            content = (TextView) v.findViewById(R.id.content);
        }
    }

    public NotesAdapter(List<Nota> items, Context contexto) {
        this.items = items;
        this.contexto = contexto;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.note_card, viewGroup, false);
        return new NotesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NotesViewHolder viewHolder, int i) {
        viewHolder.nickname.setText(items.get(i).getNicknameCreador());
        viewHolder.dateTime.setText(items.get(i).getFechaHoraCreacion());
        viewHolder.content.setText(items.get(i).getMensaje());
    }
}
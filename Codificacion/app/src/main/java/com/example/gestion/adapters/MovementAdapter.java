package com.example.gestion.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestion.R;
import com.example.gestion.cache.Movements;

import java.util.List;

public class MovementAdapter extends RecyclerView.Adapter<MovementViewHolder> {

    private final Context context;
    private final List<Movements> list;

    public MovementAdapter(Context context, List<Movements> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MovementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_movement, parent, false);
        return new MovementViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MovementViewHolder holder, int position) {

        Movements m = list.get(position);

        holder.tvId.setText("ID: " + m.id);
        holder.tvFecha.setText(m.date);
        holder.tvMonto.setText("$" + m.mount);
        holder.tvTipo.setText(m.type ? "Ingreso" : "Gasto");
        holder.tvCategoria.setText("Cat: " + m.category);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

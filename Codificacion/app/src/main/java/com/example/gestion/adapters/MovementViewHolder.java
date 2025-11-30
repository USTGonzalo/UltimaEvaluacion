package com.example.gestion.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestion.R;

public class MovementViewHolder extends RecyclerView.ViewHolder {

    TextView tvId, tvFecha, tvMonto, tvTipo, tvCategoria;

    public MovementViewHolder(@NonNull View itemView) {
        super(itemView);

        tvId = itemView.findViewById(R.id.tvId);
        tvFecha = itemView.findViewById(R.id.tvFecha);
        tvMonto = itemView.findViewById(R.id.tvMonto);
        tvTipo = itemView.findViewById(R.id.tvTipo);
        tvCategoria = itemView.findViewById(R.id.tvCategoria);
    }
}

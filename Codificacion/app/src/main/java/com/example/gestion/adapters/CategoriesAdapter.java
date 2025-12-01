package com.example.gestion.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestion.cache.Categories;
import com.example.gestion.R;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private List<Categories> categoriesList;
    private List<Categories> categoriesListFull;

    public CategoriesAdapter(List<Categories> categoriesList) {
        this.categoriesList = categoriesList;
        this.categoriesListFull = new ArrayList<>(categoriesList); // Copia completa para filtrar
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categories cat = categoriesList.get(position);

        holder.txtId.setText("ID: " + cat.getId());
        holder.txtName.setText(cat.getName());
        holder.txtDescription.setText(cat.getDescription());
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    // FILTRADO
    public void filter(String text) {
        categoriesList.clear();

        if (text.isEmpty()) {
            categoriesList.addAll(categoriesListFull);
        } else {
            text = text.toLowerCase();

            for (Categories item : categoriesListFull) {
                if (item.getName().toLowerCase().contains(text) ||
                        item.getDescription().toLowerCase().contains(text) ||
                        String.valueOf(item.getId()).contains(text)) {

                    categoriesList.add(item);
                }
            }
        }

        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtId, txtName, txtDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtId = itemView.findViewById(R.id.txtId);
            txtName = itemView.findViewById(R.id.txtName);
            txtDescription = itemView.findViewById(R.id.txtDescription);
        }
    }
}

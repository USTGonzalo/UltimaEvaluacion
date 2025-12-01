package com.example.gestion.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestion.EditCategoryActivity;
import com.example.gestion.cache.Categories;
import com.example.gestion.R;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private List<Categories> categoriesList;
    private List<Categories> categoriesListFull;
    private Context context;

    public CategoriesAdapter(Context context, List<Categories> categoriesList) {
        this.context = context;
        this.categoriesList = categoriesList;
        this.categoriesListFull = new ArrayList<>(categoriesList);
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

        holder.txtId.setText(String.valueOf(cat.getId()));
        holder.txtName.setText(cat.getName());
        holder.txtDescription.setText(cat.getDescription());

        // Click en la tarjeta
        holder.itemView.setOnClickListener(v -> {
            int realPosition = holder.getAbsoluteAdapterPosition();
            if (realPosition == RecyclerView.NO_POSITION) return;

            Categories selected = categoriesList.get(realPosition);

            Intent intent = new Intent(context, EditCategoryActivity.class);
            intent.putExtra("id", selected.getId());
            intent.putExtra("name", selected.getName());
            intent.putExtra("description", selected.getDescription());

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    // FILTRO
    public void filter(String text) {
        categoriesList.clear();

        if (text.isEmpty()) {
            categoriesList.addAll(categoriesListFull);
        } else {
            text = text.toLowerCase();

            for (Categories item : categoriesListFull) {
                if (item.getName().toLowerCase().contains(text)
                        || item.getDescription().toLowerCase().contains(text)
                        || String.valueOf(item.getId()).contains(text)) {

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

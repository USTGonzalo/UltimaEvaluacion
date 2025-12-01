package com.example.gestion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestion.database.CategoriesDatabase;

public class EditCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText txtId = findViewById(R.id.TxtIdCategory);
        EditText txtName = findViewById(R.id.TxtRename);
        EditText txtDescription = findViewById(R.id.TxtEditDescription);

        // Recibir los datos enviados desde la tarjeta
        Intent intent = getIntent();
        int categoryId = 0;

        if (intent != null) {
            categoryId = intent.getIntExtra("id", 0);
            String name = intent.getStringExtra("name");
            String description = intent.getStringExtra("description");

            txtId.setText(String.valueOf(categoryId));
            txtName.setText(name);
            txtDescription.setText(description);
        }

        // Instancia de la base de datos
        CategoriesDatabase db = new CategoriesDatabase(this);

        // BOTÓN GUARDAR
        Button BtnSave = findViewById(R.id.BtnSave2);
        int finalCategoryId = categoryId;

        BtnSave.setOnClickListener(v -> {
            String newName = txtName.getText().toString().trim();
            String newDescription = txtDescription.getText().toString().trim();

            if (newName.isEmpty()) {
                Toast.makeText(this, "El nombre no puede estar vacío.", Toast.LENGTH_LONG).show();
                return;
            }

            // Actualizar en la base de datos
            int rowsAffected = db.updateCategory(finalCategoryId, newName, newDescription);

            if (rowsAffected > 0) {
                Toast.makeText(this, "Categoría actualizada correctamente.", Toast.LENGTH_LONG).show();
                finish(); // volver atrás
            } else {
                Toast.makeText(this, "Error al actualizar la categoría.", Toast.LENGTH_LONG).show();
            }
        });

        // BOTÓN CANCELAR
        Button BtnCancel = findViewById(R.id.BtnCancel2);
        BtnCancel.setOnClickListener(v -> finish());
    }
}
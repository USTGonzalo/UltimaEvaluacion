package com.example.gestion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestion.database.CategoriesDatabase;

public class AddCategories extends AppCompatActivity {

    private EditText txtName, txtDesc;
    private Button btnSaveCat, btnCancelCat;
    private CategoriesDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_categories);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new CategoriesDatabase(this);

        txtName = findViewById(R.id.TxtName);
        txtDesc = findViewById(R.id.TxtDesc);
        btnSaveCat = findViewById(R.id.BtnSaveCat);
        btnCancelCat = findViewById(R.id.BtnCancelCat);

        // Guardar categoría
        btnSaveCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCategory();
            }
        });

        // Cancelar
        btnCancelCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveCategory() {
        String name = txtName.getText().toString().trim();
        String desc = txtDesc.getText().toString().trim();

        // Validaciones
        if (name.isEmpty()) {
            Toast.makeText(this, "Debe ingresar un nombre", Toast.LENGTH_LONG).show();
            return;
        }

        if (desc.isEmpty()) {
            Toast.makeText(this, "Debe ingresar una descripción", Toast.LENGTH_LONG).show();
            return;
        }

        long id = db.insertCategory(name, desc);

        if (id != -1) {
            Toast.makeText(this, "Categoría guardada correctamente", Toast.LENGTH_LONG).show();
            finish(); // Cerrar actividad
        } else {
            Toast.makeText(this, "Error al guardar la categoría", Toast.LENGTH_LONG).show();
        }
    }
}

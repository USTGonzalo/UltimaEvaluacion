package com.example.gestion;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestion.cache.Categories;
import com.example.gestion.cache.Movements;
import com.example.gestion.database.CategoriesDatabase;
import com.example.gestion.database.MovementsDatabase;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    private List<String> ids;
    private int selectedCategoryId = -1; // PARA GUARDAR LA CATEGORÍA NUEVA
    private long currentMovementId = -1; // ID DEL MOVIMIENTO ACTUAL
    private String currentPhotoUrl = ""; // URL DE LA FOTO
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button Exit = findViewById(R.id.btnCancel2);
        Exit.setOnClickListener(v -> finish());

        EditText txtMount = findViewById(R.id.txtMount2);
        EditText txtDate = findViewById(R.id.txtDate2);
        RadioButton radioIngreso = findViewById(R.id.RadioIngreso);
        RadioButton radioGasto = findViewById(R.id.RadioGasto);
        Button btnCategorias = findViewById(R.id.btnCategories2);
        ImageView imgPhoto = findViewById(R.id.btnComp2);
        Spinner spinner = findViewById(R.id.SpinnerIDS);

        MovementsDatabase db = new MovementsDatabase(this);

        // Cargar IDs de movimientos
        ids = db.getAllMovementIds();

        if (ids == null || ids.isEmpty()) {
            ids = new ArrayList<>();
            ids.add("Sin registros");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_white,
                ids
        );
        adapter.setDropDownViewResource(R.layout.spinner_white);
        spinner.setAdapter(adapter);

        // Cuando selecciona un movimiento
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedId = ids.get(position);
                if (selectedId.equals("Sin registros")) return;

                currentMovementId = Long.parseLong(selectedId);

                Movements mov = db.getMovementById(currentMovementId);
                if (mov == null) return;

                // Rellenar campos
                txtMount.setText(mov.mount);
                txtDate.setText(mov.date);

                // Tipo
                if (mov.type) radioIngreso.setChecked(true);
                else radioGasto.setChecked(true);

                // Cargar categoría desde SQLite
                try {
                    CategoriesDatabase catDb = new CategoriesDatabase(EditActivity.this);
                    selectedCategoryId = Integer.parseInt(String.valueOf(mov.category)); // Guardamos el ID actual

                    Categories cat = catDb.getCategoryById(selectedCategoryId);

                    if (cat != null) {
                        btnCategorias.setText("Categoría: " + cat.getName());
                    } else {
                        btnCategorias.setText("Categoría: (No encontrada)");
                    }

                } catch (Exception e) {
                    btnCategorias.setText("Categoría: Error");
                }

                // Imagen
                if (mov.photo != null && !mov.photo.isEmpty()) {
                    try {
                        Uri imageUri = Uri.parse(mov.photo);
                        Bitmap bitmap = null;

                        try (InputStream inputStream = getContentResolver().openInputStream(imageUri)) {
                            if (inputStream != null) {
                                bitmap = BitmapFactory.decodeStream(inputStream);
                            }
                        }

                        if (bitmap != null) imgPhoto.setImageBitmap(bitmap);
                        else imgPhoto.setImageResource(R.drawable.camera);

                    } catch (Exception e) {
                        imgPhoto.setImageResource(R.drawable.camera);
                    }
                } else {
                    imgPhoto.setImageResource(R.drawable.camera);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // CAMBIAR CATEGORÍA
        btnCategorias.setOnClickListener(v -> {

            CategoriesDatabase catDb = new CategoriesDatabase(EditActivity.this);
            List<Categories> categories = catDb.getAllCategories();

            if (categories.isEmpty()) {
                Toast.makeText(EditActivity.this, "No hay categorías registradas", Toast.LENGTH_SHORT).show();
                return;
            }

            List<String> names = new ArrayList<>();
            for (Categories c : categories) {
                names.add(c.getName());
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
            builder.setTitle("Seleccione una categoría");

            builder.setItems(names.toArray(new String[0]), (dialog, index) -> {
                Categories selected = categories.get(index);
                selectedCategoryId = selected.getId();  // Guardar ID REAL
                btnCategorias.setText("Categoría: " + selected.getName());
            });

            builder.show();
        });

        // ELIMINAR
        ImageButton Delete = findViewById(R.id.BtnDeleteMov);
        Delete.setOnClickListener(v -> {

            String selectedId = (String) spinner.getSelectedItem();
            if (selectedId == null || selectedId.equals("Sin registros")) {
                Toast.makeText(EditActivity.this, "No hay registros", Toast.LENGTH_SHORT).show();
                return;
            }

            long idToDelete = Long.parseLong(selectedId);

            new AlertDialog.Builder(EditActivity.this)
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Eliminar el movimiento con ID " + idToDelete + "?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {

                        MovementsDatabase db2 = new MovementsDatabase(EditActivity.this);
                        int rows = db2.deleteMovement(idToDelete);

                        if (rows > 0) {
                            Toast.makeText(EditActivity.this, "Eliminado", Toast.LENGTH_SHORT).show();

                            ids = db.getAllMovementIds();
                            if (ids.isEmpty()) ids.add("Sin registros");

                            ArrayAdapter<String> newAdapter = new ArrayAdapter<>(
                                    EditActivity.this,
                                    R.layout.spinner_white,
                                    ids
                            );
                            spinner.setAdapter(newAdapter);

                            txtMount.setText("");
                            txtDate.setText("");
                            radioIngreso.setChecked(false);
                            radioGasto.setChecked(false);
                            btnCategorias.setText("Categoría");
                            imgPhoto.setImageResource(R.drawable.camera);

                        } else {
                            Toast.makeText(EditActivity.this, "Error al eliminar", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        // GUARDAR CAMBIOS
        Button Save = findViewById(R.id.btnSave2);
        Save.setOnClickListener(v -> {

            if (currentMovementId == -1) {
                Toast.makeText(EditActivity.this, "Seleccione un movimiento", Toast.LENGTH_SHORT).show();
                return;
            }

            String mount = txtMount.getText().toString();
            String date = txtDate.getText().toString();
            boolean type = radioIngreso.isChecked();

            MovementsDatabase db3 = new MovementsDatabase(EditActivity.this);

            int result = db3.updateMovement(
                    currentMovementId,
                    mount,
                    date,
                    type,
                    selectedCategoryId,
                    currentPhotoUrl
            );

            if (result > 0) {
                Toast.makeText(EditActivity.this, "Movimiento actualizado", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EditActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

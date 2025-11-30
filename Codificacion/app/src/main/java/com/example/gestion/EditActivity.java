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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestion.cache.Movements;
import com.example.gestion.database.MovementsDatabase;

import java.io.InputStream;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    private List<String> ids;
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
        Exit.setOnClickListener(v -> {finish();});

        EditText txtMount = findViewById(R.id.txtMount2);
        EditText txtDate = findViewById(R.id.txtDate2);
        RadioButton radioIngreso = findViewById(R.id.RadioIngreso);
        RadioButton radioGasto = findViewById(R.id.RadioGasto);
        Button btnCategorias = findViewById(R.id.btnCategories2);
        ImageView imgPhoto = findViewById(R.id.btnComp2);
        Spinner spinner = findViewById(R.id.SpinnerIDS);

        MovementsDatabase db = new MovementsDatabase(this);

        //Carga los Ids en el spinner
        ids = db.getAllMovementIds();

        if (ids == null || ids.isEmpty()) {
            ids = new java.util.ArrayList<>();
            ids.add("Sin registros");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_white,
                ids
        );
        adapter.setDropDownViewResource(R.layout.spinner_white);
        spinner.setAdapter(adapter);

        //Cuando se seleccione un ID
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedId = ids.get(position);

                if (selectedId.equals("Sin registros")) return;

                // Convertir ID a número
                long idMovement = Long.parseLong(selectedId);

                // Obtener movimiento desde SQLite
                Movements mov = db.getMovementById(idMovement);
                if (mov == null) return;

                //Rellenar campos
                txtMount.setText(mov.mount);
                txtDate.setText(mov.date);

                if (mov.type) {
                    radioIngreso.setChecked(true);
                } else {
                    radioGasto.setChecked(true);
                }

                btnCategorias.setText("Categoría: " + mov.category);

                if (mov.photo != null && !mov.photo.isEmpty()) {
                    try {
                        Uri imageUri = Uri.parse(mov.photo); // convertir string a Uri
                        Bitmap bitmap = null;

                        try (InputStream inputStream = getContentResolver().openInputStream(imageUri)) {
                            if (inputStream != null) {
                                bitmap = BitmapFactory.decodeStream(inputStream);
                            }
                        }

                        if (bitmap != null) {
                            imgPhoto.setImageBitmap(bitmap);
                            Toast.makeText(EditActivity.this, "Imagen cargada desde galería", Toast.LENGTH_SHORT).show();
                        } else {
                            imgPhoto.setImageResource(R.drawable.camera);
                            Toast.makeText(EditActivity.this, "No se pudo decodificar la imagen", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        imgPhoto.setImageResource(R.drawable.camera);
                        Toast.makeText(EditActivity.this, "Error al cargar imagen", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    imgPhoto.setImageResource(R.drawable.camera);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        ImageButton Delete = findViewById(R.id.BtnDeleteMov);
        Delete.setOnClickListener(v -> {

            String selectedId = (String) spinner.getSelectedItem();

            // Validación
            if (selectedId == null || selectedId.equals("Sin registros")) {
                Toast.makeText(EditActivity.this, "No hay registros para eliminar", Toast.LENGTH_SHORT).show();
                return;
            }

            long idToDelete = Long.parseLong(selectedId);

            // Mostrar confirmación
            new androidx.appcompat.app.AlertDialog.Builder(EditActivity.this)
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Seguro que deseas eliminar el movimiento con ID " + idToDelete + "?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {

                        MovementsDatabase db2 = new MovementsDatabase(EditActivity.this);
                        int rows = db2.deleteMovement(idToDelete);

                        if (rows > 0) {
                            Toast.makeText(EditActivity.this, "Movimiento eliminado", Toast.LENGTH_SHORT).show();

                            // Actualizar spinner
                            ids = db.getAllMovementIds();
                            if (ids.isEmpty()) {
                                ids.add("Sin registros");
                            }

                            ArrayAdapter<String> newAdapter = new ArrayAdapter<>(
                                    EditActivity.this,
                                    R.layout.spinner_white,
                                    ids
                            );
                            newAdapter.setDropDownViewResource(R.layout.spinner_white);
                            spinner.setAdapter(newAdapter);

                            // Vaciar campos
                            txtMount.setText("");
                            txtDate.setText("");
                            radioIngreso.setChecked(false);
                            radioGasto.setChecked(false);
                            btnCategorias.setText("Categoría");
                            imgPhoto.setImageResource(R.drawable.camera);

                        } else {
                            Toast.makeText(EditActivity.this, "No se pudo eliminar", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        Button Save = findViewById(R.id.btnSave2);
        Save.setOnClickListener(v -> {

        });
    }
}
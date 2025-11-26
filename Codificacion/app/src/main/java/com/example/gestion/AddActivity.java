package com.example.gestion;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestion.database.MovementsDatabase;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    private Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText txtDate = findViewById(R.id.TxtDate);

        // Selector de fecha
        txtDate.setOnClickListener(v -> {

            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddActivity.this,
                    (view, y, m, d) -> {
                        String selectedDate = d + "/" + (m + 1) + "/" + y;
                        txtDate.setText(selectedDate);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });

        // categorías
        Button btnCat = findViewById(R.id.BtnCategories);
        String[] categorias = {"Comida", "Transporte", "Casa", "Trabajo", "Ocio"};

        btnCat.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
            builder.setTitle("Categorías");

            builder.setItems(categorias, (dialog, which) -> {
                btnCat.setText(categorias[which]);
            });

            builder.show();
        });

        // Guardar información
        Button BtnSave = findViewById(R.id.BtnSave);
        BtnSave.setOnClickListener(v -> {

            EditText txtMount = findViewById(R.id.TxtMount);
            RadioGroup radioGroup = findViewById(R.id.radioGroup);
            RadioButton radioSelected = findViewById(radioGroup.getCheckedRadioButtonId());

            String monto = txtMount.getText().toString().trim();
            String fecha = txtDate.getText().toString().trim();
            String tipo = (radioSelected != null) ? radioSelected.getText().toString() : "No seleccionado";
            String categoria = btnCat.getText().toString();
            String url = (imageUri != null) ? imageUri.toString() : "";

            // Validaciones
            if (monto.isEmpty() || fecha.isEmpty() || tipo.equals("No seleccionado") || categoria.equals("Categorías")) {
                Toast.makeText(this, "Debe completar todos los campos", Toast.LENGTH_LONG).show();
                return;
            }

            // Guardar en la base de datos
            MovementsDatabase db = new MovementsDatabase(AddActivity.this);

            boolean typeBool = tipo.equalsIgnoreCase("Ingreso");

            long id = db.insertMovement(
                    monto,
                    fecha,
                    typeBool,
                    1,
                    url
            );

            if (id > 0) {
                Toast.makeText(this, "Guardado correctamente (ID: " + id + ")", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Error al guardar", Toast.LENGTH_LONG).show();
            }
        });

        // Seleccionar imagen
        ImageView btnComp = findViewById(R.id.BtnComp);

        btnComp.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE);
        });

        // Cancelar y volver
        Button BtnCancel = findViewById(R.id.BtnCancel);
        BtnCancel.setOnClickListener(v -> finish());
    }

    // Muestra la imagen
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            ImageView btnComp = findViewById(R.id.BtnComp);
            btnComp.setImageURI(imageUri);
        }
    }
}
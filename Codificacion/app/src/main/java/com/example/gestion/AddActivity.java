package com.example.gestion;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestion.cache.Categories;
import com.example.gestion.database.CategoriesDatabase;
import com.example.gestion.database.MovementsDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    private static final int TAKE_PHOTO = 200;
    private static final int CAMERA_PERMISSION_CODE = 201;

    private Uri imageUri = null;
    private Uri photoUri = null;

    int selectedCategoryId = -1;
    String selectedCategoryName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add);

        // Ajuste de pantalla completa
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Solicitar permiso de cámara si falta
        checkCameraPermission();

        EditText txtDate = findViewById(R.id.TxtDate);

        // Selección de fecha
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

        // Botón seleccionar categoría
        Button btnCat = findViewById(R.id.BtnCategories);
        btnCat.setOnClickListener(v -> {

            CategoriesDatabase catDB = new CategoriesDatabase(AddActivity.this);
            List<Categories> lista = catDB.getAllCategories();

            if (lista.isEmpty()) {
                Toast.makeText(this, "No hay categorías registradas", Toast.LENGTH_LONG).show();
                return;
            }

            List<String> nombres = new ArrayList<>();
            for (Categories c : lista) nombres.add(c.getName());

            AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
            builder.setTitle("Seleccionar categoría");

            builder.setItems(nombres.toArray(new String[0]), (dialog, which) -> {
                selectedCategoryId = lista.get(which).getId();
                selectedCategoryName = lista.get(which).getName();
                btnCat.setText(selectedCategoryName);
            });

            builder.show();
        });

        // Guardar movimiento
        Button BtnSave = findViewById(R.id.BtnSave);
        BtnSave.setOnClickListener(v -> {

            EditText txtMount = findViewById(R.id.TxtMount);
            RadioGroup radioGroup = findViewById(R.id.radioGroup);
            RadioButton radioSelected = findViewById(radioGroup.getCheckedRadioButtonId());

            String monto = txtMount.getText().toString().trim();
            String fecha = txtDate.getText().toString().trim();
            String tipo = (radioSelected != null) ? radioSelected.getText().toString() : "";
            String url = (imageUri != null) ? imageUri.toString() : "";

            if (monto.isEmpty() || fecha.isEmpty() || tipo.isEmpty() || selectedCategoryId == -1) {
                Toast.makeText(this, "Debe completar todos los campos", Toast.LENGTH_LONG).show();
                return;
            }

            boolean typeBool = tipo.equalsIgnoreCase("Ingreso");

            MovementsDatabase db = new MovementsDatabase(AddActivity.this);

            long id = db.insertMovement(
                    monto,
                    fecha,
                    typeBool,
                    selectedCategoryId,
                    url
            );

            if (id > 0) {
                Toast.makeText(this, "Guardado correctamente (ID: " + id + ")", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Error al guardar", Toast.LENGTH_LONG).show();
            }
        });

        // Cargar imagen / abrir cámara
        ImageView btnComp = findViewById(R.id.BtnComp);
        btnComp.setOnClickListener(v -> {
            if (checkCameraPermission()) {
                openCamera();
            }
        });

        // Cancelar
        Button BtnCancel = findViewById(R.id.BtnCancel);
        BtnCancel.setOnClickListener(v -> finish());
    }

    // ----------- MANEJO DE PERMISOS ----------- //

    private boolean checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE
            );
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // ----------- ABRIR CÁMARA ----------- //

    private void openCamera() {
        try {
            File photoFile = File.createTempFile("photo_", ".jpg", getCacheDir());

            photoUri = androidx.core.content.FileProvider.getUriForFile(
                    this,
                    getPackageName() + ".provider",
                    photoFile
            );

            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivityForResult(intent, TAKE_PHOTO);

        } catch (Exception e) {
            Toast.makeText(this, "No se pudo abrir la cámara", Toast.LENGTH_LONG).show();
        }
    }

    // ----------- RECIBIR FOTO / IMAGEN ----------- //

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ImageView btnComp = findViewById(R.id.BtnComp);

        if (resultCode == RESULT_OK && requestCode == TAKE_PHOTO) {
            imageUri = photoUri;
            btnComp.setImageURI(imageUri);
            return;
        }

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            btnComp.setImageURI(imageUri);
        }
    }
}

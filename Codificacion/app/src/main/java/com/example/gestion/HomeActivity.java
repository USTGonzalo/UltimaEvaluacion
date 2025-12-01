package com.example.gestion;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestion.database.MovementsDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private TextView iconGastosHoy, iconGastosMes, iconIngresosHoy, iconIngresosMes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ==========================
        // REFERENCIAS DE VISTA
        // ==========================
        iconGastosHoy = findViewById(R.id.iconGastosHoy);
        iconGastosMes = findViewById(R.id.iconGastosMes);
        iconIngresosHoy = findViewById(R.id.iconIngresosHoy);
        iconIngresosMes = findViewById(R.id.iconIngresosMes);

        Button BtnAdd, BtnEdit, BtnMovements, BtnAll, BtnCat, BtnExit;
        ImageButton ImgBtnConfigs;

        BtnAdd = findViewById(R.id.BtnAdd);
        BtnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddActivity.class);
            startActivity(intent);
        });

        BtnEdit = findViewById(R.id.BtnEdit);
        BtnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, EditActivity.class);
            startActivity(intent);
        });

        BtnExit = findViewById(R.id.BtnExit);
        BtnExit.setOnClickListener(v -> finish());

        BtnAll = findViewById(R.id.BtnAll);
        BtnAll.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AllActivity.class);
            startActivity(intent);
        });

        BtnCat = findViewById(R.id.BtnCat);
        BtnCat.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CategoriesActivity.class);
            startActivity(intent);
        });

        ImgBtnConfigs = findViewById(R.id.ImgBtnConfigs);
        ImgBtnConfigs.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CategoriesActivity.class);
            startActivity(intent);
        });
    }

    private void loadDashboardValues() {
        MovementsDatabase db = new MovementsDatabase(this);

        // FECHA HOY en formato dd-MM-yyyy
        String today = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        // MES (dos dígitos) extraído de la fecha actual
        String month = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date()); // "11" por ejemplo

        // OBTENER VALORES desde la base de datos
        double gastosHoy = db.getGastosHoy(today);
        double gastosMes = db.getGastosMes(month);
        double ingresosHoy = db.getIngresosHoy(today);
        double ingresosMes = db.getIngresosMes(month);

        // FORMATEAR los números con separador de miles/decimales si quieres (opcional)
        // Aquí los muestro simples con "$ "
        iconGastosHoy.setText("$ " + String.format(Locale.getDefault(), "%.1f", gastosHoy));
        iconGastosMes.setText("$ " + String.format(Locale.getDefault(), "%.1f", gastosMes));
        iconIngresosHoy.setText("$ " + String.format(Locale.getDefault(), "%.1f", ingresosHoy));
        iconIngresosMes.setText("$ " + String.format(Locale.getDefault(), "%.1f", ingresosMes));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardValues();
    }
}

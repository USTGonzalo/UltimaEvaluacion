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

import com.example.gestion.cache.ConfigModel;
import com.example.gestion.cache.Conversion;
import com.example.gestion.database.ConfigsDAO;
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

        Button BtnAdd, BtnEdit, BtnMovements, BtnAll, BtnCat, BtnExit, Reload;
        ImageButton ImgBtnConfigs;

        BtnAdd = findViewById(R.id.BtnAdd);
        BtnAdd.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, AddActivity.class)));

        BtnEdit = findViewById(R.id.BtnEdit);
        BtnEdit.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, EditActivity.class)));

        BtnExit = findViewById(R.id.BtnExit);
        BtnExit.setOnClickListener(v -> finish());

        BtnAll = findViewById(R.id.BtnAll);
        BtnAll.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, AllActivity.class)));

        BtnCat = findViewById(R.id.BtnCat);
        BtnCat.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, CategoriesActivity.class)));

        ImgBtnConfigs = findViewById(R.id.ImgBtnBack);
        ImgBtnConfigs.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ConfigsActivity.class)));

        Reload = findViewById(R.id.BtnReload);
        Reload.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, MainActivity.class)));
    }

    private double convertFromUSD(double amountUSD, String targetCurrency) {
        double rate = Conversion.getRateByType(targetCurrency);
        return amountUSD * rate;
    }

    private void loadDashboardValues() {
        MovementsDatabase db = new MovementsDatabase(this);

        String today = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String month = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());

        // Valores en USD
        double gastosHoy = db.getGastosHoy(today);
        double gastosMes = db.getGastosMes(month);
        double ingresosHoy = db.getIngresosHoy(today);
        double ingresosMes = db.getIngresosMes(month);

        // Leer configuración actual
        ConfigsDAO dao = new ConfigsDAO(this);
        ConfigModel config = dao.getConfig();

        String currency = config.type;

        // Convertir si no es USD
        if (!currency.equals("USD")) {
            gastosHoy = convertFromUSD(gastosHoy, currency);
            gastosMes = convertFromUSD(gastosMes, currency);
            ingresosHoy = convertFromUSD(ingresosHoy, currency);
            ingresosMes = convertFromUSD(ingresosMes, currency);
        }

        iconGastosHoy.setText(currency + " " + String.format(Locale.getDefault(), "%.1f", gastosHoy));
        iconGastosMes.setText(currency + " " + String.format(Locale.getDefault(), "%.1f", gastosMes));
        iconIngresosHoy.setText(currency + " " + String.format(Locale.getDefault(), "%.1f", ingresosHoy));
        iconIngresosMes.setText(currency + " " + String.format(Locale.getDefault(), "%.1f", ingresosMes));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDashboardValues();
    }
}

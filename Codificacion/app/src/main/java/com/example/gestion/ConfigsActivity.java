package com.example.gestion;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.gestion.database.DbHelper;
import com.example.gestion.database.ConfigsContract;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class ConfigsActivity extends AppCompatActivity {

    private Spinner spinnerMoney;
    private Switch switchTheme;

    // valores leídos de la base de datos
    private String currentSavedType = "";
    private int currentSavedTheme = 0; // 0 = claro, 1 = oscuro

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_configs);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        spinnerMoney = findViewById(R.id.SpinnerMoney);
        switchTheme = findViewById(R.id.SwitchTheme);

        // Cargar moneda y tema guardados
        loadSavedConfig();

        // Aplicar el valor leído al Switch
        switchTheme.setChecked(currentSavedTheme != 0);

        Button BtnBack = findViewById(R.id.BtnBack3);
        BtnBack.setOnClickListener(v -> {
            saveConfigToSQLite();
            finish();
        });

        loadCurrencies();

        Button ExitW = findViewById(R.id.btnBack4);
        ExitW.setOnClickListener(v -> finish());
    }



    // =======================================
    // CARGAR CONFIGURACIÓN GUARDADA DEL SQLITE
    // =======================================
    private void loadSavedConfig() {
        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT " +
                        ConfigsContract.ConfigsEntry.COLUMN_CURRENT_TYPE + ", " +
                        ConfigsContract.ConfigsEntry.COLUMN_THEME +
                        " FROM " + ConfigsContract.ConfigsEntry.TABLE_NAME +
                        " LIMIT 1", null
        );

        if (cursor.moveToFirst()) {
            currentSavedType = cursor.isNull(0) ? "" : cursor.getString(0);  // Ej: "CLP"
            // theme puede ser almacenado como BOOLEAN/INTEGER
            currentSavedTheme = cursor.isNull(1) ? 0 : cursor.getInt(1);
        }

        cursor.close();
        db.close();
    }

    // =======================================
    // GUARDAR MONEDA Y TEMA EN SQLITE
    // =======================================
    private void saveConfigToSQLite() {
        String selectedCurrency = spinnerMoney.getSelectedItem() != null
                ? spinnerMoney.getSelectedItem().toString()
                : "";

        int themeValue = switchTheme.isChecked() ? 1 : 0;

        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ConfigsContract.ConfigsEntry.COLUMN_CURRENT_TYPE, selectedCurrency);
        values.put(ConfigsContract.ConfigsEntry.COLUMN_THEME, themeValue);

        // Comprobar si existe alguna fila en la tabla
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + ConfigsContract.ConfigsEntry.TABLE_NAME, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        if (count == 0) {
            db.insert(ConfigsContract.ConfigsEntry.TABLE_NAME, null, values);
        } else {
            db.update(ConfigsContract.ConfigsEntry.TABLE_NAME, values, null, null);
        }

        db.close();
    }

    // =======================================
    // CARGAR MONEDAS DESDE API
    // =======================================
    private void loadCurrencies() {
        String url = "https://api.exchangerate.host/live?access_key=8e9b99a816ab47b61d6cd74cb54215c6&format=1";

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        JSONObject quotes = response.getJSONObject("quotes");

                        ArrayList<String> currencyList = new ArrayList<>();
                        Iterator<String> keys = quotes.keys();

                        while (keys.hasNext()) {
                            String key = keys.next(); // "USDCLP"
                            if (key.length() > 3) {
                                String currency = key.substring(3); // "CLP"
                                currencyList.add(currency);
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                this,
                                android.R.layout.simple_spinner_item,
                                currencyList
                        );
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerMoney.setAdapter(adapter);

                        // Selecciona la moneda guardada si existe
                        if (!currentSavedType.isEmpty() && currencyList.contains(currentSavedType)) {
                            spinnerMoney.setSelection(currencyList.indexOf(currentSavedType));
                        }

                    } catch (Exception e) {
                        Toast.makeText(this, "Error al procesar datos.", Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Error en la solicitud.", Toast.LENGTH_LONG).show()
        );

        queue.add(request);
    }
}

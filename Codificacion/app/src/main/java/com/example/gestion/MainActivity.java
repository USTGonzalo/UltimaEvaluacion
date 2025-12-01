package com.example.gestion;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.gestion.cache.Conversion;
import com.example.gestion.database.ConfigsDAO;

import org.json.JSONObject;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private final String url = "https://api.exchangerate.host/live?access_key=8e9b99a816ab47b61d6cd74cb54215c6&format=1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Cargar configuraciones
        ConfigsDAO dao = new ConfigsDAO(this);
        if (!dao.existsConfig()) {
            dao.createDefaultConfig();
        }

        // Antes de ir a HomeActivity, cargamos las conversiones en memoria
        loadConversions();
    }

    private void loadConversions() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        Conversion.clearConversions(); // Limpiar lista previa
                        JSONObject quotes = response.getJSONObject("quotes");
                        Iterator<String> keys = quotes.keys();

                        while (keys.hasNext()) {
                            String key = keys.next(); // Ej: "USDCLP"
                            String currency = key.substring(3); // Solo "CLP"
                            double value = quotes.getDouble(key);

                            Conversion.addConversion(currency, value);
                        }

                        // Una vez cargadas las conversiones, vamos a HomeActivity
                        goToHome();

                    } catch (Exception e) {
                        e.printStackTrace();
                        goToHome(); // Aunque falle, seguimos
                    }
                },
                error -> goToHome() // Si falla la API, igual seguimos
        );

        queue.add(request);
    }

    private void goToHome() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}

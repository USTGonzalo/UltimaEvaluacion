package com.example.gestion;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestion.adapters.MovementAdapter;
import com.example.gestion.cache.Movements;
import com.example.gestion.database.MovementsDatabase;

import java.util.Calendar;
import java.util.List;

public class AllActivity extends AppCompatActivity {

    RecyclerView recyclerAll;
    MovementAdapter adapter;
    MovementsDatabase db;

    String dateStart = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_all);

        recyclerAll = findViewById(R.id.recyclerAll);
        recyclerAll.setLayoutManager(new LinearLayoutManager(this));

        db = new MovementsDatabase(this);

        // carga inicial
        adapter = new MovementAdapter(this, db.getAllMovementsList());
        recyclerAll.setAdapter(adapter);

        Button BtnBack = findViewById(R.id.BtnBack);
        BtnBack.setOnClickListener(v -> finish());

        EditText TxtDateStart = findViewById(R.id.TxtDateStart);

        // fecha inicio
// fecha inicio
        TxtDateStart.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(AllActivity.this, (view, y, m, d) -> {

                dateStart = d + "/" + (m + 1) + "/" + y; // correcto
                TxtDateStart.setText(dateStart);

                filtrar();

            }, calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

    }

    private void filtrar() {
        if (dateStart.isEmpty()) return;

        List<Movements> result = db.getMovementsByDateRange(dateStart);
        adapter = new MovementAdapter(this, result);
        recyclerAll.setAdapter(adapter);
    }
}
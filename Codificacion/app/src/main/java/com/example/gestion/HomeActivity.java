package com.example.gestion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeActivity extends AppCompatActivity {

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
        Button BtnAdd, BtnEdit, BtnMovements, BtnAll, BtnCat, BtnExit, ImgBtnConfigs;

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
        BtnExit.setOnClickListener(v -> {
            finish();
        });

        BtnAll = findViewById(R.id.BtnAll);
        BtnAll.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AllActivity.class);
            startActivity(intent);
        });

        BtnCat = findViewById(R.id.BtnCat);
        BtnCat.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AllActivity.class);
            startActivity(intent);
        });

    }
}
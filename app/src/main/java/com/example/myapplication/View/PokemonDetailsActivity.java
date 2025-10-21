package com.example.myapplication.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;

public class PokemonDetailsActivity extends Activity {

    TextView tvName, tvId, tvDescription;
    ImageView pokemonImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_details);

        // Ajuste de insets en el root si es necesario
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referencias a vistas
        tvName = findViewById(R.id.tvName);
        tvId = findViewById(R.id.tvId);
        tvDescription = findViewById(R.id.tvDescription);
        pokemonImage = findViewById(R.id.pokemonImage);

        // Recuperar datos del Intent
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);
        String name = intent.getStringExtra("name");
        String imageUrl = intent.getStringExtra("imageUrl");
        String description = intent.getStringExtra("description");

        // Rellenar vistas
        if (id != -1) {
            tvId.setText(String.valueOf(id)); // solo el id en la esquina superior izquierda
        } else {
            tvId.setText("");
        }

        if (name != null) {
            tvName.setText(capitalize(name)); // nombre como en el search
        }

        if (description != null && !description.isEmpty()) {
            tvDescription.setText(description.replace("\n", " ").replace("\f", " ").trim());
            // la PokeAPI muchas veces contienen saltos de línea y caracteres especiales (como "\f") que
            // al mostrarse tal cual producen saltos extraños, se ven fragmentados o introducen saltos de página que
            // estropean la presentación.
            //trim() quita espacios sobrantes al inicio/fin que podrían quedar tras la sustitución.
        } else {
            tvDescription.setText("Descripción no disponible");
        }

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(pokemonImage);
        } else {
            pokemonImage.setImageResource(android.R.color.transparent);
        }
    }

    String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
package com.example.myapplication.View;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.myapplication.Controller.API;
import com.example.myapplication.Model.Pokemon;
import com.example.myapplication.R;

public class PokedexSearchActivity extends AppCompatActivity {

    EditText searchInput;
    Button searchButton;
     Button detailsButton; // button2
     TextView tvName, tvId, tvHeight, tvWeight;
     ImageView pokemonImage;

    // Guardamos el último pokemon obtenido para usarlo al pulsar "Detalles"
     Pokemon lastPokemon = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokedex_search);

        searchInput = findViewById(R.id.searchInput);
        searchButton = findViewById(R.id.searchButton);
        detailsButton = findViewById(R.id.button2);
        tvName = findViewById(R.id.tvName);
        tvId = findViewById(R.id.tvId);
        tvHeight = findViewById(R.id.tvHeight);
        tvWeight = findViewById(R.id.tvWeight);
        pokemonImage = findViewById(R.id.pokemonImage);

        // Inicialmente no hay pokemon buscado
        detailsButton.setEnabled(false);

        // Listener del botón Detalles: solo abre Details si hay un pokemon guardado
        detailsButton.setOnClickListener(v -> {
            if (lastPokemon == null) {
                Toast.makeText(PokedexSearchActivity.this, "Busca primero un Pokémon antes de ver detalles", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(PokedexSearchActivity.this, PokemonDetailsActivity.class);
            intent.putExtra("id", lastPokemon.getId());
            intent.putExtra("name", lastPokemon.getName());
            intent.putExtra("imageUrl", lastPokemon.getImageUrl());
            intent.putExtra("description", lastPokemon.getDescription());
            // opcional: pasar height/weight si lo quieres en Details
            intent.putExtra("height", lastPokemon.getHeight());
            intent.putExtra("weight", lastPokemon.getWeight());
            startActivity(intent);
        });

        searchButton.setOnClickListener(v -> {
            String query = searchInput.getText().toString().trim();
            if (query.isEmpty()) {
                searchInput.setError("Introduce un nombre o ID");
                return;
            }

            API.fetchPokemon(this, query, new API.PokemonCallback() {
                @Override
                public void onSuccess(Pokemon pokemon) {
                    lastPokemon = pokemon; // guardamos el pokemon
                    detailsButton.setEnabled(true);

                    tvName.setText("" + capitalize(pokemon.getName()));
                    tvId.setText("" + pokemon.getId());
                    tvHeight.setText("Altura: " + pokemon.getHeight());
                    tvWeight.setText("Peso: " + pokemon.getWeight());

                    Glide.with(PokedexSearchActivity.this)
                            .load(pokemon.getImageUrl())
                            .into(pokemonImage);
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(PokedexSearchActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
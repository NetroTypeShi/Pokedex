package com.example.myapplication.View;

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

    private EditText searchInput;
    private Button searchButton;
    private TextView tvName, tvId, tvHeight, tvWeight;
    private ImageView pokemonImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokedex_search);

        searchInput = findViewById(R.id.searchInput);
        searchButton = findViewById(R.id.searchButton);
        tvName = findViewById(R.id.tvName);
        tvId = findViewById(R.id.tvId);
        tvHeight = findViewById(R.id.tvHeight);
        tvWeight = findViewById(R.id.tvWeight);
        pokemonImage = findViewById(R.id.pokemonImage);

        searchButton.setOnClickListener(v -> {
            String query = searchInput.getText().toString().trim();
            if (query.isEmpty()) {
                searchInput.setError("Introduce un nombre o ID");
                return;
            }

            API.fetchPokemon(this, query, new API.PokemonCallback() {
                @Override
                public void onSuccess(Pokemon pokemon) {
                    tvName.setText("" + capitalize(pokemon.getName()));
                    tvId.setText(""+pokemon.getId());
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
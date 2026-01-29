package com.example.myapplication.View

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.Controller.API.PokemonCallback
import com.example.myapplication.Controller.API.fetchPokemon
import com.example.myapplication.Model.Pokemon
import com.example.myapplication.R
import java.util.Locale

class PokedexSearchActivity : AppCompatActivity() {
    var searchInput: EditText? = null
    var searchButton: Button? = null
    var detailsButton: Button? = null // button2
    var tvName: TextView? = null
    var tvId: TextView? = null
    var tvHeight: TextView? = null
    var tvWeight: TextView? = null
    var pokemonImage: ImageView? = null

    // Guardamos el último pokemon obtenido para usarlo al pulsar "Detalles"
    var lastPokemon: Pokemon? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokedex_search)

        searchInput = findViewById<EditText>(R.id.searchInput)
        searchButton = findViewById<Button>(R.id.searchButton)
        detailsButton = findViewById<Button>(R.id.button2)
        tvName = findViewById<TextView>(R.id.tvName)
        tvId = findViewById<TextView>(R.id.tvId)
        tvHeight = findViewById<TextView>(R.id.tvHeight)
        tvWeight = findViewById<TextView>(R.id.tvWeight)
        pokemonImage = findViewById<ImageView>(R.id.pokemonImage)

        // inicialmente no hay pokemon buscado
        detailsButton!!.setEnabled(false)

        // Listener del botón Detalles: solo abre Details si hay un pokemon guardado
        detailsButton!!.setOnClickListener(View.OnClickListener { v: View? ->
            if (lastPokemon == null) {
                Toast.makeText(
                    this@PokedexSearchActivity,
                    "Busca primero un Pokémon antes de ver detalles",
                    Toast.LENGTH_SHORT
                ).show()

            }
            val intent = Intent(this@PokedexSearchActivity, PokemonDetailsActivity::class.java)
            intent.putExtra("id", lastPokemon!!.id)
            intent.putExtra("name", lastPokemon!!.name)
            intent.putExtra("imageUrl", lastPokemon!!.imageUrl)
            intent.putExtra("description", lastPokemon!!.description)
            // opcional: pasar height/weight si lo quieres en Details
            intent.putExtra("height", lastPokemon!!.height)
            intent.putExtra("weight", lastPokemon!!.weight)
            startActivity(intent)
        })

        searchButton!!.setOnClickListener(View.OnClickListener { v: View? ->
            val query = searchInput!!.getText().toString().trim { it <= ' ' }
            if (query.isEmpty()) {
                searchInput!!.setError("Introduce un nombre o ID")

            }
            fetchPokemon(this, query, object : PokemonCallback() {

                public override fun onSuccess(pokemon: Pokemon?) {
                    lastPokemon = pokemon // guardamos el pokemon
                    detailsButton!!.setEnabled(true)

                    tvName!!.setText("" + capitalize(pokemon?.name))
                    tvId!!.setText("" + pokemon?.id)
                    tvHeight!!.setText("Altura: " + pokemon?.height)
                    tvWeight!!.setText("Peso: " + pokemon?.weight)

                    Glide.with(this@PokedexSearchActivity)
                        .load(pokemon?.imageUrl)
                        .into(pokemonImage!!)
                }

                public override fun onError(message: String?) {
                    Toast.makeText(this@PokedexSearchActivity, message, Toast.LENGTH_SHORT).show()
                }
            })
        })
    }
    // NECESITO ABRIR UN PROYECTO NUEVO 
    private fun capitalize(s: String?): String? {
        if (s == null || s.isEmpty()) return s
        return s.substring(0, 1).uppercase(Locale.getDefault()) + s.substring(1)
        // pilla la primera letra y la pone en mayusculas
    }
}
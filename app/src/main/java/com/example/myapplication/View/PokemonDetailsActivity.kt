package com.example.myapplication.View

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.myapplication.R
import java.util.Locale

class PokemonDetailsActivity : Activity() {
    var tvName: TextView? = null
    var tvId: TextView? = null
    var tvDescription: TextView? = null
    var pokemonImage: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_details)

        // Ajuste de insets en el root si es necesario
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View?>(R.id.main),
            OnApplyWindowInsetsListener { v: View?, insets: WindowInsetsCompat? ->
                val systemBars = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
                v!!.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            })

        // Referencias a vistas
        tvName = findViewById<TextView>(R.id.tvName)
        tvId = findViewById<TextView>(R.id.tvId)
        tvDescription = findViewById<TextView>(R.id.tvDescription)
        pokemonImage = findViewById<ImageView>(R.id.pokemonImage)

        // Recuperar datos del Intent
        val intent = getIntent()
        val id = intent.getIntExtra("id", -1)
        val name = intent.getStringExtra("name")
        val imageUrl = intent.getStringExtra("imageUrl")
        val description = intent.getStringExtra("description")

        // Rellenar vistas
        if (id != -1) {
            tvId!!.setText(id.toString()) // solo el id en la esquina superior izquierda
        } else {
            tvId!!.setText("")
        }

        if (name != null) {
            tvName!!.setText(capitalize(name)) // nombre como en el search
        }

        if (description != null && !description.isEmpty()) {
            tvDescription!!.setText(
                description.replace("\n", " ").replace("\u000c", " ").trim { it <= ' ' })
            // la PokeAPI muchas veces contienen saltos de línea y caracteres especiales (como "\f") que
            // al mostrarse tal cual producen saltos extraños, se ven fragmentados o introducen saltos de página que
            // estropean la presentación.
            //trim() quita espacios sobrantes al inicio/fin que podrían quedar tras la sustitución.
        } else {
            tvDescription!!.setText("Descripción no disponible")
        }

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).into(pokemonImage!!)
        } else {
            pokemonImage!!.setImageResource(android.R.color.transparent)
        }
    }

    fun capitalize(s: String?): String? {
        if (s == null || s.isEmpty()) return s
        return s.substring(0, 1).uppercase(Locale.getDefault()) + s.substring(1)
    }
}
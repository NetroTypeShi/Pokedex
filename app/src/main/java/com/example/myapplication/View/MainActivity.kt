package com.example.myapplication.View

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.Controller.LoginController.cifrarCesar
import com.example.myapplication.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View?>(R.id.main),
            OnApplyWindowInsetsListener { v: View?, insets: WindowInsetsCompat? ->
                val systemBars = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
                v!!.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            })

        val btnStart = findViewById<Button>(R.id.btnStart)
        val userName = findViewById<EditText>(R.id.editTextText)
        val password = findViewById<EditText>(R.id.editTextTextPassword)

        btnStart.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val user = userName.getText().toString()
                val pass = password.getText().toString()


                if (user.isEmpty()) {
                    userName.setBackgroundTintList(ColorStateList.valueOf(Color.RED))
                } else {
                    userName.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN))
                }

                if (pass.isEmpty()) {
                    password.setBackgroundTintList(ColorStateList.valueOf(Color.RED))
                } else {
                    password.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN))
                }


                if (user.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(
                        this@MainActivity,
                        "Por favor inserte un nombre y contraseña válidos",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }


                if (pass.length < 4 || pass.length > 10) {
                    password.setBackgroundTintList(ColorStateList.valueOf(Color.RED))
                    Toast.makeText(
                        this@MainActivity,
                        "La contraseña debe tener entre 4 y 10 caracteres",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                } else {
                    password.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN))
                }


                val passCifrada = cifrarCesar(pass)
                Toast.makeText(
                    this@MainActivity,
                    "Contraseña cifrada: " + passCifrada,
                    Toast.LENGTH_LONG
                ).show()

                val intent = Intent(this@MainActivity, PokedexSearchActivity::class.java)
                startActivity(intent)
            }
        })
    }
}

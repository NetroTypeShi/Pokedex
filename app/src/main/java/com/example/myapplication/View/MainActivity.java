package com.example.myapplication.View;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;
import com.example.myapplication.Controller.LoginController;

public class MainActivity extends AppCompatActivity {

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

        Button btnStart = findViewById(R.id.btnStart);
        EditText userName = findViewById(R.id.editTextText);
        EditText password = findViewById(R.id.editTextTextPassword);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = userName.getText().toString();
                String pass = password.getText().toString();


                if (user.isEmpty()) {
                    userName.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                } else {
                    userName.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                }

                if (pass.isEmpty()) {
                    password.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                } else {
                    password.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                }


                if (user.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Por favor inserte un nombre y contraseña válidos", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (pass.length() < 4 || pass.length() > 10) {
                    password.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    Toast.makeText(MainActivity.this, "La contraseña debe tener entre 4 y 10 caracteres", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    password.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                }


                String passCifrada = LoginController.cifrarCesar(pass);
                Toast.makeText(MainActivity.this, "Contraseña cifrada: " + passCifrada, Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this, PokedexSearchActivity.class);
                startActivity(intent);
            }
        });
    }
}

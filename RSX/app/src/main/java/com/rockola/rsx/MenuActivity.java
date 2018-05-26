package com.rockola.rsx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    private String id_usuario;
    private String nombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        parametrosIntent();
    }

    private void parametrosIntent() {
        Intent intent = getIntent();
        id_usuario = intent.getStringExtra("id_usuario");
        nombre = intent.getStringExtra("nombre");

        Toast.makeText(this, "Bienvenido " + nombre,
                Toast.LENGTH_SHORT).show();
    }
}

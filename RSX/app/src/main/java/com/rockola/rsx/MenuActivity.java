package com.rockola.rsx;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {

    private String id_conductor;
    private String nombre;
    private String tel_celular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        parametrosIntent();
    }

    private void parametrosIntent() {
        Intent intent = getIntent();
        id_conductor = intent.getStringExtra("id_conductor");
        nombre = intent.getStringExtra("nombre");
        tel_celular = intent.getStringExtra("tel_celular");

        Toast.makeText(this, "Bienvenido " + id_conductor,
                Toast.LENGTH_SHORT).show();
    }

    public void registrarVehiculo(View view) {
        Intent intent = new Intent(MenuActivity.this, VehiculoActivity.class);
        intent.putExtra("id_conductor", id_conductor);
        startActivity(intent);
    }
}

package com.rockola.rsx;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rockola.rsx.ws.HttpUtils;
import com.rockola.rsx.ws.Response;
import com.rockola.rsx.ws.pojos.Aseguradora;
import com.rockola.rsx.ws.pojos.Color;
import com.rockola.rsx.ws.pojos.Conductor;
import com.rockola.rsx.ws.pojos.Marca;
import com.rockola.rsx.ws.pojos.Mensaje;
import com.rockola.rsx.ws.pojos.Vehiculo;

import java.util.List;

public class VehiculoActivity extends AppCompatActivity {

    private Spinner coloresSpi;
    private Spinner marcaSpi;
    private Spinner aseguradorasSpi;

    private List<Color> listaColores;
    private List<Marca> listaMarcas;
    private List<Aseguradora> listaAseguradoras;

    private String[] colores = new String[8];
    private String[] marcas = new String[21];
    private String[] aseguradoras = new String[11];

    private String no_placa;
    private String modelo;
    private String anio;
    private String no_poliza;
    private String marca;
    private String aseguradora;
    private String color;
    private String id_conductor;
    private EditText txt_placa;
    private EditText txt_modelo;
    private EditText txt_anio;
    private EditText txt_poliza;
    private Response resws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculo);

        txt_placa = (EditText) findViewById(R.id.txt_placa);
        txt_modelo = (EditText) findViewById(R.id.txt_modelo);
        txt_anio = (EditText) findViewById(R.id.txt_anio);
        txt_poliza = (EditText) findViewById(R.id.txt_poliza);
        coloresSpi = (Spinner) findViewById(R.id.colorSpi);
        marcaSpi = (Spinner) findViewById(R.id.marcaSpi);
        aseguradorasSpi = (Spinner) findViewById(R.id.aseguradoraSpi);

        parametrosIntent();
        llenarSpinners();
    }

    @Override
    public void onResume() {
        super.onResume();
        txt_poliza.getText().clear();
        txt_modelo.getText().clear();
        txt_anio.getText().clear();
        txt_poliza.getText().clear();
        marcaSpi.setSelection(0);
        aseguradorasSpi.setSelection(0);
        coloresSpi.setSelection(0);
    }

    private void parametrosIntent() {
        Intent intent = getIntent();
        id_conductor = intent.getStringExtra("id_conductor");
    }

    public void registrarVehiculo(View view) {
        if (validar() && isOnline()) {
            no_placa = txt_placa.getText().toString();
            modelo = txt_modelo.getText().toString();
            anio = txt_anio.getText().toString();
            if (txt_poliza.getText() == null) {
                no_poliza = null;
            } else {
                no_poliza = txt_poliza.getText().toString();
            }
            marca = Integer.toString(marcaSpi.getSelectedItemPosition() + 1);
            aseguradora = Integer.toString(aseguradorasSpi.getSelectedItemPosition() + 1);
            color = Integer.toString(coloresSpi.getSelectedItemPosition() + 1);

            Vehiculo vehiculo = new Vehiculo(no_placa, modelo, anio, no_poliza,
                    marca, aseguradora, color, id_conductor);

            WSPOSTRegistrarVehiculoTask task = new WSPOSTRegistrarVehiculoTask();
            task.execute(vehiculo);
        }
    }

    public void cancelar(View view) {
        finish();
    }

    private void llenarSpinners() {
        if (isOnline()) {
            WSGGETColoresTask taskC = new WSGGETColoresTask();
            taskC.execute();
            WSGETAseguradoras taskA = new WSGETAseguradoras();
            taskA.execute();
            WSGETMarcas taskM = new WSGETMarcas();
            taskM.execute();
        }
    }

    private boolean validar() {
        boolean ok = true;
        if (txt_placa.getText() == null || txt_placa.getText().toString().isEmpty()
                || txt_placa.getText().length() != 10) {
            txt_placa.setError("Por favor, introduce tu número de placa");
            ok = false;
        }

        if (txt_modelo.getText() == null || txt_modelo.getText().toString().isEmpty()) {
            txt_modelo.setError("Por favor, introduce el modelo de tu auto");
            ok = false;
        }

        if (txt_anio.getText() == null || txt_anio.getText().toString().isEmpty()) {
            txt_anio.setError("Por favor, introduce el año");
            ok = false;
        }
        return ok;
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(
                Context.CONNECTIVITY_SERVICE);
        boolean online = (cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnected());
        if (!online) {
            mostrarAlertDialog("Sin conexión", "No se encontró ninguna conexión a Internet, " +
                    "conéctate a alguna red para continuar utilizando la aplicación");
        }

        return online;
    }

    private void mostrarAlertDialog(String titulo, String mensaje) {
        AlertDialog dialog = new AlertDialog.Builder(
                VehiculoActivity.this).create();
        dialog.setMessage(mensaje);
        dialog.setTitle(titulo);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    class WSPOSTRegistrarVehiculoTask extends AsyncTask<Vehiculo, String, String> {
        @Override
        protected String doInBackground(Vehiculo... params) {
            resws = HttpUtils.registrarVehiculo(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            resultadoRegistrar();
        }
    }

    private void resultadoRegistrar() {
        if (resws != null && !resws.isError() && resws.getResult() != null) {
            Mensaje mensaje = new Gson().fromJson(resws.getResult(), Mensaje.class);
            if (mensaje.getStatusMensaje() == 300) {
                mostrarAlertDialog("Éxito", mensaje.getMensaje());
            } else if (mensaje.getStatusMensaje() == 301) {
                mostrarAlertDialog("Intente más tarde", mensaje.getMensaje());
            } else if (mensaje.getStatusMensaje() == 1) {
                mostrarAlertDialog("Error", mensaje.getMensaje());
            }
        } else {
            mostrarAlertDialog("Error", resws.getResult());
        }
    }

    class WSGGETColoresTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            resws = HttpUtils.consultarColores();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            resultadoColores();
        }
    }

    private void resultadoColores() {
        if (resws != null && !resws.isError() && resws.getResult() != null) {
            listaColores = new Gson().fromJson(resws.getResult(),
                    new TypeToken<List<Color>>() {}.getType());
            if (listaColores != null) {
                for(int i = 0; i < 8; i++) {
                    colores[i] = listaColores.get(i).getNombre();
                }
                coloresSpi.setAdapter(new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, colores));
            }
        } else {
            mostrarAlertDialog("Error", resws.getResult());
        }
    }

    class WSGETMarcas extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            resws = HttpUtils.consultarMarcas();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            resultadoMarcas();
        }
    }

    private void resultadoMarcas() {
        if (resws != null && !resws.isError() && resws.getResult() != null) {
            listaMarcas = new Gson().fromJson(resws.getResult(),
                    new TypeToken<List<Marca>>() {}.getType());
            if (listaMarcas != null) {
                for(int i = 0; i < 21; i++) {
                    marcas[i] = listaMarcas.get(i).getNombre();
                }
                marcaSpi.setAdapter(new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, marcas));
            }
        } else {
            mostrarAlertDialog("Error", resws.getResult());
        }
    }

    class WSGETAseguradoras extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            resws = HttpUtils.consultarAseguradoras();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            resultadoAseguradoras();
        }
    }

    private void resultadoAseguradoras() {
        if (resws != null && !resws.isError() && resws.getResult() != null) {
            listaAseguradoras = new Gson().fromJson(resws.getResult(),
                    new TypeToken<List<Aseguradora>>() {}.getType());
            if (listaAseguradoras != null) {
                for(int i = 0; i < 11; i++) {
                    aseguradoras[i] = listaAseguradoras.get(i).getNombre();
                }
                aseguradorasSpi.setAdapter(new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, aseguradoras));
            }
        } else {
            mostrarAlertDialog("Error", resws.getResult());
        }
    }
}

package com.rockola.rsx;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rockola.rsx.ws.HttpUtils;
import com.rockola.rsx.ws.Response;
import com.rockola.rsx.ws.pojos.Aseguradora;
import com.rockola.rsx.ws.pojos.Color;
import com.rockola.rsx.ws.pojos.Marca;
import com.rockola.rsx.ws.pojos.Mensaje;
import com.rockola.rsx.ws.pojos.Reporte;
import com.rockola.rsx.ws.pojos.Vehiculo;

import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class ReporteActivity extends AppCompatActivity {

    private Spinner carro_spi;
    private Spinner colores_spi_reporte;
    private Spinner aseguradoras_spi_reporte;
    private Spinner marcas_spi_reporte;
    private EditText txt_nombreSin;
    private EditText txt_paternoSin;
    private EditText txt_maternoSin;
    private EditText txt_placa;
    private EditText txt_modelo;
    private EditText txt_anio;
    private EditText txt_poliza;

    private List<Color> listaColores;
    private List<Marca> listaMarcas;
    private List<Aseguradora> listaAseguradoras;
    private List<Vehiculo> listaVehiculos;

    private String id_conductor;
    private String nombre_sin;
    private String materno_sin;
    private String paterno_sin;
    private String placa_sin;
    private String modelo_sin;
    private String anio_sin;
    private String poliza_sin;

    private Response resws;
    private FusedLocationProviderClient client;

    private Vehiculo vehiculo;
    private String id_vehiculo_reporte;

    private boolean existeVehiculo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);

        txt_nombreSin = (EditText) findViewById(R.id.txt_nombreSin);
        txt_paternoSin = (EditText) findViewById(R.id.txt_paternoSin);
        txt_maternoSin = (EditText) findViewById(R.id.txt_maternoSin);
        txt_placa = (EditText) findViewById(R.id.txt_placa);
        txt_modelo = (EditText) findViewById(R.id.txt_modelo);
        txt_anio = (EditText) findViewById(R.id.txt_anio);
        txt_poliza = (EditText) findViewById(R.id.txt_poliza);
        carro_spi = (Spinner) findViewById(R.id.carroSpi);
        colores_spi_reporte = (Spinner) findViewById(R.id.color_spi_reporte);
        aseguradoras_spi_reporte = (Spinner) findViewById(R.id.aseguradora_spi_reporte);
        marcas_spi_reporte = (Spinner) findViewById(R.id.marca_spi_reporte);

        requestPermission();

        client = LocationServices.getFusedLocationProviderClient(this);

        parametrosIntent();
        llenarSpinners();

    }

    @Override
    public void onResume() {
        super.onResume();
        carro_spi.setSelection(0);
        txt_nombreSin.getText().clear();
        txt_paternoSin.getText().clear();
        txt_maternoSin.getText().clear();
        txt_placa.getText().clear();
        txt_poliza.getText().clear();
        txt_modelo.getText().clear();
        txt_anio.getText().clear();
        marcas_spi_reporte.setSelection(0);
        colores_spi_reporte.setSelection(0);
        aseguradoras_spi_reporte.setSelection(0);
    }

    public void levantarReporte(View view) {
        if (ActivityCompat.checkSelfPermission(ReporteActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.getLastLocation().addOnSuccessListener(ReporteActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (validar() && isOnline() && (location != null)) {
                    if (existeVehiculo) {
                        Vehiculo vehiculoAfectado = (Vehiculo) carro_spi.getSelectedItem();

                        Reporte reporte = new Reporte(
                                String.valueOf(location.getLatitude()),
                                String.valueOf(location.getLongitude()),
                                txt_nombreSin.getText().toString(),
                                txt_paternoSin.getText().toString(),
                                txt_maternoSin.getText().toString(),
                                id_conductor,
                                String.valueOf(vehiculoAfectado.getIdVehiculo()),
                                String.valueOf(vehiculo.getIdVehiculo())
                        );
                    } else {
                        Vehiculo vehiculoAfectado = (Vehiculo) carro_spi.getSelectedItem();

                        Reporte reporte = new Reporte(
                                String.valueOf(location.getLatitude()),
                                String.valueOf(location.getLongitude()),
                                txt_nombreSin.getText().toString(),
                                txt_paternoSin.getText().toString(),
                                txt_maternoSin.getText().toString(),
                                id_conductor,
                                String.valueOf(vehiculoAfectado.getIdVehiculo())
                        );

                        String no_poliza_enviar;

                        if (txt_poliza.getText() == null) {
                            no_poliza_enviar = null;
                        } else {
                            no_poliza_enviar = txt_poliza.getText().toString();
                        }

                        Marca marca_obj = (Marca) marcas_spi_reporte.getSelectedItem();
                        Aseguradora ase_obj = (Aseguradora) aseguradoras_spi_reporte.getSelectedItem();
                        Color color_obj = (Color) colores_spi_reporte.getSelectedItem();

                        Vehiculo vehiculo = new Vehiculo(
                                txt_placa.getText().toString(),
                                txt_modelo.getText().toString(),
                                txt_anio.getText().toString(),
                                no_poliza_enviar,
                                Integer.toString(marca_obj.getIdMarca()),
                                Integer.toString(ase_obj.getIdAseguradora()),
                                Integer.toString(color_obj.getIdColor()),
                                null
                        );

                        ReporteActivity.WSPOSTRegistrarVehiculoTask task =
                                new ReporteActivity.WSPOSTRegistrarVehiculoTask();
                        task.execute(vehiculo);
                    }
                } else {
                    mostrarAlertDialog("Problema con la localización", "No se " +
                            "ha podido recuperar la localización del dispositivo. Asegúrese " +
                            "que la aplicación cuenta con los permisos necesarios.");
                }
            }
        });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    private void parametrosIntent() {
        Intent intent = getIntent();
        id_conductor = intent.getStringExtra("id_conductor");
    }

    public void cancelar(View view) {
        finish();
    }

    public void consultarVehiculo(View view) {
        if (isOnline()) {
            if (txt_placa.getText() == null || txt_placa.getText().toString().isEmpty()) {
                txt_placa.setError("Por favor, introduce la placa del vehículo");
            } else {
                ReporteActivity.WSPostVehiculo task = new ReporteActivity.WSPostVehiculo();
                task.execute(txt_placa.getText().toString());
            }
        }
    }

    private void llenarSpinners() {
        if (isOnline()) {
            ReporteActivity.WSGGETColoresTask taskC = new ReporteActivity.WSGGETColoresTask();
            taskC.execute();
            ReporteActivity.WSGETAseguradoras taskA = new ReporteActivity.WSGETAseguradoras();
            taskA.execute();
            ReporteActivity.WSGETMarcas taskM = new ReporteActivity.WSGETMarcas();
            taskM.execute();
            ReporteActivity.WSGETVehiculos taskV = new ReporteActivity.WSGETVehiculos();
            taskV.execute(id_conductor);
        }
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

    private boolean validar() {
        boolean ok = true;
        if (txt_nombreSin.getText() == null || txt_nombreSin.getText().toString().isEmpty()) {
            txt_nombreSin.setError("Por favor, introduce el nombre");
            ok = false;
        }

        if (txt_paternoSin.getText() == null || txt_paternoSin.getText().toString().isEmpty()) {
            txt_paternoSin.setError("Por favor, introduce el apellido paterno");
            ok = false;
        }

        if(!existeVehiculo) {
            if (txt_placa.getText() == null || txt_placa.getText().toString().isEmpty()) {
                txt_placa.setError("Por favor, introduce la placa");
                ok = false;
            }

            if (txt_modelo.getText() == null || txt_modelo.getText().toString().isEmpty()) {
                txt_modelo.setError("Por favor, introduce el modelo");
                ok = false;
            }

            if (txt_anio.getText() == null || txt_anio.getText().toString().isEmpty()) {
                txt_anio.setError("Por favor, introduce el año");
                ok = false;
            }
        }

        return ok;
    }

    private void mostrarAlertDialog(String titulo, String mensaje) {
        AlertDialog dialog = new AlertDialog.Builder(
                ReporteActivity.this).create();
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
                colores_spi_reporte.setAdapter(new ArrayAdapter<Color>(this,
                        android.R.layout.simple_spinner_item, listaColores));
                colores_spi_reporte.setAdapter(new ArrayAdapter<Color>(this,
                        R.layout.spinner_item, listaColores));

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
                marcas_spi_reporte.setAdapter(new ArrayAdapter<Marca>(this,
                        android.R.layout.simple_spinner_item, listaMarcas));
                marcas_spi_reporte.setAdapter(new ArrayAdapter<Marca>(this,
                        R.layout.spinner_item, listaMarcas));
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
                aseguradoras_spi_reporte.setAdapter(new ArrayAdapter<Aseguradora>(this,
                        android.R.layout.simple_spinner_item, listaAseguradoras));
                aseguradoras_spi_reporte.setAdapter(new ArrayAdapter<Aseguradora>(this,
                        R.layout.spinner_item, listaAseguradoras));
            }
        } else {
            mostrarAlertDialog("Error", resws.getResult());
        }
    }

    class WSGETVehiculos extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            resws = HttpUtils.consultarVehiculos(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            resultadoVehiculos();
        }
    }

    private void resultadoVehiculos() {
        if (resws != null && !resws.isError() && resws.getResult() != null) {
            listaVehiculos = new Gson().fromJson(resws.getResult(),
                    new TypeToken<List<Vehiculo>>() {}.getType());
            if (listaVehiculos != null || !listaVehiculos.isEmpty()) {
                carro_spi.setAdapter(new ArrayAdapter<Vehiculo>(this,
                        android.R.layout.simple_spinner_item, listaVehiculos));
                carro_spi.setAdapter(new ArrayAdapter<Vehiculo>(this,
                        R.layout.spinner_item, listaVehiculos));
            }
        } else {
            mostrarAlertDialog("Error", resws.getResult());
        }
    }

    class WSPostVehiculo extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            resws = HttpUtils.consultarVehiculo(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            resultadoVehiculo();
        }
    }

    private void resultadoVehiculo() {
        if (resws != null && !resws.isError() && resws.getResult() != null) {
            vehiculo = new Gson().fromJson(resws.getResult(), Vehiculo.class);
            if (vehiculo != null) {
                txt_modelo.setText(vehiculo.getModelo().toString());
                txt_anio.setText(vehiculo.getAnio().toString());
                txt_poliza.setText(vehiculo.getNoPolizaSeguro().toString());
                marcas_spi_reporte.setSelection(Integer.parseInt(vehiculo.getIdMarca()) - 1);
                colores_spi_reporte.setSelection(Integer.parseInt(vehiculo.getIdColor()) - 1);
                aseguradoras_spi_reporte.setSelection(Integer.parseInt(vehiculo.getIdAseguradora()) - 1);

                txt_modelo.setEnabled(false);
                txt_anio.setEnabled(false);
                txt_poliza.setEnabled(false);

                marcas_spi_reporte.setEnabled(false);
                colores_spi_reporte.setEnabled(false);
                aseguradoras_spi_reporte.setEnabled(false);

                existeVehiculo = true;
            } else {
                mostrarAlertDialog("No se encuentra", "La placa ingresada no se " +
                        "encuentra registrada, ingrese los datos de vehículo manualmente.");

                existeVehiculo = false;
            }
        } else {
            mostrarAlertDialog("No se encuentra", "La placa ingresada no se " +
                    "encuentra registrada, ingrese los datos de vehículo manualmente");

            existeVehiculo = false;

            txt_modelo.getText().clear();
            txt_anio.getText().clear();
            txt_poliza.getText().clear();
            marcas_spi_reporte.setSelection(0);
            aseguradoras_spi_reporte.setSelection(0);
            colores_spi_reporte.setSelection(0);
        }
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
}

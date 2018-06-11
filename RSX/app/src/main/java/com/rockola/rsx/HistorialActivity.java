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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rockola.rsx.ws.HttpUtils;
import com.rockola.rsx.ws.Response;
import com.rockola.rsx.ws.pojos.Aseguradora;
import com.rockola.rsx.ws.pojos.ResultadoDictamen;
import com.rockola.rsx.ws.pojos.ResultadoReporte;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistorialActivity extends AppCompatActivity {

    private String id_conductor;
    private Response resws;
    private ListView lstReportes;
    private List<ResultadoReporte> listaReportes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        lstReportes = (ListView) findViewById(R.id.reportes_list);

        lstReportes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mostrarDictamen(position);
            }
        });

        parametrosIntent();
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarReportes();
    }

    private void mostrarDictamen(int index) {
        ResultadoReporte reporte = this.listaReportes.get(index);
        WSPOSTConsultarDictamen task = new WSPOSTConsultarDictamen();
        task.execute(String.valueOf(reporte.getIdReporte()));

    }

    private void parametrosIntent() {
        Intent intent = getIntent();
        id_conductor = intent.getStringExtra("id_conductor");
    }

    private void cargarReportes() {
        if (isOnline()) {
            WSPOSTRecuperarReportes task = new WSPOSTRecuperarReportes();
            task.execute(id_conductor);
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

    private void mostrarAlertDialog(String titulo, String mensaje) {
        AlertDialog dialog = new AlertDialog.Builder(
                HistorialActivity.this).create();
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

    class WSPOSTRecuperarReportes extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String ... params) {
            resws = HttpUtils.recuperarReportes(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            resultadoReportes();
        }
    }

    private void resultadoReportes() {
        if (resws != null && !resws.isError() && resws.getResult() != null) {
            listaReportes = new Gson().fromJson(resws.getResult(),
                    new TypeToken<List<ResultadoReporte>>() {}.getType());
            ArrayAdapter<ResultadoReporte> adapterPersonal = new ArrayAdapter<ResultadoReporte>(this,
                    R.layout.spinner_item, listaReportes);

            this.lstReportes.setAdapter(adapterPersonal);
        } else {
            mostrarAlertDialog("Error", resws.getResult());
        }
    }

    class WSPOSTConsultarDictamen extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String ... params) {
            resws = HttpUtils.consultarDictamen(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            resultadoDictamen();
        }
    }

    private void resultadoDictamen() {
        if (resws != null && !resws.isError() && resws.getResult() != null) {
            ResultadoDictamen dictamen = new Gson().fromJson(resws.getResult(), ResultadoDictamen.class);
            if (dictamen != null) {
                String fecha = dictamen.getFechaHora();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
                Date date = null;
                try {
                    date = format.parse(fecha);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                mostrarAlertDialog("Resultado del dictamen",
                        "Perito responsable: " + dictamen.getNombre() + " "
                                + dictamen.getApPaterno() + "\n" + "Dictamen: " + dictamen.getDescripcion()
                +"\nFecha: " + DateFormat.getDateInstance(DateFormat.SHORT).format(date) +
                                "\nHora: " + DateFormat.getTimeInstance(DateFormat.SHORT).format(date));
            } else {
                mostrarAlertDialog("No disponible", "Aún no se realiza el dictamen " +
                        "de su siniestro, espere 5 minutos");
            }
        } else {
            mostrarAlertDialog("No disponible", "El dictamen aún no está disponible, " +
                    "intente nuevamente en 5 minutos.");
        }
    }
}

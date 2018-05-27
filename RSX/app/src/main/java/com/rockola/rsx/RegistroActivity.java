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
import android.widget.EditText;

import com.google.gson.Gson;
import com.rockola.rsx.ws.HttpUtils;
import com.rockola.rsx.ws.Response;
import com.rockola.rsx.ws.pojos.Conductor;
import com.rockola.rsx.ws.pojos.Mensaje;

public class RegistroActivity extends AppCompatActivity {

    private String nombre;
    private String ap_paterno;
    private String ap_materno;
    private String dia;
    private String mes;
    private String anio;
    private String no_licencia;
    private String tel_celular;
    private String password;
    private EditText txt_nombre;
    private EditText txt_paterno;
    private EditText txt_materno;
    private EditText txt_dia;
    private EditText txt_mes;
    private EditText txt_anio;
    private EditText txt_licencia;
    private EditText txt_celular;
    private EditText txt_password;
    private Response resws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        txt_nombre = (EditText) findViewById(R.id.txt_nombre);
        txt_paterno = (EditText) findViewById(R.id.txt_paterno);
        txt_materno = (EditText) findViewById(R.id.txt_materno);
        txt_dia = (EditText) findViewById(R.id.txt_dia);
        txt_mes = (EditText) findViewById(R.id.txt_mes);
        txt_anio = (EditText) findViewById(R.id.txt_anio);
        txt_licencia = (EditText) findViewById(R.id.txt_licencia);
        txt_password = (EditText) findViewById(R.id.txt_password);
        txt_celular = (EditText) findViewById(R.id.txt_celular);
    }

    @Override
    public void onResume() {
        super.onResume();
        txt_nombre.getText().clear();
        txt_paterno.getText().clear();
        txt_materno.getText().clear();
        txt_dia.getText().clear();
        txt_mes.getText().clear();
        txt_anio.getText().clear();
        txt_licencia.getText().clear();
        txt_password.getText().clear();
        txt_celular.getText().clear();
    }

    public void registrar(View view) {
        if (validar() && isOnline()) {
            nombre = txt_nombre.getText().toString();
            ap_paterno = txt_paterno.getText().toString();
            ap_materno = txt_materno.getText().toString();
            dia = txt_dia.getText().toString();
            mes = txt_mes.getText().toString();
            anio = txt_anio.getText().toString();
            no_licencia = txt_licencia.getText().toString();
            password = txt_password.getText().toString();
            tel_celular = txt_celular.getText().toString();
            String fecha_nacimiento = anio + "-" + mes + "-" + dia;
            Conductor conductor = new Conductor(nombre, ap_paterno, ap_materno, fecha_nacimiento,
                    no_licencia, tel_celular, password);
            WSPOSTRegistrarTask task = new WSPOSTRegistrarTask();
            task.execute(conductor);
        }
    }

    public void cancelar(View view) {
        finish();
    }

    private boolean validar() {
        boolean ok = true;
        if (txt_nombre.getText() == null || txt_nombre.getText().toString().isEmpty()) {
            txt_nombre.setError("Por favor, introduce tu nombre");
            ok = false;
        }

        if (txt_paterno.getText() == null || txt_paterno.getText().toString().isEmpty()) {
            txt_paterno.setError("Por favor, introduce tu apellido paterno");
            ok = false;
        }

        if (txt_dia.getText() == null || txt_dia.getText().toString().isEmpty()) {
            txt_dia.setError("Por favor, introduce tu día de nacimiento");
            ok = false;
        }

        if (txt_mes.getText() == null || txt_mes.getText().toString().isEmpty()) {
            txt_mes.setError("Por favor, introduce tu mes de nacimiento");
            ok = false;
        }

        if (txt_anio.getText() == null || txt_anio.getText().toString().isEmpty()) {
            txt_anio.setError("Por favor, introduce tu año de nacimiento");
            ok = false;
        }

        if (txt_licencia.getText() == null || txt_licencia.getText().toString().isEmpty()) {
            txt_licencia.setError("Por favor, introduce tu número de licencia");
            ok = false;
        }

        if (txt_celular.getText() == null || txt_celular.getText().toString().isEmpty()) {
            txt_celular.setError("Por favor, introduce tu teléfono celular");
            ok = false;
        }

        if (txt_password.getText() == null || txt_password.getText().toString().isEmpty()) {
            txt_password.setError("Por favor, introduce tu número de licencia");
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
                RegistroActivity.this).create();
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

    class WSPOSTRegistrarTask extends AsyncTask<Conductor, String, String> {
        @Override
        protected String doInBackground(Conductor... params) {
            resws = HttpUtils.registrarConductor(params[0]);
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
            if (mensaje.getStatusMensaje() == 0) {
                Intent intent = new Intent(this, ValidarCodigoActivity.class);
                intent.putExtra("tel_celular", txt_celular.getText().toString());
                startActivity(intent);
            } else if (mensaje.getStatusMensaje() == 1) {
                mostrarAlertDialog("Error", mensaje.getMensaje());
            } else if (mensaje.getStatusMensaje() == 149) {
                mostrarAlertDialog("Usuario existente", mensaje.getMensaje());
            }
        } else {
            mostrarAlertDialog("Error", resws.getResult());
        }
    }
}

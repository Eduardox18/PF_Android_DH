package com.rockola.rsx;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rockola.rsx.ws.HttpUtils;
import com.rockola.rsx.ws.Response;
import com.rockola.rsx.ws.pojos.Conductor;
import com.rockola.rsx.ws.pojos.Mensaje;

public class ValidarCodigoActivity extends AppCompatActivity {

    private String codigo_verificacion;
    private String tel_celular;
    private EditText txt_codigo;

    private Response resws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validar_codigo);

        txt_codigo = (EditText) findViewById(R.id.txt_codigo);

        parametrosIntent();
    }

    @Override
    public void onResume() {
        super.onResume();
        txt_codigo.getText().clear();
    }

    private void parametrosIntent() {
        Intent intent = getIntent();
        tel_celular = intent.getStringExtra("telCelular");
    }

    public void activarUsuario(View view) {
        if (validar() && isOnline()) {
            codigo_verificacion = txt_codigo.getText().toString();
            WSPostActivarConductor task = new WSPostActivarConductor();
            task.execute(tel_celular, codigo_verificacion);
        }
    }

    private boolean validar() {
        boolean ok = true;
        if (txt_codigo.getText() == null || txt_codigo.getText().toString().isEmpty()) {
            txt_codigo.setError("Por favor, introduce tu nombre");
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
                ValidarCodigoActivity.this).create();
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

    class WSPostActivarConductor extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            resws = HttpUtils.activarConductor(params[0], params[1]);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            resultadoActivar();
        }
    }

    private void resultadoActivar() {
        if (resws != null && !resws.isError() && resws.getResult() != null) {
            Mensaje mensaje = new Gson().fromJson(resws.getResult(), Mensaje.class);
            if (mensaje.getStatusMensaje() == 150) {
                Intent intent = new Intent(this, MenuActivity.class);
                intent.putExtra("id_conductor", mensaje.getConductor().getIdConductor());
                String nombreCompleto = mensaje.getConductor().getNombre() + " " +
                        mensaje.getConductor().getApPaterno() + " " +
                        mensaje.getConductor().getApMaterno();
                intent.putExtra("nombre", nombreCompleto);
                intent.putExtra("tel_celular",
                        mensaje.getConductor().getTelCelular().toString());
                startActivity(intent);
            } else if (mensaje.getStatusMensaje() == 151) {
                mostrarAlertDialog("Problema al activar", mensaje.getMensaje());
                //finish();
            } else if (mensaje.getStatusMensaje() == 253) {
                mostrarAlertDialog("Usuario no registrado", mensaje.getMensaje());
                //finish();
            } else if (mensaje.getStatusMensaje() == 152) {
                mostrarAlertDialog("Código incorrecto", mensaje.getMensaje());
            } else if (mensaje.getStatusMensaje() == 1){
                mostrarAlertDialog("Error", mensaje.getMensaje());
            }
        } else {
            mostrarAlertDialog("Error", resws.getResult());
        }
    }
}

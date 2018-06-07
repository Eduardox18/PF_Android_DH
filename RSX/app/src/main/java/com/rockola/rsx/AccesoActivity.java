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
import com.rockola.rsx.ws.pojos.Mensaje;

public class AccesoActivity extends AppCompatActivity {

    private String usuario;
    private String password;
    private EditText txt_cel;
    private EditText txt_pass;
    private Response resws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceso);

        txt_cel = (EditText) findViewById(R.id.txt_cel);
        txt_pass = (EditText) findViewById(R.id.txt_pass);
    }

    @Override
    public void onResume() {
        super.onResume();
        txt_cel.getText().clear();
        txt_pass.getText().clear();
    }

    public void ingresar(View view) {
        if (validar() && isOnline()) {
            usuario = txt_cel.getText().toString();
            password = txt_pass.getText().toString();
            WSPOSTLoginTask task = new WSPOSTLoginTask();
            task.execute(usuario, password);
        }
    }

    public void registrar (View view) {
        Intent intent = new Intent(AccesoActivity.this, RegistroActivity.class);
        startActivity(intent);
    }

    public boolean validar() {
        boolean flag = true;
        if (txt_cel.getText() == null || txt_cel.getText().toString().isEmpty()) {
            txt_cel.setError("Debes introducir tu celular de acceso");
            flag = false;
        }

        if (txt_pass.getText() == null ||txt_pass.getText().toString().isEmpty()) {
            txt_pass.setError("Debes introducir la contraseña de acceso");
            flag = false;
        }

        return flag;
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
                AccesoActivity.this).create();
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

    class WSPOSTLoginTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            resws = HttpUtils.ingresarSistema(params[0], params[1]);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            resultadoEntrar();
        }
    }

    private void resultadoEntrar() {
        if (resws != null && !resws.isError() && resws.getResult() != null) {
            Mensaje mensaje = new Gson().fromJson(resws.getResult(), Mensaje.class);
            if (mensaje.getStatusMensaje() == 250) {
                Intent intent = new Intent(this, MenuActivity.class);
                intent.putExtra("id_conductor", String.valueOf(mensaje.getConductor().getIdConductor()));
                String nombreCompleto = mensaje.getConductor().getNombre() + " " +
                        mensaje.getConductor().getApPaterno() + " " +
                        mensaje.getConductor().getApMaterno();
                intent.putExtra("nombre", nombreCompleto);
                intent.putExtra("tel_celular",
                        mensaje.getConductor().getTelCelular().toString());
                startActivity(intent);
            } else if (mensaje.getStatusMensaje() == 252){
                Intent intent = new Intent(this, ValidarCodigoActivity.class);
                intent.putExtra("tel_celular", txt_cel.getText().toString());
                startActivity(intent);
            } else {
                mostrarAlertDialog((mensaje.getStatusMensaje() == 1) ? "Error" : "Aviso",
                        mensaje.getMensaje());
            }
        } else {
            mostrarAlertDialog("Error", resws.getResult());
        }
    }
}

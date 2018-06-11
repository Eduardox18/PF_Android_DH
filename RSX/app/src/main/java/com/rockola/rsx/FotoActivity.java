package com.rockola.rsx;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.rockola.rsx.ws.HttpUtils;
import com.rockola.rsx.ws.Response;
import com.rockola.rsx.ws.pojos.Mensaje;

public class FotoActivity extends AppCompatActivity {

    public static final int REQUEST_CAPTURE = 1;
    public static final int PICK_IMAGE = 100;
    private ImageView img_foto;
    private Button button_tomar;
    private Button button_enviar;
    private ProgressDialog espera;
    private Bitmap foto;
    private Uri photoURI;
    private Response resws;

    private String idReporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);

        img_foto = (ImageView) findViewById(R.id.img_foto);
        button_enviar = (Button) findViewById(R.id.botton_enviar);

        button_enviar.setEnabled(false);
        if (!validarCamara()) {
            Toast.makeText(this, "El dispositivo no cuenta con cámara",
                    Toast.LENGTH_LONG).show();
        }
        validarPermisosAlmacenamiento();
        parametrosIntent();
    }

    private void parametrosIntent() {
        Intent intent = getIntent();
        idReporte = intent.getStringExtra("id_reporte");
    }

    private void validarPermisosAlmacenamiento() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Sin acceso al almacenamiento interno",
                    Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);
        }
    }

    private boolean validarCamara(){
        return getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY);
    }

    public void tomarFoto(View view) {
        button_enviar.setEnabled(false);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File tmp = null;

        try {
            tmp = crearArchivoTemporal();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (tmp != null) {
            photoURI = FileProvider.getUriForFile(this,
                    "com.rockola.rsx", tmp);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(intent, REQUEST_CAPTURE);
        }
    }

    private File crearArchivoTemporal() throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String nombre = sdf.format(new Date()) + ".jpg";
        File path = getExternalFilesDir(
                Environment.DIRECTORY_PICTURES + "/tmps");
        File archivo = File.createTempFile(nombre,
                ".jpg",path);
        return archivo;
    }

    @Override
    protected  void onActivityResult(int requestCode,
                                     int resultCode, Intent data) {
        //--------DESDE CAMARA----------//
        if (requestCode == REQUEST_CAPTURE) {
            if (resultCode == RESULT_OK) {
                img_foto.setImageURI(this.photoURI);
                img_foto.setRotation(90);
                foto = ((BitmapDrawable) img_foto.getDrawable()).getBitmap();
                button_enviar.setEnabled(true);
            }
        }
    }

    public void subirAServidor(View view){
        espera = new ProgressDialog(this);
        espera.setTitle("Subiendo foto");
        espera.setMessage("Espera por favor");
        espera.setCancelable(false);
        espera.show();
        WSPOSTFotosTask task = new WSPOSTFotosTask();
        task.execute(idReporte, this.foto);
    }

    class WSPOSTFotosTask extends AsyncTask<Object, String, String> {
        @Override
        protected String doInBackground(Object ... params) {
            resws = HttpUtils.subirFoto((String)params[0],(Bitmap)params[1]);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            resultadoFoto();
        }
    }

    private void resultadoFoto() {
        espera.dismiss();
        if (resws != null && !resws.isError()) {
            Mensaje mensaje = new Gson().fromJson(resws.getResult(),Mensaje.class);
            if (mensaje.getStatusMensaje() == 700) {
                mostrarAlertDialog("Éxito", mensaje.getMensaje());
            } else if (mensaje.getStatusMensaje() == 1) {
                mostrarAlertDialog("Error", mensaje.getMensaje());
            }
        } else {
            mostrarAlertDialog("Error", resws.getResult());
        }
    }

    private void mostrarAlertDialog(String titulo, String mensaje){
        AlertDialog dialog = new AlertDialog.Builder(FotoActivity.this).create();
        dialog.setMessage(mensaje);
        dialog.setTitle(titulo);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        dialog.show();
    }
}

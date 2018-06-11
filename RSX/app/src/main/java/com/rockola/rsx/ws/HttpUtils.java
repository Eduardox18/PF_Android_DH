package com.rockola.rsx.ws;

import com.rockola.rsx.ws.pojos.Conductor;
import com.rockola.rsx.ws.pojos.Reporte;
import com.rockola.rsx.ws.pojos.Vehiculo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by lalo on 4/6/18.
 */

public class HttpUtils {
    private static final String BASE_URL =
            "http://192.168.100.8:8080/Transito/transito/";
    private static final Integer CONNECT_TIMEOUT = 4000; //MILISEGUNDOS
    private static final Integer READ_TIMEOUT = 10000; //MILISEGUNDOS

    public static Response ingresarSistema(String celular, String contrasena) {
        String param = String.format("telCelular=%s&password=%s", celular, contrasena);
        return invocarServicioWeb("conductor/ingresarApp", "POST", param);
    }

    public static Response registrarConductor(Conductor conductor) {
        String param = String.format("nombre=%s&apPaterno=%s&apMaterno=%s&fechaNacimiento=%s" +
                "&noLicencia=%s&telCelular=%s&password=%s", conductor.getNombre(),
                conductor.getApPaterno(), conductor.getApMaterno(), conductor.getFechaNacimiento(),
                conductor.getNoLicencia(), conductor.getTelCelular(), conductor.getPassword());
        return invocarServicioWeb("conductor/agregarConductor", "POST", param);
    }

    public static Response activarConductor (String celular, String codigo) {
        String param = String.format("telCelular=%s&codigoVerificacion=%s", celular, codigo);
        return invocarServicioWeb("conductor/activarConductor", "POST", param);
    }

    public static Response registrarVehiculo (Vehiculo vehiculo) {
        String param = String.format("noPlaca=%s&modelo=%s&anio=%s&noPolizaSeguro=%s&idMarca=%s&" +
        "idAseguradora=%s&idColor=%s&idConductor=%s", vehiculo.getNoPlaca(), vehiculo.getModelo(),
                vehiculo.getAnio(), vehiculo.getNoPolizaSeguro(), vehiculo.getIdMarca(),
                vehiculo.getIdAseguradora(), vehiculo.getIdColor(), vehiculo.getIdConductor());
        return invocarServicioWeb("vehiculo/agregarVehiculo", "POST", param);
    }

    public static Response consultarColores() {
        String param = "";
        return invocarServicioWeb("color/consultarColores", "GET", param);
    }

    public static Response consultarAseguradoras() {
        String param = "";
        return invocarServicioWeb("aseguradora/consultarAseguradoras", "GET", param);
    }

    public static Response consultarMarcas() {
        String param = "";
        return invocarServicioWeb("marca/consultarMarcas", "GET", param);
    }

    public static Response consultarVehiculos(String idConductor) {
        String param = String.format("%s", idConductor);
        return invocarServicioWeb("vehiculo/consultarVehiculos/", "GET", param);
    }

    public static Response consultarVehiculo(String noPlaca) {
        String param = String.format("noPlaca=%s", noPlaca);
        return invocarServicioWeb("vehiculo/consultarVehiculo", "POST", param);
    }

    public static Response levantarReporte(Reporte reporte) {
        String param = String.format("latitud=%s&longitud=%s&nombreSiniestro=%s&" +
                "apPaternoSiniestro=%s&apMaternoSiniestro=%s&idConductor=%s&idVehiculoConductor" +
                "=%s&idVehiculoSiniestro=%s");
        return invocarServicioWeb("reporte/levantarReporte", "POST", param);
    }

    private static Response invocarServicioWeb(String url, String tipoinvocacion, String parametros){
        HttpURLConnection c = null;
        URL u = null;
        Response res = null;
        try {
            if(tipoinvocacion.compareToIgnoreCase("GET")==0){
                u = new URL(BASE_URL+url+((parametros!=null)?parametros:""));
                c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod(tipoinvocacion);
                c.setRequestProperty("Content-length", "0");
                c.setUseCaches(false);
                c.setAllowUserInteraction(false);
                c.setConnectTimeout(CONNECT_TIMEOUT);
                c.setReadTimeout(READ_TIMEOUT);
                c.connect();
            }else{
                u = new URL(BASE_URL+url);
                c = (HttpURLConnection) u.openConnection();
                c.setRequestMethod(tipoinvocacion);
                c.setDoOutput(true);
                c.setConnectTimeout(CONNECT_TIMEOUT);
                c.setReadTimeout(READ_TIMEOUT);
                //----PASAR PARÁMETROS EN EL CUERPO DEL MENSAJE POST, PUT y DELETE----//
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                        c.getOutputStream(), "UTF-8"));
                bw.write(parametros);
                bw.flush();
                bw.close();
                //------------------------------------------------------//
            }
            res = new Response();
            res.setStatus(c.getResponseCode());
            if(res.getStatus()!=200 && res.getStatus()!=201){
                res.setError(true);
            }
            if(c.getInputStream()!=null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();
                res.setResult(sb.toString());
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            res.setError(true);
            res.setResult(ex.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
            res.setError(true);
            res.setResult(ex.getMessage());
        } finally {
            if (c != null) {
                c.disconnect();
            }
        }
        return res;
    }
}

package com.example.proyecto1;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;


import javax.net.ssl.HttpsURLConnection;

//Servicio donde se comprueba la existencia de un usuario con ese nombre y contraseña para acceder o no a la aplicación
public class comprobarUsuarioDBWebService extends Worker {
    public comprobarUsuarioDBWebService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String usuario= getInputData().getString("usuario");
        String password= getInputData().getString("password");
        HttpsURLConnection urlConnection= GeneradorConexionesSeguras.getInstance().crearConexionSegura(getApplicationContext(),"https://134.209.235.115/mmerayo002/WEB/comprobarUsuario.php");
        JSONObject parametrosJSON= new JSONObject();
        try {
            parametrosJSON.put("usuario", usuario);
            parametrosJSON.put("password", password);
            Log.i("UsuarioAcomprobar: ", " " + usuario);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            PrintWriter out= new PrintWriter(urlConnection.getOutputStream());
            out.print(parametrosJSON.toString());
            out.close();
            Log.i("Conexion: ", "Enviada");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        int statusCode= 0;
        String usuario2="";
        String resultado="";
        try {
            statusCode = urlConnection.getResponseCode();
            Log.i("status: "," " +statusCode);
            if(statusCode== 200) {
                Log.i("Conexion: ", "Devuelta");
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String line, result = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                inputStream.close();
                Log.i("respuesta:", " "+result);
                JSONParser parser = new JSONParser();
                org.json.simple.JSONObject json = (org.json.simple.JSONObject) parser.parse(result);

                usuario2 = (String) json.get("usuario");
                String password2 = (String) json.get("password");


            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        Log.i("usuario", " "+usuario2);
        if (usuario2 != null){
            resultado = "existe";

        }else{
            resultado="noexiste";
        }
        Log.i("resultado", ""+resultado);
        Data resultados = new Data.Builder()
                .putString("resultado", resultado)
                .build();
        return Result.success(resultados);

    }
}

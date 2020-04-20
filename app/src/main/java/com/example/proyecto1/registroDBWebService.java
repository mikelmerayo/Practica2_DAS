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

public class registroDBWebService extends Worker {
    public registroDBWebService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String usuario= getInputData().getString("usuario");
        String password= getInputData().getString("password");
        HttpsURLConnection urlConnection= GeneradorConexionesSeguras.getInstance().crearConexionSegura(getApplicationContext(),"https://134.209.235.115/mmerayo002/WEB/registro.php");
        JSONObject parametrosJSON= new JSONObject();
        try {
            parametrosJSON.put("usuario", usuario);
            parametrosJSON.put("password", password);
            Log.i("UsuarioARegistrar: ", " " + usuario);
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
        /*
        Uri.Builder builder= new Uri.Builder().appendQueryParameter("usuario", usuario);
        String parametros= builder.build().getEncodedQuery();
        urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
        */

        int statusCode= 0;
        String usuario2="";
        String resultado="";
        try {
            statusCode = urlConnection.getResponseCode();
            Log.i("status: "," " +statusCode);
            if(statusCode== 200) {
                return Result.success();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Result.success() Result.failure() Result.retry()
        return Result.failure();
    }


}

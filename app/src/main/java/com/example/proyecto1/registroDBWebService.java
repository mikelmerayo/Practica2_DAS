package com.example.proyecto1;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;

import javax.net.ssl.HttpsURLConnection;

//Servicio donde se inserta un nuevo usuario en la BD
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
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            PrintWriter out= new PrintWriter(urlConnection.getOutputStream());
            out.print(parametrosJSON.toString());
            out.close();
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        int statusCode= 0;

        try {
            statusCode = urlConnection.getResponseCode();
            if(statusCode== 200) {
                return Result.success();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.failure();
    }


}

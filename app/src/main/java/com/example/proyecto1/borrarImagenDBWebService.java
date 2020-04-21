package com.example.proyecto1;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.PrintWriter;
import javax.net.ssl.HttpsURLConnection;


//Servicio donde se borra la foto de perfil almacenada en la BD remota
public class borrarImagenDBWebService extends Worker {
    public borrarImagenDBWebService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String identificador = getInputData().getString("identificador");
        HttpsURLConnection urlConnection = GeneradorConexionesSeguras.getInstance().crearConexionSegura(getApplicationContext(), "https://134.209.235.115/mmerayo002/WEB/borrarImagen.php");
        JSONObject parametrosJSON = new JSONObject();
        try {
            parametrosJSON.put("identificador", identificador);
            Log.i("identificador: ", " " + identificador);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(parametrosJSON.toString());
            out.close();
            Log.i("Conexion: ", "Enviada");
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        int statusCode = 0;
        Bitmap elBitmap=null;
        try {
            statusCode = urlConnection.getResponseCode();
            Log.i("status: ", " " + statusCode);
            if (statusCode == 200) {
                return Result.success();
            }
        } catch (IOException e) {
                e.printStackTrace();
        }

        return Result.failure();
    }

}

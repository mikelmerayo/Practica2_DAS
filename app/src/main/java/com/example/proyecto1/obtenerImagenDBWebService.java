package com.example.proyecto1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.net.ssl.HttpsURLConnection;

//Servicio para obtener la foto de perfil del usuario
public class obtenerImagenDBWebService extends Worker {
    public obtenerImagenDBWebService(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        String identificador = getInputData().getString("identificador");
        HttpsURLConnection urlConnection = GeneradorConexionesSeguras.getInstance().crearConexionSegura(getApplicationContext(), "https://134.209.235.115/mmerayo002/WEB/obtenerImagen.php");
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
                elBitmap= BitmapFactory.decodeStream(urlConnection.getInputStream());
                if(elBitmap!=null){
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    elBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] fototransformada = stream.toByteArray();
                    String fotoen64 = Base64.encodeToString(fototransformada,Base64.DEFAULT);

                    Data resultados = new Data.Builder()
                            .putString("resultado", fotoen64)
                            .build();

                    return Result.success(resultados);
                }else{
                    Data resultados = new Data.Builder()
                            .putString("resultado", "notienefoto")
                            .build();

                    return Result.success(resultados);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.failure();
    }
}

package com.example.proyecto1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CambiarFoto extends AppCompatActivity {

    final int CODIGO_FOTO_ARCHIVO=1;
    Uri uriimagen= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_foto);


        File directorio=this.getFilesDir();
        Button sacarFoto = (Button) findViewById(R.id.sacarFoto);
        //Al pulsar en cambiar foto
        sacarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Se llama al servicio que borra la foto actual
                borrarImagenABD();
                //Se crea la uri con la imagen sacada
                String timeStamp= new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String nombrefich= "IMG_" + timeStamp+ "_";
                File fichImg= null;

                try {
                    fichImg= File.createTempFile(nombrefich, ".jpg",directorio);
                    uriimagen= FileProvider.getUriForFile(getApplicationContext(), "com.example.proyecto1.provider", fichImg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent elIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                elIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriimagen);
                startActivityForResult(elIntent, CODIGO_FOTO_ARCHIVO);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODIGO_FOTO_ARCHIVO && resultCode == RESULT_OK) {
            ImageView perfil = (ImageView) findViewById(R.id.fotoPerfil);

            //Se pone la foto sacada en el image view y se redimensiona para guardarla en la BD
            perfil.setImageURI(uriimagen);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriimagen);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bitmap fotoredimensionado= Bitmap.createScaledBitmap(bitmap,50,50,true);

            fotoredimensionado.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] fototransformada = stream.toByteArray();
            String fotoen64 = Base64.encodeToString(fototransformada,Base64.DEFAULT);
            Toast.makeText(getApplicationContext(), "Cierre sesión para aplicar cualquier cambio", Toast.LENGTH_LONG).show();
            subirImagenABD(fotoen64);

        }
    }

    private void subirImagenABD(String fotoen64) {
        Log.i("foto", " Se va a subir a la BD");

        String usuario = MainActivity.getUsuario();
        Data datos = new Data.Builder()
                .putString("identificador", usuario)
                .putString("imagen", fotoen64)
                .build();

        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(subirImagenDBWebService.class).setInputData(datos).build();
        WorkManager.getInstance(CambiarFoto.this).enqueue(otwr);

    }

    private void borrarImagenABD() {
        Log.i("foto", " Se va a borrar la foto");

        String usuario = MainActivity.getUsuario();
        Data datos = new Data.Builder()
                .putString("identificador", usuario)
                //.putByteArray("imagen", fototransformada)
                .build();

        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(borrarImagenDBWebService.class).setInputData(datos).build();
        WorkManager.getInstance(CambiarFoto.this).enqueue(otwr);

    }



}

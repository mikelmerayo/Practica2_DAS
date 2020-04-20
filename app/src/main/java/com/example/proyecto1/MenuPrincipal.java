package com.example.proyecto1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.preference.PreferenceManager;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;


public class MenuPrincipal extends AppCompatActivity implements DialogoSesion.ListenerdelDialogo {


    private InterstitialAd anuncio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Se comprueban las preferencias del usuario para elegir los colores de la actividad
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String coloresPreferidos= prefs.getString("colorpref","bas");
        if(coloresPreferidos.equals("rng")){
            this.getTheme().applyStyle(R.style.rng, true);
        }else if(coloresPreferidos.equals("ang")){
            this.getTheme().applyStyle(R.style.ang, true);
        }else{
            this.getTheme().applyStyle(R.style.AppTheme, true);
        }
        setContentView(R.layout.activity_menu_principal);

        MobileAds.initialize(this,"ca-app-pub-3940256099942544~3347511713");
        anuncio = new InterstitialAd(this);
        anuncio.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        AdRequest adRequest = new AdRequest.Builder().build();
        anuncio.loadAd(adRequest);
        anuncio.setAdListener(new AdListener() {
              @Override
              public void onAdLoaded() {
                  anuncio.show();
              }
        });

        //Asignamos la action bar personalizada
        Toolbar barra = findViewById(R.id.labarra);
        setSupportActionBar(barra);

        String usuario = MainActivity.getUsuario();
        Data datos = new Data.Builder()
                .putString("identificador", usuario)
                .build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(obtenerImagenDBWebService.class).setInputData(datos).build();
        WorkManager.getInstance(MenuPrincipal.this).getWorkInfoByIdLiveData(otwr.getId()).observe(MenuPrincipal.this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                if (workInfo != null && workInfo.getState().isFinished()) {
                    String fotoperfil = workInfo.getOutputData().getString("resultado");
                    if(!fotoperfil.equals("notienefoto")){
                        byte [] encodeByte= Base64.decode(fotoperfil,Base64.DEFAULT);
                        Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                        ImageView perfil = (ImageView) findViewById(R.id.fotoPerfilM);
                        int anchoDestino= perfil.getWidth();
                        int altoDestino= perfil.getHeight();
                        int anchoImagen= bitmap.getWidth();
                        int altoImagen= bitmap.getHeight();
                        float ratioImagen= (float) anchoImagen/ (float) altoImagen;
                        float ratioDestino= (float) anchoDestino/ (float) altoDestino;
                        int anchoFinal= anchoDestino;
                        int altoFinal= altoDestino;
                        if (ratioDestino> ratioImagen) {
                            anchoFinal= (int) ((float)altoDestino* ratioImagen);
                        } else{
                            altoFinal= (int) ((float)anchoDestino/ ratioImagen);
                        }
                        Bitmap fotoredimensionado= Bitmap.createScaledBitmap(bitmap,anchoFinal,altoFinal,true);

                        perfil.setImageBitmap(fotoredimensionado);
                    }
                    Log.i("foto", " "+fotoperfil);
                }
            }
        });
        WorkManager.getInstance(MenuPrincipal.this).enqueue(otwr);




        //Al pulsar en ver videojuegos disponibles se abre la actividad donde se muestran todos los videojuegos que se pueden comprar
        Button vervideojuegosdisponibles = (Button) findViewById(R.id.videojuegosdisponibles);
        vervideojuegosdisponibles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuPrincipal.this, MostrarVideojuegosDisponibles.class);
                startActivity(i);

            }
        });

        //Al pulsar en ver mis videojuegos se abre la actividad donde se muestran los videojuegos de ese usuario
        Button misvideojuegos = (Button) findViewById(R.id.misvideojuegos);
        misvideojuegos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuPrincipal.this, MostrarMisVideojuegos.class);
                startActivity(i);

            }
        });

        //Se abre el dialogo de la clase DialogoSesion
        Button cerrarsesion = (Button) findViewById(R.id.cerrarsesion);
        cerrarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogoSesion= new DialogoSesion();
                dialogoSesion.show(getSupportFragmentManager(), "dialogosesion");

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.definicion_menu, menu);
        return true;
    }

    //Metodo donde se define que hacer en caso de pulsar una de las opciones contenidas en definicion_menu.xml, es decir, en nuestra action bar persoanlizada
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.ajustes:{ //Se abre la actividad ajustes para personalizar idioma y colores
                Log.i("Ajustes", "Se ha pulsado ajustes");
                Intent i=new Intent(MenuPrincipal.this, Ajustes.class);
                startActivity(i);

                break;

            }
            case R.id.monedero:{ //Se abre la actividad recargar monedero para añadir dinero a la cuenta

                Log.i("Monedero", "Se ha pulsado monedero");
                Intent i=new Intent(MenuPrincipal.this, RecargarMonedero.class);
                startActivity(i);

                break;

            }

            case R.id.ayuda:{ //Se abre la cuenta de gmail del usuario para mandar un mail al administrador de la aplicación con su duda
                Log.i("Ayuda", "Se ha pulsado ayuda");

                String email = "mikelmerayo@gmail.com";
                String subject = "Duda";
                String body = "Escribe tu duda... ";
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"+email));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(emailIntent, "Ayuda"));

                break;

            }
        }
        return super.onOptionsItemSelected(item);
    }


    //Al pulsar aceptar en el dialogo de sesion se vuelve a la actividad de login
    @Override
    public void alpulsarSI() {
        Intent i = new Intent(MenuPrincipal.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    //Al pulsar cancelar no pasa nada
    @Override
    public void alpulsarNO() {


    }
}

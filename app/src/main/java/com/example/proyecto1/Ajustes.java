package com.example.proyecto1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

public class Ajustes extends AppCompatActivity {

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
        setContentView(R.layout.activity_ajustes);

        //Al pulsar cambiar idioma se cambia de ingles a castellano o viceversa, en función de que idioma este puesto actualmente
        Button cambiarIdioma = (Button) findViewById(R.id.cambiaridioma);
        cambiarIdioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                miBD gestorDB = new miBD(getApplicationContext(), "miBD", null, 1);
                String usuario =  MainActivity.getUsuario();
                String idioma = gestorDB.consultarIdioma(usuario);
                if(idioma.equals("Castellano")){
                    String idiomanuevo="Ingles";
                    gestorDB.cambiarIdioma(usuario, idiomanuevo);
                    Locale nuevaloc = new Locale("en");
                    Locale.setDefault(nuevaloc);
                    Configuration config = new Configuration();
                    config.locale = nuevaloc;
                    getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
                }else if(idioma.equals("Ingles")){
                    String idiomanuevo="Castellano";
                    gestorDB.cambiarIdioma(usuario, idiomanuevo);
                    Locale nuevaloc = new Locale("es");
                    Locale.setDefault(nuevaloc);
                    Configuration config = new Configuration();
                    config.locale = nuevaloc;
                    getBaseContext().getResources().updateConfiguration(config,getBaseContext().getResources().getDisplayMetrics());
                }
                //Se le notifica al usuario el cambio y se vuelve al menu principal
                Toast.makeText(getApplicationContext(), "Se ha cambiado el idioma", Toast.LENGTH_LONG).show();
                Intent i = new Intent(Ajustes.this, MenuPrincipal.class);
                startActivity(i);
                finish();


            }
        });

        //Al pulsar cambiar colores se le llama a la actividad MostrarPreferencias donde el usuario podrá elegir sus colores favoritos
        Button cambiarColores = (Button) findViewById(R.id.cambiarcolores);
        cambiarColores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Cierre sesión para aplicar cualquier cambio", Toast.LENGTH_LONG).show();
                Intent i = new Intent(Ajustes.this, MostrarPreferencias.class);
                startActivity(i);
            }
        });

        //Al pulsar cambiar foto se le llama a la actividad CambiarFoto donde el usuario podrá cambiar su foto de perfil
        Button cambiarFoto = (Button) findViewById(R.id.cambiarFoto);
        cambiarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Si pulsa en sacar foto se borrará automaticamente la foto actual", Toast.LENGTH_LONG).show();
                Intent i = new Intent(Ajustes.this, CambiarFoto.class);
                startActivity(i);
            }
        });
    }
}

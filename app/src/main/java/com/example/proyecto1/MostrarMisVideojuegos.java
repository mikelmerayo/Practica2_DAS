package com.example.proyecto1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MostrarMisVideojuegos extends AppCompatActivity {

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
        setContentView(R.layout.activity_mostrar_mis_videojuegos);


        miBD gestorDB = new miBD(getApplicationContext(), "miBD", null, 1);
        String usuario = MainActivity.getUsuario();

        //Se obtiene la lista de videojuegos que posee el usuario
        String [] nombres = gestorDB.obtenerMisNombresVideojuegos(usuario);

        //Se crea la lista con los nombres de los videojuegos y se les asigna el adaptador
        ArrayAdapter eladaptador=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,nombres);
        ListView misVideojuegos= (ListView) findViewById(R.id.miListaVideojuegos);
        misVideojuegos.setAdapter(eladaptador);

        //Al pulsar atras se vuelve al menu principal
        Button atras = (Button) findViewById(R.id.atras2);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MostrarMisVideojuegos.this, MenuPrincipal.class);
                startActivity(i);
                finish();
            }
        });

    }


}

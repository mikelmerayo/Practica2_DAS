package com.example.proyecto1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RecargarMonedero extends AppCompatActivity {

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
        setContentView(R.layout.activity_recargar_monedero);

        //Se consulta el dinero del usuario
        String usu = MainActivity.getUsuario();
        miBD gestorDB = new miBD(getApplicationContext(), "miBD", null, 1);
        double din = gestorDB.consultarDinero(usu);

        //Se le muestra su dinero por pantalla
        TextView dineroact = (TextView) findViewById(R.id.dineroact);
        String sdinero = String.valueOf(din);
        dineroact.setText(sdinero);

        //Al pulsar recargar se coge la cantidad introducida por el usuario y se le añade a su dinero
        Button recargar = (Button) findViewById(R.id.recargar);
        recargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText cantidad = (EditText) findViewById(R.id.cantidad);
                String cant = cantidad.getText().toString();
                //La cantidad de dinero a introducir no debe estar vacía
                if (cant.length()!=0){
                    try{
                        //La cantidad de dinero a introducir debe ser un número
                        Double dinero = Double.parseDouble(cant);
                        String usuario = MainActivity.getUsuario();

                        miBD gestorDB = new miBD(getApplicationContext(), "miBD", null, 1);
                        gestorDB.agregarDinero(usuario, dinero);

                        Toast.makeText(getApplicationContext(), "Se ha añadido su dinero correctamente", Toast.LENGTH_LONG).show();

                        //Si se añade correctamente se vuelve al menú principal
                        Intent i = new Intent(RecargarMonedero.this, MenuPrincipal.class);
                        startActivity(i);
                        finish();
                    }catch(Exception e){
                        Toast.makeText(getApplicationContext(), "EL dinero debe ser un numero", Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "Debe introducir una cantidad", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState){ //Metodo para guardar los valores en caso de girar, pausar... la app
        super.onSaveInstanceState(outState);

        EditText cantidad = (EditText) findViewById(R.id.cantidad);
        String cant = cantidad.getText().toString();
        outState.putString("Cantidad", cant);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){ //Metodo para restablecer los valores guardados
        super.onRestoreInstanceState(savedInstanceState);


        String cant = savedInstanceState.getString("Cantidad");
        EditText cantidad = (EditText) findViewById(R.id.cantidad);
        cantidad.setText(String.valueOf(cant));

    }
}

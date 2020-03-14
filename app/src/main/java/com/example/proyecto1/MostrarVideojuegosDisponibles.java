package com.example.proyecto1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;




public class MostrarVideojuegosDisponibles extends AppCompatActivity implements DialogoCompra.ListenerdelDialogo{


    private  static String vj;


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
        setContentView(R.layout.activity_mostrar_videojuegos_disponibles);

        final miBD gestorDB = new miBD(getApplicationContext(), "miBD", null, 1);

        //Se obtienen los nombres, imagenes y valoraciones de los videojuegos
        String [] nombres = gestorDB.obtenerNombresVideojuegos();
        int[] imagenes = gestorDB.obtenerImagenesVideojuegos();
        double[] valoraciones = gestorDB.obtenerValoracionesVideojuegos();

        //Se crea la lista personalizada con los elemntos obtenidos anteriormente para mostrarselos al usuario
        final ListView videojuegos= (ListView) findViewById(R.id.listaVideojuegos);
        AdaptadorListavideojuegos eladap= new AdaptadorListavideojuegos(getApplicationContext(),nombres,imagenes,valoraciones);
        videojuegos.setAdapter(eladap);
        videojuegos.setClickable(true);

        //Al pulsar un videojuego se abre el dialogo compra dónde se pregunta si estas seguro de querer comprarlo
        videojuegos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){

                String nombreV = ((TextView)view.findViewById(R.id.nombreVid)).getText().toString();
                setVJ(nombreV);
                double precio = gestorDB.consultarPrecioVideojuego(nombreV);
                DialogFragment dialogoCompra= new DialogoCompra(precio);
                dialogoCompra.show(getSupportFragmentManager(), "dialogocompra");



            }
        });

        //Al pulsar atras se vuelve al menu principal
        Button atras = (Button) findViewById(R.id.atras);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MostrarVideojuegosDisponibles.this, MenuPrincipal.class);
                startActivity(i);
                finish();
            }
        });
    }

    //Se guarda el videojuego seleccionado como un atributo
    private void setVJ(String nombreV) {

        vj = nombreV;
    }

    //Cuando se pulsa aceptar en el dialogo compra
    @Override
    public void alpulsarSI() {
        String usuario = MainActivity.getUsuario();
        miBD gestorDB = new miBD(getApplicationContext(), "miBD", null, 1);
        Double dinerodisponible = gestorDB.consultarDinero(usuario);
        Double precioVideojuego = gestorDB.consultarPrecioVideojuego(vj);
        boolean loTiene = gestorDB.loTiene(usuario, vj);

        //Si el usuario no tiene el videojuego comprado anteriormente y tiene dinero suficiente para comprarlo
        if(dinerodisponible>=precioVideojuego && !loTiene){
            //Se realiza la compra descontandole el dinero y añadiendolo a su lista de videojuegos
            gestorDB.comprarJuego(vj, usuario, precioVideojuego, dinerodisponible);

            //Al usuario le llega una notificación con datos de la compra
            NotificationManager elManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder elBuilder = new NotificationCompat.Builder(MostrarVideojuegosDisponibles.this, "IdCanal");

            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
                NotificationChannel elCanal = new NotificationChannel("IdCanal", "NombreCanal", NotificationManager.IMPORTANCE_DEFAULT);
                elCanal.setDescription("Compra videojuego");
                elCanal.enableLights(true);
                elCanal.setLightColor(Color.RED);
                elCanal.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                elCanal.enableVibration(true);
                elManager.createNotificationChannel(elCanal);
            }

            elBuilder.setSmallIcon(android.R.drawable.stat_sys_warning)
                    .setContentTitle("Compra videojuego")
                    .setContentText("Se ha realizado la compra.")
                    .setSubText("Se le ha descontado " +precioVideojuego+ " euros del monedero")
                    .setVibrate(new long[]{0, 1000, 500, 1000})
                    .setAutoCancel(true);
            elManager.notify(1, elBuilder.build());

            //Se vuelve al menú principal
            Toast.makeText(getApplicationContext(), "Videojuego comprado", Toast.LENGTH_LONG).show();
            Intent i=new Intent(MostrarVideojuegosDisponibles.this, MenuPrincipal.class);
            startActivity(i);
            finish();

        }else{
            //Si lo tiene, mensaje de error
            if(loTiene){
                Toast.makeText(getApplicationContext(), "Ya tienes este videojuego", Toast.LENGTH_LONG).show();
            }else{ // Si no lo tiene pero tampoco tiene dinero suficiente, mensaje de error
                Toast.makeText(getApplicationContext(), "No tienes dinero suficiente", Toast.LENGTH_LONG).show();
            }
        }


    }

    //Al pulsar en cancelar se muestra el siguiente mensaje
    @Override
    public void alpulsarNO() {
        Toast.makeText(getApplicationContext(), "Se ha cancelado la compra", Toast.LENGTH_LONG).show();
    }

}

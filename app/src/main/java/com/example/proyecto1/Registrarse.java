package com.example.proyecto1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Registrarse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        //Al pulsar registrarse
        Button registrarse = (Button) findViewById(R.id.registrarse);
        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                miBD gestorDB = new miBD(getApplicationContext(), "miBD", null, 1);

                //Se obtienen los campos de los editText
                EditText nomUsu2 = (EditText) findViewById(R.id.nomUsu2);
                String usuario = nomUsu2.getText().toString();

                EditText password = (EditText) findViewById(R.id.password2);
                String contraseña = password.getText().toString();

                EditText edad2 = (EditText) findViewById(R.id.edad2);
                String sEdad = edad2.getText().toString();

                //Se comprueba si existe el nombre de usuario introducido
                boolean existeUsuario = gestorDB.existeUsuario(usuario);

                //Si todos los campos han sido rellenados y no existe un usuario con ese nombre de usuario
                if(usuario.length()!=0 && contraseña.length()!=0 && sEdad.length()!=0 && !existeUsuario){
                    try{ //La edad debe ser un numero
                        int edad = Integer.parseInt(sEdad);

                         //Se agrega el usuario a la BD y se muestra un mensaje de que ha ido bien
                        gestorDB.agregarUsuario(usuario, contraseña, edad);
                        Toast.makeText(getApplicationContext(), "Se ha registrado correctamente", Toast.LENGTH_LONG).show();

                        //Se escribe en el fichero logs (/data/data/com.example.proyecto1/files/logs.txt) que se ha registrado un nuevo usuario y la fecha
                        try {
                            OutputStreamWriter fichero = new OutputStreamWriter(openFileOutput("logs.txt", Context.MODE_APPEND));

                            //Se obtiene la fecha y la hora actual
                            Date date = new Date();
                            DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

                            fichero.write("Se ha registrado el usuario "+ usuario+ ". Hora y fecha: " + hourdateFormat.format(date) + '\n');
                            fichero.close();
                        } catch (IOException e){
                            e.printStackTrace();
                        }

                         //Se vuelve a la pantalla de login
                        Intent i = new Intent(Registrarse.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }catch (Exception e){ //Si la edad no es un número, mensaje de error
                        Toast.makeText(getApplicationContext(), "La edad debe ser un número", Toast.LENGTH_LONG).show();
                    }


                }else if(!existeUsuario){ //Si no se han rellenado todos los campos, mensaje de error
                    Toast.makeText(getApplicationContext(), "Rellene todos los campos", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(getApplicationContext(), "Existe un usuario con ese nombre", Toast.LENGTH_LONG).show();
                }



            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){ //Metodo para guardar los valores en caso de girar, pausar... la app
        super.onSaveInstanceState(outState);


        EditText nomUsu2 = (EditText) findViewById(R.id.nomUsu2);
        String usuarioR = nomUsu2.getText().toString();
        outState.putString("UsuarioR", usuarioR);

        EditText contraseña2 = (EditText) findViewById(R.id.password2);
        String contraseñaR = contraseña2.getText().toString();
        outState.putString("ContraseñaR", contraseñaR);

        EditText edad2 = (EditText) findViewById(R.id.edad2);
        String sEdad = edad2.getText().toString();
        outState.putString("Edad", sEdad);




    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){ //Metodo para restablecer los valores guardados
        super.onRestoreInstanceState(savedInstanceState);


        String usuarioR = savedInstanceState.getString("UsuarioR");
        EditText nomUsu2 = (EditText) findViewById(R.id.nomUsu2);
        nomUsu2.setText(String.valueOf(usuarioR));

        String contraseñaR = savedInstanceState.getString("ContraseñaR");
        EditText contraseña2 = (EditText) findViewById(R.id.password2);
        contraseña2.setText(String.valueOf(contraseñaR));

        String sEdad = savedInstanceState.getString("Edad");
        EditText edad2 = (EditText) findViewById(R.id.edad2);
        edad2.setText(String.valueOf(sEdad));

    }
}

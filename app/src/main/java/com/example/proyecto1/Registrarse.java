package com.example.proyecto1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

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

                //Se comprueba si existe el nombre de usuario introducido en ambas BD local y remota
                boolean existeUsuario = gestorDB.existeUsuario(usuario);
                final String[] resul = {""};

                Data datos = new Data.Builder()
                        .putString("usuario", usuario)
                        .putString("password", contraseña)
                        .build();
                OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(comprobarUsuarioDBWebService.class).setInputData(datos).build();
                WorkManager.getInstance(Registrarse.this).getWorkInfoByIdLiveData(otwr.getId()).observe(Registrarse.this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if (workInfo != null && workInfo.getState().isFinished()) {
                            resul[0] = workInfo.getOutputData().getString("resultado");
                            Log.i("resul", "" + resul[0]);
                            this.registrarUsuario();
                        }
                    }

                    private void registrarUsuario() {

                        //Si todos los campos han sido rellenados
                        if (usuario.length() != 0 && contraseña.length() != 0 && sEdad.length() != 0) {
                            //Si no existe un usuario con ese nombre en la BD remota y en la local
                            if (resul[0].equals("noexiste") && !existeUsuario) {
                                try { //La edad debe ser un numero
                                    int edad = Integer.parseInt(sEdad);

                                    //Se agrega el usuario a la BD y se muestra un mensaje de que ha ido bien
                                    gestorDB.agregarUsuario(usuario, contraseña, edad);
                                    Data datos = new Data.Builder()
                                            .putString("usuario", usuario)
                                            .putString("password", contraseña)
                                            .build();
                                    OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(registroDBWebService.class).setInputData(datos).build();
                                    WorkManager.getInstance(Registrarse.this).getWorkInfoByIdLiveData(otwr.getId()).observe(Registrarse.this, new Observer<WorkInfo>() {
                                        @Override
                                        public void onChanged(WorkInfo workInfo) {
                                            if (workInfo != null && workInfo.getState().isFinished()) {

                                                Log.i("resultado: ", "registrado" );

                                            }
                                        }
                                    });
                                    WorkManager.getInstance(Registrarse.this).enqueue(otwr);

                                    Toast.makeText(getApplicationContext(), "Se ha registrado correctamente", Toast.LENGTH_LONG).show();

                                    FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                            if (!task.isSuccessful()) {

                                                return;
                                            }

                                            String tokenU = task.getResult().getToken();
                                            Log.i("token: ", " " + tokenU);
                                            Data datos = new Data.Builder()
                                                    .putString("token", tokenU)
                                                    .build();
                                            OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(nuevoTokenDBWebService.class).setInputData(datos).build();
                                            WorkManager.getInstance(Registrarse.this).enqueue(otwr);
                                        }

                                    });

                                    //Se escribe en el fichero logs (/data/data/com.example.proyecto1/files/logs.txt) que se ha registrado un nuevo usuario y la fecha
                                    try {
                                        OutputStreamWriter fichero = new OutputStreamWriter(openFileOutput("logs.txt", Context.MODE_APPEND));

                                        //Se obtiene la fecha y la hora actual
                                        Date date = new Date();
                                        DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

                                        fichero.write("Se ha registrado el usuario " + usuario + ". Hora y fecha: " + hourdateFormat.format(date) + '\n');
                                        fichero.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    //Se vuelve a la pantalla de login
                                    Intent i = new Intent(Registrarse.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                } catch (Exception e) { //Si la edad no es un número, mensaje de error
                                    Toast.makeText(getApplicationContext(), "La edad debe ser un número", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Existe un usuario con ese nombre", Toast.LENGTH_LONG).show();
                            }

                        } else { //Si no se han rellenado todos los campos, mensaje de error
                            Toast.makeText(getApplicationContext(), "Rellene todos los campos", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                WorkManager.getInstance(Registrarse.this).enqueue(otwr);
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




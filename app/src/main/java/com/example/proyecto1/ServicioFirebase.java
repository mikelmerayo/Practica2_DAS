package com.example.proyecto1;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class ServicioFirebase extends FirebaseMessagingService {

    public ServicioFirebase(){

    }

    //Si la aplicación está en background, se muestra una notificación, pero no se ejecuta este método
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Si el mensaje viene con datos
        if(remoteMessage.getData().size() > 0) {

        }
        //Si el mensaje es una notificación
        if(remoteMessage.getNotification() != null) {

        }

    }

    //Qué hacer cada vez que se genere un token para el dispositivo
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

    }

}

package com.example.proyecto1;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Observer;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
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

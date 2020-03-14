package com.example.proyecto1;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


//Dialogo con el usuario que se muestra cuando este pincha sobre el boton de cerrar sesión
public class DialogoSesion extends DialogFragment{


    public interface ListenerdelDialogo {
        void alpulsarSI();
        void alpulsarNO();
    }
    ListenerdelDialogo miListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        miListener=(ListenerdelDialogo) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Cerrar sesion");
        builder.setMessage("¿Esta seguro que desea cerrar sesion?");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                miListener.alpulsarSI();

            }

        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                miListener.alpulsarNO();
            }
        });

        return builder.create();
    }

}



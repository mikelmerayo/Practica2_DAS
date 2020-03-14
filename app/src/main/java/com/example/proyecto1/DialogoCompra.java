package com.example.proyecto1;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


//Dialogo con el usuario que se muestra cuando este pincha sobre un videojuego para comprarlo
public class DialogoCompra extends DialogFragment {
    private double prec;
    public DialogoCompra(double precio) {
        prec=precio;
    }

    public interface ListenerdelDialogo {
        void alpulsarSI();
        void alpulsarNO();
    }
    DialogoCompra.ListenerdelDialogo miListenerCompra;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        miListenerCompra =(DialogoCompra.ListenerdelDialogo) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Compra");
        builder.setMessage("¿Esta seguro que desea comprar el videojuego? Se le descontarán " + prec + " euros del monedero.");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                miListenerCompra.alpulsarSI();

            }

        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                miListenerCompra.alpulsarNO();
            }
        });

        return builder.create();
    }

}

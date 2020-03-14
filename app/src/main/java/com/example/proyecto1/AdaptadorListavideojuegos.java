package com.example.proyecto1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class AdaptadorListavideojuegos extends BaseAdapter { //Es la clase del adaptador de la lista de videojuegos comun a todos los usuarios que se muestra en mostrarVideojuegosDisponibles

    private Context contexto;
    private LayoutInflater inflater;
    private String[] nombres;
    private int[] imagenes;
    private double[] medias;

    public AdaptadorListavideojuegos (Context pcontext, String[] pnombres, int[] pimagenes, double[] pmedias){
        contexto = pcontext;
        nombres = pnombres;
        imagenes = pimagenes;
        medias = pmedias;
        inflater= (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return nombres.length;
    } //Numero de elementos de la lista

    @Override
    public Object getItem(int position) { //Elemento en la posicion position
        return nombres[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    } //Posicion de un elemento

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) { //Como se visualiza un elemento

        view=inflater.inflate(R.layout.videojuego,null); //Se indica el xmlcon el layout para cada elemento

        //Se recogen los elementos del layout en variables
        TextView nombre= (TextView) view.findViewById(R.id.nombreVid);
        ImageView img=(ImageView) view.findViewById(R.id.imagen);
        RatingBar barra= (RatingBar) view.findViewById(R.id.valoracionMedia);

        //Se asigna a cada variable el contenido que se quiere mostrar en ese elemento
        nombre.setText(nombres[position]);
        img.setImageResource(imagenes[position]);
        barra.setRating((float)medias[position]);

        return view;
    }
}

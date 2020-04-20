package com.example.proyecto1;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class miBD extends SQLiteOpenHelper {

    public miBD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) { //Metodo donde se crea la Base de Datos y sus tablas, ademas se insertan los videojuegos disponibles

        db.execSQL("CREATE TABLE Usuarios('CodUsu' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'NombreUsu' TEXT, 'Password' TEXT, 'Edad' INTEGER, 'Idioma' TEXT, 'Dinero'  DOUBLE)");
        db.execSQL("CREATE TABLE Videojuegos('CodVid' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'NombreVid' TEXT, 'Imagen' INTEGER, 'Precio' DOUBLE, 'Valoracion' DOUBLE)");
        db.execSQL("CREATE TABLE Compras('CodCompra' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'NombreUsu' TEXT, 'NombreVid' TEXT)");
        //db.execSQL("CREATE TABLE imagenes( 'identificador' TEXT PRIMARY KEY not null, 'foto' blob not null)");
        db.execSQL("INSERT INTO Videojuegos ('NombreVid', 'Imagen', 'Precio', 'Valoracion') VALUES ('Fortnite', 'R.drawable.fortnite' , '35.0', '3.2')");
        db.execSQL("INSERT INTO Videojuegos ('NombreVid', 'Imagen', 'Precio', 'Valoracion') VALUES ('Minecraft', 'R.drawable.minecraft' , '23.0', '2.4')");
        db.execSQL("INSERT INTO Videojuegos ('NombreVid', 'Imagen', 'Precio', 'Valoracion') VALUES ('Fifa 20', 'R.drawable.fifa20' , '65.0', '4.6')");
        db.execSQL("INSERT INTO Videojuegos ('NombreVid', 'Imagen', 'Precio', 'Valoracion') VALUES ('Grand Theft Auto V', 'R.drawable.gta5' , '60.0', '4.9')");
        db.execSQL("INSERT INTO Videojuegos ('NombreVid', 'Imagen', 'Precio', 'Valoracion') VALUES ('Read Dead Redemption 2', 'R.drawable.rdd2' , '38.0', '3.0')");
        db.execSQL("INSERT INTO Videojuegos ('NombreVid', 'Imagen', 'Precio', 'Valoracion') VALUES ('Pokemon GO', 'R.drawable.pokemon' , '25.0', '4.5')");
        db.execSQL("INSERT INTO Videojuegos ('NombreVid', 'Imagen', 'Precio', 'Valoracion') VALUES ('NBA 2K20', 'R.drawable.nba' , '63.0', '5.0')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //Metodo para resetear la Base de Datos

        Log.w("TaskDBAdapter", "Upgrading from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS " + "Usuarios");
        db.execSQL("DROP TABLE IF EXISTS " + "Videojuegos");
        db.execSQL("DROP TABLE IF EXISTS " + "Compras");



        onCreate(db);
    }

    public void agregarUsuario(String nombre, String contraseña, int edad){ //Se introduce un nuevo usuario en la BD

        SQLiteDatabase bd = getWritableDatabase();
        if(bd!=null){
            bd.execSQL("INSERT INTO Usuarios ('NombreUsu', 'Password', 'Edad', 'Idioma', 'Dinero') VALUES ('"+nombre+"','"+contraseña+"' ,'"+edad+"', 'Ingles', '0.00')");
            bd.close();
        }

    }


    public boolean comprobarUsuario(String usuario, String contraseña) { //Se comprueba que exista un usuario con esa contraseña
        SQLiteDatabase bd = getReadableDatabase();
        Cursor c = bd.rawQuery("SELECT NombreUsu,Password FROM Usuarios", null);
        while(c.moveToNext()){
            String usu = c.getString(0);
            String pass = c.getString(1);
            if(usu.equals(usuario) && pass.equals(contraseña)){
                return true;
            }
        }
        c.close();
        return false;
    }

    public void agregarDinero(String usuario, double dinero) { //Se le agrega al usuario la cantidad de dinero que este haya introducido


        SQLiteDatabase bd = getWritableDatabase();
        if(bd!=null){
            bd.execSQL("UPDATE Usuarios SET Dinero = Dinero + '"+dinero+"' WHERE NombreUsu ='"+usuario+"' ");
            bd.close();
        }
    }


    //Se le resta al usuario el precio del videojuego de su dinero y se crea una nueva compra con el usuario y ese videojuego
    public void comprarJuego(String vj, String usuario, double precioVideojuego, Double dinerodisponible) {
        SQLiteDatabase bd = getWritableDatabase();
        double nuevoDinero = dinerodisponible-precioVideojuego;
        Log.i("Precio", " "+nuevoDinero);
        Log.i("Usuario", " "+usuario);
        if(bd!=null){
            bd.execSQL("UPDATE Usuarios SET Dinero = '"+nuevoDinero+"' WHERE NombreUsu ='"+usuario+"' ");
            bd.execSQL("INSERT INTO Compras ('NombreUsu', 'NombreVid') VALUES ('"+usuario+"','"+vj+"')");
            bd.close();
        }


    }

    public double consultarDinero(String usu) { //Consultar el dinero disponible del usuario
        SQLiteDatabase bd = getReadableDatabase();
        Cursor c = bd.rawQuery("SELECT Dinero FROM Usuarios WHERE NombreUsu = '"+usu+"'", null);
        c.moveToFirst();
        double resultado = c.getDouble(0);
        c.close();
        return resultado;
    }

    public Double consultarPrecioVideojuego(String vj) { //Consultar el precio de un videojuego

        SQLiteDatabase bd = getReadableDatabase();
        Cursor c = bd.rawQuery("SELECT Precio FROM Videojuegos WHERE NombreVid='"+vj+"'", null);
        c.moveToFirst();
        Double precio=c.getDouble(0);
        c.close();
        return precio;

    }


    public String consultarIdioma(String usuario) { //Consultar idioma de un usuario

        SQLiteDatabase bd = getReadableDatabase();
        Cursor c = bd.rawQuery("SELECT Idioma FROM Usuarios WHERE NombreUsu='"+usuario+"'", null);
        c.moveToFirst();
        String idioma=c.getString(0);
        c.close();
        return idioma;
    }

    public void cambiarIdioma(String usuario, String idioma) { //Cambiar idioma de un usuario
        SQLiteDatabase bd = getWritableDatabase();
        if(bd!=null) {
            bd.execSQL("UPDATE Usuarios SET Idioma ='"+idioma+"' WHERE NombreUsu ='" +usuario+ "' ");
        }
    }

    public String[] obtenerNombresVideojuegos() { //Se obtiene la lista de nombres de los videojuegos
        SQLiteDatabase bd = getReadableDatabase();
        Cursor c = bd.rawQuery("SELECT NombreVid FROM Videojuegos", null);
        int total = c.getCount();
        String [] nombres = new String[total];
        int i = 0;
        while(c.moveToNext()) {
            String nombre = c.getString(0);
            nombres[i] = nombre;
            i++;
        }
        c.close();
        return nombres;
    }

    public int[] obtenerImagenesVideojuegos() { //Se obtiene la lista de imágenes de los videojuegos
        SQLiteDatabase bd = getReadableDatabase();
        Cursor c = bd.rawQuery("SELECT Imagen FROM Videojuegos", null);
        int total = c.getCount();
        int [] imagenes = new int[total];
        int i = 0;
        while(c.moveToNext()) {
            int imagen = c.getInt(0);
            imagenes[i] = imagen;
            Log.i("Imagen", " " + imagen);
            i++;
        }
        c.close();
        int[] imagenes2={R.drawable.fortnite, R.drawable.minecraft, R.drawable.fifa20, R.drawable.gta5, R.drawable.rdd2, R.drawable.pokemon, R.drawable.nba};
        return imagenes2;
    }

    public double[] obtenerValoracionesVideojuegos() { //Se obtiene la lista de valoraciones de los videojuegos
        SQLiteDatabase bd = getReadableDatabase();
        Cursor c = bd.rawQuery("SELECT Valoracion FROM Videojuegos", null);
        int total = c.getCount();
        double [] valoraciones = new double[total];
        int i = 0;
        while(c.moveToNext()) {
            double valoracion = c.getDouble(0);
            valoraciones[i] = valoracion;
            i++;
        }
        c.close();
        return valoraciones;
    }

    public boolean loTiene(String usuario, String vj) { //Se comprueba si un usuario tiene un videojuego
        SQLiteDatabase bd = getReadableDatabase();
        Cursor c = bd.rawQuery("SELECT NombreVid FROM Compras WHERE NombreUsu ='" +usuario+ "'", null);
        while(c.moveToNext()){
            String nombreV = c.getString(0);
            if(nombreV.equals(vj)){
                return true;
            }
        }
        return false;
    }

    public String[] obtenerMisNombresVideojuegos(String usuario) { ////Se obtiene la lista de nombres de los videojuegos de un usuario

        SQLiteDatabase bd = getReadableDatabase();
        Cursor c = bd.rawQuery("SELECT NombreVid FROM Compras WHERE NombreUsu = '"+usuario+"'", null);
        int total = c.getCount();
        String [] nombres = new String[total];
        int i = 0;
        while(c.moveToNext()) {
            String nombre = c.getString(0);
            nombres[i] = nombre;
            i++;
        }
        c.close();
        return nombres;
    }


    public boolean existeUsuario(String usuario) { //Se comprueba si existe un nombre de usuario

        SQLiteDatabase bd = getReadableDatabase();
        Cursor c = bd.rawQuery("SELECT NombreUsu FROM Usuarios", null);
        while(c.moveToNext()){
            String usu = c.getString(0);
            if(usu.equals(usuario)){
                return true;
            }
        }
        c.close();
        return false;
    }
}

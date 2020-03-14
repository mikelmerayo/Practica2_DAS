package com.example.proyecto1;


import androidx.preference.PreferenceFragmentCompat;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;


public class Preferencias extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.values_pref);
    }

    //Para detectar cambios en el fichero de preferencias
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key){
        switch(key) {
            case "colorpref":
                break;

            default:
                break;
        }
    }

    //Para registrar preferencias cuando se reanuda
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    //Para desregistrar preferencias cuando se para la app
    @Override
    public void onPause() {
        super.onPause();getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}

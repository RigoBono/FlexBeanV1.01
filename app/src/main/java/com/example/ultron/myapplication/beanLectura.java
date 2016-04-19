package com.example.ultron.myapplication;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.Vector;

/**
 * Created by ultron on 1/04/16.
 */
public class beanLectura {
    Vector<Integer> datos=new Vector<Integer>();
    int BPM=0;
    int umbral;
    public beanLectura(Context context,int umbralP){
        DataBaseManager dbm=new DataBaseManager(context);
        datos=dbm.datos();
        BPM=bpm();
        umbral=umbralP;
    }

    public double promedio(){
        double promedio=0;
        for(int i=0;i<datos.size();i++)
            promedio+=datos.elementAt(i);
        return promedio/datos.size();
    }

    public int bpm(){
        int bpm=0;
        Log.i("PasoBPM", Integer.toString(datos.size()));
        for(int i=0;i<datos.size();i++) {
            if (datos.elementAt(i) <5 )
                bpm++;
        }

        return bpm;
    }
}

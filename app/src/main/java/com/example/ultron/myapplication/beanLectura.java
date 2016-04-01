package com.example.ultron.myapplication;

import android.content.Context;
import android.provider.ContactsContract;

import java.util.Vector;

/**
 * Created by ultron on 1/04/16.
 */
public class beanLectura {
    Vector<Integer> datos=new Vector<Integer>();
    int BPM=0;
    public beanLectura(Context context){
        DataBaseManager dbm=new DataBaseManager(context);
        datos=dbm.datos();
        BPM=bpm();
    }

    public double promedio(){
        double promedio=0;
        for(int i=0;i<datos.size();i++)
            promedio+=datos.elementAt(i);
        return promedio/datos.size();
    }

    public int bpm(){
        int bpm=0;
        for(int i=0;i<datos.size();i++)
            if(datos.elementAt(i)>((promedio()/4)*5))
                bpm++;
        return bpm;
    }
}

package com.example.ultron.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Created by ultron on 16/12/15.
 */
public class Mensajero implements Runnable {

    Context contexto;
    public Mensajero(Context context){
        contexto=context;
    }
    @Override
    public void run() {

        while(true){
            //Obtener datos
            String prev="*10*";
            for(int i=0;i<200;i++){
                prev=prev+Integer.toString((int)(Math.random()*700 + 500))+"|";
            }
            prev=prev+"*";
            Log.i("CADENA",prev);

            Random rd=new Random();
            //String cadena = Integer.toString((int)(Math.random()*700 + 500)) + "|" + Integer.toString((int)(Math.random()*1024 + 1)) + "|" + Integer.toString((int)(Math.random()*1024 + 1));

            BeanPro bp=new BeanPro(contexto,prev);
            Log.i("CADENA",bp.salida);
            StringTokenizer tknP = new StringTokenizer(bp.salida, "|");
            String Lectura = tknP.nextToken();
            Log.i("PULSO", Lectura);
            StringTokenizer tknP2 = new StringTokenizer(tknP.nextToken(), "|");
            String LecturaA0 = tknP2.nextToken();
            Log.i("A0", LecturaA0);
            StringTokenizer tknP3 = new StringTokenizer(tknP.nextToken(), "|");
            String LecturaA1 = tknP3.nextToken();
            Log.i("A1", LecturaA1);
            Log.i("TODO", Lectura + " " + LecturaA0 + " " + LecturaA1);
            DataBaseManager dbm=new DataBaseManager(contexto);
            dbm.db.execSQL("INSERT INTO InformacionPorTiempos(Tiempo,LecturaA0,LecturaA1) VALUES('"+Lectura+"','"+LecturaA0+"','"+LecturaA1+"');");
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i("Mensaje", Lectura);

        }
    }


    public void separaEntrada(byte[] datos){
        try {
            String decoded = new String(datos, "UTF-8");
            StringTokenizer tknP=new StringTokenizer(decoded,"|");
            StringTokenizer bmp=new StringTokenizer(tknP.nextToken(),":");
            bmp.nextToken();
            String Lectura=bmp.nextToken();
            StringTokenizer La0=new StringTokenizer(tknP.nextToken(),":");
            La0.nextToken();
            String LecturaA0=La0.nextToken();
            StringTokenizer La1=new StringTokenizer(tknP.nextToken(),":");
            La1.nextToken();
            String LecturaA1=La1.nextToken();
            DataBaseManager dbm=new DataBaseManager(contexto);
            dbm.db.execSQL("INSERT INTO Informacion(Lectura,LecturaA0,LecturaA1) VALUES('"+Lectura+"','"+LecturaA0+"','"+LecturaA1+"');");
            dbm.db.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

}

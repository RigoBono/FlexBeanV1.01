package com.example.ultron.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.util.Log;

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.ScratchBank;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Created by ultron on 17/12/15.
 */
public class MensajeConFrijol implements Runnable {
    Context contexto;
    Bean frijolMaster;
    boolean banderaConectado=false;
    boolean banderaDesconectado=false;
    public MensajeConFrijol(Context context){
        contexto=context;

    }
    @Override
    public void run() {
        BeanListener beanListener=new BeanListener() {
            @Override
            public void onConnected() {
                banderaConectado=true;

            }

            @Override
            public void onConnectionFailed() {
                System.exit(0);
            }

            @Override
            public void onDisconnected() {
                banderaDesconectado=true;
                //System.exit(0);
            }

            @Override
            public void onSerialMessageReceived(byte[] data) {
                separaEntrada(data);
            }

            @Override
            public void onScratchValueChanged(ScratchBank bank, byte[] value) {

            }

            @Override
            public void onError(BeanError error) {

            }
        };
        frijolMaster.connect(contexto,beanListener);
        while(true){
            try {
                Thread.sleep(500);
                DataBaseManager dbm=new DataBaseManager(contexto);
                frijolMaster.sendSerialMessage(dbm.estadoPinesDigitales());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}

package com.example.ultron.myapplication;

import android.content.Context;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Created by ultron on 9/03/16.
 */
public class BeanPro {
    String salida="";
    String A1="";
    String A0="";
    String BPM="";
    Vector<dato> lec=new Vector<dato>();
    Context contexto;

    public BeanPro(Context context,String datos){
        contexto=context;
            salida=separaMaestro(datos);

    }

    public BeanPro(Context context,byte[] datos){
        contexto=context;
        try {
            String decoded = new String(datos, "UTF-8");
            salida=separaMaestro(decoded);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public String separaMaestro(String data){
        StringTokenizer tkn=new StringTokenizer(data,"*");

        String parte=tkn.nextToken();
        String parte2=tkn.nextToken();
        Log.i("PASO",parte);
        A1=parte;
        BPM=lecturas(parte2);

       /* Log.i("PASO",parte2);
        for(int i=0;i<=tkn.countTokens();i++){

            Log.i("PASO",parte);
            if(parte.contains("|")){
                BPM=lecturas(parte);
                Log.i("PASO","PASABIEN");

            } else{
                if(parte!=null)
                    A1=parte;
            }
            parte=tkn.nextToken();
        }*/
        return BPM+"|"+A0+"|"+A1;
    }

    public String lecturas(String envio){
        StringTokenizer tkn=new StringTokenizer(envio,"|");

        for(int i=0;i<tkn.countTokens();i++){
            int aux=Integer.parseInt(tkn.nextToken().toString());
            dato prevA=new dato();
            prevA.index=i;
            prevA.lectura=aux;
            DataBaseManager dbm=new DataBaseManager(contexto);
            dbm.db.execSQL("INSERT INTO Dato(Dato) VALUES("+Integer.toString(prevA.lectura)+");");
            dbm.db.close();
            lec.add(prevA);
        }
        Log.i("IdPro",Integer.toString(lec.size()));
        return Integer.toString(bpm());
    }

    public int promedioUltimosDiez(){
        int prom=0;
        ordenaMuestra(1);

        for(int i=lec.size()-10;i<lec.size();i++){
            prom=prom+lec.elementAt(i).lectura;
        }
        prom=prom/10;
        return prom;
    }

    public int bpm(){
        int bpm=0;
        int prom=promedioUltimosDiez();
        ordenaMuestra(2);
        for(int i=0;i<lec.size();i++){
            if(lec.elementAt(i).lectura>prom){
                bpm++;
                if(bpm==3)
                    break;
            }
        }
        A0=Integer.toString(lec.lastElement().lectura);
        return bpm;
    }

    void ordenaMuestra(int modo){
        for(int i=0;i<lec.size()-1;i++)
            for(int j=0;j<lec.size()-1;j++)
                if(modo==1){
                    if(lec.elementAt(j).lectura<lec.elementAt(j+1).lectura){
                        dato temp=new dato();

                        temp.index=lec.elementAt(i).index;
                        temp.lectura=lec.elementAt(i).lectura;

                        lec.elementAt(j+1).index=lec.elementAt(j).index;
                        lec.elementAt(j+1).lectura=lec.elementAt(j).lectura;

                        lec.elementAt(j).index=temp.index;
                        lec.elementAt(j).lectura=temp.index;
                    }
                }else{
                    if(lec.elementAt(j).index>lec.elementAt(j+1).index){
                        dato temp=new dato();

                        temp.index=lec.elementAt(j).index;
                        temp.lectura=lec.elementAt(j).lectura;

                        lec.elementAt(j+1).index=lec.elementAt(j).index;
                        lec.elementAt(j+1).lectura=lec.elementAt(j).lectura;

                        lec.elementAt(j).index=temp.index;
                        lec.elementAt(j).lectura=temp.index;
                    }
                }
    }


}

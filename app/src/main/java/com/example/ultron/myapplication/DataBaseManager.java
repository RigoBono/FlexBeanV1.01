package com.example.ultron.myapplication;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.lang.System;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Created by ultron on 13/07/15.
 */
public class DataBaseManager
{
    String lectura="Lectura";
    AlmacenBase helper;
    SQLiteDatabase db;

    public DataBaseManager(Context context)
    {
        helper=new AlmacenBase(context); //Se crea el objeto de conexion
        db = helper.getWritableDatabase();//Si no existe la base se crea y si existe se habre en modo escritura
    }

    public ContentValues generaContenedor(double Lectura)
    {
        ContentValues valores = new ContentValues();
        valores.put(lectura, Lectura);
        return valores;
    }


    public ContentValues generaContenedorPin(String pin,String bo)
    {
        ContentValues valores = new ContentValues();
        valores.put("Pin", pin);
        valores.put("Estado", bo);
        return valores;
    }

    public void insertar(double Lectura)
    {
        Log.i("Insertado", Double.toString(Lectura));
        // if(Lectura>1)
        db.insert("Lecturas", null, generaContenedor(Lectura));
    }



    public Cursor fechasTomadas(){
        return db.rawQuery("SELECT strftime('%Y-%m-%d',TiempoInsercion,'localtime') as fechas  FROM Informacion GROUP BY fechas ORDER BY fechas DESC;",null);
    }

    public String ultimoIngreso(){
        String rt="";
        Cursor datos=db.rawQuery("SELECT TiempoInsercion FROM Informacion ORDER BY TiempoInsercion DESC LIMIT 1", null);
        if (datos.moveToFirst()) {
            do {
                rt=datos.getString(0);
                break;
            } while (datos.moveToNext());
        }
        datos.close();
        return rt;

    }

    public int limiteSup(){
        String rt="15";
        Cursor datos=db.rawQuery("SELECT Limite FROM LimitesSen;", null);
        if (datos.moveToFirst()) {
            do {
                rt=datos.getString(0);
                break;
            } while (datos.moveToNext());
        }
        datos.close();
        int ret=Integer.parseInt(rt);
        return ret;
    }

    public boolean limiteNuevo(int limite){
        db.execSQL("UPDATE LimitesSen SET Limite="+Integer.toString(limite)+";");
        return true;
    }

    public String fecha(){
        String rep="";
        Cursor rp=db.rawQuery("select strftime('%Y-%m-%d',TiempoInsercion,'localtime') as lapso from Informacion GROUP BY lapso ORDER BY lapso DESC;",null);
        if (rp.moveToFirst()) {
            do {
                rep=rp.getString(0);
            } while (rp.moveToNext());
        }
        rp.close();
        return rep;
    }


    public Cursor UsuarioD(){
        return db.rawQuery("SELECT * FROM Usuario;",null);
    }


    public double consultaAnalogo0()
    {
        double sal=0;
        Cursor pro= db.rawQuery("select LecturaA0, TiempoInsercion from InformacionPorTiempos   ORDER BY TiempoInsercion DESC;", null);
        if (pro.moveToFirst()) {
            do {
                sal=pro.getDouble(0);
                break;
            } while (pro.moveToNext());
        }
        pro.close();
        return sal;

    }

    public boolean consultaTipo()
    {
        int sal=0;
        Cursor pro= db.rawQuery("select A0  from Tipo;", null);
        if (pro.moveToFirst()) {
            do {
                sal=pro.getInt(0);
                if(sal==1)
                    return true;
                break;
            } while (pro.moveToNext());
        }
        pro.close();
        return false;

    }

    public double consultaAnalogo1()
    {
        double sal=0;
        Cursor pro= db.rawQuery("select LecturaA1, TiempoInsercion from InformacionPorTiempos  ORDER BY TiempoInsercion DESC;", null);
        if (pro.moveToFirst()) {
            do {
                sal=pro.getDouble(0);
                break;
            } while (pro.moveToNext());
        }
        pro.close();
        return sal;

    }

    public void  actualizaEstadoPin(String pin, String Estado){
        Log.i("Mensaje","UPDATE Estado SET "+pin+"='"+Estado+"';");
        db.execSQL("UPDATE Estado SET " + pin + "='" + Estado + "';");
    }

    public double PromedioPorFecha()
    {
        double sal=0;
        Cursor pro= db.rawQuery("select AVG(Lectura),strftime('%Y-%m-%d',TiempoInsercion,'localtime') as fecha, strftime('%Y-%m-%d','now','localtime') as hoy  from Informacion WHERE fecha=hoy;",null);
        if (pro.moveToFirst()) {
            do {
                sal=pro.getDouble(0);
            } while (pro.moveToNext());
        }
        return sal;

    }

    public double MaxVibra()
    {
        double sal=0;
        Cursor pro= db.rawQuery("SELECT RangoMax  FROM Usuario;",null);
        if (pro.moveToFirst()) {
            do {
                sal=pro.getInt(0);
            } while (pro.moveToNext());
        }
        else
            sal=pro.getCount();
        pro.close();
        return sal;

    }

    public double MinVibra()
    {
        double sal=0;
        Cursor pro= db.rawQuery("SELECT RangoMin  FROM Usuario;",null);
        if (pro.moveToFirst()) {
            do {
                sal=pro.getDouble(0);
            } while (pro.moveToNext());
        }
        pro.close();
        return sal;

    }

    public double Edad()
    {
        double sal=0;
        Cursor pro= db.rawQuery("SELECT Edad  FROM Usuario;",null);
        if (pro.moveToFirst()) {
            do {
                sal=pro.getDouble(0);
            } while (pro.moveToNext());
        }
        pro.close();
        return sal;

    }

    public void actualizaA0()
    {
        db.execSQL("UPDATE Informacion SET Lectura=1 WHERE LecturaA0>50;");
        db.execSQL("UPDATE Informacion SET Lectura=0 WHERE LecturaA0<50;");
    }


    public String avg(){
        String rep="";
        Cursor rp=db.rawQuery("select AVG(Lectura),strftime('%H:%M',TiempoInsercion,'localtime') as lapso from Informacion  GROUP BY lapso ORDER BY TiempoInsercion DESC LIMIT 20;",null);
        if (rp.moveToFirst()) {
            do {
                rep=rp.getString(0);

            } while (rp.moveToNext());
        }
        rp.close();
        return rep;
    }


    public Vector<Lectura> reporte(){
        Vector<Lectura> rep=new Vector<Lectura>();
        Cursor rp=db.rawQuery("select Lectura,strftime('%H:%M',TiempoInsercion,'localtime') as lapso from Informacion GROUP BY lapso;",null);
        if (rp.moveToFirst()) {
            do {
                Lectura aux=new Lectura();
                aux.lpm=rp.getString(0);
                aux.tiempo=rp.getString(1);
                rep.add(aux);
            } while (rp.moveToNext());
        }
        rp.close();
        return rep;
    }

    public Vector<Integer> actualizaVector(){
        Vector<Integer> vec=new Vector<Integer>();
        Cursor C=db.rawQuery("SELECT Dato FROM Dato ORDER BY TiempoInsercion DESC LIMIT 20;",null);
        if (C.moveToFirst()) {
            do {
                int res = Integer.parseInt(C.getString(0));
                vec.add(res);
            } while (C.moveToNext());
        }
        C.close();
        db.close();
        return vec;
    }

    public int MaximoLpm(){
        int res=0;
        Cursor data1=db.rawQuery("SELECT Max FROM Rangos;", null);
        if (data1.moveToFirst()) {
            do {
                res = Integer.parseInt(data1.getString(0));
            } while (data1.moveToNext());
        }
        data1.close();
        return res;
    }

    public int MinimoLpm(){
        int res=0;
        Cursor data1=db.rawQuery("SELECT Min FROM Rangos;", null);
        if (data1.moveToFirst()) {
            do {
                res = Integer.parseInt(data1.getString(0));
            } while (data1.moveToNext());
        }
        data1.close();
        return res;
    }

    public String estadoPinesDigitales(){
        Cursor data1=db.rawQuery("SELECT * FROM Estado;",null);
        String salida="";
        if(data1.moveToFirst()){
            do{
                salida="D0:"+data1.getString(0)+"|D1:"+data1.getString(1)+"|D2:"+data1.getString(2)+"|D3:"+data1.getString(3)+"|D4:"+data1.getString(4)+"|D5:"+data1.getString(5);
                return  salida;
            }while(data1.moveToNext());
        }
        data1.close();
        return "";
    }


    public int Lectura(){
        Cursor data1= db.rawQuery("SELECT Lectura  FROM Informacion ORDER BY TiempoInsercion DESC LIMIT 1;",null);
        int salida=0;
        if(data1.moveToFirst()){
            do{
                salida=data1.getInt(0);
            }while(data1.moveToNext());
        }
        data1.close();
        return salida;
    }
    public int LecturaA0(){
        Cursor data1= db.rawQuery("SELECT LecturaA0  FROM Informacion ORDER BY TiempoInsercion DESC LIMIT 1;",null);
        int salida=0;
        try{
            if(data1.moveToFirst()){
                do{
                    salida=data1.getInt(0);
                }while(data1.moveToNext());
            }
            data1.close();
        }catch(Exception e)
        {

        }

        return salida;
    }

    public Cursor obtenerlpm(){
        return db.rawQuery("SELECT SUM(Lectura)  FROM Informacion WHERE DATETIME(TiempoInsercion)>DATETIME('now','-1 minutes');",null);
    }

    public int obtenerlpm(boolean A0){
        //Cursor data1=db.rawQuery("SELECT COUNT(LecturaA0),TiempoInsercion  FROM Informacion WHERE DATETIME(TiempoInsercion)>DATETIME('now','-20 seconds') AND LecturaA0>50 ORDER BY TiempoInsercion DESC",null);
        Cursor data1=db.rawQuery("SELECT COUNT(LecturaA0),TiempoInsercion  FROM Informacion WHERE DATETIME(TiempoInsercion)>DATETIME('now','-10 seconds') AND LecturaA0>300 ORDER BY TiempoInsercion DESC",null);
        int res=0;
        if (data1.moveToFirst()) {
            do {
                res = Integer.parseInt(data1.getString(0));
                break;
            } while (data1.moveToNext());
        }
        Log.i("INFOPRO",Integer.toString(res));
        data1.close();
        return res;
    }

    public int cuentaDatos(){
        Cursor data1=db.rawQuery("SELECT COUNT(Dato),TiempoInsercion  FROM Dato  ORDER BY TiempoInsercion DESC",null);
        int res=0;
        if (data1.moveToFirst()) {
            do {
                res = Integer.parseInt(data1.getString(0));
                break;
            } while (data1.moveToNext());
        }
        Log.i("Datos->",Integer.toString(res));
        data1.close();
        return res;
    }

    public Vector<Integer> datos(){
        Cursor data1=db.rawQuery("SELECT Dato  FROM Dato  ORDER BY TiempoInsercion DESC LIMIT 6000",null);
        Vector<Integer> res=new Vector<Integer>();
        if (data1.moveToFirst()) {
            do {
                res.add(Integer.parseInt(data1.getString(0)));
            } while (data1.moveToNext());
        }
        Log.i("Datos->",Integer.toString(res.size()));
        data1.close();
        return res;
    }

    public int consultaTiempo(){
        Cursor data1=db.rawQuery("SELECT Tiempo  FROM InformacionPorTiempos ORDER BY TiempoInsercion DESC LIMIT 1",null);
        int res=0;
        if (data1.moveToFirst()) {
            do {
                res = Integer.parseInt(data1.getString(0));
                break;
            } while (data1.moveToNext());
        }
        Log.i("InfoDescente",Integer.toString(res));
        data1.close();
        return res;
    }

    public String TiempoInsercion(){
        Cursor data1=db.rawQuery("SELECT TiempoInsercion FROM InformacionPorTiempos ORDER BY TiempoInsercion DESC LIMIT 1;",null);
        String res="";
        if (data1.moveToFirst()) {
            do {
                res = data1.getString(0);
                break;
            } while (data1.moveToNext());
        }
        Log.i("InfoDescente",res);
        data1.close();
        return res;
    }


    public int pulso60(){
        //Cursor data1=db.rawQuery("SELECT COUNT(LecturaA0),TiempoInsercion  FROM Informacion WHERE DATETIME(TiempoInsercion)>DATETIME('now','-20 seconds') AND LecturaA0>50 ORDER BY TiempoInsercion DESC",null);
        Cursor data1=db.rawQuery("SELECT Lectura  FROM Informacion   LIMIT 60;",null);
        int res=0;
        if (data1.moveToFirst()) {
            do {
                res = Integer.parseInt(data1.getString(0));
                break;
            } while (data1.moveToNext());
        }
        Log.i("INFOPRO->",Integer.toString(res));
        data1.close();
        return res;
    }
    public int obtenerAVG(){
        Cursor data1=db.rawQuery("SELECT COUNT(LecturaA0) as lpm, strftime('%Y-%m-%d',TiempoInsercion,'localtime') as fecha, AVG(lpm) as promedio  FROM Informacion WHERE fecha=strftime('%Y-%m-%d','now','localtime'),DATETIME(TiempoInsercion)>DATETIME('now','-1 minutes') AND LecturaA0>100 GROUP BY fecha;",null);
        int res=0;
        if (data1.moveToFirst()) {
            do {
                res = Integer.parseInt(data1.getString(2));
                break;
            } while (data1.moveToNext());
        }
        return res;
    }

    public int obtenera0(){
        Cursor data1=db.rawQuery("SELECT LecturaA0  FROM Informacion WHERE DATETIME(TiempoInsercion)>DATETIME('now','-10 seconds') ORDER BY TiempoInsercion DESC;",null);
        int res=0;
        if (data1.moveToFirst()) {
            do {
                res = Integer.parseInt(data1.getString(0));
                break;
            } while (data1.moveToNext());
        }
        //Log.i("INFOPRO",Integer.toString(res));
        data1.close();
        db.close();
        return res;
    }

    public Cursor obtenera0alpha(){

        return db.rawQuery("SELECT LecturaA0  FROM Informacion WHERE DATETIME(TiempoInsercion)>DATETIME('now','-10 seconds') ORDER BY TiempoInsercion DESC;",null);

    }



    public boolean insertaPin(String pin,String bo){
        db.insert("Pin", null, generaContenedorPin(pin, bo));
        return false;
    }



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void EliminarBase()
    {
        /*
            Para eliminar la base de datos se tiene que hacer un enlace lógico con el directorio de la base
            para eso se pide el directorio con getPath.
            directorio es el enlace lógico.
            deleteDatabase eliminara la base si directorio es diferente de null.
            Retornara true si se concreto la operación, false en caso contrario.
         */
        final String path=db.getPath();
        File directorio=new File(path);
        db.deleteDatabase(directorio);
    }
}

class Lecturas
{
    public int valor;
    public String fecha;

    public Lecturas(int val, String date)
    {
        this.valor = val;
        this.fecha = date;
    }

    public int regresaValor()
    {
        return this.valor;
    }

    public String regresaFecha()
    {
        return this.fecha;
    }
}
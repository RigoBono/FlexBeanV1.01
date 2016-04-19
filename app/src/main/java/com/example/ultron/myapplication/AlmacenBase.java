package com.example.ultron.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Vector;

/**
 * Created by ultron on 13/07/15.
 */
public class AlmacenBase extends SQLiteOpenHelper {
    public AlmacenBase(Context context) {
        super(context,"bdasop",null,1);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Informacion(" +
                "Lectura INT," +
                "LecturaA0 INT," +
                "LecturaA1 INT," +
                "TiempoInsercion TIMESTAMP DEFAULT CURRENT_TIMESTAMP);");
        db.execSQL("CREATE TABLE Estado(" +
                "D0 TEXT ," +
                "D1 TEXT ," +
                "D2 TEXT ," +
                "D3 TEXT ," +
                "D4 TEXT ," +
                "D5 TEXT ," +
                "TiempoInsercion TIMESTAMP DEFAULT CURRENT_TIMESTAMP);");
        db.execSQL("CREATE TABLE Usuario(" +
                "Nombre TEXT ," +
                "Edad INT ," +
                "Peso INT ," +
                "Email TEXT," +
                "RangoMax INT," +
                "RangoMin INT);");

        db.execSQL("CREATE TABLE InformacionPorTiempos(" +
                "Tiempo INT," +
                "LecturaA0 INT," +
                "LecturaA1 INT," +
                "TiempoInsercion TIMESTAMP DEFAULT CURRENT_TIMESTAMP);");


        db.execSQL("CREATE TABLE Tipo(" +
                "A0 INT);");

        db.execSQL("CREATE TABLE Dato(" +
                "Dato INT," +
                "TiempoInsercion TIMESTAMP DEFAULT CURRENT_TIMESTAMP);");

        db.execSQL("CREATE TABLE LimitesSen(" +
                "Limite INT);");


        db.execSQL("INSERT INTO LimitesSen VALUES(15);");
        db.execSQL("INSERT INTO Usuario VALUES('Nombre',20,50,'mail@mail.com',1,30);");
        db.execSQL("INSERT INTO Estado VALUES('OUT','OUT','OUT','OUT','OUT','OUT','OUT');");
        db.execSQL("INSERT INTO Tipo VALUES(1);");
        db.execSQL("INSERT INTO Informacion(Lectura,LecturaA0,LecturaA1) VALUES(1,1,1);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("CREATE TABLE Lecturas (Lectura Double NOT NULL,TiempoInsercion DEFAULT (STRFTIME('%H-%M-%S-%f', 'NOW')));");
    }


}
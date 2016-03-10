package com.example.ultron.myapplication;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

public class DatosPersonales extends AppCompatActivity
{
    View vista;
    //String msgNombre = "Se requiere tu nombre debe estar aquí para saber quien está utilizando la aplicación.";
    String msgNombre ="It requires your name, should be here to see who is using the application";
    //String msgEmail = "Se requiere tu correo por si necesitas guardar tus reportes, o enviarlos a un médico o cualquier persona.";
    String msgEmail ="Email is required if you need to save your reports, or send them to a doctor or anyone .";
    String msgPeso="Your weight to regulate the amount of beats that must have required .";
    //String msgPeso = "Se requiere tu peso para poder regular la cantidad de latidos que debes de tener.";
    //String msgEdad = "Se requiere tu edad para poder asignarte un rango máximo y mínimo de latidos por minuto.";
    String msgEdad ="Your age to assign a maximum and minimum range of beats per minute, and customize vibration alerts.";

    //String msgActivaAlerta = "Con esta opción puedes ajustar un maximo personalizado de latidos por minuto antes de lanzar alertas de vibración.";
    String msgActivaAlerta = "With this option you can set a custom maximum of beats per minute before launching vibration alerts";

    //String msgDesactivaAlerta = "Con esta opción no deseas recibir alertas de vibración.";
    String msgDesactivaAlerta ="With this option you wish or not  to receive vibration customized alerts";
    //String msgLatidos = "Elige el límite de latidos que deseas controlar.";
    String msgLatidos="Choose the limit of beats you want to control ";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_personales);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff779ecb")));
        DataBaseManager dbm=new DataBaseManager(getApplicationContext());
        Cursor usu=dbm.UsuarioD();

        vista = this.getWindow().getDecorView().findViewById(android.R.id.content);

        final EditText nombre=(EditText)findViewById(R.id.Nombre);
        final Spinner edad=(Spinner)findViewById(R.id.Edad);
        final EditText peso=(EditText)findViewById(R.id.Peso);
        final EditText email=(EditText)findViewById(R.id.Email);
        final Spinner MaxVibra=(Spinner)findViewById(R.id.RangoVibra);
        final Switch switch1=(Switch)findViewById(R.id.switch1);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switch1.isChecked()) {
                    MaxVibra.setEnabled(true);
                    Snackbar.make(vista, msgActivaAlerta, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else {
                    MaxVibra.setEnabled(false);
                    Snackbar.make(vista, msgDesactivaAlerta, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                    dbm.db.execSQL("UPDATE Usuario SET RangoMax=1");
                }
            }
        });

        nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(vista, msgNombre, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        nombre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                dbm.db.execSQL("UPDATE Usuario SET Nombre='" + nombre.getText().toString() + "';");
            }
        });
        edad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(vista, msgEdad, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                dbm.db.execSQL("UPDATE Usuario SET Edad=" + edad.getSelectedItem().toString() + ";");
                Log.i("Edad", edad.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                dbm.db.execSQL("UPDATE Usuario SET Edad=" + edad.getSelectedItem().toString() + ";");
                Log.i("Edad", edad.getSelectedItem().toString());
            }
        });

        peso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(vista, msgPeso, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        peso.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Snackbar.make(vista, msgPeso, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                dbm.db.execSQL("UPDATE Usuario SET Peso=" + Integer.parseInt(peso.getText().toString()) + ";");
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(vista, msgEmail, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Snackbar.make(vista, msgEmail, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                dbm.db.execSQL("UPDATE Usuario SET Email='" + email.getText().toString() + "';");
            }
        });

        MaxVibra.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(MaxVibra.isEnabled())
                    Snackbar.make(vista, msgLatidos, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                dbm.db.execSQL("UPDATE Usuario SET RangoMax=" + MaxVibra.getSelectedItem().toString() + ";");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                dbm.db.execSQL("UPDATE Usuario SET RangoMax=" + MaxVibra.getSelectedItem().toString() + ";");
            }
        });
        if (usu.moveToFirst())
        {
            do
            {
                nombre.setText(usu.getString(0));
                Log.i("Nombre",usu.getString(0));
                edad.setSelection(getIndex(edad,usu.getString(1)));
                peso.setText(usu.getString(2));
                email.setText(usu.getString(3));
                if(usu.getInt(4)>1)
                {
                    switch1.setChecked(true);
                    MaxVibra.setEnabled(true);
                    MaxVibra.setSelection(usu.getInt(4));
                }
                else
                {
                    switch1.setChecked(false);
                    MaxVibra.setEnabled(false);
                    MaxVibra.setSelection(0);
                }
            }
            while (usu.moveToNext());
        }
    }

    private int getIndex(Spinner spinner, String myString)
    {
        int index = 0;
        for (int i=0;i<spinner.getCount();i++)
        {
            if (spinner.getItemAtPosition(i).equals(myString))
                index = i;
        }
        return index;
    }
}
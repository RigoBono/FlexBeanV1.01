package com.example.ultron.myapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Vector;

public class Historial extends AppCompatActivity {
    CalendarView calendar;
    LineChart grafica;
    LinearLayout LayoutGrafica;
    ArrayList dias=new ArrayList();
    Vector<String> listad=new Vector<String>();
    Vector<String> imagenes=new Vector<>();
    ListViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_historial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(getApplicationContext(), GraficasPro.class);
                startActivity(intent);
                Snackbar.make(view, "Good luck!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        llenaListaFechas();
        muestraArray();
        Display pantalla=getWindowManager().getDefaultDisplay();
        //LayoutGrafica.setVisibility(LinearLayout.GONE);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff779ecb")));

    }

    public void muestraArray(){
        /*ListView listView=(ListView)findViewById(R.id.listafechas);
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,dias);
        listView.setAdapter(adapter);*/


        for(int i=0;i<dias.size();i++)
        {

            listad.add(dias.get(i).toString());

        }
        ListView listView=(ListView)findViewById(R.id.listafechas);
        try{
            listView.setAdapter(new CustomAdapter(getApplicationContext(), this, listad, imagenes));
        }catch(Error  ee){

        }

    }

    public void llenaListaFechas(){
        DataBaseManager dbm=new DataBaseManager(getApplicationContext());
        Cursor datos=dbm.fechasTomadas();
        if (datos.moveToFirst()) {
            do {
                Log.i("Fecha", datos.getString(0));
                dias.add("Date: "+datos.getString(0));
                imagenes.add(Environment.getExternalStorageDirectory() + "/DCIM/"+datos.getString(0)+".png");
                //break;

            } while (datos.moveToNext());
        }


    }






}

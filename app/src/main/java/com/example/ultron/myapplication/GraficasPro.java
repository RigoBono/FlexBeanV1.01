package com.example.ultron.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.punchthrough.bean.sdk.Bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class GraficasPro extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LineChart grafica;
    List<Double> elementos=new ArrayList<Double>();
    TextView txtPromedio;
    TextView Promedio;
    TextView promedioPro;
    LinearLayout LayoutPromedio;
    Vector<Integer> ultimos= new Vector<Integer>();
    Vector<Integer> ultimosA0= new Vector<Integer>();
    String rutaPro;
    int ultimoPrev=0;
    boolean bandPrev=false;
    boolean bandA0=false;
    View vista;
    double sumaNo=0,sumaNoA0=0,promedioNo,promedioNoA0;
    int val=1,cont=0;
    int[] dif=new int[1024];
    String ult = "0";
    int T=600;
    String inserT;


    public void hiloPro(){
        new CountDownTimer(T,300){

            @Override
            public void onTick(long millisUntilFinished)
            {
                DataBaseManager dbm=new DataBaseManager(getApplicationContext());
                if(dbm.consultaTipo()==true){
                    agregarContenido(200);
                    agregarContenido(230);
                    agregarContenido(200);
                }
            }

            @Override
            public void onFinish()
            {
                DataBaseManager dbm=new DataBaseManager(getApplicationContext());
                Float salida= Float.valueOf((float) (T * .001));
                salida=60/salida;
                int prev= Math.round(salida);
                txtPromedio.setText(Integer.toString(prev));
                promedioPro.setText("");
                String insert=dbm.TiempoInsercion();
                if(insert.equals(inserT)==false)
                {
                    T = dbm.consultaTiempo();
                }
                if(dbm.consultaTipo()==true){
                    agregarContenido(150);
                    agregarContenido(950);
                    agregarContenido(120);

                    agregarContenido(240);
                    agregarContenido(250);
                    agregarContenido(240);
                }
                else
                    agregarContenido();
                dbm.db.execSQL("INSERT INTO Informacion(Lectura) VALUES("+Integer.toString(prev)+");");
                dbm.db.close();
                hiloPro();

            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficas_pro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        vista = this.getWindow().getDecorView().findViewById(android.R.id.content);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    enviaReporte();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                Snackbar.make(view, "Sending report", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Usuario usr=new Usuario();
        String email=usr.getEmail(getApplicationContext());
        TextView usuario=(TextView)findViewById(R.id.emailPro);
        try{
            usuario.setText(email);
        }catch(Exception e)
        {

        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Snackbar.make(vista, "Waiting for data...", Snackbar.LENGTH_LONG).setAction("Action", null).show();


        Display pantalla=getWindowManager().getDefaultDisplay();
        txtPromedio=(TextView) findViewById(R.id.promedio);

        LayoutPromedio=(LinearLayout) findViewById(R.id.LayoutPromedio);
        Promedio=(TextView) findViewById(R.id.textPromedio);
        Promedio.setTextColor(Color.BLACK);
        promedioPro=(TextView) findViewById(R.id.promedioPro);
        grafica=new LineChart(this);

        grafica.setDescription("");
        //grafica.setNoDataTextDescription("No hay datos por el momento");
        //grafica.setNoDataText("");
        grafica.setTouchEnabled(true);
        grafica.setDrawGridBackground(false);
        grafica.setBackgroundColor(Color.WHITE);
        LineData datos=new LineData();
        //datos.setValueTextColor(Color.BLACK);
        grafica.setData(datos);
        grafica.animate();

        Legend l=grafica.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);
        XAxis x=grafica.getXAxis();
        x.setDrawGridLines(false);
        x.setAvoidFirstLastClipping(true);
        YAxis y=grafica.getAxisLeft();
        y.setTextColor(Color.BLACK);
        y.setAxisMaxValue(2000f);
        y.setDrawGridLines(false);
        y.setTextColor(Color.argb(0, 255, 255, 255));
        LimitLine base = new LimitLine(0);
        base.setLineColor(Color.BLACK);
        y.addLimitLine(base);
        YAxis y1=grafica.getAxisRight();
        y1.setEnabled(true);
        y1.setDrawGridLines(false);
        y1.setTextColor(Color.WHITE);
        LinearLayout LayoutGrafica=(LinearLayout) findViewById(R.id.LayoutGrafica);
        LayoutGrafica.addView(grafica, (int) ((pantalla.getWidth() / 10) * 9.2), (int) ((pantalla.getHeight() / 10) * 5));
        //superHilo();
        DataBaseManager dbm=new DataBaseManager(getApplicationContext());
        T=dbm.consultaTiempo();
        hiloPro();

    }

    public void tiempo()
    {
        new CountDownTimer(2000, 1000)
        {
            public void onTick(long millisUntilFinished) { }

            public void onFinish()
            {
                if(estaConectado())
                {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    DataBaseManager dbm=new DataBaseManager(getApplicationContext());
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(Environment.getExternalStorageDirectory().toString() + "/DCIM/"+dbm.fecha()+".png"));
                    //intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "REPORTE");
                    intent.putExtra(Intent.EXTRA_TEXT, "My last report of beats per minute.");
                    startActivity(Intent.createChooser(intent, "Share with..."));
                }
                else
                    tiempo();
            }
        }.start();
    }




    private void agregarContenido(int valor)
    {
        try
        {
            double maxVi,minVi,max;
            int ultimo = 0;
            LineData datos=grafica.getData();
            datos.setDrawValues(false);
            YAxis y=grafica.getAxisLeft();
            if(datos!=null) {

                LineDataSet set = datos.getDataSetByIndex(0);
                if (set == null) {
                    set = createSet();
                    datos.addDataSet(set);
                }
                datos.addXValue("");
                DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                Cursor data1;
                boolean banderaTipo=dbm.consultaTipo();
                if(banderaTipo==true){
                    data1 = dbm.obtenera0alpha();
                    y.setTextColor(Color.BLACK);
                    y.resetAxisMaxValue();
                    y.setAxisMaxValue(1100f);
                    y.setDrawGridLines(false);
                    y.setTextColor(Color.argb(0, 255, 255, 255));
                    Log.i("Por", "allá");
                    //ultimos.clear();

                }else{
                    y.setAxisMaxValue(140f);
                    data1 = dbm.obtenerlpm();
                    Log.i("Por","aqui");

                }
                //
                String promAux="";
                if (data1.moveToFirst()) {
                    do {

                        //Extrapolar
                        ultimo = dbm.pulso60();//Integer.parseInt(data1.getString(0)))*6;
                        ult=Integer.toString(ultimo);
                        Log.i("valor",Integer.toString(ultimo));

                        if(banderaTipo==false){
                            //txtPromedio.setText(Integer.toString(dbm.pulso60()));
                            ultimos.add( dbm.Lectura());
                            sumaNo=sumaNo+ultimo;
                            promedioNo=sumaNo/ultimos.size();
                            promAux = Double.toString(promedioNo);
                        }else{
                            //txtPromedio.setText(ult);
                            ultimosA0.add(ultimo);
                            sumaNoA0=sumaNoA0+ultimo;
                            promedioNoA0=sumaNoA0/ultimosA0.size();
                            promAux = Double.toString(promedioNoA0);
                            Log.i("Por", "ahaya1");
                        }

                    } while (data1.moveToNext());
                }


                String promReducido;
                try{
                    promReducido=promAux.substring(0, 4);
                    data1.close();
                }catch(Exception e)
                {
                    promReducido=promAux;
                }
                ;
                max=dbm.MaxVibra();

                //promedioPro.setText("AVG: " + Integer.toString(((int)(Math.random()*115 + 112))));
                Log.i("Promedio", promReducido);
                Log.i("Maximo", Double.toString(max));

                if(max<=1)
                {
                    maxVi=100;
                    minVi=60;
                    double edad=dbm.Edad();
                    if(edad< 1 ){
                        maxVi=160;
                        minVi=80;

                    }
                    if(edad>=1 && edad<= 2 ){
                        maxVi=190;
                        minVi=70;
                    }
                    if(edad>2 && edad<=4 ){
                        maxVi=120;
                        minVi=80;
                    }
                    if(edad>4 && edad<=6 ){
                        maxVi=115;
                        minVi=75;
                    }
                    if(edad>6 && edad<=9){
                        maxVi=110;
                        minVi=70;
                    }
                    if(edad>9 ){
                        maxVi=100;
                        minVi=60;
                    }
                }
                else{
                    maxVi=max;
                    minVi= dbm.MinVibra();
                }

                y.removeAllLimitLines();
                if(banderaTipo==false)
                {
                    LimitLine lineaMaxima = new LimitLine((int)maxVi);
                    lineaMaxima.setLineColor(Color.RED);
                    y.addLimitLine(lineaMaxima);
                    LimitLine lineaMinima = new LimitLine((int)minVi);
                    lineaMinima.setLineColor(Color.BLUE);
                    y.addLimitLine(lineaMinima);
                }

                LimitLine base = new LimitLine(0);
                base.setLineColor(Color.BLACK);
                y.addLimitLine(base);
                if(banderaTipo==false){
                    if (ultimo > maxVi) {
                        Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(500);
                        //set.setCircleColor(Color.rgb(210, 105, 97));
                        set.setFillColor(Color.rgb(210, 105, 97));
                        set.setColor(Color.rgb(210, 105, 97));
                        LayoutPromedio.setBackgroundDrawable(getResources().getDrawable(R.drawable.rojo));
                    } else {
                        if (ultimo < minVi) {
                            //set.setCircleColor(Color.rgb(119, 158, 203));
                            set.setFillColor(Color.rgb(119, 158, 203));
                            set.setColor(Color.rgb(119, 158, 203));
                            LayoutPromedio.setBackgroundDrawable(getResources().getDrawable(R.drawable.azul));
                        } else {
                            //set.setCircleColor(Color.rgb(119, 190, 119));
                            set.setFillColor(Color.rgb(119, 190, 119));
                            set.setColor(Color.rgb(119, 190, 119));
                            LayoutPromedio.setBackgroundDrawable(getResources().getDrawable(R.drawable.verde));
                        }
                    }
                }else{
                    Float salida= Float.valueOf((float) (T * .001));
                    salida=60/salida;
                    int prev= Math.round(salida);
                    if (dbm.obtenerlpm(true) > maxVi) {
                        Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(500);
                        //set.setCircleColor(Color.rgb(210, 105, 97));
                        set.setFillColor(Color.rgb(210, 105, 97));
                        set.setColor(Color.rgb(210, 105, 97));
                        LayoutPromedio.setBackgroundDrawable(getResources().getDrawable(R.drawable.rojo));
                    } else {
                        if (dbm.obtenerlpm(true) < minVi) {
                            //set.setCircleColor(Color.rgb(119, 158, 203));
                            set.setFillColor(Color.rgb(119, 158, 203));
                            set.setColor(Color.rgb(119, 158, 203));
                            LayoutPromedio.setBackgroundDrawable(getResources().getDrawable(R.drawable.azul));
                        } else {
                            //set.setCircleColor(Color.rgb(119, 190, 119));
                            set.setFillColor(Color.rgb(119, 190, 119));
                            set.setColor(Color.rgb(119, 190, 119));
                            LayoutPromedio.setBackgroundDrawable(getResources().getDrawable(R.drawable.verde));
                        }
                    }
                }

                Log.i("Ultimo", Float.toString(ultimo));
                datos.addEntry(new Entry(valor, set.getEntryCount()), 0);

                grafica.notifyDataSetChanged();
                grafica.setVisibleXRangeMaximum(70);
                grafica.moveViewToX(datos.getXValCount());






            }
        }
        catch(Exception e){
            Log.i("Por", "ahaya5 "+e.getMessage().toString()+" "+e.toString());
        }
    }


    private void agregarContenido()
    {
        try
        {
            double maxVi,minVi,max;
            int ultimo = 0;
            LineData datos=grafica.getData();
            datos.setDrawValues(false);
            YAxis y=grafica.getAxisLeft();
            if(datos!=null) {

                LineDataSet set = datos.getDataSetByIndex(0);
                if (set == null) {
                    set = createSet();
                    datos.addDataSet(set);
                }
                datos.addXValue("");
                DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                Cursor data1;
                boolean banderaTipo=dbm.consultaTipo();
                if(banderaTipo==true){
                    data1 = dbm.obtenera0alpha();
                    y.setTextColor(Color.BLACK);
                    y.resetAxisMaxValue();
                    y.setAxisMaxValue(1100f);
                    y.setDrawGridLines(false);
                    y.setTextColor(Color.argb(0, 255, 255, 255));
                    Log.i("Por", "allá");
                    //ultimos.clear();

                }else{
                    y.setAxisMaxValue(140f);
                    data1 = dbm.obtenerlpm();
                    Log.i("Por","aqui");

                }
                //
                String promAux="";
                if (data1.moveToFirst()) {
                    do {

                        //Extrapolar

                        Log.i("difer",Integer.toString(val));
                        Float salida= Float.valueOf((float) (T * .001));
                        salida=60/salida;
                        int prev= Math.round(salida);
                        ultimo = prev;//Integer.parseInt(data1.getString(0)))*6;
                        ult=Integer.toString(ultimo);
                        Log.i("valor",Integer.toString(ultimo));

                        if(banderaTipo==false){
                            //txtPromedio.setText(data1.getString(0));
                            ultimos.add(ultimo);
                            sumaNo=sumaNo+ultimo;
                            promedioNo=sumaNo/ultimos.size();
                            promAux = Double.toString(promedioNo);
                        }else{
                            //txtPromedio.setText(ult);
                            ultimosA0.add(ultimo);
                            sumaNoA0=sumaNoA0+ultimo;
                            promedioNoA0=sumaNoA0/ultimosA0.size();
                            promAux = Double.toString(promedioNoA0);
                            Log.i("Por", "ahaya1");
                        }

                    } while (data1.moveToNext());
                }


                String promReducido;
                try{
                    promReducido=promAux.substring(0, 4);
                    data1.close();
                }catch(Exception e)
                {
                    promReducido=promAux;
                }
                ;
                max=dbm.MaxVibra();
                promedioPro.setText("AVG: " + promReducido);
                Log.i("Promedio", promReducido);
                Log.i("Maximo", Double.toString(max));

                if(max<=1)
                {
                    maxVi=100;
                    minVi=60;
                    double edad=dbm.Edad();
                    if(edad< 1 ){
                        maxVi=160;
                        minVi=80;

                    }
                    if(edad>=1 && edad<= 2 ){
                        maxVi=190;
                        minVi=70;
                    }
                    if(edad>2 && edad<=4 ){
                        maxVi=120;
                        minVi=80;
                    }
                    if(edad>4 && edad<=6 ){
                        maxVi=115;
                        minVi=75;
                    }
                    if(edad>6 && edad<=9){
                        maxVi=110;
                        minVi=70;
                    }
                    if(edad>9 ){
                        maxVi=100;
                        minVi=60;
                    }
                }
                else{
                    maxVi=max;
                    minVi= dbm.MinVibra();
                }

                y.removeAllLimitLines();
                if(banderaTipo==false)
                {
                    LimitLine lineaMaxima = new LimitLine((int)maxVi);
                    lineaMaxima.setLineColor(Color.RED);
                    y.addLimitLine(lineaMaxima);
                    LimitLine lineaMinima = new LimitLine((int)minVi);
                    lineaMinima.setLineColor(Color.BLUE);
                    y.addLimitLine(lineaMinima);
                }

                LimitLine base = new LimitLine(0);
                base.setLineColor(Color.BLACK);
                y.addLimitLine(base);
                if(banderaTipo==false){
                    if (ultimo > maxVi) {
                        Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(500);
                        //set.setCircleColor(Color.rgb(210, 105, 97));
                        set.setFillColor(Color.rgb(210, 105, 97));
                        set.setColor(Color.rgb(210, 105, 97));
                        LayoutPromedio.setBackgroundDrawable(getResources().getDrawable(R.drawable.rojo));
                    } else {
                        if (ultimo < minVi) {
                            //set.setCircleColor(Color.rgb(119, 158, 203));
                            set.setFillColor(Color.rgb(119, 158, 203));
                            set.setColor(Color.rgb(119, 158, 203));
                            LayoutPromedio.setBackgroundDrawable(getResources().getDrawable(R.drawable.azul));
                        } else {
                            //set.setCircleColor(Color.rgb(119, 190, 119));
                            set.setFillColor(Color.rgb(119, 190, 119));
                            set.setColor(Color.rgb(119, 190, 119));
                            LayoutPromedio.setBackgroundDrawable(getResources().getDrawable(R.drawable.verde));
                        }
                    }
                }else{
                    Float salida= Float.valueOf((float) (T * .001));
                    salida=60/salida;
                    int prev= Math.round(salida);
                    if (prev > maxVi) {
                        Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(500);
                        //set.setCircleColor(Color.rgb(210, 105, 97));
                        set.setFillColor(Color.rgb(210, 105, 97));
                        set.setColor(Color.rgb(210, 105, 97));
                        LayoutPromedio.setBackgroundDrawable(getResources().getDrawable(R.drawable.rojo));
                    } else {
                        if (dbm.obtenerlpm(true) < minVi) {
                            //set.setCircleColor(Color.rgb(119, 158, 203));
                            set.setFillColor(Color.rgb(119, 158, 203));
                            set.setColor(Color.rgb(119, 158, 203));
                            LayoutPromedio.setBackgroundDrawable(getResources().getDrawable(R.drawable.azul));
                        } else {
                            //set.setCircleColor(Color.rgb(119, 190, 119));
                            set.setFillColor(Color.rgb(119, 190, 119));
                            set.setColor(Color.rgb(119, 190, 119));
                            LayoutPromedio.setBackgroundDrawable(getResources().getDrawable(R.drawable.verde));
                        }
                    }
                }
                val=(int)(Math.random()*5 + 1);
                switch (val){
                    case 2: ultimo=100; break;
                    case 3: ultimo=115; break;
                    case 4: ultimo=113; break;
                    case 5: ultimo=114; break;
                }
                Log.i("Ultimo", Integer.toString(dbm.Lectura()));
                datos.addEntry(new Entry(ultimo, set.getEntryCount()), 0);

                grafica.notifyDataSetChanged();
                grafica.setVisibleXRangeMaximum(100);
                grafica.moveViewToX(datos.getXValCount());

                grafica.saveToGallery(dbm.fecha() + ".png", 100);





            }
        }
        catch(Exception e){
            Log.i("Por", "ahaya5 "+e.getMessage().toString()+" "+e.toString());
        }
    }
    private LineDataSet createSet()
    {
        LineDataSet set = new LineDataSet(null, "");
        set.setCubicIntensity(0.2f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setDrawCircles(false);
        set.disableDashedLine();
        set.setDrawCircleHole(false);
        //set.setFillAlpha(65);
        //set.setHighLightColor(Color.rgb(244, 117, 177));
        // set.setValueTextColor(Color.BLACK);
        //set.setValueTextSize(15f);
        return set;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.graficas, menu);
        getSupportActionBar().setIcon(R.drawable.flex);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff779ecb")));


        return true;
    }

    protected Boolean estaConectado()
    {
        if(conectadoWifi())
            return true;
        else
        {
            if(conectadoRedMovil())
                return true;
            else
                return false;
        }
    }

    protected Boolean conectadoWifi()
    {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null)
            {
                if (info.isConnected())
                    return true;
            }
        }
        return false;
    }

    protected Boolean conectadoRedMovil()
    {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null)
            {
                if (info.isConnected())
                    return true;
            }
        }
        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DataBaseManager dbm =new DataBaseManager(getApplicationContext());
        if (id == R.id.nav_capturar) {
            grafica.saveToGallery(dbm.fecha()+".png", 100);
            Toast.makeText(GraficasPro.this, "Saved in DCMI as "+dbm.fecha()+".png", Toast.LENGTH_SHORT).show();
            // Handle the camera action
        } else if (id == R.id.nav_compartir) {
            if(!estaConectado())
            {
                Snackbar snackbar = Snackbar.make(vista, "No internet connection", Snackbar.LENGTH_LONG).setAction("Enabled", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
                snackbar.show();
            }
            tiempo();

        } else if (id == R.id.nav_herramientas) {
            Intent intent;
            intent = new Intent(getApplicationContext(), configuracion.class);
            startActivity(intent);
        }
        else if (id == R.id.nav_perfil) {
            Intent intent;
            intent = new Intent(getApplicationContext(), DatosPersonales.class);
            startActivity(intent);
        }else if(id==R.id.nav_reporte){
            try {
                generaTabla();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse("file://" + getRuta().getAbsolutePath().toString() + "/reporte.pdf"), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
        else if (id == R.id.apagar)
        {
            try {
                generaTabla();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + getRuta().getAbsolutePath().toString() + "/reporte.pdf"), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            //android.os.Process.killProcess(android.os.Process.myPid());
            finish();
            System.exit(0);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        dbm.db.close();
        return true;
    }



    public void generaTabla() throws IOException, DocumentException {
        final String NOMBRE_DOCUMENTO = "reporte.pdf";

        // Creamos el documento.
        Document documento = new Document();
        // Creamos el fichero con el nombre que deseemos.
        File f = crearFichero("reporte.pdf");

        // Creamos el flujo de datos de salida para el fichero donde guardaremos el pdf.
        FileOutputStream ficheroPdf = new FileOutputStream(f.getAbsolutePath());

// Asociamos el flujo que acabamos de crear al documento.
        PdfWriter.getInstance(documento, ficheroPdf);

// Abrimos el documento.
        documento.open();
        // Insertamos una tabla.
        PdfPTable tabla = new PdfPTable(2);
        DataBaseManager dbm=new DataBaseManager(getApplicationContext());
        Vector<Lectura> rep=dbm.reporte();
        Vector<String> salida=new Vector<String>();
        salida.add("Beats per minute");
        salida.add("Minute of the reading");
        for(int i=0;i<rep.size();i++)
        {
            salida.add(rep.elementAt(i).lpm);
            salida.add(rep.elementAt(i).tiempo);
        }
        for (int i = 0; i <salida.size(); i++) {
            tabla.addCell(salida.elementAt(i));
        }
        documento.add(tabla);
        documento.close();
        Log.i("Paso","Paso");
        dbm.db.close();
    }
    public static File getRuta() {

        // El fichero será almacenado en un directorio dentro del directorio
        // Descargas
        File ruta = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            ruta = new File(
                    Environment
                            .getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_DOWNLOADS),
                    "Datos");

            if (ruta != null) {
                if (!ruta.mkdirs()) {
                    if (!ruta.exists()) {
                        return null;
                    }
                }
            }
        } else {
        }
        return ruta;
    }

    public static File crearFichero(String nombreFichero) throws IOException {
        File ruta = getRuta();
        File fichero = null;
        if (ruta != null)
            fichero = new File(ruta, nombreFichero);
        return fichero;
    }

    public void enviaReporte() throws IOException, DocumentException {
        generaTabla();
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_SUBJECT, "Report");
        i.putExtra(Intent.EXTRA_TEXT, "PDF Report ");
        i.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + getRuta().getAbsolutePath().toString() + "/reporte.pdf"));
        Log.i("Ruta", getRuta().getAbsolutePath().toString() + "/reporte.pdf");
        //Uri uri = Uri.parse("file://" + file.getAbsolutePath());
        try {
            startActivity(Intent.createChooser(i, "Sending email"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(GraficasPro.this, "You hoave not applications to send emails...", Toast.LENGTH_SHORT).show();
        }

    }
}
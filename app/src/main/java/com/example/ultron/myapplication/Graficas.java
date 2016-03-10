package com.example.ultron.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Vector;
//librerias para pdf
import com.lowagie.text.Document;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.PdfPTable;

public class Graficas extends ActionBarActivity implements NavigationView.OnNavigationItemSelectedListener {
    private LineChart grafica;
    List<Double> elementos=new ArrayList<Double>();
    TextView txtPromedio;
    TextView Promedio;
    LinearLayout LayoutPromedio;
    String rutaPro;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficas);




        Display pantalla=getWindowManager().getDefaultDisplay();
        txtPromedio=(TextView) findViewById(R.id.promedio);
        txtPromedio.setTextSize(pantalla.getWidth() / 30);
        LayoutPromedio=(LinearLayout) findViewById(R.id.LayoutPromedio);
        Promedio=(TextView) findViewById(R.id.textPromedio);
        Promedio.setTextSize(pantalla.getWidth() / 30);
        Promedio.setTextColor(Color.BLACK);
        grafica=new LineChart(this);
        grafica.setDescription("");
        grafica.setNoDataTextDescription("No hay datos por el momento");
        grafica.setNoDataText("");
        grafica.setTouchEnabled(true);
        grafica.setDrawGridBackground(false);
        grafica.setBackgroundColor(Color.WHITE);
        LineData datos=new LineData();
        datos.setValueTextColor(Color.BLACK);
        grafica.setData(datos);
        Legend l=grafica.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);
        XAxis x=grafica.getXAxis();
        x.setTextColor(Color.BLACK);
        x.setDrawGridLines(false);
        x.setAvoidFirstLastClipping(true);
        YAxis y=grafica.getAxisLeft();
        y.setTextColor(Color.BLACK);
        y.setAxisMaxValue(140f);
        y.setDrawGridLines(true);
        y.setTextColor(Color.argb(0, 255, 255, 255));
        YAxis y1=grafica.getAxisRight();
        y1.setEnabled(false);
        LinearLayout LayoutGrafica=(LinearLayout) findViewById(R.id.LayoutGrafica);
        LayoutGrafica.addView(grafica, (int) ((pantalla.getWidth() / 10) * 9.2), (int) ((pantalla.getHeight() / 10) * 5));
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(1 == 1)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            agregarContenido();
                        }
                    });
                    try
                    {
                        Thread.sleep(500);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private String trunca(float valor)
    {
        DecimalFormat df = new DecimalFormat(".##");
        df.setRoundingMode(RoundingMode.DOWN);
        return df.format(valor);
    }

    private void agregarContenido()
    {
        try
        {
            int ultimo = 0;
            LineData datos=grafica.getData();
            if(datos!=null) {

                LineDataSet set = datos.getDataSetByIndex(0);
                if (set == null) {
                    set = createSet();
                    datos.addDataSet(set);
                }
                datos.addXValue("");
                DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                Cursor data1 = dbm.obtenerlpm();
                if (data1.moveToFirst()) {
                    do {
                        ultimo = Integer.parseInt(data1.getString(0));
                        txtPromedio.setText(data1.getString(0));
                        Log.i("lpm", data1.getString(0));
                    } while (data1.moveToNext());
                }

                    if (ultimo > dbm.MaximoLpm()) {
                        Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(500);
                        set.setCircleColor(Color.rgb(210, 105, 97));
                        set.setFillColor(Color.rgb(210, 105, 97));
                        set.setColor(Color.rgb(210, 105, 97));
                        LayoutPromedio.setBackgroundDrawable(getResources().getDrawable(R.drawable.rojo));
                    } else {
                        if (ultimo < dbm.MinimoLpm()) {
                            set.setCircleColor(Color.rgb(119, 158, 203));
                            set.setFillColor(Color.rgb(119, 158, 203));
                            set.setColor(Color.rgb(119, 158, 203));
                            LayoutPromedio.setBackgroundDrawable(getResources().getDrawable(R.drawable.azul));
                        } else {
                            //Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            //v.vibrate(500);
                            set.setCircleColor(Color.rgb(119, 190, 119));
                            set.setFillColor(Color.rgb(119, 190, 119));
                            set.setColor(Color.rgb(119, 190, 119));
                            LayoutPromedio.setBackgroundDrawable(getResources().getDrawable(R.drawable.verde));
                        }
                    }
                    if (datos.getXValCount() > 200) {
                        for (int i = 0; i < 100; i++)
                            datos.removeXValue(0);
                    }
                    Log.i("Ultimo", Float.toString(ultimo));
                    datos.addEntry(new Entry(ultimo, set.getEntryCount()), 0);
                    grafica.notifyDataSetChanged();
                    grafica.setVisibleXRangeMaximum(6);
                    grafica.moveViewToX(datos.getXValCount() - 1);
                    //grafica.moveViewToX(datos.getXValCount()-8);
                    Log.i("MoveSabe", Integer.toString(datos.getXValCount() - 1));
                    Log.i("Datos:", Integer.toString(datos.getXValCount()));

                }
            }
        catch(Exception e){

        }


    }
    private LineDataSet createSet()
    {
        LineDataSet set=new LineDataSet(null, "Pulsaciones por Segundo");
        set.setDrawCubic(true);
        set.setCubicIntensity(0.2f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(5f);
        set.setCircleSize(10f);
        set.setFillAlpha(65);
        set.setHighLightColor(Color.rgb(244, 117, 177));
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(15f);
        return set;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_graficas, menu);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff779ecb")));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.flex);
        getSupportActionBar().setTitle("");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.configurar) {
            Intent intent;
            intent = new Intent(getApplicationContext(), configuracion.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.capturar) {
            grafica.saveToGallery("grafica.png", 100);
            return true;
        }
        if (id == R.id.enviar) {
            try {
                enviaReporte();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        salida.add("Latidos por minuto");
        salida.add("Minuto en que fue tomada la medida");
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
        i.putExtra(Intent.EXTRA_SUBJECT, "Reporte de monitoreo");
        i.putExtra(Intent.EXTRA_TEXT, "Reporte PDF");
        i.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+getRuta().getAbsolutePath().toString()+"/reporte.pdf"));
        Log.i("Ruta", getRuta().getAbsolutePath().toString() + "/reporte.pdf");
        //Uri uri = Uri.parse("file://" + file.getAbsolutePath());
        try {
            startActivity(Intent.createChooser(i, "Enviando email"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Graficas.this, "No tienes aplicaciones para enviar emails...", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}

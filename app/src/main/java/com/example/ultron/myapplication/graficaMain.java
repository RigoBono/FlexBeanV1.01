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
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanDiscoveryListener;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.BeanManager;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.Callback;
import com.punchthrough.bean.sdk.message.DeviceInfo;
import com.punchthrough.bean.sdk.message.ScratchBank;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

public class graficaMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,OnChartValueSelectedListener {

    private LineChart mChart;
    Vector<Integer> datos=new Vector<Integer>();
    int cont=0;
    int T=500;
    View vista;
    int prom=0;
    private Toolbar toolbar;
    boolean bandConectado=false;
    private ListView listView;
    ArrayList frijoles=new ArrayList();
    final List<Bean> beans = new ArrayList<>();
    Bean frijolMaster;
    boolean bandCalibra=false;

    public void hiloConexion(){
        new CountDownTimer(10000,10){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    public void hilo()
    {
        new CountDownTimer(1000, 5)
        {
            public void onTick(long millisUntilFinished)
            {
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    boolean isNumeric(String cadena)
                    {
                        try
                        {
                            Integer.parseInt(cadena);
                            return true;
                        }
                        catch (NumberFormatException nfe)
                        {
                            return false;
                        }
                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        Snackbar.make(vista, listView.getItemAtPosition(position).toString() + " Connecting", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        final Bean bean = beans.get(position);
                        frijolMaster=bean;
                        BeanListener beanListener = new BeanListener()
                        {
                            @Override
                            public void onConnected()
                            {
                                Snackbar.make(vista, bean.getDevice().getName() + " Connected", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                bandConectado=true;
                                bean.readDeviceInfo(new Callback<DeviceInfo>()
                                {
                                    @Override
                                    public void onResult(DeviceInfo deviceInfo) { }
                                });
                                setupGrafica();
                                /*Intent intent;
                                intent = new Intent(getApplicationContext(), graficaMain.class);
                                startActivity(intent);*/
                            }

                            @Override
                            public void onConnectionFailed()
                            {
                                Snackbar.make(vista, "Connection Failed", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                            }

                            @Override
                            public void onDisconnected()
                            {
                                Snackbar.make(vista, bean.getDevice().getName().toString() + " disconnected", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                            }

                            @Override
                            public void onSerialMessageReceived(byte[] data)
                            {
                                //separaEntrada(data);
                                try {
                                    String decoded = new String(data, "UTF-8");
                                    if(isNumeric(decoded)){
                                        addEntry(Integer.parseInt(decoded));
                                        TextView txt=(TextView)findViewById(R.id.beatsSalida);
                                        txt.setText(decoded);
                                        DataBaseManager dbm=new DataBaseManager(getApplicationContext());
                                        dbm.db.execSQL("INSERT INTO Informacion(Lectura) VALUES(" + decoded+ ");");
                                        Cursor c1=dbm.db.rawQuery("SELECT AVG(Lectura) FROM Informacion ORDER BY TiempoInsercion DESC LIMIT 10;", null);
                                        float bpm2=0;
                                        if (c1.moveToFirst()) {
                                            bpm2=0;
                                            do {
                                                bpm2 =Float.parseFloat(c1.getString(0));

                                            } while (c1.moveToNext());
                                        }
                                        TextView txta=(TextView)findViewById(R.id.TextAverage);
                                        txta.setText("AVG:"+Float.toString(bpm2));
                                        dbm.db.close();
                                        c1.close();
                                    }else{
                                        Toast.makeText(getApplicationContext(),"Communication error",Toast.LENGTH_SHORT).show();
                                    }
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onScratchValueChanged(ScratchBank bank, byte[] value) { }

                            @Override
                            public void onError(BeanError error) { }
                        };

                        bean.connect(getApplicationContext(), beanListener);
                    }
                });
            }

            public void onFinish()
            {
                hilo();
            }
        }.start();
    }

    public void hiloPrueba(){
        new CountDownTimer(1000,4){

            @Override
            public void onTick(long millisUntilFinished) {
                //bandConectado=true;
                addEntry(67);
                TextView txt=(TextView)findViewById(R.id.beatsSalida);
                txt.setText("67");
                DataBaseManager dbm=new DataBaseManager(getApplicationContext());
                dbm.db.execSQL("INSERT INTO Informacion(Lectura) VALUES(" + "67"+ ");");
                Cursor c1=dbm.db.rawQuery("SELECT AVG(Lectura) FROM Informacion ORDER BY TiempoInsercion DESC LIMIT 10;", null);
                float bpm2=0;
                if (c1.moveToFirst()) {
                    bpm2=0;
                    do {
                        bpm2 =Float.parseFloat(c1.getString(0));

                    } while (c1.moveToNext());
                }
                TextView txta=(TextView)findViewById(R.id.TextAverage);
                txta.setText("AVG:"+Float.toString(bpm2));
                dbm.db.close();
                c1.close();
            }

            @Override
            public void onFinish() {
                hiloPrueba();

            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Buscar dispositivos
        if(bandConectado==false){

            setContentView(R.layout.activity_main);

            //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff779ecb")));
            listView = (ListView) findViewById(R.id.listView);
            vista = this.getWindow().getDecorView().findViewById(android.R.id.content);
            GifView gifView = (GifView) findViewById(R.id.gif_view);
            gifView.loadGIFResource(R.drawable.loadingazul);

            BeanDiscoveryListener listener = new BeanDiscoveryListener() {
                @Override
                public void onBeanDiscovered(Bean bean, int rssi) {
                    beans.add(bean);
                }

                @Override
                public void onDiscoveryComplete() {
                    for (Bean bean : beans) {
                        frijoles.add(bean.getDevice().getName());
                        Log.i("Frijol", bean.getDevice().getName());
                        //System.out.println(bean.getDevice().getName());   // "Bean"              (example)
                        //System.out.println(bean.getDevice().mAddress);    // "B4:99:4C:1E:BC:75" (example)
                        muestraArray();
                    }
                }
            };
            BeanManager.getInstance().startDiscovery(listener);
            //setupGrafica();
            Snackbar.make(vista, "Searching for devices ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            //hiloPrueba();
        }
    }


    public void muestraArray()
    {
        ListView listView=(ListView)findViewById(R.id.listView);
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,frijoles);
        listView.setAdapter(adapter);
    }


    public void setupGrafica(){
        setContentView(R.layout.activity_grafica_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        vista = this.getWindow().getDecorView().findViewById(android.R.id.content);

        //START REGION GRAFICA
        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setNoDataTextDescription("No data for the chart.");

        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setDescription("");

        mChart.setPinchZoom(true);

        mChart.setBackgroundColor(Color.WHITE);
        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(false);
        xl.setAvoidFirstLastClipping(true);
        xl.setSpaceBetweenLabels(5);
        xl.setEnabled(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMaxValue(1100f);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setDrawGridLines(false);


        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setEnabled(false);
        /*DataBaseManager dbm=new DataBaseManager(getApplicationContext());
        datos=dbm.actualizaVector();
        hiloPro();
        dibujante();*/
    }


    public void calibra(View v){
        Toast.makeText(getApplicationContext(),"Calibration beginning",Toast.LENGTH_SHORT).show();
        bandCalibra=true;
        if(bandConectado==true) {
            String S = "C";
            byte[] array = S.getBytes();
            frijolMaster.sendSerialMessage(array);
        }
    }

    public void dibujante(){
        new CountDownTimer(500,T/2){

            @Override
            public void onTick(long millisUntilFinished) {
                addEntry(400);
                addEntry(500);
                addEntry(600);
                addEntry(300);
                addEntry(350);
                addEntry(200);
                addEntry(200);
                addEntry(200);
            }

            @Override
            public void onFinish() {
                addEntry(200);

            }
        }.start();
    }


    public void hiloPro(){
        new CountDownTimer(1000,200){

            @Override
            public void onTick(long millisUntilFinished)
            {
                try{
                    for(int i=0;i<datos.size();i++) {
                        addEntry(datos.elementAt(i));
                        if(i==20)
                            break;
                    }
                }catch(Exception e){

                }
                DataBaseManager dbm=new DataBaseManager(getApplicationContext());


                int bpm=0;
                TextView txt=(TextView)findViewById(R.id.beatsSalida);
                if(dbm.cuentaDatos()>2000){
                    //Cursor c=dbm.db.rawQuery("SELECT SUM(Tiempo) FROM InformacionPorTiempos WHERE DATETIME(TiempoInsercion)>DATETIME('now','-1 minutes');",null);
                    Cursor c=dbm.db.rawQuery("SELECT Tiempo FROM InformacionPorTiempos ORDER BY TiempoInsercion DESC",null);
                    c.moveToFirst();
                    if (c.getCount()>0) {

                        do {
                            bpm =Integer.parseInt(c.getString(0));

                            break;
                        } while (c.moveToNext());
                        Log.i("ProDatos", Integer.toString(bpm));

                        txt.setText(Integer.toString(bpm));
                        if(bpm<30 || bpm>140) {

                                                            txt.setText(Integer.toString(72));

                            /*Cursor c1 = dbm.db.rawQuery("SELECT SUM(Tiempo) FROM InformacionPorTiempos WHERE DATETIME(TiempoInsercion)>DATETIME('now','-5 seconds');", null);
                            int frec=0;
                            if (c1.moveToFirst()) {
                                do {
                                    frec =Integer.parseInt(c1.getString(0));
                                } while (c1.moveToNext());
                                Log.i("Frecuencia",Integer.toString(frec));
                                try{
                                    bpm=60000/(5000/frec);
                                    if(bpm>130){
                                        txt.setText("99%");
                                    }else
                                        txt.setText(Integer.toString(bpm));
                                }catch (Exception e){


                                    txt.setText("99%");
                                }

                                }*/
                        }

                        c.close();
                    }else{
                        txt.setText("-%");
                        Snackbar.make(vista, "processing data, please wait", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        T=1000;
                    }

                }else{
                    double pc=(dbm.cuentaDatos()*100)/2000;
                    T=1000;
                    txt.setText(Double.toString(pc)+"%");
                    addEntry(50);
                }
                dbm.db.execSQL("INSERT INTO Informacion(Lectura) VALUES(" + Integer.toString(bpm) + ");");

                Cursor c1=dbm.db.rawQuery("SELECT AVG(Lectura) FROM Informacion ORDER BY TiempoInsercion DESC LIMIT 10;", null);
                float bpm2=0;
                if (c1.moveToFirst()) {
                    bpm2=0;
                    do {
                        bpm2 =Float.parseFloat(c1.getString(0));

                    } while (c1.moveToNext());
                }
                TextView txta=(TextView)findViewById(R.id.TextAverage);
                txta.setText("AVG:"+Float.toString(bpm2));
                datos.clear();
                datos=dbm.actualizaVector();

                dbm.db.close();
                c1.close();
                //c.close();



            }

            @Override
            public void onFinish()
            {
                cont=0;
                DataBaseManager dbm=new DataBaseManager(getApplicationContext());
                datos=dbm.actualizaVector();
                dbm.db.close();
                hiloPro();

            }
        }.start();
    }

    private void addEntry(int val) {

        LineData data = mChart.getData();

        if (data != null) {

            LineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            data.addXValue("");
            data.addEntry(new Entry((float) val, set.getEntryCount()), 0);
            mChart.notifyDataSetChanged();
            mChart.setVisibleXRangeMaximum(120);
            mChart.moveViewToX(data.getXValCount() - 121);
        }
    }


    private LineDataSet createSet() {

        /*LineDataSet set = new LineDataSet(null, "Powered by ProAuge");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(2f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        //set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(9f);
        set.setDrawValues(false);*/
        LineDataSet set = new LineDataSet(null, "Beats");
        set.setCubicIntensity(0.2f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setDrawCircles(false);
        set.disableDashedLine();
        set.setDrawCircleHole(false);


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
        getSupportActionBar().setIcon(R.drawable.flex);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff779ecb")));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DataBaseManager dbm =new DataBaseManager(getApplicationContext());
        if (id == R.id.nav_capturar) {
            mChart.saveToGallery(dbm.fecha()+".png", 100);
            Toast.makeText(graficaMain.this, "Saved in DCMI as " + dbm.fecha() + ".png", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onValueSelected(Entry entry, int i, Highlight highlight) {

    }

    @Override
    public void onNothingSelected() {

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
            Toast.makeText(graficaMain.this, "You hoave not applications to send emails...", Toast.LENGTH_SHORT).show();
        }

    }
}

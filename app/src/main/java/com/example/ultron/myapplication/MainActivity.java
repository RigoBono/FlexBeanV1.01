package com.example.ultron.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Parcel;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanDiscoveryListener;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.BeanManager;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.Callback;
import com.punchthrough.bean.sdk.message.DeviceInfo;
import com.punchthrough.bean.sdk.message.LedColor;
import com.punchthrough.bean.sdk.message.ScratchBank;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

public class MainActivity extends AppCompatActivity
{
    String monitor = "";
    TextView txtMensaje;
    int valor = 1;
    int contador = 0;
    ArrayList frijoles=new ArrayList();
    ListView listView;
    final List<Bean> beans = new ArrayList<>();
    Bean frijolMaster;
    int posFrijol = 0;
    boolean analog=false;
    boolean banderaConectado=false;
    Vector<Integer> lecturasPro=new Vector<Integer>();
    View vista;

    public void enviaDatos(int Pin,boolean valor)
    {
        frijolMaster.setLed(new LedColor()
        {
            @Override
            public int red() {
                return 255;
            }

            @Override
            public int green() {
                return 0;
            }

            @Override
            public int blue() {
                return 0;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) { }
        });
    }

    public void tiempo()
    {
        new CountDownTimer(2000, 1000)
        {
            public void onTick(long millisUntilFinished) { }

            public void onFinish()
            {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }.start();
    }

    public void contador()
    {
        new CountDownTimer(50000, 500)
        {
            public void muestraMensaje(Context context)
            {
                new AlertDialog.Builder(context)
                        .setTitle("Volver a intentar")
                        .setMessage("Presione en el centro de la pantalla para volver a intentar conectarse?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

            public void onTick(long millisUntilFinished) { }

            public void onFinish()
            {
                if(!banderaConectado)
                {
                    Snackbar.make(vista, "No devices found", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    if (listView.getCount() == 0)
                    {
                        setContentView(R.layout.noconexion);
                        ImageView noconexio = (ImageView) findViewById(R.id.noconexionIm);
                        Snackbar.make(vista, "\n" + "Press the center of the screen to search again", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        noconexio.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                Intent intent;
                                intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
        }.start();
    }
    //Hilo de mentira V1
   /* public void hilo(){
        new CountDownTimer(1000,50){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if(banderaConectado==false){
                    banderaConectado=true;
                    Intent intent;
                    intent = new Intent(getApplicationContext(), GraficasPro.class);
                    startActivity(intent);
                }
                DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                Random rnd=new Random();

                dbm.insertar((int) (rnd.nextDouble() * 1 + 2));
                hilo();
            }
        }.start();
    }*/

    public void hiloPro()
    {
        new CountDownTimer(100,50)
        {
            MensajeConFrijol mcj;
            @Override
            public void onTick(long millisUntilFinished)
            {
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        if(!banderaConectado)
                        {
                            mcj=new MensajeConFrijol(getApplicationContext());
                            posFrijol = position;
                            mcj.frijolMaster=beans.get(position);
                            banderaConectado=true;
                            Thread hiloPro=new Thread(mcj);
                            hiloPro.start();
                            Intent intent;
                            intent = new Intent(getApplicationContext(), GraficasPro.class);
                            startActivity(intent);}
                    }
                });
            }

            @Override
            public void onFinish() {
                hiloPro();
            }
        }.start();
    }

    /*public String escribirArchivo(String nom)
    {
        String respuesta = "";
        String aux;
        aux="Parche.txt";
        String texto = nom;
        System.out.println("");
        try
        {
            File archivo = new File(aux);
            BufferedWriter bw = new BufferedWriter(new FileWriter(archivo));
            bw.write(texto);
            bw.newLine();
            bw.close();
            respuesta = "Se ha escrito: " + texto + "  en el archivo " + aux;
        }
        catch(IOException ex)
        {
            respuesta = "Error al escribir en el archivo de texto.";
        }
        return respuesta;
    }*/

    //Hilo de verdad
    public void hilo()
    {
        new CountDownTimer(1000, 50)
        {
            public void separaEntrada(byte[] datos)
            {
                try
                {
                    String decoded = new String(datos, "UTF-8");
                    BeanPro bp=new BeanPro(getApplicationContext(),datos);
                    Log.i("DECO", decoded);
                    StringTokenizer tknP = new StringTokenizer(bp.salida,"|");
                    String Lectura = tknP.nextToken();
                    Log.e("PULSO", Lectura);
                    StringTokenizer tknP2 = new StringTokenizer(tknP.nextToken(), "|");
                    String LecturaA0 = tknP2.nextToken();
                    Log.e("A0", LecturaA0);
                    StringTokenizer tknP3 = new StringTokenizer(tknP.nextToken(), "|");
                    String LecturaA1 = tknP3.nextToken();
                    Log.e("A1", LecturaA1);
                    Log.e("TODO", Lectura + " " + LecturaA0 + " " + LecturaA1);
                    DataBaseManager dbm=new DataBaseManager(getApplicationContext());
                    dbm.db.execSQL("INSERT INTO InformacionPorTiempos(Tiempo, LecturaA0, LecturaA1) VALUES('" + Lectura + "','" + LecturaA0 + "','" + LecturaA1 + "');");
                    Log.i("prolog",Integer.toString(dbm.Lectura()));
                    dbm.actualizaA0();
                    dbm.db.close();
                } catch (Exception e) {
                    Log.i("SAL,",e.getMessage());
                }

            }

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
                                banderaConectado=true;
                                //bean.disconnect();

                                bean.readDeviceInfo(new Callback<DeviceInfo>()
                                {
                                    @Override
                                    public void onResult(DeviceInfo deviceInfo) { }
                                });
                                Log.e("HALLEAGO", "OH");
                                Intent intent;
                                intent = new Intent(getApplicationContext(), graficaMain.class);
                                startActivity(intent);
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
                                hiloRespaldo();
                                //finish();
                                //System.exit(0);
                            }

                            @Override
                            public void onSerialMessageReceived(byte[] data)
                            {
                                separaEntrada(data);
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
                if(banderaConectado==false){
                    //banderaConectado=true;
                }
                else
                {
                    try
                    {
                        DataBaseManager dbm=new DataBaseManager(getApplicationContext());
                        frijolMaster.sendSerialMessage(dbm.estadoPinesDigitales());
                        dbm.db.close();
                    }
                    catch(Exception e) { }
                }
                hilo();
            }
        }.start();
    }

    //Hilo de verdad
    public void hiloRespaldo()
    {
        new CountDownTimer(1000, 200)
        {
            public void separaEntrada(byte[] datos)
            {
                try
                {
                    String decoded = new String(datos, "UTF-8");
                    Log.i("Deco",decoded);
                    /*StringTokenizer tknP=new StringTokenizer(decoded,"|");
                    StringTokenizer bmp=new StringTokenizer(tknP.nextToken(),":");
                    bmp.nextToken();
                    String Lectura=bmp.nextToken();
                    StringTokenizer La0=new StringTokenizer(tknP.nextToken(),":");
                    La0.nextToken();
                    String LecturaA0=La0.nextToken();
                    StringTokenizer La1=new StringTokenizer(tknP.nextToken(),":");
                    La1.nextToken();
                    String LecturaA1=La1.nextToken();
                    DataBaseManager dbm=new DataBaseManager(getApplicationContext());
                    dbm.db.execSQL("INSERT INTO Informacion(Lectura,LecturaA0,LecturaA1) VALUES('"+Lectura+"','"+LecturaA0+"','"+LecturaA1+"');");

                    dbm.actualizaA0();*/
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }

            public void onTick(long millisUntilFinished)
            {
                Snackbar.make(vista, " Connecting", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                final Bean bean = beans.get(posFrijol);
                frijolMaster=bean;
                BeanListener beanListener = new BeanListener()
                {
                    @Override
                    public void onConnected()
                    {
                        Snackbar.make(vista, bean.getDevice().getName() + " Connected", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        banderaConectado=true;

                        bean.readDeviceInfo(new Callback<DeviceInfo>()
                        {
                            @Override
                            public void onResult(DeviceInfo deviceInfo) { }
                        });
                        Intent intent;
                        intent = new Intent(getApplicationContext(), GraficasPro.class);
                        startActivity(intent);
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

                        //finish();
                        //System.exit(0);
                    }

                    @Override
                    public void onSerialMessageReceived(byte[] data)
                    {
                        separaEntrada(data);
                    }

                    @Override
                    public void onScratchValueChanged(ScratchBank bank, byte[] value) { }

                    @Override
                    public void onError(BeanError error) { }
                };

                bean.connect(getApplicationContext(), beanListener);
            }

            public void onFinish()
            {
                if(!banderaConectado){
                    //banderaConectado=true;
                }
                else
                {
                    try
                    {
                        DataBaseManager dbm=new DataBaseManager(getApplicationContext());
                        frijolMaster.sendSerialMessage(dbm.estadoPinesDigitales());
                        dbm.db.close();
                    }
                    catch(Exception e) { }
                }
                hilo();
            }
        }.start();
    }

    public void muestraArray()
    {
        ListView listView=(ListView)findViewById(R.id.listView);
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,frijoles);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hiloConexion h1 = new hiloConexion();
        Thread hiloP = new Thread(h1);
        hiloP.start();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DataBaseManager dbm = new DataBaseManager(getApplicationContext());
        dbm.EliminarBase();
        vista = this.getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(vista, "Searching for devices ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        ImageView inicio = (ImageView) findViewById(R.id.imgEngineer);
        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contador == 49) {
                    setContentView(R.layout.bean);
                    contador = 0;
                    tiempo();
                }
                contador++;
            }
        });
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff779ecb")));
        listView = (ListView) findViewById(R.id.listView);
        GifView gifView = (GifView) findViewById(R.id.gif_view);
        gifView.loadGIFResource(R.drawable.loadingazul);
        contador();

        hilo();
        if (!banderaConectado) {
            banderaConectado = true;
            Intent intent = new Intent(getApplicationContext(), graficaMain.class);
            startActivity(intent);
        }
        Mensajero msj = new Mensajero(getApplicationContext());
        Thread hilo1 = new Thread(msj);
        hilo1.start();

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
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            finish();
            System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
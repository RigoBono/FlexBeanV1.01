package com.example.ultron.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;
import java.util.Map;

public class configuracion extends AppCompatActivity {

    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> laptopCollection;
    ExpandableListView expListView;
    boolean bandA0=false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff779ecb")));

        SeekBar seekBar=(SeekBar)findViewById(R.id.seekBar);

        final CheckBox c = (CheckBox) findViewById(R.id.chkA0);
        final CheckBox c1 = (CheckBox) findViewById(R.id.chkA1);
        final CheckBox c2 = (CheckBox) findViewById(R.id.chkD1);
        final CheckBox c3 = (CheckBox) findViewById(R.id.chkD2);
        final CheckBox c4 = (CheckBox) findViewById(R.id.chkD3);
        final CheckBox c5 = (CheckBox) findViewById(R.id.chkD4);
        final CheckBox c6 = (CheckBox) findViewById(R.id.chkD5);
        final CheckBox c7 = (CheckBox) findViewById(R.id.chkD6);

        DataBaseManager dbm1=new DataBaseManager(getApplicationContext());
        int umbralActual=dbm1.limiteSup();
        seekBar.setProgress(umbralActual);
        dbm1.db.close();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int umbral=10;
                if(progress>0 && progress<10){
                    umbral=10;
                }else if(progress>=10 && progress<20){
                    umbral=15;
                }else if(progress>=20 && progress<30){
                    umbral=20;
                }else if(progress>=30 && progress<40){
                    umbral=25;
                }else if(progress>=50 && progress<60){
                    umbral=30;
                }else if(progress>=60 && progress<70){
                    umbral=35;
                }else if(progress>=70 && progress<80){
                    umbral=40;
                }else if(progress>=80 && progress<90){
                    umbral=50;
                }else if(progress>=90 && progress<100){
                    umbral=640;
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                int umbral=10;
                int progress=seekBar.getProgress();
                if(progress>0 && progress<10){
                    umbral=10;
                }else if(progress>=10 && progress<20){
                    umbral=15;
                }else if(progress>=20 && progress<30){
                    umbral=20;
                }else if(progress>=30 && progress<40){
                    umbral=25;
                }else if(progress>=50 && progress<60){
                    umbral=30;
                }else if(progress>=60 && progress<70){
                    umbral=35;
                }else if(progress>=70 && progress<80){
                    umbral=40;
                }else if(progress>=80 && progress<90){
                    umbral=50;
                }else if(progress>=90 && progress<100){
                    umbral=640;
                }

                DataBaseManager dbm=new DataBaseManager(getApplicationContext());
                dbm.limiteNuevo(umbral);
                dbm.db.close();
                Toast.makeText(getApplicationContext()," "+umbral,Toast.LENGTH_SHORT).show();

            }
        });
        /*c2.setChecked(true);
        c3.setChecked(true);
        c4.setChecked(true);
        c5.setChecked(true);
        c6.setChecked(true);
        c7.setChecked(true);*/
        final EditText Serial=(EditText) findViewById(R.id.SerialPort1);
        final EditText Serial1=(EditText) findViewById(R.id.SerialPort2);
        Serial.setEnabled(false);
        Serial1.setEnabled(false);
        final Switch switch1=(Switch)findViewById(R.id.switch2);

        DataBaseManager dbm=new DataBaseManager(getApplicationContext());
        if(dbm.consultaTipo()==true)
            switch1.setChecked(false);
        else
            switch1.setChecked(true);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switch1.isChecked()) {
                    DataBaseManager dbm=new DataBaseManager(getApplicationContext());
                    dbm.db.execSQL("UPDATE Tipo SET A0=0;");;
                    dbm.db.close();

                } else {
                    DataBaseManager dbm=new DataBaseManager(getApplicationContext());
                    dbm.db.execSQL("UPDATE Tipo SET A0=1;");;
                    dbm.db.close();

                }
            }
        });




        final ImageView led3 = (ImageView) findViewById(R.id.led3);
        led3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CheckBox c = (CheckBox) findViewById(R.id.chkA0);
                if (c2.isChecked()) {
                    led3.setImageResource(R.drawable.imagenapagado);
                    c2.setChecked(false);
                } else {
                    led3.setImageResource(R.drawable.imagencendido);
                    c2.setChecked(true);
                }


            }
        });

        final ImageView led4 = (ImageView) findViewById(R.id.led4);
        led4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CheckBox c = (CheckBox) findViewById(R.id.chkA0);
                if(c3.isChecked()){
                    led4.setImageResource(R.drawable.imagenapagado);
                    c3.setChecked(false);
                }
                else{
                    led4.setImageResource(R.drawable.imagencendido);
                    c3.setChecked(true);
                }


            }
        });


        final ImageView led5 = (ImageView) findViewById(R.id.led5);
        led5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CheckBox c = (CheckBox) findViewById(R.id.chkA0);
                if(c4.isChecked()){
                    led5.setImageResource(R.drawable.imagenapagado);
                    c4.setChecked(false);
                }
                else{
                    led5.setImageResource(R.drawable.imagencendido);
                    c4.setChecked(true);
                }


            }
        });

        final ImageView led6 = (ImageView) findViewById(R.id.led6);
        led6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CheckBox c = (CheckBox) findViewById(R.id.chkA0);
                if(c5.isChecked()){
                    led6.setImageResource(R.drawable.imagenapagado);
                    c5.setChecked(false);
                }
                else{
                    led6.setImageResource(R.drawable.imagencendido);
                    c5.setChecked(true);
                }


            }
        });

        final ImageView led7 = (ImageView) findViewById(R.id.led7);
        led7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CheckBox c = (CheckBox) findViewById(R.id.chkA0);
                if(c6.isChecked()){
                    led7.setImageResource(R.drawable.imagenapagado);
                    c6.setChecked(false);
                }
                else{
                    led7.setImageResource(R.drawable.imagencendido);
                    c6.setChecked(true);
                }


            }
        });

        final ImageView led8 = (ImageView) findViewById(R.id.led8);
        led8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CheckBox c = (CheckBox) findViewById(R.id.chkA0);
                if(c7.isChecked()){
                    led8.setImageResource(R.drawable.imagenapagado);
                    c7.setChecked(false);
                }
                else{
                    led8.setImageResource(R.drawable.imagencendido);
                    c7.setChecked(true);
                }


            }
        });








        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                    c1.setEnabled(false);
                    Serial.setEnabled(true);
                    Serial1.setEnabled(false);
                    Serial.setText(Double.toString(dbm.consultaAnalogo0()));
                    Serial1.setText("");
                    //dbm.db.execSQL("UPDATE Tipo SET A0=1;");
                    //dbm.db.close();

                } else {
                    DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                    c1.setEnabled(true);
                    Serial.setEnabled(false);
                    Serial.setText("");
                    Serial1.setEnabled(true);
                    Serial1.setText(Double.toString(dbm.consultaAnalogo1()));
                    c1.setChecked(true);
                    //dbm.db.execSQL("UPDATE Tipo SET A0=0;");
                    //dbm.db.close();
                    //Serial1.setText(Double.toString(dbm.consultaAnalogo1()));
                }
            }
        });

        c1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                    c.setEnabled(false);
                    Serial.setEnabled(false);
                    Serial1.setEnabled(true);
                    Serial1.setText(Double.toString(dbm.consultaAnalogo1()));
                    Serial.setText("");
                    dbm.db.close();
                } else {
                    DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                    c.setEnabled(true);
                    Serial.setEnabled(true);
                    Serial1.setEnabled(false);
                    Serial1.setText("");
                    Serial.setText(Double.toString(dbm.consultaAnalogo0()));
                    c.setChecked(true);
                    dbm.db.close();
                    //Serial.setText(Double.toString(dbm.consultaAnalogo0()));;
                }
            }
        });

        c2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                    //dbm.insertaPin("2", "TRUE");
                    dbm.actualizaEstadoPin("D0", "IN");
                    ImageView img = (ImageView) findViewById(R.id.led3);
                    img.setImageResource(R.drawable.imagencendido);
                    Toast toast1 =
                            Toast.makeText(getApplicationContext(),
                                    "D0 mode INPUT", Toast.LENGTH_SHORT);

                    toast1.show();
                    dbm.db.close();
                } else {
                    DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                    dbm.actualizaEstadoPin("D0", "OU");
                    ImageView img = (ImageView) findViewById(R.id.led3);
                    img.setImageResource(R.drawable.imagenapagado);
                    Toast toast1 =
                            Toast.makeText(getApplicationContext(),
                                    "D0 mode OUTPUT", Toast.LENGTH_SHORT);

                    toast1.show();
                    dbm.db.close();
                }
            }
        });

        c3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                    dbm.actualizaEstadoPin("D1", "IN");
                    ImageView img = (ImageView) findViewById(R.id.led4);
                    img.setImageResource(R.drawable.imagencendido);
                    Toast toast1 =
                            Toast.makeText(getApplicationContext(),
                                    "D1 mode INPUT", Toast.LENGTH_SHORT);

                    toast1.show();
                    dbm.db.close();
                } else {
                    DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                    dbm.actualizaEstadoPin("D1", "OU");
                    ImageView img = (ImageView) findViewById(R.id.led4);
                    img.setImageResource(R.drawable.imagenapagado);
                    Toast toast1 =
                            Toast.makeText(getApplicationContext(),
                                    "D1 mode OUTPUT", Toast.LENGTH_SHORT);

                    toast1.show();
                    dbm.db.close();
                }
            }
        });

        c4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                    dbm.actualizaEstadoPin("D2", "IN");
                    ImageView img = (ImageView) findViewById(R.id.led5);
                    img.setImageResource(R.drawable.imagencendido);
                    Toast toast1 =
                            Toast.makeText(getApplicationContext(),
                                    "D2 mode INPUT", Toast.LENGTH_SHORT);

                    toast1.show();
                    dbm.db.close();
                } else {
                    DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                    dbm.actualizaEstadoPin("D2", "OU");
                    ImageView img = (ImageView) findViewById(R.id.led5);
                    img.setImageResource(R.drawable.imagenapagado);
                    Toast toast1 =
                            Toast.makeText(getApplicationContext(),
                                    "D2 mode OUTPUT", Toast.LENGTH_SHORT);

                    toast1.show();
                    dbm.db.close();
                }
            }
        });

        c5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                    dbm.actualizaEstadoPin("D3", "IN");
                    ImageView img = (ImageView) findViewById(R.id.led6);
                    img.setImageResource(R.drawable.imagencendido);
                    Toast toast1 =
                            Toast.makeText(getApplicationContext(),
                                    "D3 mode INPUT", Toast.LENGTH_SHORT);

                    toast1.show();
                    dbm.db.close();
                } else {
                    DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                    dbm.actualizaEstadoPin("D3", "OU");
                    ImageView img = (ImageView) findViewById(R.id.led6);
                    img.setImageResource(R.drawable.imagenapagado);
                    Toast toast1 =
                            Toast.makeText(getApplicationContext(),
                                    "D3 mode OUTPUT", Toast.LENGTH_SHORT);

                    toast1.show();
                    dbm.db.close();
                }
            }
        });

        c6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                    dbm.actualizaEstadoPin("D4", "IN");
                    ImageView img = (ImageView) findViewById(R.id.led7);
                    img.setImageResource(R.drawable.imagencendido);
                    Toast toast1 =
                            Toast.makeText(getApplicationContext(),
                                    "D4 mode INPUT", Toast.LENGTH_SHORT);

                    toast1.show();
                    dbm.db.close();
                } else {
                    DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                    dbm.actualizaEstadoPin("D4", "OU");
                    ImageView img = (ImageView) findViewById(R.id.led7);
                    img.setImageResource(R.drawable.imagenapagado);
                    Toast toast1 =
                            Toast.makeText(getApplicationContext(),
                                    "D4 mode OUTPUT", Toast.LENGTH_SHORT);

                    toast1.show();
                    dbm.db.close();
                }
            }
        });

        c7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                    dbm.actualizaEstadoPin("D5", "IN");
                    ImageView img = (ImageView) findViewById(R.id.led8);
                    img.setImageResource(R.drawable.imagencendido);
                    Toast toast1 =
                            Toast.makeText(getApplicationContext(),
                                    "D5 mode INPUT", Toast.LENGTH_SHORT);

                    toast1.show();
                    dbm.db.close();
                } else {
                    DataBaseManager dbm = new DataBaseManager(getApplicationContext());
                    dbm.actualizaEstadoPin("D5", "OU");
                    ImageView img = (ImageView) findViewById(R.id.led8);
                    img.setImageResource(R.drawable.imagenapagado);
                    Toast toast1 =
                            Toast.makeText(getApplicationContext(),
                                    "D5 mode OUTPUT", Toast.LENGTH_SHORT);

                    toast1.show();
                    dbm.db.close();
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "configuracion Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.ultron.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "configuracion Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.ultron.myapplication/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
    /*
    public void onCheckboxClicked(View view){

        boolean checked = ((CheckBox)view).isChecked();
        switch(view.getId()) {
            case R.id.chkA0:
                if (checked){
                    // main.enviaDatos(0, true);
                    ImageView img=(ImageView)findViewById(R.id.Led1);
                    img.setImageResource(R.drawable.imagencendido);
                }

                else{

                    //main.enviaDatos(0, false);
                    ImageView img=(ImageView)findViewById(R.id.Led1);
                    img.setImageResource(R.drawable.imagenapagado);
                }

                break;
            case R.id.chkA1:
                if (checked) {
                    //main.enviaDatos(1, true);
                    ImageView img=(ImageView)findViewById(R.id.Led2);
                    img.setImageResource(R.drawable.imagencendido);
                } else {
                    // main.enviaDatos(1, false);
                    ImageView img=(ImageView)findViewById(R.id.Led2);
                    img.setImageResource(R.drawable.imagenapagado);
                }
                break;
            case R.id.chkD1:
                if (checked){
                    //main.enviaDatos(2, true);
                    ImageView img=(ImageView)findViewById(R.id.Led3);
                    img.setImageResource(R.drawable.imagencendido);
                }
                else{
                    // main.enviaDatos(2, false);
                    ImageView img=(ImageView)findViewById(R.id.Led3);
                    img.setImageResource(R.drawable.imagenapagado);
                }
                break;
            case R.id.chkD2:
                if (checked){
                    //main.enviaDatos(3, true);
                    ImageView img=(ImageView)findViewById(R.id.Led4);
                    img.setImageResource(R.drawable.imagencendido);
                }
                else{
                    // main.enviaDatos(3, false);
                    ImageView img=(ImageView)findViewById(R.id.Led4);
                    img.setImageResource(R.drawable.imagenapagado);
                }
                break;
            case R.id.chkD3:
                if (checked){
                    //main.enviaDatos(4, true);
                    ImageView img=(ImageView)findViewById(R.id.Led5);
                    img.setImageResource(R.drawable.imagencendido);
                }
                else{
                    //main.enviaDatos(4, false);
                    ImageView img=(ImageView)findViewById(R.id.Led5);
                    img.setImageResource(R.drawable.imagenapagado);

                }
                break;
            case R.id.chkD4:
                if (checked){
                    main.enviaDatos(5, true);
                    ImageView img=(ImageView)findViewById(R.id.Led6);
                    img.setImageResource(R.drawable.imagencendido);

                }
                else{
                    // main.enviaDatos(5, false);
                    ImageView img=(ImageView)findViewById(R.id.Led6);
                    img.setImageResource(R.drawable.imagenapagado);

                }
                break;
            case R.id.chkD5:
                if (checked){
                    //main.enviaDatos(6, true);
                    ImageView img=(ImageView)findViewById(R.id.Led7);
                    img.setImageResource(R.drawable.imagencendido);
                }
                else{
                    //main.enviaDatos(6, false);
                    ImageView img=(ImageView)findViewById(R.id.Led7);
                    img.setImageResource(R.drawable.imagenapagado);
                }
                break;

        }


    }*/



}
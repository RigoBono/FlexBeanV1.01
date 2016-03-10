package com.example.ultron.myapplication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;

public class Splash extends Activity
{
    boolean bandB=false;

    protected BluetoothAdapter conectadoBluetooth()
    {
        return BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        GifView gifView = (GifView) findViewById(R.id.gif_view);
        gifView.loadGIFResource(R.drawable.loadingazul);
        DataBaseManager dbm=new DataBaseManager(getApplicationContext());
        tiempo();
    }

    public void tiempo()
    {
        new CountDownTimer(5000, 1000)
        {
            public void onTick(long millisUntilFinished) { }

            public void onFinish()
            {
                final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
                int ban=0;
                if(conectadoBluetooth() == null)
                {
                    Snackbar.make(coordinatorLayout, "You have no Bluetooth", Snackbar.LENGTH_LONG).show();
                    finish();
                    System.exit(0);
                    ban = 1;
                }
                //si el dispositivo tiene bluetooth
                if(ban==0)
                {
                    if (conectadoBluetooth().isEnabled())
                    {
                        Snackbar.make(coordinatorLayout, "Bluetooth enabled", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        bandB = true;
                    }
                    else
                    {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Bluetooth Off", Snackbar.LENGTH_LONG).setAction("ENABLED", new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                Intent turnOnIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivityForResult(turnOnIntent, 1);
                            }
                        });
                        snackbar.show();
                    }
                }
                if(!bandB)
                    tiempo();
                else
                {
                    Intent intento = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intento);
                }
            }
        }.start();
    }
}
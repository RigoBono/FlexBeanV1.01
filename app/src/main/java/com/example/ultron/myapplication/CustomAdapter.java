package com.example.ultron.myapplication;

/**
 * Created by ultron on 8/12/15.
 */
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.File;
import java.util.Vector;

public class CustomAdapter extends BaseAdapter{
    Vector<String> result=new Vector<String>();
    Context context;
    LineChart grafica;
    Vector<String> imageId=new Vector<String>();
    private static LayoutInflater inflater=null;
    Context contexto;
    public CustomAdapter(Context context,Historial mainActivity, Vector<String> prgmNameList, Vector<String> prgmImages) {
        // TODO Auto-generated constructor stub
        contexto=context;
        result=prgmNameList;
        context=mainActivity;
        imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public void creaGrafica(){
        grafica=new LineChart(contexto);
        grafica.setDescription("");
        grafica.setNoDataTextDescription("No hay datos por el momento");
        grafica.setNoDataText("");
        grafica.setTouchEnabled(true);
        grafica.setDrawGridBackground(false);
        grafica.setBackgroundColor(Color.WHITE);
        LineData datos=new LineData();
        datos.setValueTextColor(Color.BLACK);
        grafica.setData(datos);
    }

    public class Holder
    {
        TextView tv;
        ImageView img;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.list_row, null);
        holder.tv=(TextView) rowView.findViewById(R.id.list_row_title);
        holder.img=(ImageView) rowView.findViewById(R.id.list_row_image);
        holder.tv.setText(result.elementAt(position));
        //holder.img.setImageResource(imageId[position]);
        File imageFile = new  File(imageId.elementAt(position));
        try{
            if(imageFile.exists()){

                holder.img.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
            }
        }catch(OutOfMemoryError e){

                holder.img.setImageResource(R.drawable.flex);

        }

        return rowView;
    }


}
package com.izv.practicafragmentos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 03/12/2014.
 */
public class Adaptador extends ArrayAdapter<Inmueble> {
    private Context contexto;
    private int recurso;
    private ArrayList<Inmueble> lista;
    private static LayoutInflater i;

    public Adaptador(Context context, int resource, ArrayList<Inmueble> objects) {
        super(context, resource, objects);
        this.contexto = context;
        this.recurso = resource;
        this.lista = objects;
        this.i=(LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public static class ViewHolder{
        public TextView tv1,tv2,tv3,tv4;
        public int posicion;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh=null;
        if(convertView==null){
            convertView=i.inflate(recurso,null);
            vh=new ViewHolder();
            vh.tv1=(TextView)convertView.findViewById(R.id.tvLocalidad);
            vh.tv2=(TextView)convertView.findViewById(R.id.tvLocalizacion);
            vh.tv3=(TextView)convertView.findViewById(R.id.tvTipo);
            vh.tv4=(TextView)convertView.findViewById(R.id.tvPrecio);
            convertView.setTag(vh);

        }else{
            vh=(ViewHolder)convertView.getTag();
        }
        vh.posicion=position;
        vh.tv1.setText(lista.get(position).getLocalidad().toString());
        vh.tv2.setText(lista.get(position).getLocalizacion().toString());
        vh.tv3.setText(lista.get(position).getTipo().toString());
        vh.tv4.setText(lista.get(position).getPrecio()+"");
        return convertView;
    }
}

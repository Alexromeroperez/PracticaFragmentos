package com.izv.practicafragmentos;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentoFotos extends Fragment {

    private ImageView iv;
    private View v;
    private Button btSigue,btAtras;
    private ImageButton ib;
    private ArrayList<Bitmap> listaFotos;

    public FragmentoFotos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.fragment_fragmento_fotos, container, false);
        iv=(ImageView)v.findViewById(R.id.ivFoto);
        ib=(ImageButton)v.findViewById(R.id.btFoto);
        btSigue=(Button)v.findViewById(R.id.btSiguiente);
        btAtras=(Button)v.findViewById(R.id.btAtras);
        return v;
    }
    public void primeraFoto(ArrayList<Bitmap> arrayFotos,int pos){

        iv = (ImageView)v.findViewById(R.id.ivFoto);

        if(arrayFotos.size()==0){
            iv.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
        }else{
            iv.setImageBitmap(arrayFotos.get(pos));
        }
    }
    public ArrayList<Bitmap> insertarFotos(ArrayList<Bitmap> arrayFotos,int posicion,ArrayList<Inmueble> datos,File cfotos){
        String[] archivosCarpetaFotos = cfotos.list();
        arrayFotos=new ArrayList<Bitmap>();
        Inmueble ib=datos.get(posicion);
        String id=ib.getId()+"";
        Bitmap bm;
        for (int i=0;i<archivosCarpetaFotos.length;i++){
            if (archivosCarpetaFotos[i].indexOf("Inmueble_"+id) != -1){
                bm = BitmapFactory.decodeFile(cfotos.getAbsolutePath() + "/" + archivosCarpetaFotos[i]);
                arrayFotos.add(bm);
            }
        }
        return arrayFotos;
    }

    public void fotoSiguiente(ArrayList<Bitmap> arrayFotos,int pos){
        ImageView iv = (ImageView)v.findViewById(R.id.ivFoto);
        iv.setImageBitmap(arrayFotos.get(pos));
    }

}

package com.izv.practicafragmentos;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class Secundaria extends Activity {

    private FragmentoFotos fF;
    private Inmueble inmueble;
    private ImageButton ib;
    private int id=0;
    private int posicion=0;
    private ArrayList<Bitmap> fotos;
    private final int IDACTIVIDADFOTO=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secundaria);
        fF=(FragmentoFotos)getFragmentManager().findFragmentById(R.id.frPortFoto);
        ib=(ImageButton)findViewById(R.id.btFoto);
        id=getIntent().getExtras().getInt("id");
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent ("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(i, IDACTIVIDADFOTO);
            }
        });


        fotos=insertarFotos(fotos);
        Log.v("LISTA FOTOS",fotos.size()+"");
        fF.primeraFoto(fotos, 0);
    }


    @Override
    public void onActivityResult(int pet, int res, Intent data) {
        if (res == RESULT_OK && pet == IDACTIVIDADFOTO) {
            Bitmap foto = (Bitmap) data.getExtras().get("data");
            FileOutputStream salida;
            try {
                salida = new FileOutputStream(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + "/"
                        + "Inmueble_"+id+"_"+ getFecha()+ ".jpg");
                foto.compress(Bitmap.CompressFormat.JPEG, 90, salida);
            } catch (FileNotFoundException e) {
            }
        }
    }
    public ArrayList<Bitmap> insertarFotos(ArrayList<Bitmap> arrayFotos){
        File carpetaFotos = new File(String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES)));
        arrayFotos=new ArrayList<Bitmap>();
        String[] archivosCarpetaFotos = carpetaFotos.list();
        Bitmap bm;

        for (int i=0;i<archivosCarpetaFotos.length;i++){

            if (archivosCarpetaFotos[i].indexOf("Inmueble_"+id) != -1){
                bm = BitmapFactory.decodeFile(carpetaFotos.getAbsolutePath() + "/" + archivosCarpetaFotos[i]);
                arrayFotos.add(bm);
                Log.v("FOTOS",archivosCarpetaFotos[i]+"");
            }
        }
        return arrayFotos;
    }
    private String getFecha(){
        Calendar cal=new GregorianCalendar();
        Date date=cal.getTime();
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String formatteDate=df.format(date);
        return formatteDate;
    }

    public void siguiente(View v){
        final FragmentoFotos fFotos = (FragmentoFotos)getFragmentManager().findFragmentById(R.id.frPortFoto);
        posicion++;
        Log.v("siguiente","boton");
        if(fotos.size()==0){

        }else {
            if (posicion>fotos.size()-1){
                posicion=fotos.size()-1;
                fFotos.fotoSiguiente(fotos,posicion);
            }else{
                fFotos.fotoSiguiente(fotos,posicion);
            }}
    }

    public void anterior(View v){
        final FragmentoFotos ffotos = (FragmentoFotos)getFragmentManager().findFragmentById(R.id.frPortFoto);
        posicion--;
        Log.v("boton","anterior");
        if(fotos.size()==0){

        }else {
            if (posicion<0){
                posicion=0;
                ffotos.fotoSiguiente(fotos,posicion);
            }else{
                ffotos.fotoSiguiente(fotos,posicion);
            }}
    }
}

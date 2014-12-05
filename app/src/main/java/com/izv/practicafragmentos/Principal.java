package com.izv.practicafragmentos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class Principal extends Activity {
    private final int ANADIR=1;
    private ArrayAdapter ad;
    private ArrayList<Inmueble> lista =new ArrayList<Inmueble>();
    private final int ACTIVIDAD2=2;
    private final int FOTO=3;
    private Inmueble casa;
    private int id;
    private int posicion=0;
    private ArrayList<Bitmap> fotos =new ArrayList<Bitmap>();

    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal);
        iniciarComp();
        final FragmentoFotos f2=(FragmentoFotos)getFragmentManager().findFragmentById(R.id.frLandFoto);
        final boolean horizontal=f2!=null && f2.isInLayout();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long l) {
               Inmueble casa = (Inmueble) lv.getItemAtPosition(posicion);
                id=casa.getId();
                if(horizontal){

                    File carpeta=new File(String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES)));
                    fotos=f2.insertarFotos(fotos,posicion,lista,carpeta);
                    f2.primeraFoto(fotos,0);
                }else {
                    Intent intent = new Intent(Principal.this, Secundaria.class);
                    intent.putExtra("id", casa.getId());
                    startActivityForResult(intent, ACTIVIDAD2);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.acction_anadir) {
            Intent i=new Intent(this,Anadir.class);
            startActivityForResult(i, ANADIR);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.contextual,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        if (id == R.id.acction_borrar) {
            borrar(index);
        } else if (id == R.id.acction_modificar) {
            editar(index);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == ANADIR) {
            Bundle b = data.getExtras();
            if (b != null) {
                String tipo = b.getString("tipo");
                String localidad = b.getString("localidad");
                String localizacion = b.getString("localizacion");
                String precio=b.getString("precio");
                casa = new Inmueble();
                idAutomatico(lista,casa);
                casa.setTipo(tipo);
                casa.setLocalidad(localidad);
                casa.setLocalizacion(localizacion);
                casa.setPrecio(Integer.parseInt(precio));
                lista.add(casa);
                crearXml(lista);
            }
            lv.setAdapter(ad);
            registerForContextMenu(lv);
            tostada(R.string.añadido);
        }
        else if (resultCode == Activity.RESULT_CANCELED && requestCode == ANADIR) {
            tostada(R.string.cancelado);
        }else  if (resultCode == RESULT_OK && requestCode == FOTO) {
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

    private String getFecha(){
        Calendar cal=new GregorianCalendar();
        Date date=cal.getTime();
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String formatteDate=df.format(date);
        return formatteDate;
    }

    public void foto(View v){
        Intent i = new Intent ("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(i, FOTO);
    }

    public void siguiente(View v){
        final FragmentoFotos fFotos = (FragmentoFotos)getFragmentManager().findFragmentById(R.id.frLandFoto);
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
        final FragmentoFotos ffotos = (FragmentoFotos)getFragmentManager().findFragmentById(R.id.frLandFoto);
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

    public void crearXml(ArrayList<Inmueble> ai){
        try {
            FileOutputStream fosxml= new FileOutputStream(new File(getExternalFilesDir(null),"listainmuebles.xml"));
            XmlSerializer docxml= Xml.newSerializer();
            docxml.setOutput(fosxml, "UTF-8");
            docxml.startDocument(null, Boolean.valueOf(true));
            docxml.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            docxml.startTag(null, "inmuebles");
            for(int i=0;i<ai.size();i++) {
                docxml.startTag(null, "inmueble");
                docxml.attribute(null, "id", String.valueOf(ai.get(i).getId()));
                docxml.attribute(null, "tipo", ai.get(i).getTipo());
                docxml.attribute(null, "localidad", ai.get(i).getLocalidad());
                docxml.attribute(null, "localizacion", ai.get(i).getLocalizacion());
                docxml.attribute(null, "precio", String.valueOf(ai.get(i).getPrecio()));
                docxml.endTag(null, "inmueble");
            }
            docxml.endTag(null, "inmuebles");
            docxml.endDocument();
            docxml.flush();
            fosxml.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void leerXml(){
        XmlPullParser lectorxml= Xml.newPullParser();
        try {
            lectorxml.setInput(new FileInputStream(new File(getExternalFilesDir(null),"listainmuebles.xml")),"utf-8");
            int evento = lectorxml.getEventType();
            while(evento != XmlPullParser.END_DOCUMENT){
                if(evento == XmlPullParser.START_TAG){
                    String etiqueta = lectorxml.getName();
                    if(etiqueta.compareTo("inmueble")==0){
                        lista.add(new Inmueble(
                                Integer.parseInt(lectorxml.getAttributeValue(null,"id")),
                                lectorxml.getAttributeValue(null, "localidad").toString(),
                                lectorxml.getAttributeValue(null, "localizacion").toString(),
                                lectorxml.getAttributeValue(null, "tipo").toString(),
                                Integer.parseInt(lectorxml.getAttributeValue(null, "precio"))
                        ));
                    }
                }
                evento = lectorxml.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void borrar(final int index){

        AlertDialog.Builder alert= new AlertDialog.Builder(this);
        alert.setTitle("¿Desea borrar el Inmueble?");
        LayoutInflater inflater= LayoutInflater.from(this);
        alert.setPositiveButton("Borrar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        lista.remove(index);
                        ad.notifyDataSetChanged();
                    }
                });
        alert.setNegativeButton(android.R.string.no, null);
        alert.show();
        crearXml(lista);
    }

    public boolean editar(final int index){
        AlertDialog.Builder alert= new AlertDialog.Builder(this);
        alert.setTitle(R.string.titulomodificar);
        LayoutInflater inflater= LayoutInflater.from(this);
        final View vista = inflater.inflate(R.layout.modificar, null);
        final EditText et1,et2,et3;
        final Inmueble uno=new Inmueble();
        final Spinner sp;
        et1 = (EditText) vista.findViewById(R.id.etLocalidad);
        et2 = (EditText) vista.findViewById(R.id.etLocalizacion);
        et3=(EditText)vista.findViewById(R.id.etPrecio);
        sp=(Spinner)vista.findViewById(R.id.spTipo);
        alert.setView(vista);
        ArrayAdapter<CharSequence> spad;
        spad=ArrayAdapter.createFromResource(this,R.array.tipos,android.R.layout.simple_spinner_item);
        spad.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sp.setAdapter(spad);
        et1.setText(lista.get(index).getLocalidad().toString());
        et2.setText(lista.get(index).getLocalizacion().toString());
        et3.setText(String.valueOf(lista.get(index).getPrecio()));
        ponerTipo(lista, sp, index);

        alert.setPositiveButton(R.string.modificar,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        uno.setTipo(sp.getSelectedItem().toString());
                        uno.setLocalidad(et1.getText().toString());
                        uno.setLocalizacion(et2.getText().toString());
                        uno.setPrecio(Integer.parseInt(et3.getText().toString()));
                        if(uno.getLocalidad().equals("")&&uno.getLocalizacion().equals("")){
                            tostada(R.string.nomodificado);
                        }else {
                            lista.set(index, uno);
                            crearXml(lista);
                            ad.notifyDataSetChanged();
                            tostada(R.string.modificado);
                        }

                    }
                });
        alert.setNegativeButton(android.R.string.no, null);
        alert.show();

        return true;
    }

    private void ponerTipo(ArrayList<Inmueble> lista,Spinner sp,int index) {
        String tipo = lista.get(index).getTipo().toString();

        if (tipo.equalsIgnoreCase("Casa")) sp.setSelection(0);
        else if (tipo.equalsIgnoreCase("Piso")) sp.setSelection(1);
        else if (tipo.equalsIgnoreCase("Cochera")) sp.setSelection(2);
    }

    public void iniciarComp(){
        lv=(ListView)findViewById(R.id.lvLista);
        lista=new ArrayList<Inmueble>();
        leerXml();
        ad=new Adaptador(this,R.layout.lista_detalle,lista);
        lv.setAdapter(ad);
        registerForContextMenu(lv);
    }

    private void tostada(int cadena){
        Toast.makeText(this, cadena, Toast.LENGTH_SHORT).show();
    }

    private void idAutomatico(ArrayList<Inmueble> lista, Inmueble ib){
        int id=1;
        if(lista.size()==0){
            ib.setId(id);
        }else if(lista.size()>0){
            int idfinal=1;
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getId() > idfinal){
                    idfinal=lista.get(i).getId();
                }
            }
            ib.setId(idfinal+1);
        }
    }

}

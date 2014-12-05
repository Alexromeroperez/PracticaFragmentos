package com.izv.practicafragmentos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class Anadir extends Activity {

    private EditText et1, et2,et3;
    private Spinner sp;
    private ArrayAdapter<CharSequence> spad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anadir);
        carga();
    }
    public void aceptar(View v){
        Intent i = new Intent();
        Bundle b= new Bundle();
        b.putString("tipo", sp.getSelectedItem().toString());
        b.putString("localidad", et1.getText().toString());
        b.putString("localizacion", et2.getText().toString());
        b.putString("precio", et3.getText().toString());
        i.putExtras(b);
        setResult(Activity.RESULT_OK, i);
        this.finish();
    }
    public void cancelar(View v){
        Intent i=new Intent();
        setResult(Activity.RESULT_CANCELED, i);
        this.finish();
    }

    public void carga(){
        sp= (Spinner) findViewById(R.id.spTipo);
        et1=(EditText) findViewById(R.id.etLocalidad);
        et2=(EditText) findViewById(R.id.etLocalizacion);
        et3=(EditText)findViewById(R.id.etPrecio);
        spad = ArrayAdapter.createFromResource(this, R.array.tipos, android.R.layout.simple_spinner_item);
        spad.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sp.setAdapter(spad);
    }
}

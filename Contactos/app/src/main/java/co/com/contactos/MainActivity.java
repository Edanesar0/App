package co.com.contactos;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import co.com.contactos.util.ConexionLocal;


public class MainActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ConexionLocal conexionLocal = new ConexionLocal(MainActivity.this);
        conexionLocal.abrir();

        ContentValues contentValues = new ContentValues();
        contentValues.put("id","1");
        contentValues.put("nombre","pepito perez");
        contentValues.put("telefono","09876543");
        contentValues.put("ubicación", "1 -1");
        contentValues.put("imagen", "");
        Long aa=conexionLocal.insert("contactos",contentValues);
        contentValues = new ContentValues();
        contentValues.put("id","2");
        contentValues.put("nombre","Juanito silva");
        contentValues.put("telefono","1234567");
        contentValues.put("ubicación", "1 -1");
        contentValues.put("imagen", "");
         aa=conexionLocal.insert("contactos",contentValues);

        conexionLocal.cerrar();
        setContentView(R.layout.menu);
        RelativeLayout r1=(RelativeLayout) findViewById(R.id.rl);
        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Ingreso.class);
                startActivity(i);

            }
        });
        RelativeLayout r12=(RelativeLayout) findViewById(R.id.rl2);
        r12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Lista.class);
                startActivity(i);

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.Eliminar) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}

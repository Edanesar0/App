package co.com.contactos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;

import co.com.contactos.util.ConexionLocal;

/**
 * Created by Axxuss on 06/04/2015.
 */
public class Edit extends ActionBarActivity {
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            EditText editText = (EditText) findViewById(R.id.edtNombre);
            editText.setText(extras.getString("nombre"));
            EditText editText2 = (EditText) findViewById(R.id.edtTelefono);
            editText2.setText(extras.getString("telefono"));
            id = extras.getString("id");
        }
        Button btnGuardar=(Button) findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validacion, validacion2;
                final EditText editText = (EditText) findViewById(R.id.edtNombre);
                if (!validacion(editText.getText().toString())) {
                    validacion = false;
                    editText.setBackgroundResource(R.drawable.borde_error);
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            editText.setBackgroundResource(R.drawable.edittext_rounded_corners);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else {
                    validacion = true;
                }
                final EditText editText2 = (EditText) findViewById(R.id.edtTelefono);
                if (!validacion(editText2.getText().toString())) {
                    validacion2 = false;
                    editText2.setBackgroundResource(R.drawable.borde_error);
                    editText2.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            editText2.setBackgroundResource(R.drawable.edittext_rounded_corners);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else {
                    validacion2 = true;
                }
                if (validacion && validacion2) {
                    try {
                        ConexionLocal conexionLocal = new ConexionLocal(Edit.this);
                        conexionLocal.abrir();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("nombre", editText.getText().toString());
                        contentValues.put("telefono", editText2.getText().toString());

                        Long aa=conexionLocal.update("contactos",contentValues,id);
                        conexionLocal.cerrar();

                        if(aa==1){
                        Toast toast= Toast.makeText(Edit.this,"Contacto Guardado",Toast.LENGTH_LONG);
                        toast.show();
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast toast= Toast.makeText(Edit.this,"Por favor ingrese los datos requridos",Toast.LENGTH_LONG);
                    toast.show();
                }


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.Eliminar:
                AlertDialog.Builder builder;
                final AlertDialog dialog;
                builder = new AlertDialog.Builder(Edit.this);
                builder.setMessage("Desea eliminar el contacto?");

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Toast toast = Toast.makeText(Edit.this, "Contacto Eliminado", Toast.LENGTH_LONG);
                            toast.show();
                            ConexionLocal conexionLocal = new ConexionLocal(Edit.this);
                            conexionLocal.abrir();
                            conexionLocal.clear(id);
                            conexionLocal.cerrar();
                            finish();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).setNegativeButton("Cancelar", null);
                dialog = builder.create();
                dialog.setTitle("Eliminar");
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public boolean validacion(String text) {
        boolean val;
        val = text != null && !text.equals("") && !text.equals(" ");
        return val;
    }

}

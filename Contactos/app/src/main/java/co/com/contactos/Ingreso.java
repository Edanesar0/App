package co.com.contactos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import co.com.contactos.util.ConexionLocal;

/**
 * Created by Axxuss on 05/04/2015.
 */
public class Ingreso extends ActionBarActivity {
    private GoogleMap gMap;
    private MapView mapView;
    EditText edtPosition;
    private String name = "";
    private AlertDialog alertDialog;
    int code;
    Intent data;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }

        name = Environment.getExternalStorageDirectory() + "/test.jpg";
        edtPosition = (EditText) findViewById(R.id.edtUbicacion);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        gMap = mapView.getMap();
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.setMyLocationEnabled(true);
        gMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                edtPosition.setText("Latitude: " + latitude + "\nLongitude: " + longitude);
            }
        });
        final ImageView iv = (ImageView) findViewById(R.id.imageView);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder getImageFrom = new AlertDialog.Builder(Ingreso.this);
                LayoutInflater inflater = Ingreso.this.getLayoutInflater();
                View view = inflater.inflate(R.layout.select, null);
                ImageButton ib = (ImageButton) view.findViewById(R.id.ib);
                ib.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        code = 1;
                        startActivityForResult(intent, code);

                    }
                });
                ImageButton ib2 = (ImageButton) view.findViewById(R.id.ib2);
                ib2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        code = 2;
                        startActivityForResult(intent, code);

                    }
                });
                getImageFrom.setView(view);
                alertDialog = getImageFrom.create();
                alertDialog.show();
                //alertDialog.getWindow().setLayout(400, 200);

            }
        });
        Button button = (Button) findViewById(R.id.btnAgregar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean validacion, validacion2, validacion3, validacion4 = false;
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

                final EditText editText3 = (EditText) findViewById(R.id.edtUbicacion);
                if (!validacion(editText3.getText().toString())) {
                    if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                        buildAlertMessageNoGps();
                    }
                    validacion3 = false;
                    editText3.setBackgroundResource(R.drawable.borde_error);
                    editText3.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            editText3.setBackgroundResource(R.drawable.edittext_rounded_corners);
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                } else {
                    validacion3 = true;
                }
                if (iv.getDrawable() != null) {
                    iv.setBackgroundResource(R.drawable.borde);
                    validacion4=true;


                } else {
                    iv.setBackgroundResource(R.drawable.borde_error);

                }

                if (validacion && validacion2 && validacion3 && validacion4) {
                    try {
                        ConexionLocal conexionLocal = new ConexionLocal(Ingreso.this);
                        conexionLocal.abrir();
                        Bitmap bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                        byte[] bArray = bos.toByteArray();
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("nombre", editText.getText().toString());
                        contentValues.put("telefono", editText2.getText().toString());
                        contentValues.put("ubicación", editText3.getText().toString());
                        contentValues.put("imagen", bArray);
                        Long aa=conexionLocal.insert("contactos",contentValues);

                        conexionLocal.cerrar();
                        editText.setText("");
                        editText2.setText("");
                        editText3.setText("");
                        iv.setImageResource(R.mipmap.ic_launcher);
                        Toast toast= Toast.makeText(Ingreso.this,"Contacto Agregado",Toast.LENGTH_LONG);
                        toast.show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast toast= Toast.makeText(Ingreso.this,"Por favor ingrese los datos requridos",Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });


    }

    public boolean validacion(String text) {
        boolean val;
        val = text != null && !text.equals("") && !text.equals(" ");
        return val;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.data = data;


        if (code == 1) {
            if (data != null) {
                if (data.hasExtra("data")) {
                    ImageView iv = (ImageView) findViewById(R.id.imageView);
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    iv.setImageBitmap(photo);
                    alertDialog.dismiss();
                }

            }
        }
        if (code == 2) {

            try {
                if (data != null) {
                    Uri selectedImage = data.getData();
                    InputStream is;
                    is = getContentResolver().openInputStream(selectedImage);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    Bitmap bitmap = BitmapFactory.decodeStream(bis);
                    ImageView iv = (ImageView) findViewById(R.id.imageView);
                    iv.setImageBitmap(bitmap);
                    alertDialog.dismiss();
                }
            } catch (Exception e) {
                alertDialog.dismiss();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Su GPS parece estar deshabilitado ¿Quiere habilitarlo?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }




}

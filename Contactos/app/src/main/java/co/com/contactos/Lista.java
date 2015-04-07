package co.com.contactos;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import co.com.contactos.util.ConexionLocal;

/**
 * Created by Axxuss on 05/04/2015.
 */
public class Lista extends ActionBarActivity {


    ListView listView;
    MyAdapter myAdapter;

    public class Node {
        public String id;
        public String nombre;
        public String telefono;
        public String imagen;
    }

    public static ArrayList<Node> mArray = new ArrayList<Node>();

    public void setData(String id, String nombre, String telefono) {
        //mArray.clear();
        Node mNode = new Node();
        mNode.id = id;
        mNode.nombre = nombre;
        mNode.telefono = telefono;
        mNode.imagen = "hola";
        mArray.add(mNode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista);
        cargar();
    }
    public void cargar(){
        mArray.clear();
        if (myAdapter != null)
            myAdapter.notifyDataSetChanged();

        ConexionLocal conexionLocal = new ConexionLocal(Lista.this);
        conexionLocal.abrir();
        String sql = "select *" +
                "from contactos";

        final ArrayList alist = new ArrayList();
        Cursor ct = conexionLocal.read(sql);
        //recorre y agrega
        for (ct.moveToFirst(); !ct.isAfterLast(); ct.moveToNext()) {

            setData(ct.getString(0), ct.getString(1), ct.getString(2));

        }

        conexionLocal.cerrar();
        Log.e("", "" + alist);


        listView = (ListView) findViewById(R.id.listView);
        myAdapter = new MyAdapter(this);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView3 = (TextView) view.findViewById(R.id.id);
                Intent i = new Intent(Lista.this, Edit.class);

                ConexionLocal conexionLocal = new ConexionLocal(Lista.this);
                conexionLocal.abrir();
                String sql = "select *" +
                        "from contactos where id=" + textView3.getText();

                final ArrayList alist = new ArrayList();
                Cursor ct = conexionLocal.read(sql);
                //recorre y agrega
                for (ct.moveToFirst(); !ct.isAfterLast(); ct.moveToNext()) {
                    i.putExtra("id", textView3.getText());
                    i.putExtra("nombre", ct.getString(1));
                    i.putExtra("telefono", ct.getString(2));

                }

                conexionLocal.cerrar();

                startActivity(i);

                Log.e("", "" + textView3.getText());
            }
        });


    }

    public static class MyAdapter extends BaseAdapter {
        private Context mContext;

        public MyAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public int getCount() {
            return mArray.size();
        }

        @Override
        public Object getItem(int position) {
            return mArray.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.contacto, null);
            } else {
                view = convertView;
            }
            TextView textView = (TextView) view.findViewById(R.id.Nombre);
            textView.setText(mArray.get(position).nombre);
            TextView textView2 = (TextView) view.findViewById(R.id.telefono);
            textView2.setText(mArray.get(position).telefono);
            TextView textView3 = (TextView) view.findViewById(R.id.id);
            textView3.setText(mArray.get(position).id);
            return view;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (myAdapter != null) myAdapter.notifyDataSetChanged();
        cargar();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (myAdapter != null) myAdapter.notifyDataSetChanged();
        cargar();
    }
}

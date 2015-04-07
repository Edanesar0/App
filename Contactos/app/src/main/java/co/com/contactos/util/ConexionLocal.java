package co.com.contactos.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class ConexionLocal {

    public static final String N_BD = "prueba";
    public static final int VERSION_BD = 1;
    private BDhelper nHelper;
    private final Context nContexto;
    private SQLiteDatabase nBD;



    private static class BDhelper extends SQLiteOpenHelper {

        public BDhelper(Context context) {
            super(context, N_BD, null, VERSION_BD);

        }


        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE IF NOT EXISTS `contactos` (" +
                    "  `id` INTEGER PRIMARY KEY," +
                    "  `nombre` VARCHAR(150) NULL," +
                    "  `telefono` VARCHAR(150) NULL," +
                    "  `ubicación` VARCHAR(150) NULL," +
                    "  `imagen` BLOB NULL" +
                    "  );");


        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);


        }


        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS contactos");
            onCreate(db);


        }
    }

    public ConexionLocal(Context c) {
        nContexto = c;
    }

    public ConexionLocal abrir() {
        nHelper = new BDhelper(nContexto);
        nBD = nHelper.getWritableDatabase();//ecribir en la base de datos
        return this;
    }


    public void cerrar() {
        nHelper.close();
    }


    public long insert(String tabla, ContentValues cv) {

        return nBD.insertWithOnConflict(tabla, null, cv,SQLiteDatabase.CONFLICT_REPLACE);
    }
    public long update(String tabla, ContentValues cv,String id) {

        return nBD.updateWithOnConflict(tabla,cv,"id = "+id,null,SQLiteDatabase.CONFLICT_REPLACE);
    }



    public Cursor read(String sql) {
        Cursor c = nBD.rawQuery(sql, null);

        return c;
    }
    public void clear(String user) {
        try {

            nBD.delete("contactos", "id ="+user, null);


        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

    }
    public void clearAll() {
        try {

            nBD.delete("contactos", null, null);


        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

    }


}

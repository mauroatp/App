package isp.com.nooranv1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;

import static isp.com.nooranv1.Constantes.NOMBRE_TABLA;

/**
 * Created by Mauro on 12/11/2017.
 */

public class BaseDatosMenu extends SQLiteOpenHelper {
    public static final String NOMBRE_BASE = "menu.db";
    public static final int VERSION_BASE = 1;

    public BaseDatosMenu(Context ctx){
        super(ctx, NOMBRE_BASE, null, VERSION_BASE);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
/*
        db.execSQL("CREATE TABLE " + NOMBRE_TABLA + " ( " + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NOMBRE + " CHAR, "

                + DESCRIPCION + " CHAR, "  + PRECIO + " CHAR, " + LATITUD + " CHAR, "

                + LONGITUD + " CHAR );");
                */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
        onCreate(db);
    }
}

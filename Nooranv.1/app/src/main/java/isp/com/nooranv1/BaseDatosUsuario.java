package isp.com.nooranv1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static android.provider.BaseColumns._ID;
import static isp.com.nooranv1.Constantes.NOMBRE_TABLA_USUARIO;
import static isp.com.nooranv1.Constantes.NOMBRE_USUARIO;
import static isp.com.nooranv1.Constantes.EMAIL_USUARIO;
import static isp.com.nooranv1.Constantes.FOTO_USUARIO;

/**
 * Created by Mauro on 12/11/2017.
 */

public class BaseDatosUsuario extends SQLiteOpenHelper {
    public static final String NOMBRE_BASE = "menu.db";
    public static final int VERSION_BASE = 1;

    public BaseDatosUsuario(Context ctx){
        super(ctx, NOMBRE_BASE, null, VERSION_BASE);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + NOMBRE_TABLA_USUARIO + " ( " + _ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NOMBRE_USUARIO + " CHAR, "
                + EMAIL_USUARIO + " CHAR, "  + FOTO_USUARIO + " CHAR);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA_USUARIO);
        onCreate(db);
    }
}

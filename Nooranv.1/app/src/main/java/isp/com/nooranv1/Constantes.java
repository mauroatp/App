package isp.com.nooranv1;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Mauro on 12/11/2017.
 */

public interface Constantes extends BaseColumns{

    //USUARIO
    public  static final String NOMBRE_TABLA_USUARIO = "usuario";
    public  static final String NOMBRE_USUARIO = "nombre";
    public  static final String EMAIL_USUARIO = "email";
    public  static final String FOTO_USUARIO = "foto";

    //MENU
    public  static final String NOMBRE_TABLA = "menu";
    public  static final String NOMBRE_MENU = "nombre";
    public  static final String DESCRIPCION_MENU = "descripcion";
    public  static final String FOTO_MENU = "foto";
    public  static final String LATITUD_MENU = "latitud";
    public  static final String LONGITUD_MENU = "longitud";
    public  static final String INGREDIENTES_MENU = "ingredientes";


    public  static final String AUTORIDAD = "dispmoviles.com.menu";
    public static final Uri CONTENT_URI =   Uri.parse("content://" + AUTORIDAD + "/" + NOMBRE_TABLA );
}

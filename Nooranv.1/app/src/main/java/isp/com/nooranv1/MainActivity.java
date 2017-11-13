package isp.com.nooranv1;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import static android.provider.BaseColumns._ID;
import static isp.com.nooranv1.Constantes.EMAIL_USUARIO;
import static isp.com.nooranv1.Constantes.FOTO_USUARIO;
import static isp.com.nooranv1.Constantes.NOMBRE_TABLA_USUARIO;
import static isp.com.nooranv1.Constantes.NOMBRE_USUARIO;

public class MainActivity extends AppCompatActivity {

    public Usuario usuario;
    private static String[] FROM = {_ID, NOMBRE_USUARIO, EMAIL_USUARIO, FOTO_USUARIO};
    //private static String ORDER_BY = NOMBRE_USUARIO + " DESC";
    private BaseDatosUsuario events;
    //Usuario usuario;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                   transaction.replace(R.id.content, new Home()).commit();
                    return true;
                case R.id.navigation_dashboard:
                    transaction.replace(R.id.content, new Favoritos()).commit();
                    return true;
                case R.id.navigation_notifications:
                    transaction.replace(R.id.content, new Perfil()).commit();

                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide(); //<< esconde la barra del titulo
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();

        events = new BaseDatosUsuario(this);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content, new Home()).commit();

       // String nom = getSharedPreferences(LoginActivity.usu ) //intent.getStringExtra("Nombre");
       // String email = intent.getStringExtra("Email");
       // String foto = intent.getStringExtra("Foto");
         usuario = obtenerUsuario();
        //llenarCamposPerfil(usuario);

    }

    private void llenarCamposPerfil(Usuario u) {

        TextView text = (TextView) findViewById(R.id.txtNombrePerfil);
        text.setText(u.nombre);
        TextView text1 = (TextView) findViewById(R.id.txtEmailPerfil);
        text1.setText(u.email);
    }


    private Usuario obtenerUsuario() {
        SQLiteDatabase db = events.getReadableDatabase();
        Cursor cursor = db.query(NOMBRE_TABLA_USUARIO, FROM, null, null, null, null, null);
        startManagingCursor(cursor);
       //Usuario[] usu = new Usuario[cursor.getCount()];
        Usuario u = new Usuario();
        int i = 0;

        while (cursor.moveToNext()) {
            u = new Usuario();
            u.nombre = cursor.getString(1);
            u.email = cursor.getString(2);
            u.foto = cursor.getString(3);

            //usu[i] = u;
            i++;
        }
        return u;
    }


}

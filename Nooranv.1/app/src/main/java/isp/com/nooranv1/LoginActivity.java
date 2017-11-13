package isp.com.nooranv1;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.provider.BaseColumns._ID;
import static isp.com.nooranv1.Constantes.EMAIL_USUARIO;
import static isp.com.nooranv1.Constantes.FOTO_USUARIO;
import static isp.com.nooranv1.Constantes.NOMBRE_TABLA_USUARIO;
import static isp.com.nooranv1.Constantes.NOMBRE_USUARIO;

/**
 * Created by Mauro on 4/11/2017.
 */

public class LoginActivity extends ActionBarActivity implements View.OnClickListener {

    private EditText email;
    private EditText password;
    private SharedPreferences usu;
    private boolean conect;
    public Usuario usuario;
    private static String[] FROM = {_ID, NOMBRE_USUARIO, EMAIL_USUARIO, FOTO_USUARIO};
    private static String ORDER_BY = NOMBRE_USUARIO + " DESC";
    private BaseDatosUsuario events;
    //private static int[] TO = {R.id.itemNombre, R.id.itemDescripcion, R.id.itemPrecio};
    String verDB ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        events = new BaseDatosUsuario(this);

        usu = getApplicationContext().getSharedPreferences("usuario", Context.MODE_ENABLE_WRITE_AHEAD_LOGGING);

        String login = usu.getString("usuario", "");

        if (!login.equals("")) {
            Intent e = new Intent(this, MainActivity.class);
            startActivity(e);
        } else {

            email = (EditText) findViewById(R.id.txtEmailLogin);
            password = (EditText) findViewById(R.id.txtPasswordLogin);
            Button v = (Button) this.findViewById(R.id.btn_login);
            v.setOnClickListener(this);
            v = (Button) this.findViewById(R.id.btn_registro);
            v.setOnClickListener(this);
        }

    }

    public String getEmail() {
        return this.email.getText().toString();
    }

    public String getPassword() {
        return this.password.getText().toString();
    }

    @Override
    public void onClick(View v) {

        if (conectado()) {
            switch (v.getId()) {
                case R.id.btn_registro:
                    Intent i = new Intent(this, RegistroActivity.class);
                    startActivity(i);
                    break;
                case R.id.btn_login:
                    ingresar();
                    break;
            }
        } else {
            Toast.makeText(getApplicationContext(), "Error, no hay conexion", Toast.LENGTH_SHORT).show();
        }
    }

    private void ingresar() {
        String n = this.getEmail();
        String a = this.getPassword();

        if (conectado()) {
            if (!n.equals("") || !a.equals("")) {
                //new GetDataTask(this).execute("http://10.0.2.2/user/login/"+n);
                new GetDataTask(this).execute("http://192.168.0.107:3000/user/login/" + n);
            } else {
                Toast.makeText(getApplicationContext(), "email/password requeridos",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Error no hay conexion",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private class GetDataTask extends AsyncTask<String, Void, String> {

        private LoginActivity m_activity;

        public GetDataTask(LoginActivity activity) {
            m_activity = activity;
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                String valor = reader.readLine();
                reader.close();
                con.disconnect();

                JSONArray desc = new JSONArray(valor);
                //JSONObject respuesta = new JSONObject(valor);

                //JSONArray desc = respuesta.getJSONArray("id");

                String email = null;
                String password = null;
                String nombre = null;
                String foto = null;

                if (desc != null && desc.length() > 0) {
                    email = desc.getJSONObject(0).getString("mail");
                    password = desc.getJSONObject(0).getString("clave");
                    nombre = desc.getJSONObject(0).getString("nombre");
                    String p = this.m_activity.getPassword();

                    AESCrypt s = new AESCrypt();
                    p = s.encryptIt(p);

                    if (p.equals(password)) {
                        if (email != null && email != "") {
                            SharedPreferences.Editor editor = usu.edit();
                            editor.putString("usuario", email);
                            editor.putString("nombre", nombre);
                            editor.commit();
                            String a = usu.getString("usuario", "");
                            usuario = new Usuario(nombre, email, "", password);

                            agregarUsuario(usuario);
                        } else {
                            email = null;
                        }
                    } else {
                        email = "no";
                    }
                }
                return email;
            } catch (Exception ex) {
                Log.e("Error", ex.getMessage());
                return null;
            }
        }

        private void agregarUsuario(Usuario U) {
            SQLiteDatabase db = events.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(NOMBRE_USUARIO, U.getNombre());
            values.put(FOTO_USUARIO, U.getFoto());
            values.put(EMAIL_USUARIO, U.getEmail());


            db.insertOrThrow(NOMBRE_TABLA_USUARIO, null, values);
        }

        protected void onPostExecute(String result) {
            if (result != null && result != "no") {
                if (m_activity != null) {
                    //SharedPreferences prefe=getSharedPreferences(result, 0);
                    Intent e = new Intent(this.m_activity, MainActivity.class);
                    /*e.putExtra("Nombre", usuario.getNombre());
                    e.putExtra("Email", usuario.getEmail());
                    e.putExtra("Password", usuario.getPassword());
                    e.putExtra("Foto", usuario.getFoto());*/
                    startActivity(e);
                }
            } else if (result == "no") {
                Toast.makeText(getApplicationContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Usuario no registrado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean conectado() {

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;

        return connected;
    }

}



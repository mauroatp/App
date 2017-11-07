package isp.com.nooranv1;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

/**
 * Created by Mauro on 4/11/2017.
 */

public class LoginActivity extends ActionBarActivity implements View.OnClickListener{

    private EditText email;
    private EditText password;
    private SharedPreferences usu;
    private boolean conect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        usu = getApplicationContext().getSharedPreferences("usuario", Context.MODE_PRIVATE);

        String login = usu.getString("usuario", "");

        if (!login.equals("")) {
            Intent e = new Intent(this, MainActivity.class);
            startActivity(e);
        } else{

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

        if(conectado()) {
            switch (v.getId()) {
                case R.id.btn_registro:
                    Intent i = new Intent(this, RegistroActivity.class);
                    startActivity(i);
                    break;
                case R.id.btn_login:
                    ingresar();
                    break;
            }
        }else{
            Toast.makeText(getApplicationContext(), "Error, no hay conexion", Toast.LENGTH_SHORT).show();
        }
    }

    private void ingresar() {
        String n = this.getEmail();
        String a = this.getPassword();

        if(conectado()) {
            if (!n.equals("") || !a.equals("")) {
                //new GetDataTask(this).execute("http://10.0.2.2/user/login/"+n);
                new GetDataTask(this).execute("http://172.29.1.17/user/login/getall");
            } else {
                Toast.makeText(getApplicationContext(), "email/password requeridos",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
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
                JSONObject respuesta = new JSONObject(valor);
                JSONArray desc = respuesta.getJSONArray("description");
                int arraysize = desc.length();
                String email = null;
                String password = null;
                String em = null;
                String pass = null;
                for (int i = 0; i < arraysize; i++) {

                    email = desc.getJSONObject(i).getString("email");
                    password = desc.getJSONObject(i).getString("clave");
                    em = this.m_activity.getEmail();
                    pass = this.m_activity.getPassword();

                    if (email.equals(this.m_activity.getEmail()) && password.equals(this.m_activity.getPassword())) {

                        SharedPreferences.Editor editor = usu.edit();
                        editor.putString("usuario", email);
                        editor.commit();
                        String a = usu.getString("usuario", "");
                        return email;

                    }
                }
                return null;
            } catch (Exception ex) {
                Log.e("Error", ex.getMessage());

                return null;
            }
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                if (m_activity != null) {
                    //SharedPreferences prefe=getSharedPreferences(result, 0);
                    Intent e = new Intent(this.m_activity, MainActivity.class);
                    startActivity(e);
                }
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



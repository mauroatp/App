package isp.com.nooranv1;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Mauro on 4/11/2017.
 */

public class RegistroActivity extends Activity implements View.OnClickListener {

    public Usuario usuario;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

       Button v = (Button) this.findViewById(R.id.btnRegistrarse);
        v.setOnClickListener(this);
        //v = (Button) this.findViewById(R.id.btn_login);
        //v.setOnClickListener(this);
    }

    public void Registrarse(){

        EditText eT1 = (EditText)findViewById(R.id.nombreRegistro);
        String sNombre = eT1.getText().toString();


        EditText eT3 = (EditText)findViewById(R.id.emailRegistro);
        String sEmail = eT3.getText().toString();

        EditText eT4 = (EditText)findViewById(R.id.passwordRegistro);
        String sPassword = eT4.getText().toString();

        AESCrypt s = new AESCrypt();
        sPassword = s.encryptIt(sPassword);
        //sPassword = encryption(sPassword);

        Map<String, String> postData = new HashMap<>();
        postData.put("nombre", sNombre);
        postData.put("mail", sEmail);
        postData.put("clave", sPassword);


        HttpPostAsyncTask task = new HttpPostAsyncTask(postData);
        task.execute("http://192.168.0.107:3000/user/create");

    }
/*
    public String encryption(String strNormalText){
        String seedValue = "YourSecKey";
        String normalTextEnc="";
        try {
            normalTextEnc = Encriptar.encrypt(seedValue, strNormalText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return normalTextEnc;
    }
*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_registro:
                Intent i = new Intent(this, RegistroActivity.class);
                startActivity(i);
                break;
            case R.id.btnRegistrarse:
                Registrarse();
                break;
        }
    }

    private class HttpPostAsyncTask extends AsyncTask<String, Void, String> {

        JSONObject postData;

        public HttpPostAsyncTask(Map<String, String> postData){
            if (postData != null) {
                this.postData = new JSONObject(postData);
            }
        }

        String convertInputStreamToString(java.io.InputStream is) {
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }

        protected String doInBackground(String... params){
            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");

                if (this.postData != null) {
                    OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                    writer.write(postData.toString());
                    writer.flush();
                }

                int statusCode = urlConnection.getResponseCode();

                if (statusCode ==  200) {

                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    String response = convertInputStreamToString(inputStream);
                    return "ok";
                }
                else
                {
                    InputStream inputStream = new BufferedInputStream(urlConnection.getErrorStream());
                    String response = convertInputStreamToString(inputStream);
                    Log.e("Error",response);
                    if(statusCode == 400) {
                        return  "409";
                    }
                    return null;
                }

            }
            catch (Exception ex)
            {
                Log.e("Error", ex.getMessage());
                return null;
            }
        }

        protected void onPostExecute(String result) {

            if (result != null && result != "409") {
                Intent e = new Intent(RegistroActivity.this, MainActivity.class);

                e.putExtra("Nombre", usuario.getNombre());
                e.putExtra("Email", usuario.getEmail());
                e.putExtra("Password", usuario.getPassword());
                e.putExtra("Foto", usuario.getFoto());

                startActivity(e);
                Toast.makeText(getApplicationContext(), "Ok",
                        Toast.LENGTH_SHORT).show();

            }
            else if(result.equals("409"))
            {
                Toast.makeText(getApplicationContext(), "Error!!! email ya registrado",
                        Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Error!!! al conectar",
                        Toast.LENGTH_SHORT).show();

            }
        }
    }
}

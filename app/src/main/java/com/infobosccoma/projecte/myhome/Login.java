package com.infobosccoma.projecte.myhome;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.infobosccoma.projecte.myhome.Controller.UsuariSessio;
import com.infobosccoma.projecte.myhome.Model.ListFlatsActivity;
import com.infobosccoma.projecte.myhome.Model.users;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class Login extends ActionBarActivity implements View.OnClickListener {

    private static final String URL_DATA = "http://52.16.108.57/scripts/users.php";

    private DescarregarDades download;

    UsuariSessio sessioUsuari;

    TextView txtUser, txtPassword;
    Button btnRegistrar;

    private ArrayList<users> dades;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessioUsuari = new UsuariSessio(getApplicationContext());

        txtUser = (TextView)findViewById(R.id.txtUsuari);
        txtPassword = (TextView)findViewById(R.id.txtPassword);

        btnRegistrar = (Button)findViewById(R.id.btnRegistra);
        btnRegistrar.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        new DescarregarDades(txtUser.getText().toString(),txtPassword.getText().toString()).execute();
    }


    class DescarregarDades extends AsyncTask<String,Void,ArrayList<users>>{

        private String usuari;
        private String contrasenya;

        DescarregarDades(String usuari, String contrasenya){
            this.usuari = usuari;
            this.contrasenya = contrasenya;
        }


        @Override
        protected void onPreExecute(){super.onPreExecute();}

        @Override
        protected ArrayList<users> doInBackground(String... params) {
            ArrayList<users> llistaUsers = null;
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpostreq = new HttpPost(URL_DATA);
            HttpResponse httpresponse = null;
            try{
                List<NameValuePair> parametres = new ArrayList<NameValuePair>(3);
                parametres.add(new BasicNameValuePair("peticio","inserir"));
                parametres.add(new BasicNameValuePair("nameUsers",usuari));
                parametres.add(new BasicNameValuePair("passwordUser",contrasenya));
                httpostreq.setEntity(new UrlEncodedFormEntity(parametres));
                httpresponse = httpClient.execute(httpostreq);
                String responseText = EntityUtils.toString(httpresponse.getEntity());
                //llistaUsers = tractarJSON(responseText);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return llistaUsers;
        }

        @Override
        protected void onPostExecute(ArrayList<users> llista){
            sessioUsuari.createUserLoginSession(
                    usuari,contrasenya
            );

            Intent act = new Intent(getApplicationContext(), ListFlatsActivity.class);
            act.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);



            //Add new Flag to start new Activity
            act.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(act);
            finish();

        }

        private ArrayList<users> tractarJSON(String json){
            Gson convert = new Gson();
            return convert.fromJson(json,new TypeToken<ArrayList<users>>(){}.getType());
        }
            }
    public void err_login() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }



}



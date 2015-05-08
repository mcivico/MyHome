package com.infobosccoma.projecte.myhome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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

    TextView txtUser, txtPassword;
    Button btnRegistrar;
    private DescarregarDades download;
    private ArrayList<users> dades;
    private static final String URL_DATA = "http://52.16.108.57/scripts/users.php";
    String usuari, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUser = (TextView)findViewById(R.id.txtUsuari);
        txtPassword = (TextView)findViewById(R.id.txtPassword);

        btnRegistrar = (Button)findViewById(R.id.btnLogin);
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
        usuari = txtUser.getText().toString();
        password = txtPassword.getText().toString();
        download = new DescarregarDades();
        try{
            download.execute(URL_DATA);
        }catch(IllegalStateException ex) {
    }

    }

    private void comprovaSessio(){
        SharedPreferences prefs = this.getSharedPreferences("userdata",this.MODE_PRIVATE);
        String usuariString = prefs.getString("userName",null);
        String passwordString = prefs.getString("password",null);
        if(usuariString!=null)entra(usuariString,passwordString);
    }

    private void entra(String usuari, String password){
        Intent i = new Intent(this,ListFlats_Activity.class);
        startActivity(i);
        finish();
    }

    class DescarregarDades extends AsyncTask<String,Void,ArrayList<users>>{



        @Override
        protected void onPreExecute(){super.onPreExecute();}



        @Override
        protected ArrayList<users> doInBackground(String... params) {
            ArrayList<users> llistaUsers = null;
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpostreq = new HttpPost(URL_DATA);
            HttpResponse httpresponse = null;
            String usuari = txtUser.getText().toString();
            String password = txtPassword.getText().toString();
            try{
                List<NameValuePair> parametres = new ArrayList<NameValuePair>(1);
                parametres.add(new BasicNameValuePair("peticio","select"));
                //parametres.add(new BasicNameValuePair("userName",usuari));
                //parametres.add(new BasicNameValuePair("password",password));
                httpostreq.setEntity(new UrlEncodedFormEntity(parametres));
                httpresponse = httpClient.execute(httpostreq);
                String responseText = EntityUtils.toString(httpresponse.getEntity());
                llistaUsers = tractarJSON(responseText);

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
            dades = llista;

        }

        private ArrayList<users> tractarJSON(String json){
            Gson convert = new Gson();
            return convert.fromJson(json,new TypeToken<ArrayList<users>>(){}.getType());
        }
    }



}



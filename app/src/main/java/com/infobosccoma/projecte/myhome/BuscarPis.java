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
import android.widget.Toast;

import com.infobosccoma.projecte.myhome.Controller.UsuariSessio;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class BuscarPis extends ActionBarActivity implements View.OnClickListener {

    private static final String URL_DATA = "http://52.16.108.57/scripts/flat.php";

    private DescarregarDades download;
    private InsertaDades insertaDades;

    private TextView txtNom, txtPassword;
    private Button btnSearch;

    private UsuariSessio sessioUsuari;
    String nomUsuari;
    HashMap<String,String> usuari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_pis);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtNom = (TextView)findViewById(R.id.txtFlat);
        txtPassword = (TextView)findViewById(R.id.txtFlatPass);
        btnSearch = (Button)findViewById(R.id.btnSearchFlat);
        btnSearch.setOnClickListener(this);

        sessioUsuari = new UsuariSessio(getApplicationContext());
        usuari = sessioUsuari.getUserDetails();
        Iterator it = usuari.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry e = (Map.Entry) it.next();
            if(e.getKey().equals("name")&& e.getValue()!= null){
                nomUsuari = e.getValue().toString();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_buscar_pis, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_settings:
                return true;
            case R.id.home:
                finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(!txtNom.getText().toString().equals("") && !txtPassword.getText().toString().equals(""))
            new DescarregarDades(txtNom.getText().toString(),txtPassword.getText().toString()).execute();
        else{
            err_login();
            Toast.makeText(BuscarPis.this, "Els camps no poden estar buits", Toast.LENGTH_LONG).show();
        }
    }


    class DescarregarDades extends AsyncTask<String,Void,Boolean> {

        private String usuari;
        private String contrasenya;

        Boolean resultat = false;
        int codiEstat =0;

        DescarregarDades(String usuari, String contrasenya){
            this.usuari = usuari;
            this.contrasenya = contrasenya;
        }


        @Override
        protected void onPreExecute(){super.onPreExecute();}

        @Override
        protected Boolean doInBackground(String... params) {
            //ArrayList<users> llistaUsers = null;
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpostreq = new HttpPost(URL_DATA);
            HttpResponse httpresponse = null;
            try{
                List<NameValuePair> parametres = new ArrayList<NameValuePair>(3);
                parametres.add(new BasicNameValuePair("peticio","valida"));
                parametres.add(new BasicNameValuePair("nameFlat",usuari));
                parametres.add(new BasicNameValuePair("passwordFlat",contrasenya));
                httpostreq.setEntity(new UrlEncodedFormEntity(parametres));
                httpresponse = httpClient.execute(httpostreq);
                resultat = comprovaAcces(httpresponse.getEntity().getContent());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return resultat;
        }

        @Override
        protected void onPostExecute(Boolean resultat){
            if(resultat){
                new InsertaDades(nomUsuari,txtNom.getText().toString(),txtPassword.getText().toString()).execute();
                            }
            else{
                err_login();
                Toast.makeText(BuscarPis.this, "Pis Incorrecte!", Toast.LENGTH_LONG).show();
                //txtUser.requestFocus();
                //txtUser.setText(null);
                //txtPassword.setText(null);
            }


        }
        private boolean comprovaAcces(InputStream is) {
            String rLine = "";
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            boolean retorn = false;

            try {
                while ((rLine = rd.readLine()) != null) {
                    if (rLine.substring(14, 22).equals("correcte")) {
                        retorn = true;
                    } else {
                        retorn = false;
                    }
                }


            }

            catch (IOException e) {
                // e.printStackTrace();
                retorn = false;
            }

            return retorn;

        }



    }


    public void err_login() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    class InsertaDades extends AsyncTask<String,Void,Boolean> {

        private String usuari, password;
        private String pis;

        Boolean resultat = false;
        int codiEstat =0;

        InsertaDades(String usuari, String pis, String passw){
            this.usuari = usuari;
            this.pis = pis;
            this.password = passw;
        }


        @Override
        protected void onPreExecute(){super.onPreExecute();}

        @Override
        protected Boolean doInBackground(String... params) {
            //ArrayList<users> llistaUsers = null;
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpostreq = new HttpPost(URL_DATA);
            HttpResponse httpresponse = null;
            try{
                List<NameValuePair> parametres = new ArrayList<NameValuePair>(3);
                parametres.add(new BasicNameValuePair("peticio","inserirFlatUser"));
                parametres.add(new BasicNameValuePair("nameFlat",pis));
                parametres.add(new BasicNameValuePair("passwordFlat",password));
                parametres.add(new BasicNameValuePair("nameUserFlat",usuari));
                httpostreq.setEntity(new UrlEncodedFormEntity(parametres));
                httpresponse = httpClient.execute(httpostreq);
                resultat = comprovaAcces(httpresponse.getEntity().getContent());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return resultat;
        }

        @Override
        protected void onPostExecute(Boolean resultat){
            if(resultat){
                Toast.makeText(BuscarPis.this, "Pis Correcte!!", Toast.LENGTH_LONG).show();
                Intent act = new Intent(getApplicationContext(), ListFlatsActivity.class);
                act.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //Add new Flag to start new Activity
                act.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(act);
                finish();

            }
            else{
                err_login();
                Toast.makeText(BuscarPis.this, "Pis erroni!!", Toast.LENGTH_LONG).show();
                //txtUser.requestFocus();
                //txtUser.setText(null);
                //txtPassword.setText(null);
            }
        }
        private boolean comprovaAcces(InputStream is) {
            String rLine = "";
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            boolean retorn = false;
            try {
                while ((rLine = rd.readLine()) != null) {
                    if (rLine.substring(14, 22).equals("correcte")) {
                        retorn = true;
                    } else {
                        retorn = false;
                    }
                }
            }
            catch (IOException e) {
                // e.printStackTrace();
                retorn = false;
            }
            return retorn;
        }



    }








}

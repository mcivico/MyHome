package com.infobosccoma.projecte.myhome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.infobosccoma.projecte.myhome.Controller.UsuariSessio;
import com.infobosccoma.projecte.myhome.Model.users;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
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
import java.util.List;


public class Login extends Activity implements View.OnClickListener {

    private static final String URL_DATA = "http://52.16.108.57/scripts/users.php";

    private DescarregarDades download;

    UsuariSessio sessioUsuari;

    TextView txtUser, txtPassword;
    ImageButton btnRegistrar, btnLogin;

    private ArrayList<users> dades;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessioUsuari = new UsuariSessio(getApplicationContext());

        txtUser = (TextView)findViewById(R.id.txtUsuari);
        txtPassword = (TextView)findViewById(R.id.txtPassword);

        btnRegistrar = (ImageButton)findViewById(R.id.crearCompte);
        btnRegistrar.setOnClickListener(this);

        btnLogin = (ImageButton)findViewById(R.id.Login);
        btnLogin.setOnClickListener(this);
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.crearCompte:
                String m = txtUser.getText().toString();
                if(!txtUser.getText().toString().equals("") && !txtPassword.getText().toString().equals(""))
                    new DescarregarDades(txtUser.getText().toString(),txtPassword.getText().toString()).execute();
                else{
                    err_login();
                    Toast.makeText(Login.this, "Els camps no poden estar buits", Toast.LENGTH_LONG).show();
                }

                break;
            case R.id.Login:
                if(!txtUser.getText().toString().equals("") && !txtPassword.getText().toString().equals(""))
                    new ValidaLogin(txtUser.getText().toString(),txtPassword.getText().toString()).execute();
                else{
                    err_login();
                    Toast.makeText(Login.this, "Els camps no poden estar buits", Toast.LENGTH_LONG).show();
                }

                break;


        }

    }


    class DescarregarDades extends AsyncTask<String,Void,Boolean>{

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
            else{
                err_login();
                Toast.makeText(Login.this, "Usuari no vàlid!", Toast.LENGTH_LONG).show();
                txtUser.requestFocus();
                txtUser.setText(null);
                txtPassword.setText(null);
            }


        }

        private boolean comprovaAcces(InputStream is) {
            String rLine = "";
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            boolean retorn = false;

            try {
                while ((rLine = rd.readLine()) != null) {
                    if (rLine.equals("true")) {
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

    class ValidaLogin extends AsyncTask<String,Void,Boolean>{

        private String usuari;
        private String contrasenya;

        Boolean resultat = false;
        int codiEstat =0;

        ValidaLogin(String usuari, String contrasenya){
            this.usuari = usuari;
            this.contrasenya = contrasenya;
        }


        @Override
        protected void onPreExecute(){super.onPreExecute();}

        @Override
        protected Boolean doInBackground(String... params) {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpostreq = new HttpPost(URL_DATA);
            HttpResponse httpresponse = null;
            try{
                List<NameValuePair> parametres = new ArrayList<NameValuePair>(3);
                parametres.add(new BasicNameValuePair("peticio","valida"));
                parametres.add(new BasicNameValuePair("nameUsers",usuari));
                parametres.add(new BasicNameValuePair("passwordUser",contrasenya));
                httpostreq.setEntity(new UrlEncodedFormEntity(parametres));
                httpresponse = httpClient.execute(httpostreq);
                resultat = comprovaAcces(httpresponse.getEntity().getContent());

                StatusLine estat = httpresponse.getStatusLine();
                codiEstat = estat.getStatusCode();

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
            else{
                err_login();
                Toast.makeText(Login.this, "Usuari o contrasenya incorrectes!!", Toast.LENGTH_LONG).show();
                txtUser.requestFocus();
                txtUser.setText(null);
                txtPassword.setText(null);
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



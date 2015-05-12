package com.infobosccoma.projecte.myhome;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.infobosccoma.projecte.myhome.Controller.UsuariSessio;
import com.infobosccoma.projecte.myhome.Model.flat;
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


public class NewFlatActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String URL_DATA = "http://52.16.108.57/scripts/users.php";

    private DescarregarDades download;

    private UsuariSessio sessioUsuari;

    TextView txtNomPis, txtPasswdPis;
    Button btnNewFlat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_flat);
        sessioUsuari = new UsuariSessio(getApplicationContext());

        txtNomPis = (TextView)findViewById(R.id.txtNameFlat);
        txtPasswdPis = (TextView)findViewById(R.id.txtPasswdFlat);

        btnNewFlat = (Button)findViewById(R.id.btnNewFlat);
        btnNewFlat.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_flat, menu);
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
            case R.id.menu_logout:
                AlertDialog dialog = new AlertDialog.Builder(this).create();
                dialog.setTitle("Log Out!!");
                dialog.setMessage("Estas segur de fer el Log Out?");
                dialog.setButton("Ok", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sessioUsuari.logoutUser();
                        Intent login = new Intent(getApplicationContext(),Login.class);
                        startActivity(login);
                    }
                });
                //dialog.setIcon();
                dialog.show();
        }




        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        new DescarregarDades(txtNomPis.getText().toString(),txtPasswdPis.getText().toString()).execute();
    }

    class DescarregarDades extends AsyncTask<String,Void,Boolean> {

        private String nomPis;
        private String passwd;

        Boolean resultat = false;

        DescarregarDades(String usuari, String contrasenya){
            this.nomPis = usuari;
            this.passwd = contrasenya;
        }


        @Override
        protected void onPreExecute(){super.onPreExecute();}

        @Override
        protected Boolean doInBackground(String... params) {
            ArrayList<flat> llistaFlat = null;
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpostreq = new HttpPost(URL_DATA);
            HttpResponse httpresponse = null;
            try{
                List<NameValuePair> parametres = new ArrayList<NameValuePair>(3);
                parametres.add(new BasicNameValuePair("peticio","inserir"));
                parametres.add(new BasicNameValuePair("nameFlat",nomPis));
                parametres.add(new BasicNameValuePair("passwordFlat",passwd));
                httpostreq.setEntity(new UrlEncodedFormEntity(parametres));
                httpresponse = httpClient.execute(httpostreq);
                String responseText = EntityUtils.toString(httpresponse.getEntity());
                //llistaUsers = tractarJSON(responseText);

                //TODO  no funciona
                resultat = true;

                //resultat = comprovaAcces(httpresponse.getEntity().getContent());



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
            if(resultat == true){


                Intent act = new Intent(getApplicationContext(), ListFlatsActivity.class);
                act.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);



                //Add new Flag to start new Activity
                act.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(act);
                finish();
            }
            else{
                Toast.makeText(NewFlatActivity.this, "No s'ha pogut crear el pis!", Toast.LENGTH_LONG).show();
                txtNomPis.requestFocus();
                txtNomPis.setText(null);
                txtPasswdPis.setText(null);
            }


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

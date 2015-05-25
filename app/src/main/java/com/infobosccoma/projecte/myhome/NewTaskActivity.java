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

import com.infobosccoma.projecte.myhome.Controller.FlatSessio;

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

public class NewTaskActivity extends ActionBarActivity implements View.OnClickListener {

    private static final String URL_DATA = "http://52.16.108.57/scripts/tasks.php";

    private FlatSessio flatSessio;
    String nomPis;
    HashMap<String, String> pis;

    TextView txtNomTasca;
    Button btnNewTasca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        flatSessio = new FlatSessio(getApplicationContext());
        pis = flatSessio.getUserDetails();
        Iterator it = pis.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry e = (Map.Entry)it.next();
            if(e.getKey().equals("name"))
                nomPis = e.getValue().toString();
        }

        txtNomTasca = (TextView) findViewById(R.id.txtNameTask);

        btnNewTasca = (Button)findViewById(R.id.btnNewTask);
        btnNewTasca.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_task, menu);
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
        new DescarregarDades(txtNomTasca.getText().toString()).execute();
    }

    class DescarregarDades extends AsyncTask<String,Void,Boolean> {

        private String nomTasca;

        Boolean resultat = false;

        DescarregarDades(String nomTasca){
            this.nomTasca = nomTasca;

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
                parametres.add(new BasicNameValuePair("peticio","inserir"));
                parametres.add(new BasicNameValuePair("nameTask",nomTasca));
                parametres.add(new BasicNameValuePair("nameFlat",nomPis));


                httpostreq.setEntity(new UrlEncodedFormEntity(parametres));
                httpresponse = httpClient.execute(httpostreq);
                resultat = comprovaAcces(httpresponse.getEntity().getContent());
                String a ="";

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
                Toast.makeText(NewTaskActivity.this, "Tasca Afegit!!", Toast.LENGTH_SHORT).show();
                Intent act = new Intent(getApplicationContext(), TasquesActivity.class);
                act.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                act.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(act);
                finish();
            }
            else{
                Toast.makeText(NewTaskActivity.this, "No s'ha pogut afegir la tasca!", Toast.LENGTH_SHORT).show();
                err_login();
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
}

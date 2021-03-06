package com.infobosccoma.projecte.myhome;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.infobosccoma.projecte.myhome.Controller.AdapterTask;
import com.infobosccoma.projecte.myhome.Controller.FlatSessio;
import com.infobosccoma.projecte.myhome.Controller.UsuariSessio;
import com.infobosccoma.projecte.myhome.Model.Tasks;
import com.infobosccoma.projecte.myhome.Model.userName;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

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
import java.util.Random;


public class TasquesActivity extends ActionBarActivity {

    private static final String URL_DATA = "http://52.16.108.57/scripts/tasks.php";
    private static final String URL_USERS = "http://52.16.108.57/scripts/selectUsers.php";

    private ListView listViewTasks;
    private ArrayList<Tasks> dades;
    private AdapterTask adapter;

    private ArrayList<userName> usuaris;
    private ArrayList<userName> usuarisAux;

    private TextView tasca, encarregat;
    private ProgressBar progresBar;

    private FlatSessio flatSessio;
    private String nomPis;

    private UsuariSessio usuariSessio;
    private String nomUsuari;

    HashMap<String, String> pis;
    HashMap<String, String> usuari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasques);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tasca = (TextView)findViewById(R.id.txtTitolTask);
        encarregat = (TextView)findViewById(R.id.txtTitolEncarregat);
        progresBar = (ProgressBar)findViewById(R.id.pbTasques);
        progresBar.setVisibility(View.GONE);

        flatSessio = new FlatSessio(getApplicationContext());
        pis = flatSessio.getUserDetails();
        Iterator it = pis.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            if (e.getKey().equals("name")&& e.getValue() != null)
                nomPis = e.getValue().toString();
        }

        usuariSessio = new UsuariSessio(getApplicationContext());
        usuari = usuariSessio.getUserDetails();
        Iterator at = usuari.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) at.next();
            if (e.getKey().equals("name")&& e.getValue() != null)
                 nomUsuari = e.getValue().toString();
        }


        dades = new ArrayList<Tasks>();
        listViewTasks = (ListView) findViewById(R.id.listViewTask);
        refreshData();


        new DescarregarDades().execute();
        new UsuarisPis().execute();

    }

    private void updateTaskUser(){
        usuarisAux = (ArrayList)usuaris.clone();
        progresBar.setVisibility(View.VISIBLE);
        Random rand = new Random();
        for(int i = 0;i<dades.size();i++){
            if(usuaris.size()<1){
               usuaris = (ArrayList)usuarisAux.clone();
            }
            int aleatori = rand.nextInt(usuaris.size());
            new UpdateTasks(dades.get(i).getIdTask()+"",usuaris.get(aleatori).getNameUser()).execute();
            usuaris.remove(aleatori);
        }
        usuaris = (ArrayList)usuarisAux.clone();
        progresBar.setVisibility(View.GONE);
    }

    private void refreshData(){
        adapter = new AdapterTask(this,dades);
        listViewTasks.setAdapter(adapter);
        listViewTasks.setVisibility(View.VISIBLE);

        listViewTasks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> p, View v, final int po, long id) {
                String idTaska = dades.get(po).getIdTask()+"";
                new EliminarTaska(idTaska).execute();

                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tasques, menu);
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == 1)&& resultCode == RESULT_OK) {
            /*flat pis;
            pis = (flat) data.getExtras().getSerializable("nouArticle");
            //segConv.save(article);
            //refreshData();*/
            new DescarregarDades().execute();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_settings:
                return true;
            case R.id.menu_addTask:
                Intent newTask = new Intent(this, NewTaskActivity.class);
                startActivityForResult(new Intent(getBaseContext(),NewTaskActivity.class),1);
                break;
            case R.id.menu_assignarTasques:
                updateTaskUser();
                break;
            case R.id.menu_logout:
                AlertDialog dialog = new AlertDialog.Builder(this).create();
                dialog.setTitle("Log Out!!");
                dialog.setMessage("Estas segur de fer el Log Out?");
                dialog.setButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        usuariSessio.logoutUser();
                        Intent login = new Intent(getApplicationContext(), Login.class);
                        startActivity(login);
                    }
                });
                //dialog.setIcon();
                dialog.show();
                break;
            case R.id.home:
                finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    class DescarregarDades extends AsyncTask<String,Void,ArrayList<Tasks>> {

        Boolean resultat;

        @Override
        protected void onPreExecute(){super.onPreExecute();}

        @Override
        protected ArrayList<Tasks> doInBackground(String... params) {
            ArrayList<Tasks> listTasques = null;
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpostreq = new HttpPost(URL_DATA);
            HttpResponse httpresponse = null;
            try{
                List<NameValuePair> parametres = new ArrayList<NameValuePair>(3);
                parametres.add(new BasicNameValuePair("peticio","valida"));
                parametres.add(new BasicNameValuePair("nameFlat",nomPis));
                httpostreq.setEntity(new UrlEncodedFormEntity(parametres));
                httpresponse = httpClient.execute(httpostreq);
                String responseText = EntityUtils.toString(httpresponse.getEntity());
                listTasques = tractarJSON(responseText);
                //resultat = comprovaAcces(httpresponse.getEntity().getContent());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return listTasques;
        }

        @Override
        protected void onPostExecute(ArrayList<Tasks> llista){

            dades = llista;
            refreshData();
        }

        private ArrayList<Tasks> tractarJSON(String json) {
            Gson convert = new Gson();
            return convert.fromJson(json, new TypeToken<ArrayList<Tasks>>() {
            }.getType());
        }

    }

    class UsuarisPis extends AsyncTask<String,Void,ArrayList<userName>> {

        Boolean resultat;

        @Override
        protected void onPreExecute(){super.onPreExecute();}

        @Override
        protected ArrayList<userName> doInBackground(String... params) {
            ArrayList<userName> llistaUsuaris = null;
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpostreq = new HttpPost(URL_USERS);
            HttpResponse httpresponse = null;
            try{
                List<NameValuePair> parametres = new ArrayList<NameValuePair>(3);
                parametres.add(new BasicNameValuePair("peticio","select"));
                parametres.add(new BasicNameValuePair("nameFlat",nomPis));
                httpostreq.setEntity(new UrlEncodedFormEntity(parametres));
                httpresponse = httpClient.execute(httpostreq);
                String responseText = EntityUtils.toString(httpresponse.getEntity());
                llistaUsuaris = tractarJSON(responseText);
                String m = "";
                //resultat = comprovaAcces(httpresponse.getEntity().getContent());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return llistaUsuaris;
        }

        @Override
        protected void onPostExecute(ArrayList<userName> llista){

            usuaris = llista;

        }

        private ArrayList<userName> tractarJSON(String json) {
            Gson convert = new Gson();
            return convert.fromJson(json, new TypeToken<ArrayList<userName>>() {
            }.getType());
        }

}

    class UpdateTasks extends AsyncTask<String,Void,Boolean> {

        Boolean resultat;
        String idTask, usuari;

        UpdateTasks(String idTask, String usuari){
            this.idTask = idTask;
            this.usuari = usuari;
        }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpostreq = new HttpPost(URL_DATA);
            HttpResponse httpresponse = null;
            try{
                List<NameValuePair> parametres = new ArrayList<NameValuePair>(3);
                parametres.add(new BasicNameValuePair("peticio","inserirUser"));
                parametres.add(new BasicNameValuePair("nomUserTask",usuari));
                parametres.add(new BasicNameValuePair("idTasks",idTask));
                httpostreq.setEntity(new UrlEncodedFormEntity(parametres));
                httpresponse = httpClient.execute(httpostreq);
                String responseText = EntityUtils.toString(httpresponse.getEntity());
//                resultat = comprovaAcces(httpresponse.getEntity().getContent());
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
            new DescarregarDades().execute();
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

    class EliminarTaska extends AsyncTask<String, Void, Boolean> {

        String idTask;
        Boolean resultat;

        EliminarTaska(String idTask){
            this.idTask = idTask;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }



        @Override
        protected Boolean doInBackground(String... params) {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpostreq = new HttpPost(URL_DATA);
            HttpResponse httpresponse = null;
            try{
                List<NameValuePair> parametres = new ArrayList<NameValuePair>(2);
                parametres.add(new BasicNameValuePair("peticio","delete"));
                parametres.add(new BasicNameValuePair("idTasks",idTask));
                httpostreq.setEntity(new UrlEncodedFormEntity(parametres));
                httpresponse = httpClient.execute(httpostreq);
                resultat = comprovaAcces(httpresponse.getEntity().getContent());

                String m = "";
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
        protected void onPostExecute(Boolean resultat) {
            if(resultat){
                Toast.makeText(TasquesActivity.this, "S'ha borrat la tasca", Toast.LENGTH_SHORT).show();
                new DescarregarDades().execute();
            }
            else{
                Toast.makeText(TasquesActivity.this, "No s'ha pogut borrar la tasca!", Toast.LENGTH_SHORT).show();
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

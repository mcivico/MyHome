package com.infobosccoma.projecte.myhome;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.infobosccoma.projecte.myhome.Controller.UsuariSessio;
import com.infobosccoma.projecte.myhome.Model.flat;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ListFlatsActivity extends ActionBarActivity {


    private static final String URL_DATA = "http://52.16.108.57/scripts/flat_user.php";

    private DescarregarDades download;

    private ListView listViewPisos;
    private ArrayList<flat> llistaPisos;

    private UsuariSessio sessioUsuari;

    String nomUsuari;

    HashMap<String, String> usuari;

    final static int ADD_PIS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_flats);
        sessioUsuari = new UsuariSessio(getApplicationContext());
        usuari = sessioUsuari.getUserDetails();
        Iterator it = usuari.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            if (e.getKey().equals("name")&& e.getValue()!=null)
                nomUsuari = e.getValue().toString();
        }
        if (!sessioUsuari.checkLogin()) {
            finish();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_flats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.menu_addFlat:
                Intent nouPis = new Intent(this, NewFlatActivity.class);
                startActivityForResult(new Intent(getBaseContext(), NewFlatActivity.class), ADD_PIS);
                return true;
            case R.id.menu_logout:
                AlertDialog dialog = new AlertDialog.Builder(this).create();
                dialog.setTitle("Log Out!!");
                dialog.setMessage("Estas segur de fer el Log Out?");
                dialog.setButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sessioUsuari.logoutUser();
                        Intent login = new Intent(getApplicationContext(), activity_map.class);
                        startActivity(login);
                    }
                });
                //dialog.setIcon();
                dialog.show();

        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_PIS && resultCode == RESULT_OK) {
            flat pis;
            pis = (flat) data.getExtras().getSerializable("nouArticle");
            //segConv.save(article);
            //refreshData();
        }
    }


    class DescarregarDades extends AsyncTask<String, Void, ArrayList<flat>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<flat> doInBackground(String... params) {
            ArrayList<flat> llista = null;
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpreq = new HttpPost(URL_DATA);
            HttpResponse httpresponse = null;
            try {
                List<NameValuePair> parametres = new ArrayList<NameValuePair>(1);
                parametres.add(new BasicNameValuePair("peticio", "validar"));
                parametres.add(new BasicNameValuePair("nameUsers",nomUsuari));
                httpreq.setEntity(new UrlEncodedFormEntity(parametres));
                httpresponse = httpClient.execute(httpreq);
                String responseText = EntityUtils.toString(httpreq.getEntity());
                llista = tractarJSON(responseText);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return llista;
        }

        @Override
        protected void onPostExecute(ArrayList<flat> llista) {
            llistaPisos = llista;
        }

        private ArrayList<flat> tractarJSON(String json) {
            Gson convert = new Gson();
            return convert.fromJson(json, new TypeToken<ArrayList<flat>>() {
            }.getType());
        }
    }
}

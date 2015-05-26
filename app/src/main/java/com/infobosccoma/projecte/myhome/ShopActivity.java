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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.infobosccoma.projecte.myhome.Controller.AdapterShop;
import com.infobosccoma.projecte.myhome.Controller.FlatSessio;
import com.infobosccoma.projecte.myhome.Controller.UsuariSessio;
import com.infobosccoma.projecte.myhome.Model.shoppingcard;

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


public class ShopActivity extends ActionBarActivity {

    private static final String URL_DATA = "http://52.16.108.57/scripts/shopping.php";

    private ListView listViewShop;
    private ArrayList<shoppingcard> dades;
    private AdapterShop adapter;
    private UsuariSessio sessioUsuari;

    private FlatSessio flatSessio;
    private String nomPis;

    private TextView producte,quantitat;

    HashMap<String, String> pis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        producte = (TextView)findViewById(R.id.txtTitolShop);
        quantitat = (TextView)findViewById(R.id.txtTitolQuantitat);

        sessioUsuari = new UsuariSessio(getApplicationContext());

        flatSessio = new FlatSessio(getApplicationContext());
        pis = flatSessio.getUserDetails();
        Iterator it = pis.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry e = (Map.Entry) it.next();
            if (e.getKey().equals("name")&& e.getValue() != null)
                nomPis = e.getValue().toString();
        }

        dades = new ArrayList<shoppingcard>();
        listViewShop = (ListView) findViewById(R.id.listViewShop);
        refreshData();

        new DescarregarDades().execute();



    }

    private void refreshData(){
        adapter = new AdapterShop(this, dades);
        listViewShop.setAdapter(adapter);
        listViewShop.setVisibility(View.VISIBLE);

        listViewShop.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> p, View v, final int po, long id) {
                String idShop = dades.get(po).getIdShoppingCard()+"";
                new EliminarPis(idShop).execute();

                return true;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shop, menu);
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
            case R.id.menu_addShop:
                Intent nouShop = new Intent(this, NewShopActivity.class);
                startActivityForResult(new Intent(getBaseContext(),NewShopActivity.class),1);
                break;
            case R.id.menu_logout:
                AlertDialog dialog = new AlertDialog.Builder(this).create();
                dialog.setTitle("Log Out!!");
                dialog.setMessage("Estas segur de fer el Log Out?");
                dialog.setButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sessioUsuari.logoutUser();
                        Intent login = new Intent(getApplicationContext(), Login.class);
                        startActivity(login);
                    }
                });
                //dialog.setIcon();
                dialog.show();
                break;
            case R.id.home:
                finish();
                break;

        }

        return super.onOptionsItemSelected(item);
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


    class DescarregarDades extends AsyncTask<String, Void, ArrayList<shoppingcard>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<shoppingcard> doInBackground(String... params) {
            ArrayList<shoppingcard> llistaShop = null;
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpostreq = new HttpPost(URL_DATA);
            HttpResponse httpresponse = null;
            try{
                List<NameValuePair> parametres = new ArrayList<NameValuePair>(2);
                parametres.add(new BasicNameValuePair("peticio","valida"));
                parametres.add(new BasicNameValuePair("nameFlat",nomPis));

                httpostreq.setEntity(new UrlEncodedFormEntity(parametres));
                httpresponse = httpClient.execute(httpostreq);
                String responseText = EntityUtils.toString(httpresponse.getEntity());
                llistaShop=tractarJSON(responseText);

                String m = "";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return llistaShop;
        }

        @Override
        protected void onPostExecute(ArrayList<shoppingcard> llista) {
            dades = llista;

            refreshData();
        }

        private ArrayList<shoppingcard> tractarJSON(String json) {
            Gson convert = new Gson();
            return convert.fromJson(json, new TypeToken<ArrayList<shoppingcard>>() {
            }.getType());
        }
    }

    class EliminarPis extends AsyncTask<String, Void, Boolean> {

        String idShop;
        Boolean resultat;

        EliminarPis(String idShop){
            this.idShop = idShop;
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
                parametres.add(new BasicNameValuePair("idShoppingCard",idShop));
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
                Toast.makeText(ShopActivity.this, "S'ha borrat el producte", Toast.LENGTH_SHORT).show();
                new DescarregarDades().execute();
            }
            else{
                Toast.makeText(ShopActivity.this, "No s'ha pogut borrar el producte!", Toast.LENGTH_SHORT).show();
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

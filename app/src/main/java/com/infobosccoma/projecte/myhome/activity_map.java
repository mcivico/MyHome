package com.infobosccoma.projecte.myhome;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.infobosccoma.projecte.myhome.Model.Pois;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class activity_map extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static final String URL_DATA = "http://52.16.108.57/scripts/pois.php";

    private ArrayList<Pois> dades;
    private DescarregarDades download;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_map);
        setUpMapIfNeeded();
        new DescarregarDades().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    class DescarregarDades extends AsyncTask<String, Void, ArrayList<Pois>> {


        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }


        @Override
        protected ArrayList<Pois> doInBackground(String... params) {
            ArrayList<Pois> llistaPois = null;
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httppostreq = new HttpPost(URL_DATA);
            HttpResponse httpresponse = null;

            try{
                    List<NameValuePair> parametres = new ArrayList<NameValuePair>(1);
                    parametres.add(new BasicNameValuePair("peticio", "valida"));
                    httppostreq.setEntity(new UrlEncodedFormEntity(parametres));
                    httpresponse = httpClient.execute(httppostreq);
                    String responseText = EntityUtils.toString(httpresponse.getEntity());
                    llistaPois = tractarJSON(responseText);


            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return llistaPois;
        }

        @Override
        protected void onPostExecute(ArrayList<Pois> llista) {
            dades = llista;


                LatLng augment = new LatLng(dades.get(0).getLatitude(),dades.get(0).getLongitude());
                LatLngBounds.Builder builder = new LatLngBounds.Builder();

                for (int i = 0; i < dades.size(); i++) {
                    Pois poi = dades.get(i);
                    double lat = poi.getLatitude();
                    double lng = poi.getLongitude();
                    LatLng posicio = new LatLng(lat, lng);
                    mMap.addMarker(new MarkerOptions()
                            .position(posicio)
                            .snippet(poi.getCity())
                            .title(poi.getName()));
                    builder.include(posicio);
                }

                //LatLngBounds bounds = new LatLngBounds();
                LatLngBounds tmpBounds = builder.build();
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(tmpBounds, 200));
            }


        }

        private ArrayList<Pois> tractarJSON(String json) {
            Gson converter = new Gson();
            return converter.fromJson(json, new TypeToken<ArrayList<Pois>>(){}.getType());
        }
    }

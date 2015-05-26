package com.infobosccoma.projecte.myhome;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.infobosccoma.projecte.myhome.Controller.UsuariSessio;


public class MainMenuActivity extends ActionBarActivity implements View.OnClickListener {

    private UsuariSessio sessioUsuari;

    ImageButton btnCompra, btnTasques, btnMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessioUsuari = new UsuariSessio(getApplicationContext());

        btnTasques = (ImageButton)findViewById(R.id.imgTasques);
        btnTasques.setOnClickListener(this);

        btnCompra = (ImageButton)findViewById(R.id.imgCompra);
        btnCompra.setOnClickListener(this);

        btnMapa = (ImageButton)findViewById(R.id.imgMapa);
        btnMapa.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
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
            case R.id.home:
                finish();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgTasques:
                Intent tasca = new Intent(getApplicationContext(),TasquesActivity.class);
                startActivity(tasca);
                break;
            case R.id.imgCompra:
                Intent compra = new Intent(getApplicationContext(),ShopActivity.class);
                startActivity(compra);
                break;
            case R.id.imgMapa:
                Intent mapa = new Intent(getApplicationContext(),activity_map.class);
                startActivity(mapa);
                break;

        }
    }
}

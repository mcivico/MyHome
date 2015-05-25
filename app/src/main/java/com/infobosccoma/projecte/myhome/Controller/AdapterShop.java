package com.infobosccoma.projecte.myhome.Controller;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.infobosccoma.projecte.myhome.Model.Vista;
import com.infobosccoma.projecte.myhome.Model.shoppingcard;
import com.infobosccoma.projecte.myhome.R;

import java.util.List;

/**
 * Created by Marc on 22/05/2015.
 */
public class AdapterShop extends ArrayAdapter {

    private Activity context;
    private List<shoppingcard> dades;

    public AdapterShop(Activity context, List<shoppingcard> dades){
        super(context, R.layout.listitem_shop,dades);
        this.context = context;
        this.dades = dades;
    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent){
        View element = convertView;
        Vista vista;

        if(element == null){
            LayoutInflater inflater = context.getLayoutInflater();
            element = inflater.inflate(R.layout.listitem_shop,null);

            vista = new Vista();
            vista.lblShop = (TextView) element.findViewById(R.id.txtNomTasca);
            vista.quantity = (TextView)element.findViewById(R.id.txtQuantitat);

            element.setTag(vista);

        } else{
            vista = (Vista) element.getTag();
        }

        vista.lblShop.setText(dades.get(position).getProductName());
        vista.quantity.setText(dades.get(position).getQuantity()+"");


        return element;

    }




}

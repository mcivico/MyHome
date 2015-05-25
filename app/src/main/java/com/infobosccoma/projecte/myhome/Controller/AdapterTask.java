package com.infobosccoma.projecte.myhome.Controller;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.infobosccoma.projecte.myhome.Model.Tasks;
import com.infobosccoma.projecte.myhome.Model.VistaTasks;
import com.infobosccoma.projecte.myhome.R;

import java.util.List;

/**
 * Created by Marc on 19/05/2015.
 */
public class AdapterTask extends ArrayAdapter{

    private Activity context;
    private List<Tasks> dades;

    public AdapterTask(Activity context, List<Tasks> dades){
        super(context,R.layout.listitem_tasques,dades);
        this.context = context;
        this.dades = dades;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View element = convertView;
        VistaTasks vista;

        if(element == null){
            LayoutInflater inflater = context.getLayoutInflater();
            element = inflater.inflate(R.layout.listitem_tasques,null);

            vista = new VistaTasks();
            vista.lblNameTask = (TextView) element.findViewById(R.id.txtNomTasca);
            vista.lblNameUser = (TextView) element.findViewById(R.id.txtUsuari);

            element.setTag(vista);
        } else{
            vista = (VistaTasks) element.getTag();
        }

        vista.lblNameTask.setText(dades.get(position).getNameTask());
        vista.lblNameUser.setText(dades.get(position).getNomUserTask());

        return element;
    }




}

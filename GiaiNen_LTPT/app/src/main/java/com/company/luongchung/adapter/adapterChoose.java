package com.company.luongchung.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.company.luongchung.Iface.Iupdatecheck;
import com.company.luongchung.giainen_ltpt.MainActivity;
import com.company.luongchung.giainen_ltpt.R;
import com.company.luongchung.model.O_file_choose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luongchung on 9/22/17.
 */

public class adapterChoose extends ArrayAdapter<O_file_choose> {
    Iupdatecheck iupdatecheck;
    Activity context;
    int resource;
    ArrayList<O_file_choose> objects;
    public adapterChoose( Activity context, int resource,ArrayList<O_file_choose> objects,Iupdatecheck iupdatecheck) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
        this.iupdatecheck=iupdatecheck;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(this.resource,null);
        TextView textView = (TextView) view.findViewById(R.id.txtURL);
        Button btnChon =(Button) view.findViewById(R.id.btnChon);
        CheckBox radioChon =(CheckBox) view.findViewById(R.id.radioCheck);
        O_file_choose file_choose =objects.get(position);
        textView.setText(file_choose.getNameFile());
        if (file_choose.isChoose()) {
            radioChon.setChecked(true);
            radioChon.setText(position+"");
        }
        else {
            radioChon.setChecked(false);
            radioChon.setText(position+"");
        }
        btnChon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        radioChon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iupdatecheck.updatecheckbox(position);
            }
        });
        return view;
    }
}

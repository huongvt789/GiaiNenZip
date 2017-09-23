package com.company.luongchung.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.company.luongchung.Iface.Iclick;
import com.company.luongchung.Iface.Iupdatecheck;
import com.company.luongchung.giainen_ltpt.MainActivity;
import com.company.luongchung.giainen_ltpt.R;
import com.company.luongchung.model.O_file_choose;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ar.com.daidalos.afiledialog.FileChooserActivity;
import ar.com.daidalos.afiledialog.FileChooserDialog;

/**
 * Created by luongchung on 9/22/17.
 */

public class adapterChoose extends ArrayAdapter<O_file_choose> {
    private int CODE_SENT=1996;
    Iupdatecheck iupdatecheck;
    Iclick iclick;
    Activity context;
    int resource;
    ArrayList<O_file_choose> objects;
    public adapterChoose( Activity context, int resource,ArrayList<O_file_choose> objects,Iupdatecheck iupdatecheck,Iclick iclick) {
        super(context, resource, objects);
        this.context=context;
        this.resource=resource;
        this.objects=objects;
        this.iupdatecheck=iupdatecheck;
        this.iclick=iclick;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(this.resource,null);
        TextView textView = (TextView) view.findViewById(R.id.txtURL);
        Button btnChon =(Button) view.findViewById(R.id.btnChon);
        CheckBox radioChon =(CheckBox) view.findViewById(R.id.radioCheck);
        O_file_choose file_choose =objects.get(position);




        if(!file_choose.getNameFile().isEmpty())
        textView.setText(file_choose.getNameFile());
        else textView.setText("Chưa được chọn");




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
                iclick.getid(position);
                Intent intent = new Intent(getContext(), FileChooserActivity.class);
                intent.putExtra(FileChooserActivity.INPUT_REGEX_FILTER, ".*zip|.*rar");
                // lọc đuôi
                intent.putExtra(FileChooserActivity.INPUT_START_FOLDER, Environment.getExternalStorageDirectory());
                // chọn đường dẫn files
                context.startActivityForResult(intent,CODE_SENT);
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

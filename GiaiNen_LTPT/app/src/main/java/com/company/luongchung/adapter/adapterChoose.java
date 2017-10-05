package com.company.luongchung.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import android.support.v4.content.ContextCompat;
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
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
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
        {
            String ten=file_choose.getNameFile();
            if(ten.length()>17)
            ten =ten.substring(ten.length()-17,ten.length());

            textView.setText("..."+ten);
        }
        else textView.setText("Chưa được chọn");




        if (file_choose.isChoose()) {
            radioChon.setChecked(true);
            radioChon.setText((position+1)+"");
        }
        else {
            radioChon.setChecked(false);
            radioChon.setText((position+1)+"");
        }
        btnChon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkAndRequestPermissions()) {
                    iclick.getid(position);
                    Intent intent = new Intent(getContext(), FileChooserActivity.class);
                    intent.putExtra(FileChooserActivity.INPUT_REGEX_FILTER, ".*zip|.*rar");
                    // lọc đuôi
                    intent.putExtra(FileChooserActivity.INPUT_START_FOLDER, Environment.getExternalStorageDirectory());
                    // chọn đường dẫn files
                    context.startActivityForResult(intent, CODE_SENT);
                }
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


    private  boolean checkAndRequestPermissions() {
        int permissionCamera = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }




}

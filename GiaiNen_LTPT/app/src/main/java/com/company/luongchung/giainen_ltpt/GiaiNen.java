package com.company.luongchung.giainen_ltpt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.company.luongchung.Iface.Iupdatecheck;
import com.company.luongchung.adapter.adapterChoose;
import com.company.luongchung.model.O_file_choose;

import java.util.ArrayList;
import java.util.List;

public class GiaiNen extends AppCompatActivity implements Iupdatecheck{
    Button btnThemFiles;
    adapterChoose adapter;
    ArrayList<O_file_choose>  arrFile;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giai_nen);
        addControls();
        setlistview();
        addEvents();
    }

    private void addEvents() {
        btnThemFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrFile.add(new O_file_choose(1,true,"HAHA","CHƯA CÓ FILE (NULL)"));
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void addControls() {
        btnThemFiles = (Button) findViewById(R.id.btnThemRadio);
    }

    private void setlistview() {
        listView= (ListView) findViewById(R.id.lvGiaiNen);
        arrFile =new ArrayList<>();
        arrFile.add(new O_file_choose(1,true,"HAHA","CHƯA CÓ FILE (NULL)"));
        adapter =new adapterChoose(GiaiNen.this,R.layout.item_file,arrFile,this);
        listView.setAdapter(adapter);
    }

    @Override
    public void updatecheckbox(int i) {
        if (arrFile.get(i).isChoose())
        {
            arrFile.get(i).setChoose(false);
        }
        else
        {
            arrFile.get(i).setChoose(true);
        }
        adapter.notifyDataSetChanged();
    }
}

package com.company.luongchung.giainen_ltpt;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.company.luongchung.Iface.Iclick;
import com.company.luongchung.Iface.Iupdatecheck;
import com.company.luongchung.adapter.adapterChoose;
import com.company.luongchung.model.O_file_choose;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ar.com.daidalos.afiledialog.FileChooserActivity;
import ir.mahdi.mzip.zip.ZipArchive;

public class GiaiNen extends AppCompatActivity implements Iupdatecheck,Iclick{
    private String urlOpen = Environment.getExternalStorageDirectory()+"/Download";
    private Button btnThemFiles,btnGN,btnOpen;
    private adapterChoose adapter;
    private EditText txtPass;
    private ArrayList<O_file_choose>  arrFile;
    private ListView listView;
    private int Postision=0;
    private int countt=0;
    final int[] Counting = {0};
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
                arrFile.add(new O_file_choose());
                adapter.notifyDataSetChanged();
            }
        });
        btnGN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GiaiNenFile();
            }
        });
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GiaiNen.this, FileChooserActivity.class);
                startActivity(intent);
            }
        });
    }

    private void GiaiNenFile() {
        Counting[0]=0;
        countt=0;
        for (int i=0 ;i<arrFile.size();i++)
        {
            if(arrFile.get(i).isChoose() && !arrFile.get(i).getUrlFile().isEmpty())
            {
                countt++;
            }
        }
        for (int i=0 ;i<arrFile.size();i++)
        {
            if(arrFile.get(i).isChoose() && !arrFile.get(i).getUrlFile().isEmpty())
            {
                final int dem=i;
                final String PassGN =txtPass.getText().toString();
                final String namecut=arrFile.get(i).getNameFile().substring(0,arrFile.get(i).getNameFile().lastIndexOf("."));
                Thread thread =new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("GJHDGJ", "băt đau thread" + dem);
                        ZipArchive zipArchive = new ZipArchive();
                        zipArchive.unzip(arrFile.get(dem).getUrlFile(),urlOpen+"/"+namecut,PassGN);
                        Counting[0]++;
                        Log.d("GJHDGJ", "kết thúc thread" + dem);
                        Log.d("GJHDGJ", "Counting: "+Counting[0] + "   Count: "+countt);
                        if (countt==Counting[0])
                        {
                            Log.d("GJHDGJ","xong");
                        }
                    }
                });
                thread.start();
            }
        }
    }

    private void addControls() {
        btnThemFiles = (Button) findViewById(R.id.btnThemRadio);
        btnGN = (Button) findViewById(R.id.btn_GN);
        btnOpen=(Button) findViewById(R.id.btn_Openfodel);
        txtPass= (EditText) findViewById(R.id.txtPass);
    }

    private void setlistview() {
        listView= (ListView) findViewById(R.id.lvGiaiNen);
        arrFile =new ArrayList<>();
        arrFile.add(new O_file_choose());
        adapter =new adapterChoose(GiaiNen.this,R.layout.item_file,arrFile,this,this);
        listView.setAdapter(adapter);
    }

    @Override
    public void updatecheckbox(int i) {
        if (arrFile.get(i).isChoose()) {
            arrFile.get(i).setChoose(false);
        } else {
            arrFile.get(i).setChoose(true);
        }
        adapter.notifyDataSetChanged();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            boolean fileCreated = false;
            String filePath = "";

            Bundle bundle = data.getExtras();
            if(bundle != null)
            {
                if(bundle.containsKey(FileChooserActivity.OUTPUT_NEW_FILE_NAME)) {
                    fileCreated = true;
                    File folder = (File) bundle.get(FileChooserActivity.OUTPUT_FILE_OBJECT);
                    String name = bundle.getString(FileChooserActivity.OUTPUT_NEW_FILE_NAME);
                    filePath = folder.getAbsolutePath() + "/" + name;
                } else {
                    fileCreated = false;
                    File file = (File) bundle.get(FileChooserActivity.OUTPUT_FILE_OBJECT);
                    filePath = file.getAbsolutePath();
                }
            }
            String s=filePath;
            s=s.substring(s.lastIndexOf("/")+1,s.length());
            arrFile.get(Postision).setNameFile(s);
            arrFile.get(Postision).setUrlFile(filePath);
            Toast.makeText(GiaiNen.this, filePath, Toast.LENGTH_LONG).show();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getid(int i) {
        Postision =i;
    }
}

package com.company.luongchung.giainen_ltpt;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.dd.processbutton.iml.ActionProcessButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ar.com.daidalos.afiledialog.FileChooserActivity;
import dmax.dialog.SpotsDialog;
import ir.mahdi.mzip.zip.ZipArchive;

public class GiaiNen extends AppCompatActivity implements Iupdatecheck,Iclick{
    private String urlOpen = Environment.getExternalStorageDirectory()+"/Download";
    private Button btnThemFiles,btnOpen;
    private Button btnGN;
    private adapterChoose adapter;

    private ArrayList<O_file_choose>  arrFile;
    private ListView listView;
    private int Postision=0;
    private int countt=0;
    final int[] Counting = {0};
    private AlertDialog dialog;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
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
                if(!btnGN.getText().toString().equals("Giải nén xong ✓"))
                {
                    GiaiNenFile();
                }
                else {
                    Toast.makeText(GiaiNen.this,"Tùy chọn này đã được giải nén trước đó. /n mời bạn chọn tùy chọn khác rồi giải nén.",Toast.LENGTH_LONG).show();
                }
            }
        });
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions())
                {
                    Intent intent = new Intent(GiaiNen.this, FileChooserActivity.class);
                    startActivity(intent);
                }
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
        if (countt>0)dialog.show();

        if (!checknull(arrFile)) Toast.makeText(GiaiNen.this,"Không có file nào được chọn \n mời bạn chọn file để giải nén.",Toast.LENGTH_LONG).show();
        else
        {
            for (int i=0 ;i<arrFile.size();i++)
            {
                if(arrFile.get(i).isChoose() && !arrFile.get(i).getUrlFile().isEmpty())
                {
                    final int dem=i;
                    final String namecut=arrFile.get(i).getNameFile().substring(0,arrFile.get(i).getNameFile().lastIndexOf("."));
                    Thread thread =new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ZipArchive zipArchive = new ZipArchive();
                            zipArchive.unzip(arrFile.get(dem).getUrlFile(),urlOpen+"/"+namecut,"");
                            Counting[0]++;
                            if (countt==Counting[0])
                            {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.dismiss();
                                        btnGN.setText("Giải nén xong ✓");
                                        btnGN.setBackgroundResource(R.drawable.custom_button_thanhcong);
                                        btnGN.setTextColor(getResources().getColor(R.color.trang));
                                    }
                                });
                            }
                        }
                    });
                    thread.start();
                }
            }
        }

    }
    private boolean checknull(ArrayList<O_file_choose> arr)
    {
        boolean kt= false;
        for(int i=0;i<arr.size();i++)
        {
            if(!arr.get(i).getUrlFile().equals(""))
            {
                return true;
            }
        }
        return kt;
    }
    private void addControls() {
        btnThemFiles = (Button) findViewById(R.id.btnThemRadio);
        btnGN = (Button) findViewById(R.id.btn_GN);
        btnOpen=(Button) findViewById(R.id.btn_Openfodel);
        dialog = new SpotsDialog(GiaiNen.this,R.style.Custom);
        dialog.setCanceledOnTouchOutside(false);
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
            boolean kt =true;
            for (int j=0;j<arrFile.size();j++)
            {
                if (filePath.equals(arrFile.get(j).getUrlFile()))
                {
                    kt=false;
                }
            }
            if (kt)
            {
                arrFile.get(Postision).setNameFile(s);
                arrFile.get(Postision).setUrlFile(filePath);
            }
            else
            {
                Toast.makeText(GiaiNen.this,"Tệp này đã được chọn or trùng tên. ", Toast.LENGTH_LONG).show();
            }
            adapter.notifyDataSetChanged();
        }
        btnGN.setText("Giải nén");
        btnGN.setBackgroundResource(R.drawable.custom_button);
        btnGN.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void getid(int i) {
        Postision =i;
    }
    private  boolean checkAndRequestPermissions() {
        int permissionCamera = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(GiaiNen.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

}

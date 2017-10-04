package com.company.luongchung.giainen_ltpt;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.company.luongchung.adapter.AdapterChon_N;
import com.company.luongchung.model.O_file_choose;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import ar.com.daidalos.afiledialog.FileChooserActivity;
import dmax.dialog.SpotsDialog;
import ir.mahdi.mzip.rar.RarArchive;
import ir.mahdi.mzip.zip.ZipArchive;

public class NenFile extends AppCompatActivity implements Iclick,Iupdatecheck {
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private int countt=0;
    final int[] Counting = {0};
    private Button btnNF,btnThem,btn_Openfodel_N;
    private ArrayList<O_file_choose> arrFile;
    private ListView listView;
    private EditText txtTenFileNen;
    AdapterChon_N adapter;
    private int Postision=0;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nen_file);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btn_Openfodel_N.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions())
                {
                    Intent intent = new Intent(NenFile.this, FileChooserActivity.class);
                    startActivity(intent);
                }
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                arrFile.add(new O_file_choose());
                adapter.notifyDataSetChanged();
            }
        });
        btnNF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!btnNF.getTag().equals("1"))
                {
                    nenFile();
                }
                else {
                    Toast.makeText(NenFile.this,"Tùy chọn này đã được nén trước đó. \n mời bạn chọn tùy chọn khác rồi nén lại file.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void nenFile() { ///xử lý nén file.
        Counting[0]=0;
        countt=0;
        for (int i=0 ;i<arrFile.size();i++)
        {
            if(arrFile.get(i).isChoose() && !arrFile.get(i).getUrlFile().isEmpty())
            {
                countt++;
            }
        }
        if(!checknull()) Toast.makeText(NenFile.this,"Bạn chưa chọn file nào để nén \n Mời chọn các file để nén.",Toast.LENGTH_LONG).show();
        if (countt>0)dialog.show();

        final String ten_file=txtTenFileNen.getText().toString();
        for (int i=0 ;i<arrFile.size();i++)
        {
            if(arrFile.get(i).isChoose() && !arrFile.get(i).getUrlFile().isEmpty())
            {
                final int dem=i;
                Thread thread =new Thread(new Runnable() {
                    @Override
                    public void run() {
                        copyFileOrDirectory(arrFile.get(dem).getUrlFile(),"/storage/emulated/0/GiainenFile/"+ten_file);
                        Counting[0]++;
                        if (countt==Counting[0])
                        {
                            Thread thread1 =new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    ZipArchive zipArchive = new ZipArchive();
                                    zipArchive.zip("/storage/emulated/0/GiainenFile/"+ten_file,"/storage/emulated/0/GiainenFile/"+ten_file+".zip",""); //Nén file
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            dialog.dismiss();
                                            btnNF.setText("Nén xong ✓");
                                            btnNF.setTag("1");
                                        }
                                    });
                                    File filevd =new File("/storage/emulated/0/GiainenFile/"+ten_file);
                                    XoaThuMuc(filevd);

                                }
                            });
                            thread1.start();

                        }
                    }
                });
                thread.start();
            }
        }
    }

    private boolean checknull() {
        for (int i=0;i<arrFile.size();i++)
        {
            if (!arrFile.get(i).getUrlFile().equals(""))
            {
                return true;
            }
        }
        return false;
    }

    private void setlistview() {
        listView= (ListView) findViewById(R.id.lvNenFile);
        arrFile =new ArrayList<>();
        arrFile.add(new O_file_choose());
        adapter =new AdapterChon_N(NenFile.this,R.layout.item_file,arrFile,this,this);
        listView.setAdapter(adapter);
    }
    private void addControls() {
        btnNF= (Button) findViewById(R.id.btn_NF);
        btnThem = (Button) findViewById(R.id.btnThemRadioNen);
        btn_Openfodel_N = (Button) findViewById(R.id.btn_Openfodel_N);
        txtTenFileNen= (EditText) findViewById(R.id.txtTenfilenen);
        txtTenFileNen.setText("filenen");
        dialog = new SpotsDialog(NenFile.this,R.style.Custom);
        dialog.setCanceledOnTouchOutside(false);
        setlistview();
    }

    @Override
    public void getid(int i) {
        Postision=i;
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
                Toast.makeText(NenFile.this,"[ERROR][NENFILE] \n Tệp này đã được chọn or trùng tên.", Toast.LENGTH_LONG).show();
            }
            adapter.notifyDataSetChanged();
        }
        btnNF.setText("NÉN FILE");
        btnNF.setTag("0");

    }


    private  boolean checkAndRequestPermissions() {
        int permissionCamera = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(NenFile.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }



    private void copyFileOrDirectory(String srcDir, String dstDir) {

        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());
            if (src.isDirectory()) {
                String files[] = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);
                }
            } else {
                copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }


    private void XoaThuMuc(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
            {
                child.delete();
                XoaThuMuc(child);
            }

        fileOrDirectory.delete();
    }
}

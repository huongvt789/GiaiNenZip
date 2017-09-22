package com.company.luongchung.giainen_ltpt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.company.luongchung.Iface.Iupdatecheck;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnGiaiNen,btnNen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnNen.setOnClickListener(this);
        btnGiaiNen.setOnClickListener(this);
    }

    private void addControls() {
        btnGiaiNen = (Button) findViewById(R.id.btnGiaiNen);
        btnNen= (Button) findViewById(R.id.btnNen);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnNen:
                Intent intent1 =new Intent(MainActivity.this,NenFile.class);
                startActivity(intent1);
                break;
            case R.id.btnGiaiNen:
                Intent intent =new Intent(MainActivity.this,GiaiNen.class);
                startActivity(intent);
                break;
        }

    }


}

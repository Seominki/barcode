package com.example.alsrl.a20184566_test;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class IndexActivity extends AppCompatActivity implements View.OnClickListener {
    TextView title;
    Button createBtn;
    Button inOutBtn;
    Button productBtn;
    Button logoutBtn;
    private static String id = "";
    private static String name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        Intent index = getIntent();
        id = index.getStringExtra("id");
        name = index.getStringExtra("name");
        title = (TextView) findViewById(R.id.textTitle);
        createBtn = (Button)findViewById(R.id.createBtn);
        inOutBtn = (Button)findViewById(R.id.inOutBtn);
        productBtn = (Button)findViewById(R.id.productBtn);
        logoutBtn = (Button)findViewById(R.id.logoutBtn);
        title.setText(id+"("+name+")님 안녕하세요.");

        createBtn.setOnClickListener(this);
        inOutBtn.setOnClickListener(this);
        productBtn.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.createBtn:
                //해당 물품에 바코드 생성(상품등록)
                Intent create = new Intent(IndexActivity.this, AddActivity.class);
                startActivity(create);
                break;
            case R.id.inOutBtn:
                //입고 출고 등록
                Intent inOut = new Intent(IndexActivity.this, ScanActivity.class);
                startActivity(inOut);
                break;
            case R.id.productBtn:
                //물품 리스트
                Intent product = new Intent(IndexActivity.this, ListActivity.class);
                startActivity(product);
                break;
            case R.id.logoutBtn:
                //로그아웃
                new AlertDialog.Builder(this)
                        .setTitle("Error Message")
                        .setMessage("로그아웃 하시겠습니까?")
                        .setNegativeButton("예",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        Intent logout = new Intent(IndexActivity.this, MainActivity.class);
                                        startActivity(logout);
                                    }
                                })
                        .setPositiveButton("아니요",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dlg, int num) {
                            }
                        }).show();
                break;
        }
    }

}

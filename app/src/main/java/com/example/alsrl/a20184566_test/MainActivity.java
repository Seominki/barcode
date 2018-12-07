package com.example.alsrl.a20184566_test;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editId ;
    EditText editPw ;
    Button login ;
    Button sigin ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editId = (EditText) findViewById(R.id.editId);
        editPw = (EditText)findViewById(R.id.editPw);
        login = (Button)findViewById(R.id.loginBtn);
        sigin = (Button)findViewById(R.id.signupBtn);

        login.setOnClickListener(this);
        sigin.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginBtn:
                String id = editId.getText().toString();
                String pw = editPw.getText().toString();

                if(TextUtils.isEmpty(id) || TextUtils.isEmpty(pw)){
                    new AlertDialog.Builder(this)
                            .setTitle("Error Message")
                            .setMessage("ID 와 Password 모두 입력해주세요.")
                            .setPositiveButton("닫기",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dlg, int num) {
                                }
                            }).show();
                }else{
                    AQuery aQuery = new AQuery(MainActivity.this);
                    String url = Config.HOME_URL + "Login";

                    Map<String, Object> params = new LinkedHashMap<>();

                    params.put("uid", id);
                    params.put("upw", pw);

                    aQuery.ajax(url, params, String.class, new AjaxCallback<String>() {
                        @Override
                        public void callback(String url, String result, AjaxStatus status) {
                            try {
                                JSONObject jReponse = new JSONObject(result);
                                if(jReponse != null){
                                    if("1".equals(jReponse.get("error"))){
                                        new AlertDialog.Builder(MainActivity.this)
                                                .setTitle("Error Message")
                                                .setMessage((String)jReponse.get("message"))
                                                .setPositiveButton("닫기",new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dlg, int num) {
                                                        editId.setText("");
                                                        editPw.setText("");

                                                    }
                                                }).show();
                                    }else if("0".equals(jReponse.get("error"))){
                                        Intent login = new Intent(MainActivity.this, IndexActivity.class);
                                        login.putExtra("id",(String)jReponse.get("mid"));
                                        login.putExtra("name",(String)jReponse.get("name"));
                                        startActivity(login);
                                    }
                                }
                            } catch (Exception e) {
                                Toast.makeText(MainActivity.this, "오류가 발생 하였습니다", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                break;
            case R.id.signupBtn:
                Intent singup = new Intent(MainActivity.this, SiginActivity.class);
                startActivity(singup);
                break;
        }

    }
}

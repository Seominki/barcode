package com.example.alsrl.a20184566_test;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class SiginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editId ;
    EditText editPw ;
    EditText editcPw ;
    EditText editname ;
    Button check ;
    String code = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sigin);
        String[] select = getResources().getStringArray(R.array.company_array);
        ArrayAdapter<String> selectAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, select);
        Spinner spi = (Spinner)findViewById(R.id.companyCode);
        spi.setAdapter(selectAdapter);
        spi.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Spinner sp = (Spinner)findViewById(R.id.companyCode);
                        code = (String)sp.getAdapter().getItem(sp.getSelectedItemPosition());
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );
        editId = (EditText) findViewById(R.id.siginId);
        editPw = (EditText)findViewById(R.id.siginPw);
        editcPw = (EditText)findViewById(R.id.siginConfirm);
        editname = (EditText)findViewById(R.id.siginName);
        check = (Button)findViewById(R.id.checkBtn);
        check.setOnClickListener(this);
    }

    @Override
        public void onClick(View v) {
        switch (v.getId()){
            case R.id.checkBtn:
                String id = editId.getText().toString();
                String pw = editPw.getText().toString();
                String cpw = editcPw.getText().toString();
                String name = editname.getText().toString();
                if("" == code){
                    code = "1";
                }else{
                    if("본사".equals(code)){
                        code = "1";
                    }else{
                        code = "2";
                    }
                }
                if(TextUtils.isEmpty(id) || TextUtils.isEmpty(pw) || TextUtils.isEmpty(cpw) || TextUtils.isEmpty(name) ){
                    new AlertDialog.Builder(this)
                                .setTitle("Error Message")
                                .setMessage("빈 값이 없게 모두 입력해주세요.")
                                .setPositiveButton("닫기",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dlg, int num) {
                                    }
                                }).show();
                }else{
                    if(!pw.equals(cpw)){
                        new AlertDialog.Builder(this)
                                .setTitle("Error Message")
                                .setMessage("Password 와 Confirm Pw의 값이 같지않습니다.")
                                .setPositiveButton("닫기",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dlg, int num) {
                                    }
                                }).show();
                    }else{
                        AQuery aQuery = new AQuery(SiginActivity.this);
                        String url = Config.HOME_URL + "Sigin";

                        Map<String, Object> params = new LinkedHashMap<>();

                        params.put("uid", id);
                        params.put("upw", pw);
                        params.put("name", name);
                        params.put("site", code);

                        aQuery.ajax(url, params, String.class, new AjaxCallback<String>() {
                            @Override
                            public void callback(String url, String result, AjaxStatus status) {
                                try {
                                    JSONObject jReponse = new JSONObject(result);
                                    if(jReponse != null){
                                        if("1".equals(jReponse.get("error"))){
                                            new AlertDialog.Builder(SiginActivity.this)
                                                    .setTitle("Error Message")
                                                    .setMessage((String)jReponse.get("message"))
                                                    .setPositiveButton("닫기",new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dlg, int num) {
                                                        }
                                                    }).show();
                                        }else if("0".equals(jReponse.get("error"))){
                                            Toast.makeText(SiginActivity.this, "등록이 완료 되었습니다", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(SiginActivity.this, "등록 하는데 오류가 발생 하였습니다", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                }
                break;
        }
    }
}

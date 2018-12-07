package com.example.alsrl.a20184566_test;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class ScanActivity extends AppCompatActivity implements View.OnClickListener {
    RadioGroup groupRadio;
    TextView editName;
    TextView editPrice;
    TextView editCount;
    TextView editBarcode;
    ImageView imgView;
    Context context;
    Button subBtn;
    EditText updateCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan);
        groupRadio = (RadioGroup)findViewById(R.id.groupRadio);
        editName = (TextView)findViewById(R.id.editName);
        editPrice = (TextView)findViewById(R.id.editPrice);
        editCount = (TextView)findViewById(R.id.editCount);
        editBarcode = (TextView)findViewById(R.id.editBarcode);
        imgView = (ImageView)findViewById(R.id.barImage);
        updateCount = (EditText)findViewById(R.id.updateCount);
        subBtn = (Button)findViewById(R.id.subBtn);
        subBtn.setOnClickListener(this);
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // QR코드/바코드를 스캔한 결과 값을 가져옵니다.
        IntentResult value = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String barcode = value.getContents();
        /*String result = "8800000100039";*/

        // 결과값 출력
        if(null != barcode){
            AQuery aQuery = new AQuery(ScanActivity.this);
            String url = Config.HOME_URL + "BarcodeInfo";

            Map<String, Object> params = new LinkedHashMap<>();

            params.put("barcode", barcode);

            aQuery.ajax(url, params, String.class, new AjaxCallback<String>() {

                @Override
                public void callback(String url, String result, AjaxStatus status) {
                    context = getApplicationContext();
                    try {
                        JSONObject jReponse = new JSONObject(result);
                        if(jReponse != null){
                            if("1".equals(jReponse.get("error"))){
                                new AlertDialog.Builder(ScanActivity.this)
                                        .setTitle("Error Message")
                                        .setMessage((String)jReponse.get("message"))
                                        .setPositiveButton("닫기",new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dlg, int num) {
                                                finish();
                                            }
                                        }).show();
                            }else if("0".equals(jReponse.get("error"))){
                                String imageUrl = Config.HOME_URL+ "ImageView?id=" + (String)jReponse.get("image");
                                Picasso.with(context)
                                        .load(imageUrl)
                                        .placeholder(R.mipmap.ic_launcher)
                                        .into(imgView);
                                editName.setText((String)jReponse.get("name"));
                                editPrice.setText((String)jReponse.get("price"));
                                editCount.setText((String)jReponse.get("count"));
                                editBarcode.setText((String)jReponse.get("barcode"));
                            }else if("2".equals(jReponse.get("error"))){
                                new AlertDialog.Builder(ScanActivity.this)
                                        .setTitle("Error Message")
                                        .setMessage("해당 바코드에 대한 물품정보가 존재하지 않습니다. 추가하시겠습니까?")
                                        .setNegativeButton("예",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent create = new Intent(ScanActivity.this, AddActivity.class);
                                                        create.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(create);
                                                    }
                                                })
                                        .setPositiveButton("아니요",new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dlg, int num) {
                                                //메뉴로 복귀
                                                finish();
                                            }
                                        }).show();
                            }
                        }else{
                            new AlertDialog.Builder(ScanActivity.this)
                                    .setTitle("Error Message")
                                    .setMessage("해당 바코드에 대한 물품정보가 존재하지 않습니다. 추가하시겠습니까?")
                                    .setNegativeButton("예",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    finish();
                                                    Intent create = new Intent(ScanActivity.this, AddActivity.class);
                                                    startActivity(create);
                                                }
                                            })
                                    .setPositiveButton("아니요",new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dlg, int num) {
                                            //메뉴로 복귀
                                            finish();
                                        }
                                    }).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(ScanActivity.this, "오류가 발생 하였습니다", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            new AlertDialog.Builder(this)
                    .setTitle("Error Message")
                    .setMessage("바코드를 인식 할 수 없습니다.")
                    .setPositiveButton("닫기",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dlg, int num) {
                            //메뉴로 복귀
                            finish();
                        }
                    }).show();
        }
    }

    @Override
    public void onClick(View v) {
        RadioButton rd = (RadioButton) findViewById(groupRadio.getCheckedRadioButtonId());
        String url = Config.HOME_URL + "updateProduct";

        AQuery aQuery = new AQuery(ScanActivity.this);

        Map<String, Object> params = new LinkedHashMap<>();

        params.put("barcode", editBarcode.getText());
        params.put("count", updateCount.getText());
        if("입고".equals(rd.getText().toString())){
            params.put("status", "IN");
        }else{
            params.put("status", "OUT");
        }
        aQuery.ajax(url, params, String.class, new AjaxCallback<String>() {
            @Override
            public void callback(String url, String result, AjaxStatus status) {
                try {
                    JSONObject jReponse = new JSONObject(result);
                    if(jReponse != null){
                        if("1".equals(jReponse.get("error"))){
                            new AlertDialog.Builder(ScanActivity.this)
                                    .setTitle("Error Message")
                                    .setMessage((String)jReponse.get("message"))
                                    .setPositiveButton("닫기",new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dlg, int num) {
                                        }
                                    }).show();
                        }else if("0".equals(jReponse.get("error"))){
                            //물품 리스트
                            finish();
                            Intent product = new Intent(ScanActivity.this, ListActivity.class);
                            startActivity(product);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(ScanActivity.this, "오류가 발생 하였습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //앱의 전반적으로 돌아가던 스레드를 종료할 때 이 Debug.stopMethodTracing();
    }
    @Override
    protected void onStart() {
        super.onStart();
        //앱의 전반적으로 돌아가던 스레드를 종료할 때 이 Debug.stopMethodTracing();
    }

}

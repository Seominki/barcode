package com.example.alsrl.a20184566_test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class ListActivity extends AppCompatActivity {
    ListView lv;
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        lv = (ListView) findViewById(R.id.lv);
        adapter = new MainAdapter(ListActivity.this);
        lv.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        AQuery aQuery = new AQuery(ListActivity.this);
        String url = Config.HOME_URL + "ProductList";
        aQuery.ajax(url, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jReponse, AjaxStatus status) {
                if(jReponse == null){

                }
                else{
                    try {
                        Gson gson = new Gson();
                        if (jReponse != null && !jReponse.isNull("result")) {
                            JSONArray jarBoard = jReponse.optJSONArray("result");

                            ArrayList<ListEntity> arItem = new ArrayList<ListEntity>();
                            for(int i=0; i<jarBoard.length(); i++){
                                ListEntity boardEntity = gson.fromJson(jarBoard.optJSONObject(i).toString(), ListEntity.class);
                                if(boardEntity != null){
                                    arItem.add(boardEntity);
                                }
                            }

                            adapter.setArItem(arItem);
                            adapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                    }
                }

            }
        });
    }
}
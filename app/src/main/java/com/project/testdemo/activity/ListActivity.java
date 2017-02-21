package com.project.testdemo.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.testdemo.R;
import com.project.testdemo.model.Product;
import com.project.testdemo.model.Result;
import com.project.testdemo.tool.OKHttpTool;
import com.project.testdemo.url.UrlClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class ListActivity extends Activity implements OnRefreshListener, OnLoadMoreListener {

    SwipeToLoadLayout swipeToLoadLayout;

    ArrayAdapter<String> mAdapter;


    private static final String TAG = "ListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);

//        ListView listView = (ListView) findViewById(R.id.swipe_target);

        swipeToLoadLayout.setOnRefreshListener(this);

        swipeToLoadLayout.setOnLoadMoreListener(this);

//        mAdapter = new ArrayAdapter<String>(ListActivity.this, android.R.layout.simple_expandable_list_item_1);

//        listView.setAdapter(mAdapter);

        autoRefresh();
    }

    @Override
    public void onLoadMore() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setLoadingMore(false);
//                mAdapter.add("LOAD MORE:\n" + new Date());
                initProduct(null,null);
            }
        }, 2000);
    }

    @Override
    public void onRefresh() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(false);
//                mAdapter.add("REFRESH:\n" + new Date());
                initProduct(null,null);
            }
        }, 2000);
    }
    private void autoRefresh() {
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }
    private void initProduct(String page,String cid){

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("page",page!=null ?page : "1");
            jsonObject.put("cid",cid!=null?cid:"0");
            Log.e(TAG, "initProduct: "+jsonObject.toString() );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread(){
            @Override
            public void run() {
                String responseStr = null;
                try {
                    responseStr = OKHttpTool.post(UrlClass.PRODUCT_URL, jsonObject.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e(TAG, "initProduct: "+responseStr);
                String jsonStr = new String(Base64.decode(responseStr.getBytes(), Base64.DEFAULT));
                Log.e(TAG, "run: "+jsonStr);
                Gson gson = new Gson();
                Type type = new TypeToken<Result<List<Product>>>(){}.getType();
                final Result<List<Product>> result = gson.fromJson(jsonStr, type);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addAll("REFRESH:\n" +result.obj);

//                        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
//                        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
//                        recyclerView.setLayoutManager(layoutManager);
//                        ProductAdapter adapter = new ProductAdapter(getApplicationContext(),result.obj);
//                        recyclerView.setAdapter(adapter);
                    }
                });

            }
        }.start();


    }
}

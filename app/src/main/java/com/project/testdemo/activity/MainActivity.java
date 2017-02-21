package com.project.testdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.project.testdemo.R;
import com.project.testdemo.model.Admin;
import com.project.testdemo.tool.OKHttpTool;
import com.project.testdemo.url.UrlClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static android.text.TextUtils.*;

public class MainActivity extends Activity implements View.OnClickListener {

    private EditText etAdmin, etPassword;
    private Button btnLogin;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView(){
        etAdmin = (EditText) findViewById(R.id.et_admin);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                final String admin = etAdmin.getText().toString().trim();//这里trim()作用是去掉首位空格，防止不必要的错误
                final String pass = etPassword.getText().toString().trim();//这里trim()作用是去掉首位空格，防止不必要的错误
                if(!isEmpty(admin)&&!isEmpty(pass)) {

                    new Thread() {
                        @Override
                        public void run() {
                            Gson gson = new GsonBuilder()
                                    .excludeFieldsWithoutExposeAnnotation()
                                    .create();
                            String response = null;
                            try {
                                Log.e(TAG, "run: "+admin +pass);
                                response = OKHttpTool.post(UrlClass.login_url, gson.toJson(new Admin(admin,pass)));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            // base64转码
                            String jsonStr = new String(Base64.decode(response.getBytes(), Base64.DEFAULT));
                            Log.e(TAG, "run: "+jsonStr);
                            try {
                                JSONObject jsonobject = new JSONObject(jsonStr);
                                if(jsonobject.get("code").equals("200")) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(MainActivity.this, ProductActivity.class));
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }.start();
                } else {
                    Toast.makeText(MainActivity.this, "用户名和密码不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

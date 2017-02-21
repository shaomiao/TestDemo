package com.project.testdemo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.testdemo.R;
import com.project.testdemo.adapter.ProductAdapter;
import com.project.testdemo.model.City;
import com.project.testdemo.model.Product;
import com.project.testdemo.model.Result;
import com.project.testdemo.tool.OKHttpTool;
import com.project.testdemo.url.UrlClass;
import com.project.testdemo.view.ChildView;
import com.project.testdemo.view.ExpandTabView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 包含上拉加载
 *
 */
public class ProductActivity extends Activity implements OnLoadMoreListener,OnRefreshListener {

    // 头部二级菜单
    private ExpandTabView expandTabView;
    private ArrayList<View> mViewArray = new ArrayList<>();
    private ChildView viewLeft;
    /////////////////////////

    private ProductAdapter proAdapter;
    private SwipeToLoadLayout swipeToLoadLayout;
    private RecyclerView recyclerView;

    private static final String TAG = "ProductActivity";
    private List<Product> proList;

    private  String page = "1";
    private  String cid = "0";

    // 所有城市集合
    List<City> cityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        // 初始化头部
        initTwoLevelMenu();

        // initView
        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        // 初始化下拉加载控件
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);

        proList = new ArrayList<>();

        // 初始化adapter
        proAdapter = new ProductAdapter(this, proList);

        // 绑定RecyclerView
        recyclerView = (RecyclerView) findViewById(R.id.swipe_target);

        recyclerView.addItemDecoration(new DividerItemDecoration(ProductActivity.this, DividerItemDecoration.VERTICAL));
        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(proAdapter);
//        getProductList(false);
        // 自动刷新
        autoRefresh();
    }

    ////////////////////***///////////////////////////
    private void initTwoLevelMenu() {
        expandTabView = (ExpandTabView) findViewById(R.id.expandtab_view);

//         String LeftFaString[] = new String[] { "美食", "快餐小吃", "火锅", "海鲜/烧烤",
//                "特色菜", "香锅/烤鱼", "地方菜", "东南亚菜", "西餐", "日韩料理" };
//        //子ListView每一个Item对应的text..采用了二维数组的实现方式..
//         String LeftCh1String[][] = new String[][] {
//                { "全部" },
//                { "全部", "中式简餐", "地方小吃", "盖浇饭", "米粉米线", "面馆", "麻辣烫", "黄焖鸡米饭",
//                        "鸭脖卤味", "饺子馄饨", "炸鸡炸串", "包子/粥", "零食", "生煎锅贴", "冒菜" },
//                { "全部", "其他火锅" }, { "全部", "小龙虾" }, { "全部" }, { "全部", "香锅", "烤鱼" },
//                { "全部", "鲁菜", "川菜", "其他" }, { "全部" },
//                { "全部", "意面披萨", "西式快餐", "其他西餐" }, { "全部", "韩式简餐", "韩国料理" } };

        new Thread() {
            @Override
            public void run() {
                try {
                    // 获取所有城市
                    String responseStr = OKHttpTool.post(UrlClass.CITY_URL, "");
                    String jsonStr = new String(Base64.decode(responseStr.getBytes(), Base64.DEFAULT));
                    Log.e(TAG, "run: "+jsonStr);
                    JSONObject jsonobject = new JSONObject(jsonStr);
                    if (jsonobject.getString("code").equals("200")) {
                        JSONArray obj = jsonobject.getJSONArray("obj");
                        final String LeftFaString[] = new String[obj.length()];
                        final String LeftCh1String[][] = new String[2][2];
                        for (int i =0 ;i<obj.length();i++){
                            LeftFaString[i] = obj.getJSONObject(i).getString("name");
                            JSONArray sum = obj.getJSONObject(i).getJSONArray("som");
                            for(int j = 0; j <sum.length(); j++) {
                                Log.e(TAG, "zzz: "+sum.getJSONObject(j).getString("name"));
                                // 添加。。。
                                LeftCh1String[i][j] = sum.getJSONObject(j).getString("name");
                                cityList.add(new City(sum.getJSONObject(j).getString("id"),sum.getJSONObject(j).getString("name"),sum.getJSONObject(j).getString("pid")));
                            }

                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                viewLeft = new ChildView(ProductActivity.this,LeftFaString,LeftCh1String);
                                mViewArray.add(viewLeft);

                                ArrayList<String> mTextArray = new ArrayList<>();
                                mTextArray.add("分类");
                                expandTabView.setValue(mTextArray, mViewArray);

//                                expandTabView.setTitle(viewLeft.getShowText(), 0);
                                viewLeft.setOnSelectListener(new ChildView.OnSelectListener() {
                                    @Override
                                    public void getValue(String showText) {
                                        // 重新刷新数据
                                        onRefreshMenu(viewLeft,showText);
                                    }
                                });
                            }});
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();


        //viewLeft = new ChildView(this,LeftFaString,LeftCh1String);
//        mViewArray.add(viewLeft);
//
//        ArrayList<String> mTextArray = new ArrayList<>();
//        mTextArray.add("分类");
//        expandTabView.setValue(mTextArray, mViewArray);
//
//        expandTabView.setTitle(viewLeft.getShowText(), 0);


//        viewLeft.setOnSelectListener(new ChildView.OnSelectListener() {
//            @Override
//            public void getValue(String showText) {
//                // 重新刷新数据
//                onRefreshMenu(viewLeft,showText);
//            }
//        });
    }


    private void onRefreshMenu(View view, String showText) {
        expandTabView.onPressBack();
        int position = getPositon(view);
        if (position >= 0 && !expandTabView.getTitle(position).equals(showText)) {
            expandTabView.setTitle(showText, position);
        }
        Toast.makeText(this, showText, Toast.LENGTH_SHORT).show();

        //点击了
        for (City c:cityList
                ) {
            if (showText.equals(c.getName())){
                //重新请求商品刷新数据
                cid = c.getId();
                getProductList(true);
            }

        }

    }

    private int getPositon(View tView) {
        for (int i = 0; i < mViewArray.size(); i++) {
            if (mViewArray.get(i) == tView) {
                return i;
            }
        }
        return -1;
    }
    /////////////////***////////////////////

    private void autoRefresh() {
        swipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(true);
            }
        });
    }


    /**
     * 重新请求商品数据
     * @param flag 是否刷新 true 刷新
     */
    private void getProductList(final boolean flag) {

        new Thread(){

            @Override
            public void run() {

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("page",page);
                    jsonObject.put("cid",cid);
                    String responseStr = OKHttpTool.post(UrlClass.PRODUCT_URL, jsonObject.toString());
                    String jsonStr = new String(Base64.decode(responseStr.getBytes(), Base64.DEFAULT));
                    Gson gson = new Gson();
                    Type type = new TypeToken<Result<List<Product>>>(){}.getType();
                    final Result<List<Product>> result = gson.fromJson(jsonStr, type);
                    if (flag) {
                        proList.clear();
                        proList.addAll(result.obj);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                proAdapter.notifyDataSetChanged();
//                                recyclerView.scrollToPosition(proList.size() -1);

                            }
                        });
                    }else {
                        proList.addAll(result.obj);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                proAdapter.notifyItemInserted(proList.size()-1);
//                                recyclerView.scrollToPosition(proList.size() -1);
                            }
                        });
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    @Override
    public void onLoadMore() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setLoadingMore(false);
//                proList.add(new Product("shaomiao","nihao","tupian","xxx"));
//                proList.add(new Product("shaomiao","nihao","tupian","xxx"));
                page = String.valueOf((Integer.parseInt(page) +1));
                getProductList(false);
            }
        },10);
    }
    @Override
    public void onRefresh() {
        swipeToLoadLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeToLoadLayout.setRefreshing(false);
                getProductList(true);
            }
        }, 2000);
    }

}

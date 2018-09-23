package com.example.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements ExampleAdapter.OnItemClickListener {
    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_CREATOR = "creatorName";
    public static final String EXTRA_LIKES = "likeCount";
    public static final int SUCCESS_GET_DATA = 1;
    public static final int FAIL_GET_DATA = 2;

    private SwipeRefreshLayout swipeRefresh;



    private Handler mHandler;
    private ExampleAdapter mExampleAdapter;
    private List<CatBean> mExampleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshCats();
            }
        });

        initView();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case SUCCESS_GET_DATA:
                        List<CatBean> catBean = (List) msg.obj;
                        mExampleList.clear();
                        mExampleList.addAll(catBean);
                        mExampleAdapter.notifyDataSetChanged();

                        break;
                    case FAIL_GET_DATA:
                        Toast.makeText(MainActivity.this, "网络异常", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        };
    }

    private void refreshCats() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initView();
                        mExampleAdapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void initView() {
        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mExampleList = new ArrayList<>();
        mExampleAdapter = new ExampleAdapter(mExampleList, this);
        mRecyclerView.setAdapter(mExampleAdapter);
        mExampleAdapter.setOnItemClickListener(this);
        getJSON();
    }


    private void getJSON() {
        OkHttpClient client = OkHttpClientHelper.getInstance();
        Request request = new Request.Builder()
                .url("https://pixabay.com/api/?key=5303976-fd6581ad4ac165d1b75cc15b3&q=kitten&image_type=photo&pretty=true")
                .get()
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(FAIL_GET_DATA);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                List<CatBean> list = CatBean.extractCatBean(response.body().string());
                if (list == null) {
                    mHandler.sendEmptyMessage(FAIL_GET_DATA);
                    return;
                }
                Message message = mHandler.obtainMessage();
                message.what = SUCCESS_GET_DATA;
                message.obj = list;
                mHandler.sendMessage(message);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        CatBean clickedItem = mExampleList.get(position);

        detailIntent.putExtra(EXTRA_URL, clickedItem.getImageUrl());
        detailIntent.putExtra(EXTRA_CREATOR, clickedItem.getCreator());
        detailIntent.putExtra(EXTRA_LIKES, clickedItem.getLikes());

        startActivity(detailIntent);
    }
}

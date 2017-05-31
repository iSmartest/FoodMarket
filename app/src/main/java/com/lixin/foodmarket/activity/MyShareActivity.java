package com.lixin.foodmarket.activity;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lixin.foodmarket.R;
import com.lixin.foodmarket.adapter.MyCollectionShareAdapter;
import com.lixin.foodmarket.bean.MyCollectionShareBean;
import com.lixin.foodmarket.http.StringCallback;
import com.lixin.foodmarket.utils.OkHttpUtils;
import com.lixin.foodmarket.utils.SPUtils;
import com.lixin.foodmarket.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/5/19
 * My mailbox is 1403241630@qq.com
 */

public class MyShareActivity extends BaseActivity{
    private PullToRefreshListView collection_list;
    private MyCollectionShareAdapter mAdapter;
    private int nowPage = 1;
    private String uid;
    private String handleType = "1";
    private List<MyCollectionShareBean.commoditysList> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        hideBack(false);
        setTitleText("我的分享");
        uid = (String) SPUtils.get(MyShareActivity.this,"uid","");
        mList = new ArrayList<>();
        initView();
        getdata();
    }
    private void initView() {
        collection_list = (PullToRefreshListView)findViewById(R.id.collection_list);
        mAdapter = new MyCollectionShareAdapter();
        collection_list.setAdapter(mAdapter);
        collection_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(MyShareActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                nowPage = 1;
                mList.clear();
                getdata();
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                nowPage ++;
                getdata();
            }
        });
        collection_list.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                Toast.makeText(getApplication(), "已经到底了", Toast.LENGTH_SHORT).show();
            }
        });
        //在刷新时允许继续滑动
        collection_list.setScrollingWhileRefreshingEnabled(true);
        collection_list.setMode(PullToRefreshBase.Mode.BOTH);
        collection_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void getdata() {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"getCollectionOrShare\",\"handleType\":\"" + handleType +"\",\"uid\":\""
                + uid + "\",\"nowPage\":\"" + nowPage + "\"}";
        params.put("json", json);
        Log.i("MyShareActivity", "onResponse: " + json.toString());
        dialog1.show();
        OkHttpUtils.post().url(context.getString(R.string.url)).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showMessageShort(context, e.getMessage());
                dialog1.dismiss();
                collection_list.onRefreshComplete();
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("MyShareActivity", "onResponse: " + response.toString());
                Gson gson = new Gson();
                dialog1.dismiss();
                collection_list.onRefreshComplete();
                MyCollectionShareBean myCollectionShareBean = gson.fromJson(response, MyCollectionShareBean.class);
                if (myCollectionShareBean.result.equals("1")) {
                    ToastUtils.showMessageShort(context, myCollectionShareBean.resultNote);
                    return;
                }
                if (myCollectionShareBean.totalPage < nowPage) {
                    ToastUtils.showMessageShort(context, "没有更多了");
                    return;
                }
                List<MyCollectionShareBean.commoditysList> securitiesList = myCollectionShareBean.commoditysList;
                mList.addAll(securitiesList);
                mAdapter.setMyCollectionShare(MyShareActivity.this,mList,uid);
                collection_list.setAdapter(mAdapter);
                collection_list.onRefreshComplete();
            }
        });
    }

}

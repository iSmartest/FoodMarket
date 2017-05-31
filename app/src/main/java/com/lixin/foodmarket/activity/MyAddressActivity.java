package com.lixin.foodmarket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lixin.foodmarket.R;
import com.lixin.foodmarket.adapter.MyAddressAdapter;
import com.lixin.foodmarket.bean.MyAddressBean;
import com.lixin.foodmarket.bean.ShopCartBean;
import com.lixin.foodmarket.http.StringCallback;
import com.lixin.foodmarket.utils.OkHttpUtils;
import com.lixin.foodmarket.utils.SPUtils;
import com.lixin.foodmarket.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static com.lixin.foodmarket.activity.MyApplication.getApplication;

/**
 * Created by 小火
 * Create time on  2017/5/18
 * My mailbox is 1403241630@qq.com
 */

public class MyAddressActivity extends BaseActivity implements MyAddressAdapter.CallBackValue {
    private PullToRefreshListView my_address_list;
    private ImageView mBack,mAddAddress;
    private MyAddressAdapter mAdapter;
    private int nowPage = 1;
    private String uid;
    private String temp;
    private List<MyAddressBean.addressList> mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);
        uid = (String) SPUtils.get(MyAddressActivity.this,"uid","");
        mList = new ArrayList<>();
        initView();
        getdata();
    }
    private void initView() {
        mBack = (ImageView) findViewById(R.id.Iv_my_address_back);
        mBack.setOnClickListener(this);
        mAddAddress = (ImageView) findViewById(R.id.iv_my_address_edit);
        mAddAddress.setOnClickListener(this);
        my_address_list = (PullToRefreshListView) findViewById(R.id.my_address_list);
        mAdapter = new MyAddressAdapter();
        my_address_list.setAdapter(mAdapter);
        my_address_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(MyAddressActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                nowPage = 1;
                mList.clear();
                getdata();
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                nowPage++;
                getdata();
            }
        });
        my_address_list.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                Toast.makeText(getApplication(), "已经到底了", Toast.LENGTH_SHORT).show();
            }
        });
        //在刷新时允许继续滑动
        my_address_list.setScrollingWhileRefreshingEnabled(true);
        my_address_list.setMode(PullToRefreshBase.Mode.BOTH);
        my_address_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(ClassListActivity.this,ShopDecActivity.class);
//                intent.putExtra("rotateid",mList.get(position-1).commodityid);
//                intent.putExtra("rotateIcon",mList.get(position-1).commodityIcon);
//                startActivity(intent);
            }
        });
    }

    private void getdata() {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"getAdressListInfo\",\"uid\":\"" + uid + "\"}";
        params.put("json", json);
        Log.i("MyAddressActivity", "onResponse: " + json.toString());
        dialog1.show();
        OkHttpUtils.post().url(context.getString(R.string.url)).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showMessageShort(context, e.getMessage());
                dialog1.dismiss();
                my_address_list.onRefreshComplete();
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("MyAddressActivity", "onResponse: " + response.toString());
                Gson gson = new Gson();
                dialog1.dismiss();
                my_address_list.onRefreshComplete();
                MyAddressBean myAddressBean = gson.fromJson(response, MyAddressBean.class);
                if (myAddressBean.result.equals("1")) {
                    ToastUtils.showMessageShort(context, myAddressBean.resultNote);
                    return;
                }
                List<MyAddressBean.addressList> addressList = myAddressBean.addressList;

                mList.addAll(addressList);
                mAdapter.setShopCart(MyAddressActivity.this,mList,uid);
                my_address_list.setAdapter(mAdapter);
                my_address_list.onRefreshComplete();
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Iv_my_address_back:
                finish();
                break;
            case R.id.iv_my_address_edit:
                Intent intent = new Intent(MyAddressActivity.this,AddAddressActivity.class);
                startActivityForResult(intent,2001);
                break;
        }
    }
    @Override
    public void SendMessageValue(String strValue,int position) {
        temp = strValue;
        if (temp.equals("0")){
            getdata();
            my_address_list.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }else if (temp.equals("1")){
            mList.remove(position);
            mAdapter.notifyDataSetChanged();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        my_address_list.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}

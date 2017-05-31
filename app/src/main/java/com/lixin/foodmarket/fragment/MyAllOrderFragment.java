package com.lixin.foodmarket.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lixin.foodmarket.R;
import com.lixin.foodmarket.adapter.MyOrderAdapter;
import com.lixin.foodmarket.bean.MyOrderBean;
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
 * Create time on  2017/5/31
 * My mailbox is 1403241630@qq.com
 */

public class MyAllOrderFragment extends BaseFragment{
    private View view;
    private PullToRefreshListView order_list;
    private MyOrderAdapter mAdapter;
    private int nowPage = 1;
    private String uid;
    private String orderState = "0";
    private List<MyOrderBean.orders> mList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_content,null);
        uid = (String) SPUtils.get(getActivity(),"uid","");
        mList = new ArrayList<>();
        initView();
        getdata();
        return view;
    }

    private void initView() {
        order_list = (PullToRefreshListView) view.findViewById(R.id.order_list);
        mAdapter = new MyOrderAdapter();
        order_list.setAdapter(mAdapter);
        order_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
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
        order_list.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                Toast.makeText(getApplication(), "已经到底了", Toast.LENGTH_SHORT).show();
            }
        });
        //在刷新时允许继续滑动
        order_list.setScrollingWhileRefreshingEnabled(true);
        order_list.setMode(PullToRefreshBase.Mode.BOTH);
        order_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void getdata() {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"getOrderInfo\",\"orderState\":\"" + orderState +"\",\"uid\":\""
                + uid + "\",\"nowPage\":\"" + nowPage + "\"}";
        params.put("json", json);
        Log.i("MyAllOrderFragment", "onResponse: " + json.toString());
        dialog.show();
        OkHttpUtils.post().url(context.getString(R.string.url)).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showMessageShort(context, e.getMessage());
                dialog.dismiss();
                order_list.onRefreshComplete();
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("MyAllOrderFragment", "onResponse: " + response.toString());
                Gson gson = new Gson();
                dialog.dismiss();
                order_list.onRefreshComplete();
                MyOrderBean myOrderBean = gson.fromJson(response, MyOrderBean.class);
                if (myOrderBean.result.equals("1")) {
                    ToastUtils.showMessageShort(context, myOrderBean.resultNote);
                    return;
                }
                if (myOrderBean.totalPage < nowPage) {
                    ToastUtils.showMessageShort(context, "没有更多了");
                    return;
                }
                List<MyOrderBean.orders> securitiesList = myOrderBean.orders;
                mList.addAll(securitiesList);
                mAdapter.setMyOrder(getActivity(),mList,uid);
                order_list.setAdapter(mAdapter);
                order_list.onRefreshComplete();
            }
        });
    }

}

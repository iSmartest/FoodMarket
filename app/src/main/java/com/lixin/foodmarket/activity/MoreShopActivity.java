package com.lixin.foodmarket.activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lixin.foodmarket.R;
import com.lixin.foodmarket.adapter.MoreShopAdapter;
import com.lixin.foodmarket.bean.MoreShopBean;
import com.lixin.foodmarket.http.StringCallback;
import com.lixin.foodmarket.utils.OkHttpUtils;
import com.lixin.foodmarket.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

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

public class MoreShopActivity extends BaseActivity{
    private int flag;
    private PullToRefreshGridView more_shop_grid;
    private GridView mGridView;
    private MoreShopAdapter mAdapter;
    private int nowPage = 1;
    private List<MoreShopBean.moreCommoditys> mList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_shop);
        Intent intent = getIntent();
        flag = Integer.parseInt(intent.getStringExtra("flag"));
        hideBack(false);
        if (flag == 0){
            setTitleText("促销专区");
        }else if (flag == 1){
            setTitleText("热销推荐");
        }else if (flag == 2){
            setTitleText("新品上市");
        }
        initView();
        getdata();
    }

    private void initView() {
        more_shop_grid = (PullToRefreshGridView) findViewById(R.id.more_shop_grid);
        more_shop_grid.setMode(PullToRefreshBase.Mode.BOTH);
        mGridView = more_shop_grid.getRefreshableView();
        mAdapter = new MoreShopAdapter(this);
        mGridView.setAdapter(mAdapter);
        more_shop_grid.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                nowPage = 1;
                mList.clear();
                getdata();
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                nowPage++;
                getdata();
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MoreShopActivity.this,ShopDecActivity.class);
                intent.putExtra("rotateid",mList.get(position).commodityid);
                intent.putExtra("rotateIcon",mList.get(position).commodityIcon);
                startActivity(intent);
            }
        });
    }
    private void getdata() {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"getMoreCommoditys\",\"nowPage\":\"" + nowPage + "\",\"moreType\":\"" + flag + "\"}";
        params.put("json", json);
        dialog1.show();
        OkHttpUtils//
                .post()//
                .url(context.getString(R.string.url))//
                .params(params)//
                .build()//
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showMessageShort(context, e.getMessage());
                        dialog1.dismiss();
                        more_shop_grid.onRefreshComplete();
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Gson gson = new Gson();
                        dialog1.dismiss();
                        more_shop_grid.onRefreshComplete();
                        Log.i("MoreShopActivity", "onResponse: " + response.toString());
                        MoreShopBean moreShopBean = gson.fromJson(response, MoreShopBean.class);
                        if (moreShopBean.result.equals("1")) {
                            ToastUtils.showMessageShort(context, moreShopBean.resultNote);
                            return;
                        }
                        try {
                            JSONObject jsonObject = new JSONObject("jj");
                            if (moreShopBean.totalPage.equals("")){
                                ToastUtils.showMessageShort(context, "没有更多了");
                            }else {
                                if (nowPage > Integer.parseInt(moreShopBean.totalPage)) {
                                    ToastUtils.showMessageShort(context, "没有更多了");
                                    return;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        List<MoreShopBean.moreCommoditys> commodityslist = moreShopBean.moreCommoditys;
                        mList.addAll(commodityslist);
                        Log.i("ExchangeZoneActivity", "onResponse: " + mList.get(0).getCommodityTitle());
                        mAdapter.setMoreShop(mList);
                        mGridView.setAdapter(mAdapter);
                        more_shop_grid.onRefreshComplete();
                    }
                });
    }
}

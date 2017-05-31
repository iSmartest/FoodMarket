package com.lixin.foodmarket.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lixin.foodmarket.R;
import com.lixin.foodmarket.activity.ClassListActivity;
import com.lixin.foodmarket.activity.NowBuyActivity;
import com.lixin.foodmarket.activity.ShopDecActivity;
import com.lixin.foodmarket.adapter.ShopCartAdapter;
import com.lixin.foodmarket.bean.BaseBean;
import com.lixin.foodmarket.bean.ClassListBean;
import com.lixin.foodmarket.bean.GenerateOrderBean;
import com.lixin.foodmarket.bean.ShopCartBean;
import com.lixin.foodmarket.http.StringCallback;
import com.lixin.foodmarket.utils.OkHttpUtils;
import com.lixin.foodmarket.utils.SPUtils;
import com.lixin.foodmarket.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static com.lixin.foodmarket.activity.MyApplication.getApplication;
import static com.lixin.foodmarket.bean.GenerateOrderBean.*;

/**
 * Created by 小火
 * Create time on  2017/5/15
 * My mailbox is 1403241630@qq.com
 */

public class ShopCartFragment extends BaseFragment implements View.OnClickListener,ShopCartAdapter.CheckInterface, ShopCartAdapter.ModifyCountInterface  {
    private View view;
    private TextView mSure,mTotalPrice;
    private PullToRefreshListView shop_cart_list;
    private ShopCartAdapter shopCartAdapter;
    private int nowPage = 1;
    private String uid;
    private List<ShopCartBean.shop> mList;
    private double totalPrice = 0.00;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量
    private CheckBox mAllChoose;
    private String mMoney;
    private String orderId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shop_cart,null);
        uid = (String) SPUtils.get(getActivity(),"uid","");
        mList = new ArrayList<>();
        initView();
        getdata();
        return view;
    }

    private void initView() {
        mSure = (TextView) view.findViewById(R.id.text_shop_car_sure);
        mTotalPrice = (TextView) view.findViewById(R.id.text_fragment_shop_total_price);
        mSure.setOnClickListener(this);
        mAllChoose = (CheckBox) view.findViewById(R.id.ck_fragment_shop_all_chose);
        mAllChoose.setOnClickListener(this);
        shop_cart_list = (PullToRefreshListView)view.findViewById(R.id.shop_car_list);
        shopCartAdapter = new ShopCartAdapter();
        shopCartAdapter.setCheckInterface(this);
        shopCartAdapter.setModifyCountInterface(this);
        shop_cart_list.setAdapter(shopCartAdapter);
        shop_cart_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
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
                nowPage++;
                getdata();
            }
        });
        shop_cart_list.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                Toast.makeText(getApplication(), "已经到底了", Toast.LENGTH_SHORT).show();
            }
        });
        //在刷新时允许继续滑动
        shop_cart_list.setScrollingWhileRefreshingEnabled(true);
        shop_cart_list.setMode(PullToRefreshBase.Mode.BOTH);
        shop_cart_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        final String json="{\"cmd\":\"getShopCarListInfo\",\"nowPage\":\"" + nowPage +"\",\"uid\":\""
                + uid + "\"}";
        params.put("json", json);
        Log.i("ShopCartFragment", "onResponse: " + json.toString());
        dialog.show();
        OkHttpUtils.post().url(context.getString(R.string.url)).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showMessageShort(context, e.getMessage());
                dialog.dismiss();
                shop_cart_list.onRefreshComplete();
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("ShopCartFragment", "onResponse: " + response.toString());
                Gson gson = new Gson();
                dialog.dismiss();
                shop_cart_list.onRefreshComplete();
                ShopCartBean shopCartBean = gson.fromJson(response, ShopCartBean.class);
                if (shopCartBean.result.equals("1")) {
                    ToastUtils.showMessageShort(context, shopCartBean.resultNote);
                    return;
                }
                if (shopCartBean.totalPage < nowPage) {
                    ToastUtils.showMessageShort(context, "没有更多了");
                    return;
                }
                List<ShopCartBean.shop> shopList = shopCartBean.shop;
                Log.i("ShopCartFragment", "onResponse: " + shopList.get(0).getCommodityTitle());
                mList.addAll(shopList);
                shopCartAdapter.setShopCart(getActivity(),shopList,uid);
                shop_cart_list.setAdapter(shopCartAdapter);
                shop_cart_list.onRefreshComplete();
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_shop_car_sure:
                List<GenerateOrderBean.commoditys> list = new ArrayList<>();
                for (int i = 0; i < mList.size() ; i++) {
                    ShopCartBean.shop shopList = mList.get(i);
                    if (shopList.isChoosed){
                        GenerateOrderBean.commoditys comm = new GenerateOrderBean.commoditys(shopList.getCommodityid(),
                                shopList.getCommodityShooCarNum(),shopList.getCommodityFirstParam(),shopList.getCommoditySecondParam());
                        list.add(comm);
                    }
                }
                if (!list.isEmpty()){
                    getOrderData(list);
                }else {
                    ToastUtils.showMessageLong(getActivity(),"未选中任何商品!");
                }
                break;
            case R.id.ck_fragment_shop_all_chose:
                if (mList.size() != 0) {
                    if (mAllChoose.isChecked()) {
                        for (int i = 0; i < mList.size(); i++) {
                            mList.get(i).setChoosed(true);
                        }
                        shopCartAdapter.notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < mList.size(); i++) {
                            mList.get(i).setChoosed(false);
                        }
                        shopCartAdapter.notifyDataSetChanged();
                    }
                }
                statistics();
                break;
        }
    }

    private void getOrderData(List<GenerateOrderBean.commoditys> list) {
        Map<String, String> params = new HashMap<>();
        GenerateOrderBean generateOrderBean = new GenerateOrderBean("buyCommodity",uid,list);
        String json = new Gson().toJson(generateOrderBean);
        params.put("json", json);
        Log.i("ShopCartFragment", "getdata: " + json);
        dialog.show();
        OkHttpUtils.post().url(context.getString(R.string.url)).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showMessageShort(context, e.getMessage());
                dialog.dismiss();
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("ShopCartFragment", "onResponse: " + response.toString());
                Gson gson = new Gson();
                dialog.dismiss();
                BaseBean baseBean = gson.fromJson(response, BaseBean.class);
                if (baseBean.result.equals("1")) {
                    ToastUtils.showMessageShort(context, baseBean.resultNote);
                    return;
                }
                orderId = baseBean.getOrderId();
                mMoney = baseBean.getTotalMoney();
                Intent intent = new Intent(getActivity(),NowBuyActivity.class);
                startActivity(intent);
                Log.i("ShopCartFragment", "onResponse: " + orderId);
            }
        });
    }

    /**
     * 遍历list集合
     *
     * @return
     */
    private boolean isAllCheck() {
        for (ShopCartBean.shop group : mList) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }
    /**
     * 统计操作
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作
     * 3.给底部的textView进行数据填充
     */
    public void statistics() {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < mList.size(); i++) {
            ShopCartBean.shop shop = mList.get(i);
            if (shop.isChoosed()) {
                totalCount ++;
                totalPrice += Double.parseDouble(mList.get(i).commodityNewPrice) * Integer.parseInt(mList.get(i).getCommodityShooCarNum());
            }
        }
        mTotalPrice.setText(totalPrice + "");
    }

    @Override
    public void doIncrease(int position, View showCountView, boolean isChecked) {
        ShopCartBean.shop shopBean = mList.get(position);
        int currentCount = Integer.parseInt(shopBean.getCommodityShooCarNum());
        Log.i("currentCount", "doIncrease: " + currentCount);
        currentCount++;
        String num = String.valueOf(currentCount);
        shopBean.setCommodityShooCarNum(num);
        ((TextView) showCountView).setText(num);
        shopCartAdapter.notifyDataSetChanged();
        statistics();
    }

    @Override
    public void doDecrease(int position, View showCountView, boolean isChecked) {
        ShopCartBean.shop shopBean = mList.get(position);
        int currentCount = Integer.parseInt(shopBean.getCommodityShooCarNum());
        if (currentCount == 1) {
            return;
        }
        currentCount--;
        String num = String.valueOf(currentCount);
        shopBean.setCommodityShooCarNum(num);
        ((TextView) showCountView).setText(num);
        shopCartAdapter.notifyDataSetChanged();
        statistics();
    }

    @Override
    public void childDelete(int position) {
        mList.remove(position);
        shopCartAdapter.notifyDataSetChanged();
        statistics();
    }

    @Override
    public void checkGroup(int position, boolean isChecked) {
        mList.get(position).setChoosed(isChecked);
        if (isAllCheck()){
            mAllChoose.setChecked(true);
        } else{
            mAllChoose.setChecked(false);
        }
        shopCartAdapter.notifyDataSetChanged();
        statistics();
    }
}

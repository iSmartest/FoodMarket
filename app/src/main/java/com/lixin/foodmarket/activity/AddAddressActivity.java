package com.lixin.foodmarket.activity;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lixin.foodmarket.R;
import com.lixin.foodmarket.adapter.GroupAdapter;
import com.lixin.foodmarket.bean.BaseBean;
import com.lixin.foodmarket.dialog.ChangeAddressPopwindow;
import com.lixin.foodmarket.http.StringCallback;
import com.lixin.foodmarket.utils.OkHttpUtils;
import com.lixin.foodmarket.utils.SPUtils;
import com.lixin.foodmarket.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/5/18
 * My mailbox is 1403241630@qq.com
 */

public class AddAddressActivity extends BaseActivity{
    private EditText mName,mPhone,mCode,mDec,tvCommunity,tvBuilding,tvUnit;
    private TextView tvProvinceCity,tvSure;
    private PopupWindow popupWindow;
    private LinearLayout mProvinceCity;
    private View view;
    private ListView lv_group;
    private ArrayList groups;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_add_address);
        uid = (String) SPUtils.get(AddAddressActivity.this,"uid","");
        hideBack(false);
        setTitleText("添加收货地址");
        initView();
    }

    private void initView() {
        mName = (EditText) findViewById(R.id.edi_add_address_name);
        mPhone = (EditText) findViewById(R.id.edi_add_address_phone);
        mCode = (EditText) findViewById(R.id.edi_add_address_post_code);
        mDec = (EditText) findViewById(R.id.edit_add_address_dec);
        tvProvinceCity = (TextView) findViewById(R.id.text_add_address_province);
        mProvinceCity = (LinearLayout) findViewById(R.id.linear_add_address_province);
        mProvinceCity.setOnClickListener(this);
        tvCommunity = (EditText) findViewById(R.id.text_add_address_community);
        tvBuilding = (EditText) findViewById(R.id.text_add_address_building);
        tvUnit = (EditText) findViewById(R.id.text_add_address_unit);
        tvSure = (TextView) findViewById(R.id.text_add_address_submit);
        tvSure.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_add_address_province:
                ChangeAddressPopwindow mChangeAddressPopwindow = new ChangeAddressPopwindow(AddAddressActivity.this);
                mChangeAddressPopwindow.setAddress("河南", "郑州", "管城回族区");
                mChangeAddressPopwindow.showAtLocation(tvProvinceCity, Gravity.BOTTOM, 0, 0);
                mChangeAddressPopwindow
                        .setAddresskListener(new ChangeAddressPopwindow.OnAddressCListener() {

                            @Override
                            public void onClick(String province, String city, String area) {
                                // TODO Auto-generated method stub
                                Toast.makeText(AddAddressActivity.this,
                                        province + "-" + city + "-" + area,
                                        Toast.LENGTH_LONG).show();
                                tvProvinceCity.setText(province + city + area);
                            }
                        });
                break;
            case R.id.text_add_address_submit:
                submit();
                break;
        }
    }

    private void submit() {
        String receiverName = mName.getText().toString().trim();
        String receiverPhone = mPhone.getText().toString().trim();
        String receiverPostcode = mCode.getText().toString().trim();
        String receiverAddress = tvProvinceCity.getText().toString().trim();
        String receiverCell = tvCommunity.getText().toString().trim();
        String receiverBuildingNum = tvBuilding.getText().toString().trim();
        String receiverElement = tvUnit.getText().toString().trim();
        String receiverDetailAddress = mDec.getText().toString().trim();
        if (TextUtils.isEmpty(receiverName)){
            ToastUtils.showMessageLong(AddAddressActivity.this,"请填写收货人姓名");
        }else if (TextUtils.isEmpty(receiverPhone)){
            ToastUtils.showMessageLong(AddAddressActivity.this,"请填写收货人电话");
        }else if (TextUtils.isEmpty(receiverPostcode)){
            ToastUtils.showMessageLong(AddAddressActivity.this,"请填写收货人邮编");
        }else if (TextUtils.isEmpty(receiverAddress)){
            ToastUtils.showMessageLong(AddAddressActivity.this,"请选择收货人省市区");
        }else if (TextUtils.isEmpty(receiverCell)){
            ToastUtils.showMessageLong(AddAddressActivity.this,"请填写收货人小区");
        }else if (TextUtils.isEmpty(receiverBuildingNum)){
            ToastUtils.showMessageLong(AddAddressActivity.this,"请填写收货人楼栋");
        }else if (TextUtils.isEmpty(receiverElement)){
            ToastUtils.showMessageLong(AddAddressActivity.this,"请填写收货人单元");
        }else if (TextUtils.isEmpty(receiverDetailAddress)){
            ToastUtils.showMessageLong(AddAddressActivity.this,"请填写收货人街道");
        }else
        getReceiverData(receiverName,receiverPhone,receiverPostcode,receiverAddress,receiverCell,receiverBuildingNum,receiverElement,receiverDetailAddress);
    }

    private void getReceiverData(String receiverName, String receiverPhone, String receiverPostcode, String receiverAddress, String receiverCell, String receiverBuildingNum, String receiverElement, String receiverDetailAddress) {
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"addAdressInfo\",\"uid\":\"" + uid + "\",\"receiverName\":\"" + receiverName + "\",\"receiverPostcode\":\"" +
                "" + receiverPostcode + "\",\"receiverPhone\":\"" + receiverPhone + "\",\"receiverAddress\":\"" + receiverAddress + "\",\"receiverCell\":\""+ receiverCell +"\"" +
                ",\"receiverBuildingNum\":\"" + receiverBuildingNum + "\",\"receiverElement\":\"" + receiverElement +"\",\"receiverDetailAddress\":\"" + receiverDetailAddress +"\"}";
        params.put("json", json);
        Log.i("AddAddressActivity", "response: " + json.toString());
        dialog1.show();
        OkHttpUtils.post().url(context.getString(R.string.url)).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showMessageLong(context, "网络异常");
                dialog1.dismiss();
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("AddAddressActivity", "response: " + response.toString());
                Gson gson = new Gson();
                dialog1.dismiss();
                BaseBean baseBean = gson.fromJson(response, BaseBean.class);
                if (baseBean.getResult().equals("1")) {
                    ToastUtils.showMessageLong(AddAddressActivity.this, baseBean.getResultNote());
                }
                ToastUtils.showMessageLong(AddAddressActivity.this,"个人信息保存成功！");
            }
        });
    }

    private void popuwindShow(View parent) {
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.group_list, null);
            lv_group = (ListView) view.findViewById(R.id.lvGroup);
            groups = new ArrayList<String>();
            groups.add("全部");
            groups.add("我的微博");
            groups.add("好友");
            groups.add("亲人");
            groups.add("同学");
            groups.add("朋友");
            groups.add("陌生人");
            Collections.reverse(groups);
            GroupAdapter groupAdapter = new GroupAdapter(this, groups);
            lv_group.setAdapter(groupAdapter);
            int width = parent.getMeasuredWidth();
            popupWindow = new PopupWindow(view, 300, 320);
        }
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(parent);
        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (popupWindow != null)
                    popupWindow.dismiss();
            }
        });
    }
}

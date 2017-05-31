package com.lixin.foodmarket.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.foodmarket.R;
import com.lixin.foodmarket.activity.MyAddressActivity;
import com.lixin.foodmarket.activity.MyPersonalInformationActivity;
import com.lixin.foodmarket.bean.BaseBean;
import com.lixin.foodmarket.bean.MyAddressBean;
import com.lixin.foodmarket.http.StringCallback;
import com.lixin.foodmarket.utils.OkHttpUtils;
import com.lixin.foodmarket.utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static android.R.attr.resource;

/**
 * Created by 小火
 * Create time on  2017/5/18
 * My mailbox is 1403241630@qq.com
 */

public class MyAddressAdapter extends BaseAdapter {
    private Context context;
    private List<MyAddressBean.addressList> mList;
    private String uid;
    private CallBackValue callBackValue;
    private int handleType;
    public void setShopCart(Context context, List<MyAddressBean.addressList> mList,String uid) {
        this.context = context;
        this.mList = mList;
        this.uid = uid;
    }
    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_address,null);
            viewHolder = new ViewHolder();
            viewHolder.mAddress = (TextView) convertView.findViewById(R.id.tv_my_address_personal_information);
            viewHolder.mSetting = (TextView) convertView.findViewById(R.id.text_item_my_address_setting);
            viewHolder.mName = (TextView) convertView.findViewById(R.id.text_item_my_address_name);
            viewHolder.mPhone = (TextView) convertView.findViewById(R.id.text_item_my_address_phone);
            viewHolder.mSpot = (ImageView) convertView.findViewById(R.id.iv_item_my_address_spot);
            viewHolder.mEdit = (LinearLayout) convertView.findViewById(R.id.linear_item_my_address_edit);
            viewHolder.mDelete = (LinearLayout) convertView.findViewById(R.id.linear_item_my_address_delete);
            viewHolder.mDefault = (LinearLayout) convertView.findViewById(R.id.linear_item_my_address_setting);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final MyAddressBean.addressList addressList = mList.get(position);
        viewHolder.mName.setText(addressList.getReceiverName());
        viewHolder.mPhone.setText(addressList.getReceiverPhone());
        viewHolder.mAddress.setText(addressList.getReceiverAddress() +addressList.getReceiverCell()  +
         addressList.getReceiverBuildingNum() + "栋" + addressList.getReceiverElement() + "单元" + addressList.getReceiverDetailAddress());
        if (addressList.getReceiverSelected().equals("0")){
            viewHolder.mSpot.setImageResource(R.drawable.page_focuese);
            viewHolder.mSetting.setText("默认地址");
            viewHolder.mSetting.setTextColor(context.getResources().getColorStateList(R.color.btn_login_color));
        }else if (addressList.getReceiverSelected().equals("1")){
            viewHolder.mSpot.setImageResource(R.drawable.page_unfocused);
            viewHolder.mSetting.setText("设置为默认地址");
            viewHolder.mSetting.setTextColor(context.getResources().getColorStateList(R.color.black));
        }
        viewHolder.mDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleType = 0;
                getSettingDefault(addressList.getReceiverid(),handleType,position);
            }
        });
        viewHolder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleType = 1;
                getSettingDefault(addressList.getReceiverid(),handleType,position);
            }
        });
        return convertView;
    }
    private void getSettingDefault(String receiverid, final int handleType, final int position) {
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"handleAdress\",\"uid\":\"" + uid + "\",\"handleType\":\"" + handleType + "\",\"receiverid\":\"" +
                "" + receiverid + "\"}";
        params.put("json", json);
        Log.i("MyPersonalInformationActivity", "response: " + json.toString());
        OkHttpUtils.post().url(context.getString(R.string.url)).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showMessageLong(context, "网络异常");
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("MyPersonalInformationActivity", "response: " + response.toString());
                Gson gson = new Gson();
                BaseBean baseBean = gson.fromJson(response, BaseBean.class);
                if (baseBean.getResult().equals("1")) {
                    ToastUtils.showMessageLong(context, baseBean.getResultNote());
                }
                if (handleType == 0){
                    callBackValue.SendMessageValue("0",position);
                }else if (handleType == 1){
                    callBackValue.SendMessageValue("1",position);
                }
            }
        });
    }
    class ViewHolder{
        TextView mAddress,mSetting,mName,mPhone;
        ImageView mSpot;
        LinearLayout mEdit,mDelete,mDefault;
    }
    public interface CallBackValue{
        public void SendMessageValue(String strValue,int position);
    }
}

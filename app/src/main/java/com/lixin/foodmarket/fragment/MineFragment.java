package com.lixin.foodmarket.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lixin.foodmarket.R;
import com.lixin.foodmarket.activity.LoginActivity;
import com.lixin.foodmarket.activity.MyAllOrderActivity;
import com.lixin.foodmarket.activity.MyCollectionActivity;
import com.lixin.foodmarket.activity.MyCouponActivity;
import com.lixin.foodmarket.activity.MyMessageActivity;
import com.lixin.foodmarket.activity.MyPersonalInformationActivity;
import com.lixin.foodmarket.activity.MySettingActivity;
import com.lixin.foodmarket.activity.MyShareActivity;
import com.lixin.foodmarket.activity.MyWalletActivity;
import com.lixin.foodmarket.utils.SPUtils;

/**
 * Created by 小火
 * Create time on  2017/5/15
 * My mailbox is 1403241630@qq.com
 */

public class MineFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private LinearLayout mMyCoupon,mMyWallet;
    private int[] bigBGs = new int[]{
            R.drawable.my_wallet,
            R.drawable.my_wallet,
            R.drawable.my_wallet,
            R.drawable.my_wallet,
            R.drawable.my_wallet,
    };
    private String[] funcTxts;
    private View[] funcViews = new View[5];
    private LinearLayout mAllOrder,mMyCollection,mMyShare,mSetting;
    private ImageView mMessage;
    private TextView mName;
    private String uid;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine,container,false);
        initView();
        return view;
    }

    private void initView() {
        mMyCoupon = (LinearLayout) view.findViewById(R.id.linear_mine_my_coupon);
        mMyCoupon.setOnClickListener(this);
        mMyWallet = (LinearLayout) view.findViewById(R.id.linear_mine_my_wallet);
        mMyWallet.setOnClickListener(this);
        mAllOrder = (LinearLayout) view.findViewById(R.id.linear_all_order);
        mAllOrder.setOnClickListener(this);
        mMyCollection = (LinearLayout) view.findViewById(R.id.linear_my_collection);
        mMyCollection.setOnClickListener(this);
        mMyShare = (LinearLayout) view.findViewById(R.id.linear_my_share);
        mMyShare.setOnClickListener(this);
        mSetting = (LinearLayout) view.findViewById(R.id.linear_my_setting);
        mSetting.setOnClickListener(this);
        mMessage = (ImageView) view.findViewById(R.id.iv_mine_message);
        mMessage.setOnClickListener(this);
        mName = (TextView) view.findViewById(R.id.head_text);
        mName.setOnClickListener(this);
        funcTxts = getActivity().getResources().getStringArray(R.array.mine_functions);
        funcViews[0] = view.findViewById(R.id.text_wait_pay_money);
        funcViews[1] = view.findViewById(R.id.text_wait_goods_deliver);
        funcViews[2] = view.findViewById(R.id.text_wait_goods_receipt);
        funcViews[3] = view.findViewById(R.id.text_wait_evaluate);
        funcViews[4] = view.findViewById(R.id.text_wait_refund);

        for (int i = 0; i < funcViews.length; i++) {
            ImageView imageView = (ImageView) funcViews[i]
                    .findViewById(R.id.include_imagetext_view_image);
            TextView textView = (TextView) funcViews[i]
                    .findViewById(R.id.include_imagetext_textview_text);
            textView.setText(funcTxts[i]);
            imageView.setImageResource(bigBGs[i]);
            funcViews[i].setOnClickListener(this);
            funcViews[i].setId(i);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_mine_my_coupon:
                Intent intent = new Intent(getActivity(), MyCouponActivity.class);
                startActivity(intent);
                break;
            case R.id.linear_mine_my_wallet:
                Intent intent1 = new Intent(getActivity(),MyWalletActivity.class);
                startActivity(intent1);
                break;
            case R.id.linear_all_order:
                Intent intent2 = new Intent(getActivity(),MyAllOrderActivity.class);
                intent2.putExtra("temp","0");
                startActivity(intent2);
                break;
            case 0:
                Intent intent3 = new Intent(getActivity(),MyAllOrderActivity.class);
                intent3.putExtra("temp","0");
                startActivity(intent3);
                break;
            case 1:
                Intent intent4 = new Intent(getActivity(),MyAllOrderActivity.class);
                intent4.putExtra("temp","1");
                startActivity(intent4);
                break;
            case 2:
                Intent intent5 = new Intent(getActivity(),MyAllOrderActivity.class);
                intent5.putExtra("temp","2");
                startActivity(intent5);
                break;
            case 3:
                Intent intent6 = new Intent(getActivity(),MyAllOrderActivity.class);
                intent6.putExtra("temp","3");
                startActivity(intent6);
                break;
            case 4:
                Intent intent7 = new Intent(getActivity(),MyAllOrderActivity.class);
                intent7.putExtra("temp","4");
                startActivity(intent7);
                break;
            case R.id.linear_my_collection:
                Intent intent8 = new Intent(getActivity(),MyCollectionActivity.class);
                startActivity(intent8);
                break;
            case R.id.linear_my_share:
                Intent intent9 = new Intent(getActivity(),MyShareActivity.class);
                startActivity(intent9);
                break;
            case R.id.linear_my_setting:
                Intent intent10 = new Intent(getActivity(),MySettingActivity.class);
                startActivity(intent10);
                break;
            case R.id.iv_mine_message:
                Intent intent11 = new Intent(getActivity(),MyMessageActivity.class);
                startActivity(intent11);
                break;
            case R.id.head_text:
                Intent intent12 = new Intent();
                if (TextUtils.isEmpty((CharSequence) SPUtils.get(getActivity(),"uid",""))){
                    intent12.setClass(getActivity(), LoginActivity.class);
                }else {
                    intent12.setClass(getActivity(), MyPersonalInformationActivity.class);
                }
                startActivity(intent12);
                break;
        }
    }
}

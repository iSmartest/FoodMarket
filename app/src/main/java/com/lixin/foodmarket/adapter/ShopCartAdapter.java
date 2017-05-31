package com.lixin.foodmarket.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.foodmarket.R;
import com.lixin.foodmarket.bean.BaseBean;
import com.lixin.foodmarket.bean.ShopCartBean;
import com.lixin.foodmarket.http.StringCallback;
import com.lixin.foodmarket.tool.ImageManager;
import com.lixin.foodmarket.utils.OkHttpUtils;
import com.lixin.foodmarket.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/5/25
 * My mailbox is 1403241630@qq.com
 */

public class ShopCartAdapter extends BaseAdapter{
    private Context context;
    private List<ShopCartBean.shop> mList;
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    private String uid ;
    /**
     * 单选接口
     *
     * @param checkInterface
     */
    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    /**
     * 改变商品数量接口
     *
     * @param modifyCountInterface
     */
    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    public void setShopCart(Context context, List<ShopCartBean.shop> mList,String uid) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shop_cart,null);
            viewHolder = new ViewHolder();
            viewHolder.mName = (TextView) convertView.findViewById(R.id.tv_shop_cart_commodity_name);
            viewHolder.mFirstParam = (TextView) convertView.findViewById(R.id.tv_shop_cart_first_param);
            viewHolder.mSecondParam = (TextView) convertView.findViewById(R.id.tv_shop_cart_second_param);
            viewHolder.mNum = (TextView) convertView.findViewById(R.id.tv_shop_cart_num);
            viewHolder.mPrice = (TextView) convertView.findViewById(R.id.tv_shop_cart_price);
            viewHolder.mSub = (ImageView) convertView.findViewById(R.id.iv_shop_cart_sub);
            viewHolder.mAdd = (ImageView) convertView.findViewById(R.id.iv_shop_cart_add);
            viewHolder.mDelete = (ImageView) convertView.findViewById(R.id.iv_shop_cart_delete);
            viewHolder.mPicture = (ImageView) convertView.findViewById(R.id.iv_shop_cart_show_pic);
            viewHolder.mChose = (CheckBox) convertView.findViewById(R.id.ck_shop_cart_chose);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ShopCartBean.shop shopList = mList.get(position);
        viewHolder.mName.setText(shopList.getCommodityTitle());
        viewHolder.mFirstParam.setText(shopList.getCommodityFirstParam());
        viewHolder.mSecondParam.setText(shopList.getCommoditySecondParam());
        viewHolder.mNum.setText(shopList.getCommodityShooCarNum());
        viewHolder.mPrice.setText("￥" + shopList.getCommodityNewPrice());
        viewHolder.mChose.setChecked(shopList.isChoosed());
        String img = shopList.getCommodityIcon();
        if (TextUtils.isEmpty(img)){
            viewHolder.mPicture.setImageResource(R.drawable.image_fail_empty);
        }else {
            ImageManager.imageLoader.displayImage(img,viewHolder.mPicture,ImageManager.options3);
        }
        viewHolder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alert = new AlertDialog.Builder(context).create();
                alert.setTitle("操作提示");
                alert.setMessage("您确定要将这个商品从购物车中移除吗？");
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                modifyCountInterface.childDelete(position);//删除 目前只是从item中移除
                                getRemove(shopList.getCommodityid(),shopList.getCommodityFirstParam(),shopList.getCommoditySecondParam());
                            }
                        });
                alert.show();
            }
        });
        viewHolder.mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doIncrease(position, viewHolder.mNum, viewHolder.mChose.isChecked());//暴露增加接口
            }
        });
        viewHolder.mSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyCountInterface.doDecrease(position, viewHolder.mNum, viewHolder.mChose.isChecked());//暴露删减接口
            }
        });
        //单选框按钮
        viewHolder.mChose.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shopList.setChoosed(((CheckBox) v).isChecked());
                        checkInterface.checkGroup(position, ((CheckBox) v).isChecked());//向外暴露接口
                    }
                }
        );
        return convertView;
    }

    private void getRemove(String commodityid, String commodityFirstParam, String commoditySecondParam) {
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"deleCommodityFromCar\",\"commodityid\":\"" + commodityid + "\",\"uid\":\"" + uid + "\"," +
                "\"commodityFirstParam\":\"" + commodityFirstParam +"\",\"commoditySecondParam\":\"" + commoditySecondParam +"\"}";
        params.put("json", json);
        OkHttpUtils.post().url(context.getString(R.string.url)).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showMessageLong(context, "网络异常");
            }
            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                BaseBean baseBean = gson.fromJson(response, BaseBean.class);
                if (baseBean.getResult().equals("1")) {
                    ToastUtils.showMessageLong(context, baseBean.getResultNote());
                }
                ToastUtils.showMessageLong(context,"商品删除成功！");
            }
        });

    }

    class ViewHolder{
        TextView mName,mFirstParam,mSecondParam,mNum,mPrice;
        ImageView mSub,mAdd,mDelete,mPicture;
        CheckBox mChose;
    }
    /**
     * 复选框接口
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param position  元素位置
         * @param isChecked 元素选中与否
         */
        void checkGroup(int position, boolean isChecked);
    }


    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param position      组元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doIncrease(int position, View showCountView, boolean isChecked);

        /**
         * 删减操作
         *
         * @param position      组元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doDecrease(int position, View showCountView, boolean isChecked);

        /**
         * 删除子item
         *
         * @param position
         */
        void childDelete(int position);
    }
}

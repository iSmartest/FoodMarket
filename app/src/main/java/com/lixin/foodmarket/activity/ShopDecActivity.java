package com.lixin.foodmarket.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.foodmarket.R;
import com.lixin.foodmarket.bean.BaseBean;
import com.lixin.foodmarket.bean.ShopDecBean;
import com.lixin.foodmarket.bean.SkuBean;
import com.lixin.foodmarket.dialog.LoadingDialog;
import com.lixin.foodmarket.dialog.ProgressDialog;
import com.lixin.foodmarket.http.StringCallback;
import com.lixin.foodmarket.tool.ImageManager;
import com.lixin.foodmarket.utils.CommonLog;
import com.lixin.foodmarket.utils.OkHttpUtils;
import com.lixin.foodmarket.utils.SPUtils;
import com.lixin.foodmarket.utils.ToastUtils;
import com.lixin.foodmarket.view.FlowLayout;
import com.lixin.foodmarket.view.ImageSlideshow;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/5/17
 * My mailbox is 1403241630@qq.com
 */

public class ShopDecActivity extends Activity implements View.OnClickListener {
    private ImageSlideshow mGallery;
    private LinearLayout mBack,mShare,mCollection,mChoose;
    private TextView mShopName,mFreight,mSalesVolume,mNowPrice,mMarketValue,TextCollection,mBuyNow,mAddShoppingCart;
    private ImageView IvCollection;
    private WebView mWebView;
    protected Context context;
    protected Dialog dialog;
    private String rotateid;
    private String rotateIcon;
    private String uid;
    private int temp;
    private int collectType;
    private LoadingDialog dialog1;
    private int flag;
    private List<ShopDecBean.commoditySelectParameters> mList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_dec);
        Intent intent = getIntent();
        context = this;
        dialog = ProgressDialog.createLoadingDialog(context, "加载中.....");
        rotateid = intent.getStringExtra("rotateid");
        rotateIcon = intent.getStringExtra("rotateIcon");
        uid = (String) SPUtils.get(ShopDecActivity.this,"uid","");
        initView();
        getdata();
    }
    private void initView() {
        mGallery = (ImageSlideshow) findViewById(R.id.img_shop_gallery);
        IvCollection = (ImageView) findViewById(R.id.iv_shop_dec_collection);
        mBack = (LinearLayout) findViewById(R.id.ly_shop_back);
        mBack.setOnClickListener(this);
        mShare = (LinearLayout) findViewById(R.id.linear_shop_share);
        mShare.setOnClickListener(this);
        mCollection = (LinearLayout) findViewById(R.id.linear_shop_dec_collection);
        mCollection.setOnClickListener(this);
        mShopName = (TextView) findViewById(R.id.text_shop_dec_name);
        mFreight = (TextView) findViewById(R.id.text_shop_dec_freight);
        mSalesVolume = (TextView) findViewById(R.id.text_shop_dec_sales_volume);
        mNowPrice = (TextView) findViewById(R.id.text_shop_dec_now_price);
        mMarketValue = (TextView) findViewById(R.id.text_shop_dec_market_value);
        mMarketValue.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);//设置中划线并加清晰
        TextCollection = (TextView) findViewById(R.id.text_shop_dec_collection);
        mChoose = (LinearLayout) findViewById(R.id.linear_shop_dec_choose);
        mWebView = (WebView) findViewById(R.id.webView);
        mAddShoppingCart = (TextView) findViewById(R.id.text_shop_dec_add_shopping_cart);
        mAddShoppingCart.setOnClickListener(this);
        mBuyNow = (TextView) findViewById(R.id.text_shop_dec_buy_now);
        mBuyNow.setOnClickListener(this);
    }

    private void getdata() {
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"getCommoditysDetilInfo\",\"commodityid\":\"" + rotateid + "\",\"uid\":\"" + uid + "\"}";
        params.put("json", json);
        dialog.show();
        OkHttpUtils.post().url(context.getString(R.string.url)).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showMessageLong(context, "网络异常");
                dialog.dismiss();
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("ShopDecActivity", "response: " + response.toString());
                Gson gson = new Gson();
                dialog.dismiss();
                ShopDecBean shopDecBean = gson.fromJson(response, ShopDecBean.class);
                if (shopDecBean.getResult().equals("1")) {
                    ToastUtils.showMessageLong(ShopDecActivity.this, shopDecBean.getResultNote());
                }
                List<String> rotateCommodityPics = shopDecBean.rotateCommodityPics;
                initTopViewData(rotateCommodityPics);
                mShopName.setText(shopDecBean.getCommodityDescription());
                mFreight.setText("运费：" + shopDecBean.getCommodityFreight());
                mSalesVolume.setText("销量：" + shopDecBean.getCommoditysellerNum());
                mNowPrice.setText("现价：￥"+ shopDecBean.getCommodityNewPrice());
                mMarketValue.setText("￥" + shopDecBean.getCommodityOriginalPrice());
                initWeb(shopDecBean.getCommodityWebLink());
                List<ShopDecBean.commoditySelectParameters> commoditySelectParameters = shopDecBean.commoditySelectParameters;
                mList.addAll(commoditySelectParameters);
                if (shopDecBean.getCommodityIscollotion() == 1){
                    IvCollection.setImageResource(R.drawable.love_no_collection);
                    TextCollection.setText("收藏");
                    temp = 1;
                }else if (shopDecBean.getCommodityIscollotion() == 1){
                    IvCollection.setImageResource(R.drawable.shop_collection);
                    TextCollection.setText("取消收藏");
                    temp = 0;
                }
            }
        });
    }

    private void initWeb(String commodityWebLink) {
        dialog1 = new LoadingDialog(ShopDecActivity.this);
        WebSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setSupportZoom(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setDefaultTextEncodingName("utf-8");
        mWebSettings.setLoadsImagesAutomatically(true);
        mWebSettings.setJavaScriptEnabled(true);
        //改变加载文字的大小
        mWebSettings.setTextSize(WebSettings.TextSize.LARGER);
        saveData(mWebSettings);
        newWin(mWebSettings);
        mWebView.setWebChromeClient(webChromeClient);
        mWebView.setWebViewClient(webViewClient);
        if (!TextUtils.isEmpty(commodityWebLink)) {
            mWebView.loadUrl(commodityWebLink);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
        mWebView.pauseTimers();
    }
    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
        mWebView.resumeTimers();
    }
    private void newWin(WebSettings mWebSettings) {
        mWebSettings.setSupportMultipleWindows(false);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
    }
    private void saveData(WebSettings mWebSettings) {
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setDatabaseEnabled(true);
        mWebSettings.setAppCacheEnabled(true);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        mWebSettings.setAppCachePath(appCachePath);
    }

    WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    };

    WebChromeClient webChromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            try {
                if (newProgress == 100) {
                    if (dialog1 != null && dialog1.isShowing())
                        dialog1.dismiss();
                } else {
                    if (dialog1 != null && !dialog1.isShowing()) {
                        dialog1.show();
                    }
                }
            } catch (Exception e) {
                CommonLog.e(e);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {
            super.onReceivedIcon(view, icon);
        }

        @Override
        public void onGeolocationPermissionsHidePrompt() {
            super.onGeolocationPermissionsHidePrompt();
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }
        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(view);
            resultMsg.sendToTarget();
            return true;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mWebView != null) {
            mWebView.clearHistory();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.loadUrl("about:blank");
            mWebView.stopLoading();
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.destroy();
            mWebView = null;
        }
    }

    private void initTopViewData(List<String> rotateCommodityPics) {
        for (int i = 0; i < rotateCommodityPics.size(); i++) {
            mGallery.addImageTitle(rotateCommodityPics.get(i));
        }
        mGallery.setDotSpace(12);
        mGallery.setDotSize(12);
        mGallery.setDelay(3000);
        mGallery.setOnItemClickListener(new ImageSlideshow.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        });
        mGallery.commit();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ly_shop_back:
                finish();
                break;
            case R.id.linear_shop_share:
                //分享
                break;
            case R.id.text_shop_dec_buy_now:
                if (TextUtils.isEmpty(uid)){
                    ToastUtils.showMessageLong(ShopDecActivity.this,"请登录");
                }else {
                    flag = 0;
                    CommodityAttribute mCommodityAttribute = new CommodityAttribute(ShopDecActivity.this);
                    mCommodityAttribute.showAtLocation(mBuyNow, Gravity.BOTTOM, 0, 0);
                }
                break;
            case R.id.text_shop_dec_add_shopping_cart:
                if (TextUtils.isEmpty(uid)){
                    ToastUtils.showMessageLong(ShopDecActivity.this,"请登录");
                }else {
                    flag = 1;
                    CommodityAttribute mCommodityAttribute1 = new CommodityAttribute(ShopDecActivity.this);
                    mCommodityAttribute1.showAtLocation(mAddShoppingCart, Gravity.BOTTOM, 0, 0);
                }
                break;
            case R.id.linear_shop_dec_collection:
                if (TextUtils.isEmpty(uid)){
                    ToastUtils.showMessageLong(ShopDecActivity.this,"请登录");
                }else {
                    if (temp == 0){
                        collectType = 1;
                        getCollection(collectType);
                    }else if (temp == 1){
                        collectType = 0;
                        getCollection(collectType);
                    }
                }
                break;
        }
    }
    private void getCollection(final int collectType) {
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"collectCommoditys\",\"commodityid\":\"" + rotateid + "\",\"uid\":\"" + uid + "\",\"collectType\":" +
                "\"" + collectType + "\",}";
        params.put("json", json);
        dialog.show();
        OkHttpUtils.post().url(context.getString(R.string.url)).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showMessageLong(context, "网络异常");
                dialog.dismiss();
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("ShopDecActivity", "response: " + response.toString());
                Gson gson = new Gson();
                dialog.dismiss();
                BaseBean baseBean = gson.fromJson(response, BaseBean.class);
                if (baseBean.getResult().equals("1")) {
                    ToastUtils.showMessageLong(ShopDecActivity.this, baseBean.getResultNote());
                }
                if (collectType == 0){
                    ToastUtils.showMessageLong(ShopDecActivity.this, "收藏成功！");
                    IvCollection.setImageResource(R.drawable.shop_collection);
                    TextCollection.setText("取消收藏");
                    temp = 0;
                }else if (collectType == 1){
                    ToastUtils.showMessageLong(ShopDecActivity.this, "已取消收藏!");
                    IvCollection.setImageResource(R.drawable.love_no_collection);
                    TextCollection.setText("收藏");
                    temp = 1;
                }
            }
        });
    }

    /**
     * 商品属性PopupWind
     */
    public class CommodityAttribute extends PopupWindow {
        private LayoutInflater mInflater1;
        private View view;
        private TextView mTvOk, mNum, mShopStock, mShopPrice,mBack,mTitle,mTitle01;
        private ImageView shopReduce, shopAdd, mShopPicture;
        private String totalNum;
        private FlowLayout mFlavor,mFlavor01;
        private LinearLayout linear_sku;
        private int totalPrice = 0;
        private String[] title = new String[mList.get(0).getParameters().size()];
        private String[] title01 = new String[mList.get(1).getParameters().size()];
        private String commodityFirstParam;
        private String commoditySecondParam;
        public CommodityAttribute(final Activity context) {
            super(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.sku_popupwindow, null);
            linear_sku = (LinearLayout) view.findViewById(R.id.linear_sku);
            //确定
            mTvOk = (TextView) view.findViewById(R.id.popupwind_ok);
            if (flag == 1){
                mTvOk.setText("加入购物车");
            }
            //加 减 数量
            mNum = (TextView) view.findViewById(R.id.tv_shop_num);
            shopReduce = (ImageView) view.findViewById(R.id.iv_shop_reduce);
            shopAdd = (ImageView) view.findViewById(R.id.iv_shop_add);
            // 属性名称 属性
            mTitle = (TextView) view.findViewById(R.id.text_item_sku_title);
            mTitle01 = (TextView) view.findViewById(R.id.text_item_sku_title1);
            mFlavor = (FlowLayout) view.findViewById(R.id.tf_flavor);
            mFlavor01 = (FlowLayout) view.findViewById(R.id.tf_flavor1);

            //商品详情，图片 价格 库存
            mShopPicture = (ImageView) view.findViewById(R.id.iv_shop_picture1);
            mShopPrice = (TextView) view.findViewById(R.id.text_sku_shop_price);
            mShopStock = (TextView) view.findViewById(R.id.text_sku_shop_stock);

            mBack = (TextView) view.findViewById(R.id.text_sku_back);
            mInflater1 = LayoutInflater.from(context);

            if (mList.size() == 2){
                for (int i = 0; i < mList.get(0).getParameters().size(); i++) {
                    title[i] = mList.get(0).getParameters().get(i);
                }
                for (int i = 0; i < mList.get(1).getParameters().size(); i++) {
                    title01[i] = mList.get(1).getParameters().get(i);
                }
                commodityFirstParam =  title[0];
                commoditySecondParam =  title01[0];
                getSukdata();
                linear_sku.setVisibility(View.VISIBLE);
                mTitle.setText(mList.get(0).getParameterTitle());
                mTitle01.setText(mList.get(1).getParameterTitle());
                for (int i = 0; i < title.length; i++) {
                    final TextView tv = (TextView) mInflater1.inflate(R.layout.search_label_tv, mFlavor, false);
                    tv.setText(title[i]);
                    //点击事件
                    final int finalI = i;
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            commodityFirstParam =  title[finalI];
                            getSukdata();
                        }
                    });
                    mFlavor.addView(tv);
                }
                for (int i = 0; i < title01.length; i++) {
                    final TextView tv = (TextView) mInflater1.inflate(R.layout.search_label_tv, mFlavor01, false);
                    tv.setText(title01[i]);
                    //点击事件
                    final int finalI = i;
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            commoditySecondParam = title01[finalI];
                            getSukdata();
                        }
                    });
                    mFlavor01.addView(tv);
                }
            }else if (mList.size() == 1){
                linear_sku.setVisibility(View.GONE);
                mTitle.setText(mList.get(0).getParameterTitle());
            }
            //增加按钮
            shopAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int temp = Integer.parseInt(mNum.getText().toString().trim());
                    temp++;
                    mNum.setText("" + temp);
//                    int tempPrice = Integer.parseInt(mShopPrice.getText().toString().trim());
//                    totalPrice = totalPrice + tempPrice;
                }
            });
            //减少按钮
            shopReduce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int temp = Integer.parseInt(mNum.getText().toString().trim());
                    int tempPrice = Integer.parseInt(mShopPrice.getText().toString().trim());
                    temp--;
                    if (temp <= 1) {
                        mNum.setText("1");
//                        totalPrice = tempPrice;
                    } else {
                        mNum.setText("" + temp);
//                        totalPrice = totalPrice - tempPrice;
                    }
                }
            });
            mTvOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag == 0){
                        Intent intent = new Intent(ShopDecActivity.this,NowBuyActivity.class);
                        startActivity(intent);
                    }else {
                        int commodityShooCarNum = Integer.parseInt(mNum.getText().toString().trim());
                        getAddShoppingCart(commodityShooCarNum,commodityFirstParam,commoditySecondParam);
                    }
                    dismiss();
                }
            });
            mBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });

            // 设置SelectPicPopupWindow的View
            this.setContentView(view);
            // 设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(ViewPager.LayoutParams.MATCH_PARENT);
            // 设置SelectPicPopupWindow弹出窗体的高
            this.setHeight(ViewPager.LayoutParams.WRAP_CONTENT);
            // 在PopupWindow里面就加上下面两句代码，让键盘弹出时，不会挡住pop窗口。
            this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            // 设置popupWindow以外可以触摸
            this.setOutsideTouchable(true);
            // 以下两个设置点击空白处时，隐藏掉pop窗口
            this.setFocusable(true);
            this.setBackgroundDrawable(new BitmapDrawable());
            // 设置popupWindow以外为半透明0-1 0为全黑,1为全白
            backgroundAlpha(0.3f);
            // 添加pop窗口关闭事件
            this.setOnDismissListener(new poponDismissListener());
            // 设置动画--这里按需求设置成系统输入法动画
            this.setAnimationStyle(R.style.AnimBottom);
            // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    int height = view.findViewById(R.id.pop_layout)
                            .getTop();
                    int y = (int) event.getY();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y < height) {
                            dismiss();
                        }
                    }
                    return true;
                }
            });
        }

        private void getSukdata() {
            Map<String, String> params = new HashMap<>();
            final String json = "{\"cmd\":\"selectCommodityParameters\",\"commodityid\":\"" + rotateid + "\",\"commodityFirstParam\":\"" + commodityFirstParam + "\"," +
                    "\"commoditySecondParam\":\"" + commoditySecondParam + "\"}";
            params.put("json", json);
            dialog.show();
            Log.i("sku", "getSukdata: " + json.toString());
            OkHttpUtils.post().url(context.getString(R.string.url)).params(params)
                    .build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    ToastUtils.showMessageLong(context, "网络异常");
                    dialog.dismiss();
                }
                @Override
                public void onResponse(String response, int id) {
                    Log.i("sku", "response: " + response.toString());
                    Gson gson = new Gson();
                    dialog.dismiss();
                    SkuBean skuBean = gson.fromJson(response, SkuBean.class);
                    if (skuBean.getResult().equals("1")) {
                        ToastUtils.showMessageLong(ShopDecActivity.this, skuBean.getResultNote());
                    }
                    mShopPrice.setText(skuBean.getCommodityNewPrice());
                    mShopStock.setText("库存" + skuBean.getCommodityInventory() + "件");
                    if (TextUtils.isEmpty(skuBean.getCommodityIcon())){
                        ImageManager.imageLoader.displayImage(rotateIcon,mShopPicture,ImageManager.options3);
                    }else {
                        ImageManager.imageLoader.displayImage(skuBean.getCommodityIcon(),mShopPicture,ImageManager.options3);
                    }
                }
            });
        }
    }

    private void getAddShoppingCart(int commodityShooCarNum, String commodityFirstParam, String commoditySecondParam) {
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"addShopCar\",\"commodityid\":\"" + rotateid + "\",\"uid\":\"" + uid + "\",\"commodityShooCarNum\":" +
                "\"" + commodityShooCarNum + "\",\"commodityFirstParam\":\"" + commodityFirstParam +"\",\"commoditySecondParam\":\"" + commoditySecondParam +"\"}";
        params.put("json", json);
        Log.i("ShopDecActivity", "response: " + json.toString());
        dialog.show();
        OkHttpUtils.post().url(context.getString(R.string.url)).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showMessageLong(context, "网络异常");
                dialog.dismiss();
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("ShopDecActivity", "response: " + response.toString());
                Gson gson = new Gson();
                dialog.dismiss();
                BaseBean baseBean = gson.fromJson(response, BaseBean.class);
                if (baseBean.getResult().equals("1")) {
                    ToastUtils.showMessageLong(ShopDecActivity.this, baseBean.getResultNote());
                }
                ToastUtils.showMessageLong(ShopDecActivity.this,"加入购物车成功");
            }
        });
    }


    /**
         * PopouWindow设置添加屏幕的背景透明度
         *
         * @param bgAlpha
         */
        public void backgroundAlpha(float bgAlpha) {
            WindowManager.LayoutParams lp = this.getWindow().getAttributes();
            lp.alpha = bgAlpha;
            this.getWindow().setAttributes(lp);
        }

        /**
         * PopouWindow添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
         *
         * @author cg
         */
        class poponDismissListener implements PopupWindow.OnDismissListener {

            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        }
}

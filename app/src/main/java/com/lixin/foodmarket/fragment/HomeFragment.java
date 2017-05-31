package com.lixin.foodmarket.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.foodmarket.R;
import com.lixin.foodmarket.activity.EventDetailsWebActivity;
import com.lixin.foodmarket.activity.MoreShopActivity;
import com.lixin.foodmarket.activity.ShopDecActivity;
import com.lixin.foodmarket.adapter.HomeAdapter;
import com.lixin.foodmarket.adapter.MyGridViewAdpter;
import com.lixin.foodmarket.adapter.MyViewPagerAdapter;
import com.lixin.foodmarket.adapter.NewShopAdapter;
import com.lixin.foodmarket.bean.HomeBean;
import com.lixin.foodmarket.http.StringCallback;
import com.lixin.foodmarket.tool.ImageManager;
import com.lixin.foodmarket.utils.OkHttpUtils;
import com.lixin.foodmarket.utils.ToastUtils;
import com.lixin.foodmarket.view.ImageSlideshow;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/5/15
 * My mailbox is 1403241630@qq.com
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private View view;
    private ListView home_list;
    private View headView;
    private ImageSlideshow mSlideshow01,mSlideshow02;
    private ViewPager viewPager;
    private LinearLayout group,promotionShop,promotionShop01,promotionShop02,promotionShop03,hotShop,newShop;
    private View footView;
    private GridView newGrid;
    private EditText shopSearch;
    private ImageView[] ivPoints;//小圆点图片的集合
    private int totalPage; //总的页数
    private int mPageSize = 8; //每页显示的最大的数量
    private TextView mTitle01,mNowPrice01,mOldPrice01,mTitle02,mNowPrice02,mOldPrice02,mTitle03,mNowPrice03,mOldPrice03;
    private ImageView mPicture01,mPicture02,mPicture03;
    private List<View> viewPagerList;//GridView作为一个View对象添加到ViewPager集合中
    private HomeAdapter homeAdapter;
    private CallBackValue callBackValue;
    private NewShopAdapter mNewShopAdapter;
    private List<HomeBean.rotateTopCommoditys> rotateTopList;
    private List<HomeBean.rotateAdvertisement> rotateAdList;
    private List<HomeBean.classifyBottom> classifyBottomList;
    private List<HomeBean.promoteCommoditys> promoteCommoditysList;
    private List<HomeBean.newsCommoditys> newsCommoditysList;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callBackValue =(HomeFragment.CallBackValue) getActivity();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frgment_home,container,false);
        rotateTopList = new ArrayList<>();
        rotateAdList = new ArrayList<>();
        classifyBottomList = new ArrayList<>();
        promoteCommoditysList = new ArrayList<>();
        newsCommoditysList = new ArrayList<>();
        getdata();
        initView();
        return view;
    }

    private void initView() {
        home_list = (ListView) view.findViewById(R.id.home_list);
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.home_list_head,null);
        shopSearch = (EditText) headView.findViewById(R.id.a_shop_edt_search);
        mSlideshow01 = (ImageSlideshow) headView.findViewById(R.id.img_home_gallery);
        mSlideshow02 = (ImageSlideshow) headView.findViewById(R.id.img_store_gallery1);
        viewPager =(ViewPager)headView.findViewById(R.id.viewpager);
        group = (LinearLayout) headView.findViewById(R.id.points);

        mTitle01 = (TextView) headView.findViewById(R.id.text_shop_title01);
        mNowPrice01 = (TextView) headView.findViewById(R.id.text_shop_now_price01);
        mOldPrice01 = (TextView) headView.findViewById(R.id.text_shop_old_price01);
        mOldPrice01.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
        mTitle02 = (TextView) headView.findViewById(R.id.text_shop_title02);
        mNowPrice02 = (TextView) headView.findViewById(R.id.text_shop_now_price02);
        mOldPrice02 = (TextView) headView.findViewById(R.id.text_shop_old_price02);
        mOldPrice02.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
        mTitle03 = (TextView) headView.findViewById(R.id.text_shop_title03);
        mNowPrice03 = (TextView) headView.findViewById(R.id.text_shop_now_price03);
        mOldPrice03 = (TextView) headView.findViewById(R.id.text_shop_old_price03);
        mOldPrice03.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
        mPicture01 = (ImageView) headView.findViewById(R.id.iv_shop_picture01);
        mPicture02 = (ImageView) headView.findViewById(R.id.iv_shop_picture02);
        mPicture03 = (ImageView) headView.findViewById(R.id.iv_shop_picture03);


        promotionShop = (LinearLayout) headView.findViewById(R.id.linear_shop_promotion);
        promotionShop.setOnClickListener(this);
        promotionShop01 = (LinearLayout) headView.findViewById(R.id.linear_promotion_01);
        promotionShop01.setOnClickListener(this);
        promotionShop02 = (LinearLayout) headView.findViewById(R.id.linear_promotion_02);
        promotionShop02.setOnClickListener(this);
        promotionShop03 = (LinearLayout) headView.findViewById(R.id.linear_promotion_03);
        promotionShop03.setOnClickListener(this);
        hotShop = (LinearLayout) headView.findViewById(R.id.linear_shop_hot);
        hotShop.setOnClickListener(this);
        footView = LayoutInflater.from(getActivity()).inflate(R.layout.home_list_foot,null);
        newShop = (LinearLayout) footView.findViewById(R.id.linear_shop_new);
        newShop.setOnClickListener(this);
        newGrid = (GridView) footView.findViewById(R.id.grid_new_shop);
        mNewShopAdapter = new NewShopAdapter();
        newGrid.setAdapter(mNewShopAdapter);
        newGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),ShopDecActivity.class);
                intent.putExtra("rotateid",newsCommoditysList.get(position).getCommodityid());
                intent.putExtra("rotateIcon",newsCommoditysList.get(position).getCommodityIcon());
                startActivity(intent);
            }
        });
        home_list.addHeaderView(headView);
        home_list.addFooterView(footView);
        homeAdapter = new HomeAdapter();
        home_list.setAdapter(homeAdapter);
    }
    // 类别展示
    private void initData(List<HomeBean.classifyBottom> classifyBottomList) {
        //总的页数向上取整
        totalPage = (int) Math.ceil(classifyBottomList.size() * 1.0 / mPageSize);
        viewPagerList = new ArrayList<View>();
        for(int i = 0; i < totalPage; i++){
            //每个页面都是inflate出一个新实例
            final View view = LayoutInflater.from(context).inflate( R.layout.item_gridview, null);
            final GridView gridView = (GridView) view.findViewById(R.id.gridView);
            gridView.setAdapter(new MyGridViewAdpter(getActivity(), classifyBottomList, i, mPageSize));
            //添加item点击监听
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        ClassFragment userFragment = new ClassFragment ();
                        Bundle args = new Bundle();
                        args.putInt("index", position);
                        Log.i("111", "传值： " + position);
                        userFragment.setArguments(args);
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.activity_new_main_layout_content, userFragment);
                        transaction.commit();
                        callBackValue.SendMessageValue("6");
                }
            });
            //每一个GridView作为一个View对象添加到ViewPager集合中
            viewPagerList.add(view);
        }
        //设置ViewPager适配器
        viewPager.setAdapter(new MyViewPagerAdapter(viewPagerList));
        //添加小圆点
        ivPoints = new ImageView[totalPage];
        if (totalPage > 1){
            for(int i = 0; i < totalPage; i++){
                //循坏加入点点图片组
                ivPoints[i] = new ImageView(context);
                if(i==0){
                    ivPoints[i].setImageResource(R.drawable.page_focuese);
                }else {
                    ivPoints[i].setImageResource(R.drawable.page_unfocused);
                }
                ivPoints[i].setPadding(8, 8, 8, 8);
                group.addView(ivPoints[i]);
            }
        }else {
            return;
        }

        //设置ViewPager的滑动监听，主要是设置点点的背景颜色的改变
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                //currentPage = position;
                for(int i=0 ; i < totalPage; i++){
                    if(i == position){
                        ivPoints[i].setImageResource(R.drawable.page_focuese);
                    }else {
                        ivPoints[i].setImageResource(R.drawable.page_unfocused);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_shop_promotion:
                Intent intent0 = new Intent(getActivity(),MoreShopActivity.class);
                intent0.putExtra("flag","0");
                startActivity(intent0);
                break;
            case R.id.linear_shop_hot:
                Intent intent1 = new Intent(getActivity(),MoreShopActivity.class);
                intent1.putExtra("flag","1");
                startActivity(intent1);
                break;
            case R.id.linear_shop_new:
                Intent intent2 = new Intent(getActivity(),MoreShopActivity.class);
                intent2.putExtra("flag","2");
                startActivity(intent2);
                break;
            case R.id.linear_promotion_01:
                Intent intent3 = new Intent(getActivity(),ShopDecActivity.class);
                intent3.putExtra("rotateid",promoteCommoditysList.get(2).getCommodityid());
                intent3.putExtra("rotateIcon",promoteCommoditysList.get(2).getCommodityIcon());
                startActivity(intent3);
                break;
            case R.id.linear_promotion_02:
                Intent intent4 = new Intent(getActivity(),ShopDecActivity.class);
                intent4.putExtra("rotateid",promoteCommoditysList.get(2).getCommodityid());
                intent4.putExtra("rotateIcon",promoteCommoditysList.get(2).getCommodityIcon());
                startActivity(intent4);
                break;
            case R.id.linear_promotion_03:
                Intent intent5 = new Intent(getActivity(),ShopDecActivity.class);
                intent5.putExtra("rotateid",promoteCommoditysList.get(2).getCommodityid());
                intent5.putExtra("rotateIcon",promoteCommoditysList.get(2).getCommodityIcon());
                startActivity(intent5);
                break;
        }
    }

    //写一个回调接口
    public interface CallBackValue{
        public void SendMessageValue(String strValue);
    }
    //顶部轮播
    private void initTopViewData(final List<HomeBean.rotateTopCommoditys> rotateTopList) {
        for (int i = 0; i < rotateTopList.size(); i++) {
            mSlideshow01.addImageTitle(rotateTopList.get(i).getRotateIcon());
        }
        mSlideshow01.setDotSpace(12);
        mSlideshow01.setDotSize(12);
        mSlideshow01.setDelay(3000);
        mSlideshow01.setOnItemClickListener(new ImageSlideshow.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int type = rotateTopList.get(position).getRotateType();
                if (type == 0){
                    Intent intent = new Intent(context, EventDetailsWebActivity.class);
                    intent.putExtra("isStoreDetailsOrCheckviolation","2");
                    intent.putExtra(EventDetailsWebActivity.URL, rotateTopList.get(position).getRotateid());
                    startActivity(intent);
                }else if (type == 1){
                    Intent intent = new Intent(context, ShopDecActivity.class);
                    intent.putExtra("rotateid",rotateTopList.get(position).getRotateid());
                    intent.putExtra("rotateIcon",rotateTopList.get(position).getRotateIcon());
                    startActivity(intent);
                }
            }
        });
        mSlideshow01.commit();
    }
    // 中部轮播
    private void initAdViewData(List<HomeBean.rotateAdvertisement> rotateAdList) {
        for (int i = 0; i < rotateAdList.size(); i++) {
            mSlideshow02.addImageTitle(rotateAdList.get(i).getAdvertisementIcon());
        }
        mSlideshow02.setDotSpace(12);
        mSlideshow02.setDotSize(12);
        mSlideshow02.setDelay(3000);
        mSlideshow02.setOnItemClickListener(new ImageSlideshow.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
        mSlideshow02.commit();
    }
    private void getdata() {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"getMianCommoditysInfo\"}";
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
                Log.i("HomeFragment", "response: " + response.toString());
                Gson gson = new Gson();
                dialog.dismiss();
                HomeBean homeBean = gson.fromJson(response,HomeBean.class);
                if (homeBean.getResult().equals("1")){
                    ToastUtils.showMessageLong(getActivity(),homeBean.getResultNote());
                }

                List<HomeBean.rotateTopCommoditys> rotateTopCommoditys = homeBean.rotateTopCommoditys;//顶部轮播图集合
                rotateTopList.addAll(rotateTopCommoditys);
                initTopViewData(rotateTopList);
                List<HomeBean.rotateAdvertisement> rotateAdvertisement = homeBean.rotateAdvertisement;//中部轮播图
                rotateAdList.addAll(rotateAdvertisement);
                initAdViewData(rotateAdList);
                List<HomeBean.classifyBottom> classifyBottom = homeBean.classifyBottom;//类别
                classifyBottomList.addAll(classifyBottom);
                initData(classifyBottom);
                List<HomeBean.hotCommoditys> hotCommoditys = homeBean.hotCommoditys;
                homeAdapter.setHome(getActivity(),hotCommoditys);
                home_list.setAdapter(homeAdapter);
                List<HomeBean.newsCommoditys> newsCommoditys = homeBean.newsCommoditys;
                newsCommoditysList.addAll(newsCommoditys);
                mNewShopAdapter.setNewShop(getActivity(),newsCommoditysList);
                newGrid.setAdapter(mNewShopAdapter);
                List<HomeBean.promoteCommoditys> promoteCommoditys = homeBean.promoteCommoditys;
                promoteCommoditysList.addAll(promoteCommoditys);
                mTitle01.setText(promoteCommoditys.get(0).getCommodityTitle());
                mTitle02.setText(promoteCommoditys.get(1).getCommodityTitle());
                mTitle03.setText(promoteCommoditys.get(2).getCommodityTitle());

                mNowPrice01.setText("现价:" + promoteCommoditys.get(0).getCommodityNewPrice());
                mNowPrice02.setText("现价:" + promoteCommoditys.get(1).getCommodityNewPrice());
                mNowPrice03.setText("现价:" + promoteCommoditys.get(2).getCommodityNewPrice());

                mOldPrice01.setText("市场价:" + promoteCommoditys.get(0).getCommodityOriginalPrice());
                mOldPrice02.setText("市场价:" + promoteCommoditys.get(1).getCommodityOriginalPrice());
                mOldPrice03.setText("市场价:" + promoteCommoditys.get(2).getCommodityOriginalPrice());
                String img1 = promoteCommoditys.get(0).getCommodityIcon();
                String img2 = promoteCommoditys.get(1).getCommodityIcon();
                String img3 = promoteCommoditys.get(2).getCommodityIcon();
                if (TextUtils.isEmpty(img1)){
                    mPicture01.setImageResource(R.drawable.image_fail_empty);
                }else {
                    ImageManager.imageLoader.displayImage(img1,mPicture01,ImageManager.options3);
                }
                if (TextUtils.isEmpty(img2)){
                    mPicture02.setImageResource(R.drawable.image_fail_empty);
                }else {
                    ImageManager.imageLoader.displayImage(img2,mPicture02,ImageManager.options3);
                }
                if (TextUtils.isEmpty(img3)){
                    mPicture03.setImageResource(R.drawable.image_fail_empty);
                }else {
                    ImageManager.imageLoader.displayImage(img3,mPicture03,ImageManager.options3);
                }
            }
        });
    }


}

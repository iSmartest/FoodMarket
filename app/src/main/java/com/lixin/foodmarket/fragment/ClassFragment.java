package com.lixin.foodmarket.fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lixin.foodmarket.R;
import com.lixin.foodmarket.activity.ClassListActivity;
import com.lixin.foodmarket.activity.ShopDecActivity;
import com.lixin.foodmarket.adapter.FirstAdapter;
import com.lixin.foodmarket.adapter.GridAdapter;
import com.lixin.foodmarket.bean.ClassBean;
import com.lixin.foodmarket.http.StringCallback;
import com.lixin.foodmarket.tool.ImageManager;
import com.lixin.foodmarket.utils.OkHttpUtils;
import com.lixin.foodmarket.utils.ToastUtils;
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

public class ClassFragment extends BaseFragment{
    private View view;
    private ListView firstList;
    private GridView secondGrid;
    private EditText keySearch;
    private ImageView mImage;
    private FirstAdapter firstAdapter;
    private GridAdapter secondAdapter;
    private List<ClassBean.classifyMeun> classifyMeunList;
    private List<ClassBean.classifyMeun.meun> meunList;
    private int defClass1;
    private String rotateid;
    private String rotateIcon;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_class,null);
        classifyMeunList = new ArrayList<>();
        if (getArguments() != null) {
            defClass1 = getArguments().getInt("index");
            Log.i("111", "接收：" + defClass1);
        }
        Log.i("111", "什么鬼：" + defClass1);
        initView();
        getdata();
        return view;
    }
    private void initView() {
        firstList = (ListView) view.findViewById(R.id.first_list);
        mImage = (ImageView) view.findViewById(R.id.iv_class_icon);
        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ShopDecActivity.class);
                intent.putExtra("rotateid",rotateid);
                intent.putExtra("rotateIcon",rotateIcon);
                startActivity(intent);
            }
        });
        secondGrid = (GridView) view.findViewById(R.id.second_list);
        keySearch = (EditText) view.findViewById(R.id.a_key_edt_search);
        firstAdapter = new FirstAdapter();
        firstAdapter.setDefSelect(defClass1);
        firstAdapter.notifyDataSetChanged();
        firstList.setAdapter(firstAdapter);
        firstList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                firstAdapter.setDefSelect(position);
                firstAdapter.notifyDataSetChanged();
                secondAdapter.setGrid(getActivity(),classifyMeunList.get(position).getMeun());
                secondGrid.setAdapter(secondAdapter);
                String img = classifyMeunList.get(position).getCommodityIcon();
                if (TextUtils.isEmpty(img)){
                    mImage.setImageResource(R.drawable.image_fail_empty);
                }else {
                    ImageManager.imageLoader.displayImage(img, mImage, ImageManager.options3);
                }
                rotateid = classifyMeunList.get(position).getCommodityid();
                rotateIcon = classifyMeunList.get(position).getCommodityIcon();
                meunList = classifyMeunList.get(position).getMeun();
            }
        });
        secondAdapter = new GridAdapter(getActivity());
        secondGrid.setAdapter(secondAdapter);
        secondGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),ClassListActivity.class);
                intent.putExtra("meunid",meunList.get(position).getMeunid());
                startActivity(intent);
            }
        });
    }
    private void getdata() {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"getCommodityClassifyInfo\"}";
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
                Log.i("ClassFragment", "response: " + response.toString());
                Gson gson = new Gson();
                dialog.dismiss();
                ClassBean classBean = gson.fromJson(response,ClassBean.class);
                if (classBean.getResult().equals("1")){
                    ToastUtils.showMessageLong(getActivity(),classBean.getResultNote());
                }
                List<ClassBean.classifyMeun> classifyMeun = classBean.classifyMeun;
                classifyMeunList.addAll(classifyMeun);
                firstAdapter.setFirst(getActivity(),classifyMeunList);
                firstList.setAdapter(firstAdapter);
                secondAdapter.setGrid(getActivity(),classifyMeunList.get(defClass1).getMeun());
                secondGrid.setAdapter(secondAdapter);
                String img = classifyMeunList.get(defClass1).getCommodityIcon();
                if (TextUtils.isEmpty(img)){
                    mImage.setImageResource(R.drawable.image_fail_empty);
                }else {
                    ImageManager.imageLoader.displayImage(img,mImage,ImageManager.options3);
                }
                rotateid = classifyMeunList.get(defClass1).getCommodityid();
                rotateIcon = classifyMeunList.get(defClass1).getCommodityIcon();
                meunList = classifyMeunList.get(defClass1).getMeun();
            }
        });
    }
}

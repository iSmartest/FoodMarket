package com.lixin.foodmarket.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.foodmarket.R;
import com.lixin.foodmarket.bean.BaseBean;
import com.lixin.foodmarket.http.StringCallback;
import com.lixin.foodmarket.tool.ImageManager;
import com.lixin.foodmarket.utils.ImageUtil;
import com.lixin.foodmarket.utils.OkHttpUtils;
import com.lixin.foodmarket.utils.SPUtils;
import com.lixin.foodmarket.utils.ToastUtils;
import com.lixin.foodmarket.view.RoundedImageView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/5/19
 * My mailbox is 1403241630@qq.com
 */

public class MyPersonalInformationActivity extends BaseActivity{
    private RelativeLayout mAddressManagement,mNickName,mSex;
    private TextView tvNickName,tvSex,mSubmit;
    private LinearLayout mHeader;
    private AlertDialog builder; //底部弹出菜单
    private RoundedImageView iv_personal_icon;
    protected static final int CHOOSE_PICTURE = 0;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 2;
    protected static Uri tempUri;
    private String uid;
    private int userSex;
    private String HeadPortrait;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        uid = (String) SPUtils.get(MyPersonalInformationActivity.this,"uid","");
        hideBack(false);
        setTitleText("个人信息");
        initView();
        getPersonalInformation();
    }
    private void initView() {
        mAddressManagement = (RelativeLayout) findViewById(R.id.rl_personal_information_address_management);
        iv_personal_icon = (RoundedImageView) findViewById(R.id.a_my_info_iv_header);
        mAddressManagement.setOnClickListener(this);
        mNickName = (RelativeLayout) findViewById(R.id.rl_personal_information_nick_name);
        mNickName.setOnClickListener(this);
        mSex = (RelativeLayout) findViewById(R.id.rl_personal_information_sex);
        mSex.setOnClickListener(this);
        mHeader = (LinearLayout) findViewById(R.id.a_my_info_lay_header);
        mHeader.setOnClickListener(this);
        tvNickName = (TextView) findViewById(R.id.text_personal_information_nick_name);
        tvSex = (TextView) findViewById(R.id.text_personal_information_sex);
        mSubmit = (TextView) findViewById(R.id.text_personal_information_submit);
        mSubmit.setOnClickListener(this);
    }
    private void getPersonalInformation() {
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"getUserDeatilInfo\",\"uid\":\"" + uid + "\"}";
        params.put("json", json);
        Log.i("MyPersonalInformationActivity", "response: " + json.toString());
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
                Log.i("MyPersonalInformationActivity", "response: " + response.toString());
                Gson gson = new Gson();
                dialog1.dismiss();
                BaseBean baseBean = gson.fromJson(response, BaseBean.class);
                if (baseBean.getResult().equals("1")) {
                    ToastUtils.showMessageLong(MyPersonalInformationActivity.this, baseBean.getResultNote());
                }
                if (TextUtils.isEmpty(baseBean.getUserIcon())){
                    iv_personal_icon.setImageResource(R.drawable.image_fail_empty);
                }else{
                    ImageManager.imageLoader.displayImage(baseBean.getUserIcon(),iv_personal_icon,ImageManager.options3);
                }
                tvNickName.setText(baseBean.getUserName());
                if (baseBean.getUserSex().equals("0")){
                    tvSex.setText("男");
                }else if (baseBean.getUserSex().equals("1")){
                    tvNickName.setText("女");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_personal_information_address_management:
                Intent intent = new Intent(MyPersonalInformationActivity.this,MyAddressActivity.class);
                startActivity(intent);
                break;
            case R.id.a_my_info_lay_header:
                showChoosePicDialog();
                break;
            case R.id.rl_personal_information_nick_name:
                Intent intent1 = new Intent(MyPersonalInformationActivity.this,ModifyNameActivity.class);
                startActivityForResult(intent1,3001);
                break;
            case R.id.rl_personal_information_sex:
                new AlertDialog.Builder(MyPersonalInformationActivity.this).setMessage("性别")
                        .setPositiveButton("男", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tvSex.setText("男");
                                userSex = 0;
                            }
                        }).setNegativeButton("女", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvSex.setText("女");
                        userSex = 1;
                    }
                }).show();
                break;
            case R.id.text_personal_information_submit:
                submit();
                break;
            case R.id.tv_album://相册
                Intent openAlbumIntent = new Intent(
                        Intent.ACTION_GET_CONTENT);
                openAlbumIntent.setType("image/*");
                startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
                builder.dismiss();
                break;
            case R.id.tv_photograph://拍照
                Intent openCameraIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                tempUri = Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "image.jpg"));
                // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
                startActivityForResult(openCameraIntent, TAKE_PICTURE);
                builder.dismiss();
                break;
            case R.id.tv_cancel://取消
                builder.dismiss();
                break;
        }
    }
    private void submit() {
        String nickname = tvNickName.getText().toString().trim();
        if (TextUtils.isEmpty(nickname)) {
            ToastUtils.showMessageLong(context, "请输入昵称");
            return;
        }

        String sex = tvSex.getText().toString().trim();
        if (TextUtils.isEmpty(sex)) {
            ToastUtils.showMessageLong(context, "请选择性别");
            return;
        }
        getSubmitPersonalInformation(nickname,userSex,HeadPortrait);
    }

    private void getSubmitPersonalInformation(String nickname, int userSex, String headPortrait) {
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"commitUserInfoDeatil\",\"uid\":\"" + uid + "\",\"userIcon\":\"" + headPortrait + "\",\"userName\":\"" +
                "" + nickname + "\",\"userSex\":\"" + userSex + "\"}";
        params.put("json", json);
        Log.i("MyPersonalInformationActivity", "response: " + json.toString());
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
                Log.i("MyPersonalInformationActivity", "response: " + response.toString());
                Gson gson = new Gson();
                dialog1.dismiss();
                BaseBean baseBean = gson.fromJson(response, BaseBean.class);
                if (baseBean.getResult().equals("1")) {
                    ToastUtils.showMessageLong(MyPersonalInformationActivity.this, baseBean.getResultNote());
                }
                ToastUtils.showMessageLong(MyPersonalInformationActivity.this,"个人信息保存成功！");
            }
        });
    }

    private void showChoosePicDialog() {
        builder = new AlertDialog.Builder(context, R.style.Dialog).create(); // 先得到构造器
        builder.show();
        LayoutInflater factory = LayoutInflater.from(context);
        View view = factory.inflate(R.layout.dialog_photo_upload, null);
        builder.getWindow().setContentView(view);
        TextView tv_album = (TextView) view.findViewById(R.id.tv_album);
        tv_album.setOnClickListener(this);
        TextView tv_photograph = (TextView) view.findViewById(R.id.tv_photograph);
        tv_photograph.setOnClickListener(this);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);
        Window dialogWindow = builder.getWindow();
        dialogWindow.setWindowAnimations(R.style.Dialog);
        dialogWindow.setGravity(Gravity.BOTTOM);//显示在底部
        WindowManager m = getWindowManager();
        Display display = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        Point size = new Point();
        display.getSize(size);
        p.width = size.x;
        dialogWindow.setAttributes(p);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case TAKE_PICTURE:
                    startPhotoZoom(tempUri); // 开始对图片进行裁剪处理
                    break;
                case CHOOSE_PICTURE:
                    startPhotoZoom(data.getData()); // 开始对图片进行裁剪处理
                    break;
                case CROP_SMALL_PICTURE:
                    if (data != null) {
                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
        if(requestCode == 3001 && resultCode == 3002){
            String result=data.getStringExtra("result");
            tvNickName.setText(result);
        }
    }
    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    protected void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "The uri is not exist.");
        }
        tempUri = uri;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CROP_SMALL_PICTURE);
    }

    /**
     * 保存裁剪之后的图片数据
     *
     * @param
     *
     * @param
     */
    protected void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            photo = ImageUtil.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
            iv_personal_icon.setImageBitmap(photo);
            uploadPic(photo);
        }
    }

    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了

        String imagePath = ImageUtil.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), String
                .valueOf(System.currentTimeMillis()));
        Log.i("imagePath", "uploadPic: " + imagePath);
        if(imagePath != null){
            // 拿着imagePath上传了
            // ..
        HeadPortrait = imagePath;
        }
    }
}

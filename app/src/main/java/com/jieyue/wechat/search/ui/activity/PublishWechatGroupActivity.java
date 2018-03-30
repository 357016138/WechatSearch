package com.jieyue.wechat.search.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.common.BaseActivity;
import com.jieyue.wechat.search.common.ShareData;
import com.jieyue.wechat.search.network.HttpType;
import com.jieyue.wechat.search.network.RequestParams;
import com.jieyue.wechat.search.network.ResultData;
import com.jieyue.wechat.search.network.Task;
import com.jieyue.wechat.search.network.UrlConfig;
import com.jieyue.wechat.search.utils.DeviceUtils;
import com.jieyue.wechat.search.utils.LogUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PublishWechatGroupActivity extends BaseActivity {
    @BindView(R.id.et_title)
    EditText et_title;
    @BindView(R.id.et_wechat_num)
    EditText et_wechat_num;
    @BindView(R.id.et_category)
    EditText et_category;
    @BindView(R.id.et_address)
    EditText et_address;
    @BindView(R.id.et_tag)
    EditText et_tag;
    @BindView(R.id.et_des)
    EditText et_des;
    @BindView(R.id.iv_group_qcode)
    ImageView iv_group_qcode;
    @BindView(R.id.iv_cover)
    ImageView iv_cover;
    @BindView(R.id.ll_btn)
    LinearLayout ll_btn;
    @BindView(R.id.btn_submit)
    TextView btn_submit;
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private File tempFile;

    private String groupImage ="https://snailhouse.static.iqianjindai.com/appStore/img/bank/102.png";    //微信群二维码图片地址
    private String userImage = "https://snailhouse.static.iqianjindai.com/appStore/img/bank/103.png";     //封面图片地址


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_publish_wechat_group);
    }

    @Override
    public void dealLogicBeforeInitView() {

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        topBar.setTitle("发布");
        topBar.setLineVisible(true);

    }

    @Override
    public void dealLogicAfterInitView() {

//        getProvinceList();
//        getCityList("2");


    }
    @OnClick({R.id.iv_group_qcode, R.id.iv_cover, R.id.btn_submit})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_group_qcode:             //微信群二维码图片地址
                // 激活系统图库，选择一张图片
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                break;

            case R.id.iv_cover:                 //封面图片地址
                if (!isLogin()) return;
                goPage(SettingActivity.class);
                break;

            case R.id.btn_submit:                 //提交
                if (!isLogin()) return;
                submitInfo();
                break;

            default:
                break;

        }
    }

    @Override
    public void OnTopLeftClick() {
         finish();
    }

    @Override
    public void OnTopRightClick() {

    }

    private void submitInfo() {

        String title = et_title.getText().toString().trim();
        String wechat_num = et_wechat_num.getText().toString().trim();
        String category = et_category.getText().toString().trim();
        String address = et_address.getText().toString().trim();
        String tag = et_tag.getText().toString().trim();
        String des = et_des.getText().toString().trim();

        RequestParams params = new RequestParams(UrlConfig.URL_PUBLISH_WECHAT_GROUP);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        params.add("groupName", title);
        params.add("groupImage", groupImage);
        params.add("userImage", userImage);
        params.add("userWechat", wechat_num);
        params.add("tags", tag);
        params.add("description", des);
        params.add("provinceId", "1");
        params.add("cityId","1");
        params.add("parentCategory", "1");
        params.add("cityId","1");
        startRequest(Task.PUBLISH_WECHAT_GROUP, params, null);

    }

    /**
     * 上传头像
     */
    public  void uploadImg(File file, String type) {
        RequestParams params = new RequestParams(UrlConfig.URL_UPLOAD_IMAGE);
        params.setHttpType(HttpType.UPLOAD);
        params.setContentType("image/png");
        params.add("file", file);
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        params.add("type", type);
        startRequest(Task.UPLOAD_IMAGE, params, null);
    }


    /**
     * 获得省级区域列表
     */
    public  void getProvinceList() {
        RequestParams params = new RequestParams(UrlConfig.URL_GET_PROVINCE_LIST);
        startRequest(Task.GET_PROVINCE_LIST, params, null);
    }
    /**
     * 获得市级区域列表
     */
    public  void getCityList(String provinceId) {
        RequestParams params = new RequestParams(UrlConfig.URL_GET_CITY_LIST);
        params.add("provinceId", provinceId);
        startRequest(Task.GET_CITY_LIST, params, null);
    }




    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.PUBLISH_WECHAT_GROUP:
                if (handlerRequestErr(data)) {

                }
                break;
            case Task.UPLOAD_IMAGE:
                if (handlerRequestErr(data)) {

                }
                break;
            default:
                break;
        }
    }
    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }
        } else if (requestCode == PHOTO_REQUEST_CUT) {
            // 从剪切图片返回的数据
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                iv_group_qcode.setImageBitmap(bitmap);
                //压缩并上传图片
                uploadImg(compressImage(bitmap),"1");
                //保存到SharedPreferences
//                saveBitmapToSharedPreferences(bitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /*
       * 剪切图片
       */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }


    /**
     * 压缩图片（质量压缩）
     * @param bitmap
     */
    public static File compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            long length = baos.toByteArray().length;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        String filename = format.format(date);
        File file = new File(Environment.getExternalStorageDirectory(),filename+".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        recycleBitmap(bitmap);
        return file;
    }


    public static void recycleBitmap(Bitmap... bitmaps) {
        if (bitmaps==null) {
            return;
        }
        for (Bitmap bm : bitmaps) {
            if (null != bm && !bm.isRecycled()) {
                bm.recycle();
            }
        }
    }



}

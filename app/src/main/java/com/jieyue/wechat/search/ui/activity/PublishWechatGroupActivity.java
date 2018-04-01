package com.jieyue.wechat.search.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.bean.DataBean;
import com.jieyue.wechat.search.common.BaseActivity;
import com.jieyue.wechat.search.common.ShareData;
import com.jieyue.wechat.search.network.HttpType;
import com.jieyue.wechat.search.network.RequestParams;
import com.jieyue.wechat.search.network.ResultData;
import com.jieyue.wechat.search.network.Task;
import com.jieyue.wechat.search.network.UrlConfig;
import com.jieyue.wechat.search.utils.DateUtils;
import com.jieyue.wechat.search.utils.DeviceUtils;
import com.jieyue.wechat.search.utils.FileUtils;
import com.jieyue.wechat.search.utils.UserManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

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
    private static final int PHOTO_REQUEST_GROUP = 1;// 图片选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private File tempFile;
    private String type = "1";

    private String groupImage ;    //微信群二维码图片地址
    private String coverImage ;     //封面图片地址


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
        getAddressList();

    }
    @OnClick({R.id.iv_group_qcode, R.id.iv_cover, R.id.btn_submit})
    @Override
    public void onClickEvent(View view) {
        Intent intent =null;
        switch (view.getId()) {
            case R.id.iv_group_qcode:             //微信群二维码图片地址
                // 激活系统图库，选择一张图片
                intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                type = "1";    //设置图片上传类型  1.二维码 2.封面
                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GROUP
                startActivityForResult(intent, PHOTO_REQUEST_GROUP);
                break;

            case R.id.iv_cover:                 //封面图片地址
                // 激活系统图库，选择一张图片
                intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                type = "2"; //设置图片上传类型  1.二维码 2.封面
                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GROUP
                startActivityForResult(intent, PHOTO_REQUEST_GROUP);
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
        String tag = et_tag.getText().toString().trim();
        String des = et_des.getText().toString().trim();


        RequestParams params = new RequestParams(UrlConfig.URL_PUBLISH_WECHAT_GROUP);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        params.add("groupName", title);
        params.add("groupImage", groupImage);
        params.add("userImage", coverImage);
        params.add("userWechat", wechat_num);
        params.add("tags", tag);
        params.add("description", des);
        params.add("provinceId", "2");
        params.add("cityId","54");
        params.add("parentCategory", "1");
        params.add("category","16");
        startRequest(Task.PUBLISH_WECHAT_GROUP, params, DataBean.class);

    }

    /**
     * 上传头像
     */
    public  void uploadImg(String file) {
        RequestParams params = new RequestParams(UrlConfig.URL_UPLOAD_IMAGE);
        params.setHttpType(HttpType.UPLOAD);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.setContentType("multipart/form-data");
        params.add("file", file);
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        params.add("type", "1");
        startRequest(Task.UPLOAD_IMAGE, params, DataBean.class);
    }


    /**
     * 获得城市列表
     */
    public  void getAddressList() {
        RequestParams params = new RequestParams(UrlConfig.URL_GET_PROVINCE_LIST);
        startRequest(Task.GET_PROVINCE_LIST, params, null);
    }




    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.PUBLISH_WECHAT_GROUP:
                if (handlerRequestErr(data)) {
                    //发布成功
                    DataBean dataBean = (DataBean) data.getBody();
                    String orderId = dataBean.getData();
                    Bundle bd = new Bundle();
                    bd.putString("orderId", orderId);
                    goPage(PayActivity.class);

                }
                break;
            case Task.UPLOAD_IMAGE:
                if (handlerRequestErr(data)) {

                    DataBean dataBean = (DataBean) data.getBody();
                    String url = dataBean.getData();
                    if ("1".equals(type)){
                        groupImage = url;
                    }else if ("2".equals(type)){
                        coverImage = url;
                    }else {
                        return;
                    }

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
        if (requestCode == PHOTO_REQUEST_GROUP) {
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

                if ("1".equals(type)){
                    iv_group_qcode.setImageBitmap(bitmap);
                }else if ("2".equals(type)){
                    iv_cover.setImageBitmap(bitmap);
                }

                //压缩并上传图片
                compressImage(bitmap);

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
    public void compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 500) {  //循环判断如果压缩后图片是否大于500kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            long length = baos.toByteArray().length;
        }

        checkPermission(new CheckPermListener(){
            @Override
            public void superPermission() {
                try {
                    String filePath = FileUtils.TEMP + UserManager.getUserId()
                            + DateUtils.format(System.currentTimeMillis(), "yyyyMMddkkmmssSSS") + ".png";
                    File file = new File(filePath);

                    if (!file.exists()) {
                        File fileParentDir = file.getParentFile();
                        if (!fileParentDir.exists()) {
                            fileParentDir.mkdirs();
                        }
                        file.createNewFile();
                    }

                    FileOutputStream fos = new FileOutputStream(file);
                    try {
                        fos.write(baos.toByteArray());
                        fos.flush();
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    uploadImg(filePath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
       //        recycleBitmap(bitmap);

            }
        }, R.string.ask_again, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);

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

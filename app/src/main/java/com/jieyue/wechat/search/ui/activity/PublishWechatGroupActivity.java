package com.jieyue.wechat.search.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.bean.ArtificialAskPriceResultBean;
import com.jieyue.wechat.search.bean.UploadMultipleImgResult;
import com.jieyue.wechat.search.common.BaseActivity;
import com.jieyue.wechat.search.common.Constants;
import com.jieyue.wechat.search.common.ShareData;
import com.jieyue.wechat.search.network.RequestParams;
import com.jieyue.wechat.search.network.ResultData;
import com.jieyue.wechat.search.network.Task;
import com.jieyue.wechat.search.network.UrlConfig;
import com.jieyue.wechat.search.service.MessageEvent;
import com.jieyue.wechat.search.utils.DeviceUtils;
import com.jieyue.wechat.search.utils.FileUtils;
import com.jieyue.wechat.search.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;

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

    }
    @OnClick({R.id.iv_group_qcode, R.id.iv_cover, R.id.btn_submit})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_group_qcode:             //微信群二维码图片地址
                // 激活系统图库，选择一张图片
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
                startActivityForResult(intent1, PHOTO_REQUEST_GALLERY);
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
        params.add("provinceId", 1);
        params.add("cityId",1);
        params.add("parentCategory", 1);
        params.add("cityId",1);
        startRequest(Task.PUBLISH_WECHAT_GROUP, params, null);


    }
    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.PUBLISH_WECHAT_GROUP:
                if (handlerRequestErr(data)) {




                }
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
                //保存到SharedPreferences
                saveBitmapToSharedPreferences(bitmap);
            }
            try {
                // 将临时文件删除
                tempFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
       /**
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
    //保存图片到SharedPreferences
    private void saveBitmapToSharedPreferences(Bitmap bitmap) {
        // Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        //第一步:将Bitmap压缩至字节数组输出流ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        //第三步:将String保持至SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("testSP", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("image", imageString);
        editor.commit();

        //上传图片
        setImgByStr(imageString,"");
    }
    /**
     * 上传图片       此处使用用的OKHttp post请求上传的图片
     * @param imgStr
     * @param imgName
     */
    public  void setImgByStr(String imgStr, String imgName) {
        RequestParams params = new RequestParams(UrlConfig.URL_PUBLISH_WECHAT_GROUP);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        params.add("cityId",1);
        startRequest(Task.PUBLISH_WECHAT_GROUP, params, null);
    }

}

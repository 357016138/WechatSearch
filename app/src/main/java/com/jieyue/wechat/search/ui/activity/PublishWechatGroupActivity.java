package com.jieyue.wechat.search.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.adapter.CityArrayWheelAdapter;
import com.jieyue.wechat.search.adapter.ProvinceArrayWheelAdapter;
import com.jieyue.wechat.search.bean.ProvinceBean;
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
import com.jieyue.wechat.search.utils.StringUtils;
import com.jieyue.wechat.search.utils.UserManager;
import com.jieyue.wechat.search.view.wheelview.adapter.ArrayWheelAdapter;
import com.jieyue.wechat.search.view.wheelview.widget.WheelView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

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
    @BindView(R.id.tv_address)
    TextView tv_address;
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
    @BindView(R.id.ll_publish_address)
    LinearLayout ll_publish_address;


    @BindView(R.id.btn_submit)
    TextView btn_submit;
    private static final int PHOTO_REQUEST_GROUP = 1;// 图片选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private File tempFile;
    private String type = "1";

    private String groupImage ;    //微信群二维码图片地址
    private String coverImage ;     //封面图片地址
    private List<ProvinceBean> mProvinceList;        //省份列表
    private List<ProvinceBean.CityBean> mCityList;  //市或区列表
    private String mCurrentProvinceId;   //当前省份的id
    private String mCurrentCityId;      //当前市区的id




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
        //获取城市列表
        getAddressList("city.json",this);
    }
    @OnClick({R.id.iv_group_qcode, R.id.iv_cover, R.id.btn_submit, R.id.ll_publish_address})
    @Override
    public void onClickEvent(View view) {
        Intent intent =null;
        switch (view.getId()) {
            case R.id.ll_publish_address:                 //选择城市
                if (mProvinceList.size() == 0) {
                    getAddressList("city.json",this);
                    return;
                }
                initDialog(mProvinceList);
                break;
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

        if (StringUtils.isEmpty(mCurrentProvinceId)||StringUtils.isEmpty(mCurrentCityId)){
            toast("请选择地区");
            return;
        }


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

    /**
     * 销毁图片
     */
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

    /**
     * 获得城市列表
     */
    public  void  getAddressList(String fileName,Context context) {
            //将json数据变成字符串
            StringBuilder stringBuilder = new StringBuilder();
            try {
                //获取assets资源管理器
                AssetManager assetManager = context.getAssets();
                //通过管理器打开文件并读取
                BufferedReader bf = new BufferedReader(new InputStreamReader(assetManager.open(fileName)));
                String line;
                while ((line = bf.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Type listType = new TypeToken<List<ProvinceBean>>() {}.getType();
            //这里的json是字符串类型 = jsonArray.toString();
            mProvinceList = new Gson().fromJson(stringBuilder.toString(), listType );
    }

    /**
     * 展示城市选择框
     * */

    private void initDialog(List<ProvinceBean> mProvinceList) {
        final Dialog selectStoreDialog = new Dialog(this, R.style.bottom_dialog);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_regist_choose, null);
        final WheelView wv_province = view.findViewById(R.id.wv_province);
        final WheelView wv_city = view.findViewById(R.id.wv_city);

        ProvinceArrayWheelAdapter adapter = new ProvinceArrayWheelAdapter(this);
        adapter.setData(mProvinceList);
        wv_province.setWheelAdapter(adapter);

        CityArrayWheelAdapter mCityadapter = new CityArrayWheelAdapter(this);
        wv_city.setWheelAdapter(mCityadapter);
        //根据省份选择加载相应的市区
        wv_province.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener() {
            @Override
            public void onItemSelected(int position, Object o) {
                ProvinceBean mProvinceBean = mProvinceList.get(position);
                mCityList = mProvinceBean.getCity();
                mCityadapter.setData(mCityList);


            }
        });

        if (mProvinceList.size() >= 5) {
            wv_province.setWheelSize(5);
            wv_province.setSelection(3);
        } else {
            wv_province.setWheelSize(3);
            wv_province.setSelection(2);
        }
        wv_province.setDrawSelectorOnTop(true);
        wv_province.setSkin(WheelView.Skin.Holo); //设置背景颜色
//        wv_city.setDrawSelectorOnTop(true);
//        wv_city.setSkin(WheelView.Skin.Holo);
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        style.holoBorderColor = getResources().getColor(R.color.color_CCCCCC);
        style.textSize = 24;
        style.selectedTextColor = getResources().getColor(R.color.color_374953);
        style.textColor = getResources().getColor(R.color.color_6E757F);
        wv_province.setStyle(style);
//        wv_city.setStyle(style);
        selectStoreDialog.setContentView(view);
        view.findViewById(R.id.bt_cancel).setOnClickListener(v -> selectStoreDialog.dismiss());

        view.findViewById(R.id.bt_conform).setOnClickListener(v -> {
            //获得当前选择的省市位置
            int provincePosition = wv_province.getCurrentPosition();
            int cityPosition = wv_city.getCurrentPosition();
           //根据位置取出实体类
            ProvinceBean mCurrentProvinceBean = mProvinceList.get(provincePosition);
            ProvinceBean.CityBean mCurrentCityBean = mCityList.get(cityPosition);

            mCurrentProvinceId = mCurrentCityBean.getParentId();
            mCurrentCityId= mCurrentCityBean.getId();
            tv_address.setText(mCurrentProvinceBean.getAreaName()+""+mCurrentCityBean.getAreaName());

            selectStoreDialog.dismiss();
        });
        DisplayMetrics dm = new DisplayMetrics();
        int height = dm.heightPixels;
        Window window = selectStoreDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottom_dialog_window_style);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.x = 0;
        lp.y = height;
        lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        selectStoreDialog.setCanceledOnTouchOutside(false);
        selectStoreDialog.show();

    }


}

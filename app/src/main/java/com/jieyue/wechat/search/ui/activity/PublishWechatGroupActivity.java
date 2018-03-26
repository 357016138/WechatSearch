package com.jieyue.wechat.search.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

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

import java.io.File;

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
                if (!isLogin()) return;
                goPage(SettingActivity.class);
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



}

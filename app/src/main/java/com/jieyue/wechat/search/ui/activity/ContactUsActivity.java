package com.jieyue.wechat.search.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.bean.DataBean;
import com.jieyue.wechat.search.common.BaseActivity;
import com.jieyue.wechat.search.common.ShareData;
import com.jieyue.wechat.search.network.RequestParams;
import com.jieyue.wechat.search.network.ResultData;
import com.jieyue.wechat.search.network.Task;
import com.jieyue.wechat.search.network.UrlConfig;
import com.jieyue.wechat.search.utils.DeviceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class ContactUsActivity extends BaseActivity {


    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_contact_us);
    }

    @Override
    public void dealLogicBeforeInitView() {

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        topBar.setTitle("联系我们");
        topBar.setLineVisible(true);
    }

    @Override
    public void dealLogicAfterInitView() {

    }
    @OnClick({R.id.tv_qq})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.tv_qq:             //设置
                try {
                    //第二种方式：可以跳转到添加好友，如果qq号是好友了，直接聊天
                    String url = "mqqwpa://im/chat?chat_type=wpa&uin=357016138";//uin是发送过去的qq号码
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                } catch (Exception e) {
                    e.printStackTrace();
                    toast( "请检查是否安装QQ");
                }
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

}



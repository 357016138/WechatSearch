package com.jieyue.wechat.search.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

 import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.common.BaseActivity;

import butterknife.ButterKnife;

public class TestActivity extends BaseActivity {

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_test);
    }

    @Override
    public void dealLogicBeforeInitView() {

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        topBar.setTitle("充值");
        topBar.setLineVisible(true);
    }

    @Override
    public void dealLogicAfterInitView() {

    }
    //    @OnClick({R.id.iv_pic_code,R.id.signup_obtainCode})
    @Override
    public void onClickEvent(View view) {

    }

    @Override
    public void OnTopLeftClick() {
        finish();
    }

    @Override
    public void OnTopRightClick() {

    }
}

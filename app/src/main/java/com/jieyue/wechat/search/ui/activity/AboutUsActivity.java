package com.jieyue.wechat.search.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.common.BaseActivity;
import com.jieyue.wechat.search.utils.DeviceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutUsActivity extends BaseActivity {

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_about_us);
    }

    @Override
    public void dealLogicBeforeInitView() {

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        topBar.setTitle("关于我们");
        topBar.setLineVisible(true);
    }

    @Override
    public void dealLogicAfterInitView() {
    }
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
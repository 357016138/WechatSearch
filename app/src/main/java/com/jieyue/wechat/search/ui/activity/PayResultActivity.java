package com.jieyue.wechat.search.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.common.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayResultActivity extends BaseActivity {

    private String orderId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_pay_result);
    }

    @Override
    public void dealLogicBeforeInitView() {
        Bundle bundle = getIntentData();
        orderId = bundle.getString("orderId");
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        topBar.setTitle("支付结果");
        topBar.setLineVisible(true);
    }

    @Override
    public void dealLogicAfterInitView() {
    }

    @OnClick({R.id.btn_return, R.id.btn_detail})
    @Override
    public void onClickEvent(View view) {
        Bundle bd = new Bundle();
        switch (view.getId()) {
            case R.id.btn_return:
                bd.putInt(MainActivity.CURRENT_POSITION, 0);
                bd.putBoolean(MainActivity.IS_CLEAR_FRAGMENT, true);
                goPage(MainActivity.class, bd);
                this.finish();
                break;
            case R.id.btn_detail:
                bd.putString("uniqueId", orderId);
                goPage(ProductDetailActivity.class, bd);
                this.finish();
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

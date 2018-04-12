package com.jieyue.wechat.search.ui.activity;

import android.view.View;
import android.widget.CheckBox;

import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.common.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by song on 2018/4/11 0017.
 * 充值的界面
 */
public class RechargeActivity extends BaseActivity {

    @BindView(R.id.cb_use_1)
    CheckBox cb_use_1;
    @BindView(R.id.cb_use_2)
    CheckBox cb_use_2;
    @BindView(R.id.cb_use_3)
    CheckBox cb_use_3;
    @BindView(R.id.cb_use_4)
    CheckBox cb_use_4;
    @BindView(R.id.cb_use_5)
    CheckBox cb_use_5;
    @BindView(R.id.cb_use_6)
    CheckBox cb_use_6;
    @BindView(R.id.cb_use_7)
    CheckBox cb_use_7;
    @BindView(R.id.cb_use_8)
    CheckBox cb_use_8;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_recharge);
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
    @OnClick({R.id.cb_use_1,R.id.cb_use_2,R.id.cb_use_3,R.id.cb_use_4,R.id.cb_use_5,R.id.cb_use_6,R.id.cb_use_7,R.id.cb_use_8,R.id.btn_submit})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.cb_use_1:
                setCheckBoxBackground();
                cb_use_1.setChecked(true);
                break;
            case R.id.cb_use_2:
                setCheckBoxBackground();
                cb_use_2.setChecked(true);
                break;
            case R.id.cb_use_3:
                setCheckBoxBackground();
                cb_use_3.setChecked(true);
                break;
            case R.id.cb_use_4:
                setCheckBoxBackground();
                cb_use_4.setChecked(true);
                break;
            case R.id.cb_use_5:
                setCheckBoxBackground();
                cb_use_5.setChecked(true);
                break;
            case R.id.cb_use_6:
                setCheckBoxBackground();
                cb_use_6.setChecked(true);
                break;
            case R.id.cb_use_7:
                setCheckBoxBackground();
                cb_use_7.setChecked(true);
                break;
            case R.id.cb_use_8:
                setCheckBoxBackground();
                cb_use_8.setChecked(true);
                break;
            case R.id.btn_submit:


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

    public void createOrder(){

    }




    public void setCheckBoxBackground(){
        cb_use_1.setChecked(false);
        cb_use_2.setChecked(false);
        cb_use_3.setChecked(false);
        cb_use_4.setChecked(false);
        cb_use_5.setChecked(false);
        cb_use_6.setChecked(false);
        cb_use_7.setChecked(false);
        cb_use_8.setChecked(false);
    }

}

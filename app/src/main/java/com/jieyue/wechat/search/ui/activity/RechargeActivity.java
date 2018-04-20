package com.jieyue.wechat.search.ui.activity;

import android.view.View;
import android.widget.LinearLayout;

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

    @BindView(R.id.ll_1)
    LinearLayout ll_1;
    @BindView(R.id.ll_2)
    LinearLayout ll_2;
    @BindView(R.id.ll_3)
    LinearLayout ll_3;
    @BindView(R.id.ll_4)
    LinearLayout ll_4;
    @BindView(R.id.ll_5)
    LinearLayout ll_5;
    @BindView(R.id.ll_6)
    LinearLayout ll_6;
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
        ll_1.setBackground(getResources().getDrawable(R.drawable.bg_yellow));
    }
    @OnClick({R.id.ll_1,R.id.ll_2,R.id.ll_3,R.id.ll_4,R.id.ll_5,R.id.ll_6,R.id.btn_submit})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.ll_1:
                setLinearLayoutBackground();
                ll_1.setBackground(getResources().getDrawable(R.drawable.bg_yellow));
                break;
            case R.id.ll_2:
                setLinearLayoutBackground();
                ll_2.setBackground(getResources().getDrawable(R.drawable.bg_yellow));
                break;
            case R.id.ll_3:
                setLinearLayoutBackground();
                ll_3.setBackground(getResources().getDrawable(R.drawable.bg_yellow));
                break;
            case R.id.ll_4:
                setLinearLayoutBackground();
                ll_4.setBackground(getResources().getDrawable(R.drawable.bg_yellow));
                break;
            case R.id.ll_5:
                setLinearLayoutBackground();
                ll_5.setBackground(getResources().getDrawable(R.drawable.bg_yellow));
                break;
            case R.id.ll_6:
                setLinearLayoutBackground();
                ll_6.setBackground(getResources().getDrawable(R.drawable.bg_yellow));
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




    //背景色初始化
    public void setLinearLayoutBackground(){
        ll_1.setBackground(getResources().getDrawable(R.drawable.bg_loading_dialog));
        ll_2.setBackground(getResources().getDrawable(R.drawable.bg_loading_dialog));
        ll_3.setBackground(getResources().getDrawable(R.drawable.bg_loading_dialog));
        ll_4.setBackground(getResources().getDrawable(R.drawable.bg_loading_dialog));
        ll_5.setBackground(getResources().getDrawable(R.drawable.bg_loading_dialog));
        ll_6.setBackground(getResources().getDrawable(R.drawable.bg_loading_dialog));
    }

    //选中效果
    public void setSelectEffect(){
        ll_1.setBackground(getResources().getDrawable(R.drawable.bg_loading_dialog));
        ll_2.setBackground(getResources().getDrawable(R.drawable.bg_loading_dialog));
        ll_3.setBackground(getResources().getDrawable(R.drawable.bg_loading_dialog));
        ll_4.setBackground(getResources().getDrawable(R.drawable.bg_loading_dialog));
        ll_5.setBackground(getResources().getDrawable(R.drawable.bg_loading_dialog));
        ll_6.setBackground(getResources().getDrawable(R.drawable.bg_loading_dialog));
    }





}

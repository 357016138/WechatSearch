package com.jieyue.wechat.search.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.common.BaseFragment;
import com.jieyue.wechat.search.network.UrlConfig;
import com.jieyue.wechat.search.ui.activity.PublishWechatGroupActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 应用
 * Created by song on 2018/1/30.
 */
public class UseFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.ll_use_1)
    LinearLayout ll_use_1;
    @BindView(R.id.ll_use_2)
    LinearLayout ll_use_2;
    @BindView(R.id.ll_use_5)
    LinearLayout ll_use_5;
    @BindView(R.id.ll_use_6)
    LinearLayout ll_use_6;
    @BindView(R.id.ll_use_7)
    LinearLayout ll_use_7;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_use, container, false);
        initView(view);
        initData();

        return view;
    }

    private void initView(View view) {
        //一定要解绑 在onDestroyView里
        unbinder = ButterKnife.bind(this,view);
    }

    private void initData() {

    }

    @OnClick({R.id.ll_use_1,R.id.ll_use_2,R.id.ll_use_5,R.id.ll_use_6,R.id.ll_use_7})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.ll_use_1:
                if (!isLogin()) return;
                goPage(PublishWechatGroupActivity.class);
                break;
            case R.id.ll_use_2:
                goWebPage("佣金试算",UrlConfig.URL_COMMISION_CALCULATION);
                break;
            case R.id.ll_use_5:
                toast("敬请期待");
                break;
            case R.id.ll_use_6:
                toast("敬请期待");
                break;
            case R.id.ll_use_7:
                toast("敬请期待");
                break;
            default:
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }



}

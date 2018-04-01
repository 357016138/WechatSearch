package com.jieyue.wechat.search.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.bean.AppBaseDataBean;
import com.jieyue.wechat.search.bean.IsHasNewMsgNotice;
import com.jieyue.wechat.search.common.BaseFragment;
import com.jieyue.wechat.search.common.Constants;
import com.jieyue.wechat.search.network.RequestParams;
import com.jieyue.wechat.search.network.ResultData;
import com.jieyue.wechat.search.network.Task;
import com.jieyue.wechat.search.network.UrlConfig;
import com.jieyue.wechat.search.service.MessageEvent;
import com.jieyue.wechat.search.ui.activity.ConsultPriceActivity;
import com.jieyue.wechat.search.ui.activity.MsgNoticeActivity;
import com.jieyue.wechat.search.ui.activity.PayActivity;
import com.jieyue.wechat.search.ui.activity.ReportApplySuccessActivity;
import com.jieyue.wechat.search.utils.DeviceUtils;
import com.jieyue.wechat.search.utils.StringUtils;
import com.jieyue.wechat.search.utils.UserManager;
import com.jieyue.wechat.search.utils.UserUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 首页
 * Created by song on 2018/1/30.
 */
public class HomeFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.iv_msg_new)
    ImageView iv_new_msg;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        initData();
        return view;
    }

    /**
     * 初始化控件
     * */
    private void initView(View view) {
        //一定要解绑 在onDestroyView里
        unbinder = ButterKnife.bind(this,view);
        EventBus.getDefault().register(this);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                refreshLayout.finishRefresh();
            }
        });
    }

    /**
     * 初始化数据
     * */
    private void initData() {

    }

    @OnClick({R.id.rl_msg})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.rl_msg:
                if (!isLogin()) return;
//                goPage(MsgNoticeActivity.class);
                goPage(PayActivity.class);


                break;
//            case R.id.tv_consult_price:
//                if (!isLogin()) return;
//                goPage(ConsultPriceActivity.class);
//                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.APP_BASE_DATA:
                refreshLayout.finishRefresh();
                if (handlerRequestErr(data)) {
                    AppBaseDataBean dataBean = (AppBaseDataBean) data.getBody();
                    if (dataBean != null) {
                    }
                }
                break;
            case Task.APP_MSG_DATA:
                if (handlerRequestErr(data)) {
                    IsHasNewMsgNotice dataBean = (IsHasNewMsgNotice) data.getBody();
                    if (dataBean != null) {
                        if("0".equals(dataBean.getIsRead())) {
                            iv_new_msg.setVisibility(View.VISIBLE);
                        } else {
                            iv_new_msg.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        iv_new_msg.setVisibility(View.INVISIBLE);
                    }
                } else {
                    iv_new_msg.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getTag() == Constants.GET_NEW_MSG) {
            if (UserUtils.isLogin()) {
//                getMsgData();
            } else {
                iv_new_msg.setVisibility(View.INVISIBLE);
            }
        }
    }
}

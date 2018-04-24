package com.jieyue.wechat.search.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;

import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.common.BaseFragment;
import com.jieyue.wechat.search.network.UrlConfig;
import com.jieyue.wechat.search.utils.DeviceUtils;
import com.jieyue.wechat.search.utils.GsonUtil;
import com.jieyue.wechat.search.utils.LogUtils;
import com.jieyue.wechat.search.utils.UserManager;
import com.jieyue.wechat.search.view.CustomWebView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author baipeng
 * @Title NoticeListFragment
 * @Date 2018/3/6 11:36
 * @Description NoticeListFragment.
 */
public class NoticeListFragment extends BaseFragment {
    Unbinder unbinder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_webview, container, false);
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
    }

    /**
     * 初始化数据
     * */
    private void initData() {
        String url = String.format(Locale.US, UrlConfig.URL_NOTICE_LIST,
                DeviceUtils.getDeviceUniqueId(getActivity()),
                UserManager.getUserId());
    }

    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
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

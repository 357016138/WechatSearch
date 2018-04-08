package com.jieyue.wechat.search.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.bean.ProductDetailBean;
import com.jieyue.wechat.search.common.BaseActivity;
import com.jieyue.wechat.search.network.RequestParams;
import com.jieyue.wechat.search.network.ResultData;
import com.jieyue.wechat.search.network.Task;
import com.jieyue.wechat.search.network.UrlConfig;
import com.jieyue.wechat.search.utils.DeviceUtils;
import com.jieyue.wechat.search.utils.UserManager;

import butterknife.ButterKnife;
import okhttp3.Call;


/**
 * 详情页
 *
 * */
public class ProductDetailActivity extends BaseActivity {

    private String uniqueId;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_product_detail);
    }

    @Override
    public void dealLogicBeforeInitView() {
        Bundle bundle = getIntentData();
        uniqueId = bundle.getString("uniqueId");
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        topBar.setTitle("产品详情");
        topBar.setLineVisible(true);
    }

    @Override
    public void dealLogicAfterInitView() {
        getProductDetail(uniqueId);
        addLookCount(uniqueId);
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
    /**
     * 增加浏览量
     * */
    public void addLookCount(String uniqueId) {
        RequestParams params = new RequestParams(UrlConfig.URL_ADD_LOOK_COUNT);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("uniqueId", uniqueId);
        params.add("type", "1");
        startRequest(Task.ADD_LOOK_COUNT, params, null);
    }
    /**
     * 获得商品详情
     * */
    public void getProductDetail(String uniqueId) {
        RequestParams params = new RequestParams(UrlConfig.URL_PRODUCT_DETAIL);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("uniqueId", uniqueId);
        startRequest(Task.PRODUCT_DETAIL, params, ProductDetailBean.class);
    }

    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.PRODUCT_DETAIL:
                if (handlerRequestErr(data)) {
                    ProductDetailBean dataBean = (ProductDetailBean) data.getBody();
                    if (dataBean != null) {
                        updateDetailInfo(dataBean);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 赋值
     * */
    private void updateDetailInfo(ProductDetailBean dataBean) {





    }

}

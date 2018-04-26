package com.jieyue.wechat.search.ui.activity;

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
import okhttp3.Call;


/**
 * 意见反馈
 *
 */

public class SuggestionActivity extends BaseActivity {

    @BindView(R.id.et_content)
    EditText et_content;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_suggestion);
    }

    @Override
    public void dealLogicBeforeInitView() {

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        topBar.setTitle("意见反馈");
        topBar.setLineVisible(true);
        topBar.setRightVisible(true);
        topBar.setRightText("提交");
    }

    @Override
    public void dealLogicAfterInitView() {
        DeviceUtils.showSoftInputFromWindow(this,et_content);
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

        String content = et_content.getText().toString().trim();

        RequestParams params = new RequestParams(UrlConfig.URL_PUBLISH_WECHAT_GROUP);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        params.add("content",content);
        startRequest(Task.PUBLISH_WECHAT_GROUP, params, DataBean.class);
        finish();
    }



    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        if (data != null) {
            switch (tag) {
                case Task.PUBLISH_WECHAT_GROUP:
                    if (handlerRequestErr(data)) {

                        finish();
                    }
                    break;

                default:
                    break;
            }
        }
    }
}


package com.jieyue.wechat.search.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.base.bj.paysdk.utils.TrPay;
import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.bean.VersionBean;
import com.jieyue.wechat.search.common.BaseActivity;
import com.jieyue.wechat.search.common.ShareData;
import com.jieyue.wechat.search.network.RequestParams;
import com.jieyue.wechat.search.network.ResultData;
import com.jieyue.wechat.search.network.Task;
import com.jieyue.wechat.search.network.UrlConfig;
import com.jieyue.wechat.search.service.DownloadService;
import com.jieyue.wechat.search.utils.DeviceUtils;
import com.jieyue.wechat.search.utils.ToastUtils;
import com.jieyue.wechat.search.view.DownloadDialog;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by song on 2018/1/17 0017.
 */
public class StartActivity extends BaseActivity {

    private final int KEY_GET_NEW_VERSION = 1;
    private final int KEY_TO_MAIN = 2;
    private final int KEY_UPLOAD_POSITION = 3;

    private final String KEY_NO_UPDATE = "0";
    private final String KEY_CAN_UPDATE = "1";
    private final String KEY_FORCE_UPDATE = "2";

    private WeakHandler mHandler;

    public static class WeakHandler extends Handler {
        private WeakReference<StartActivity> refreence;

        public WeakHandler(StartActivity startActivity) {
            refreence = new WeakReference<>(startActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            refreence.get().handleResult(msg);
        }
    }

    private void handleResult(Message msg) {
        switch (msg.what) {
            case KEY_GET_NEW_VERSION:
                getNewVersion();
                break;

            case KEY_TO_MAIN:
                nextPage();
                break;

            default:
                break;
        }

    }

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_start, BasePageSet.NO_TOPBAR_DEFAULT_PAGE);
    }

    @Override
    public void dealLogicBeforeInitView() {
        setCheckLock(false);
        mHandler = new WeakHandler(this);
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        /**

         * 初始化PaySdk(context请传入当前Activity对象(如：MainActivity.this))

         * 第一个参数:是您在trPay后面应用的appkey（需要先提交应用资料(若应用未上线，需上传测试APK文件)，审核通过后appkey生效）

         * 第二个参数:是您的渠道，一般是baidu,360,xiaomi等

         */

        TrPay.getInstance(StartActivity.this).initPaySdk("48b8f3bf366148a9aa0ba89e2b5d3ec1","baidu");
    }

    @Override
    public void dealLogicAfterInitView() {
        nextPage();
//        getNewVersion();   //查看是否有新版本
    }

    @Override
    public void OnTopLeftClick() {

    }

    @Override
    public void OnTopRightClick() {

    }

    @Override
    public void onClickEvent(View view) {

    }

    private void getNewVersion() {
        RequestParams params = new RequestParams(UrlConfig.URL_GET_NEW_VERSION);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("appVersion", DeviceUtils.getCurrentAppVersionCode(this));
        params.add("operatSystem", "android");
        startRequest(Task.NEW_VERSION, params, VersionBean.class, false);
    }

    private void nextPage() {
        if (TextUtils.isEmpty(ShareData.getShareStringData(ShareData.IS_OPEN_GUIDE))) {
            goPage(GuideActivity.class);
        } else {
            goPage(MainActivity.class);
        }
        finish();
    }

    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.NEW_VERSION:
                if (handlerRequestErr(data)) {
                    VersionBean versionBean = (VersionBean) data.getBody();
                    if (versionBean != null) {
                        setUpdateTips(versionBean);
                    }
                } else {
                    mHandler.sendEmptyMessage(KEY_TO_MAIN);
                }
                break;
        }
    }

    private void setUpdateTips(VersionBean versionBean) {
        switch (versionBean.getForceState()) {
            case KEY_NO_UPDATE:
                mHandler.sendEmptyMessage(KEY_TO_MAIN);
                break;

            case KEY_CAN_UPDATE:
                showUpdate(versionBean, false);
                break;

            case KEY_FORCE_UPDATE:
                showUpdate(versionBean, true);
                break;
        }
    }

    private void showUpdate(VersionBean versionBean, boolean isForce) {
        DownloadDialog dialog = new DownloadDialog(this);
        dialog.setContent("版本号:" + versionBean.getNewAppVersion() + "\n更新内容:\n" + versionBean.getVersionContent());
        dialog.setCancelText(isForce ? "退出" : "取消");
        dialog.setOnDownLoadClickListener(new DownloadDialog.OnDownLoadClickListener() {
            @Override
            public void onLeftClick() {
                if (isForce) {
                    dialog.dismiss();
                    app.exitSystem();
                } else {
                    dialog.dismiss();
                    mHandler.sendEmptyMessage(KEY_TO_MAIN);
                }
            }

            @Override
            public void onRightClick() {
                if (!RxPermissions.getInstance(StartActivity.this).isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    toast("没有SD卡权限");
                    if (!isForce) mHandler.sendEmptyMessage(KEY_TO_MAIN);
                    return;
                }
                dialog.dismiss();
                downLoad(versionBean);
                //if (!isForce) mHandler.sendEmptyMessage(KEY_TO_MAIN);
                //toast("后台下载中");
                ToastUtils.showLong(StartActivity.this, "后台下载中");
            }
        });
        dialog.show();
    }

    private void downLoad(VersionBean versionBean) {
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra("key_version", versionBean);
        startService(intent);
    }

}

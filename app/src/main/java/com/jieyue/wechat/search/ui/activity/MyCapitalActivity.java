package com.jieyue.wechat.search.ui.activity;

import android.view.View;

import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.common.BaseActivity;
import com.jieyue.wechat.search.view.DownloadDialog;
import com.jieyue.wechat.search.view.OneButtonDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jieyue.wechat.search.common.BaseActivity.BasePageSet.NO_TOPBAR_DEFAULT_PAGE;

public class MyCapitalActivity extends BaseActivity {

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_my_capital,NO_TOPBAR_DEFAULT_PAGE);
    }

    @Override
    public void dealLogicBeforeInitView() {

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);

    }

    @Override
    public void dealLogicAfterInitView() {

    }
    @OnClick({R.id.iv_capital_1})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_capital_1:
                showTipDialog();
                break;
            default:
                break;
        }
    }

    @Override
    public void OnTopLeftClick() {

    }

    @Override
    public void OnTopRightClick() {

    }

    protected void showTipDialog() {
        final OneButtonDialog dialog = new OneButtonDialog(this);
        dialog.setContent("账户余额＋待收收益＋提现冻结\n＝资产总额" );
        dialog.setOkText("知道了");
        dialog.setOnDownLoadClickListener(new DownloadDialog.OnDownLoadClickListener() {
            @Override
            public void onLeftClick() {
                    dialog.dismiss();
            }

            @Override
            public void onRightClick() {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}

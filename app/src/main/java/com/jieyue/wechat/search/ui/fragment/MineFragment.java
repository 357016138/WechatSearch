package com.jieyue.wechat.search.ui.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.bean.BindBankCardInfoBean;
import com.jieyue.wechat.search.bean.UserAccount;
import com.jieyue.wechat.search.common.BaseFragment;
import com.jieyue.wechat.search.common.ShareData;
import com.jieyue.wechat.search.network.RequestParams;
import com.jieyue.wechat.search.network.ResultData;
import com.jieyue.wechat.search.network.Task;
import com.jieyue.wechat.search.network.UrlConfig;
import com.jieyue.wechat.search.response.BankCardListResponse;
import com.jieyue.wechat.search.response.WhetherSetPaymentPasswordResponse;
import com.jieyue.wechat.search.ui.activity.BankCardListActivity;
import com.jieyue.wechat.search.ui.activity.BindBankCardActivity;
import com.jieyue.wechat.search.ui.activity.LoginActivity;
import com.jieyue.wechat.search.ui.activity.MyPublishListActivity;
import com.jieyue.wechat.search.ui.activity.SetWithdrawPasswordActivity;
import com.jieyue.wechat.search.ui.activity.SettingActivity;
import com.jieyue.wechat.search.ui.activity.WithdrawDepositActivity;
import com.jieyue.wechat.search.utils.DeviceUtils;
import com.jieyue.wechat.search.utils.LogUtils;
import com.jieyue.wechat.search.utils.StringUtils;
import com.jieyue.wechat.search.utils.UserManager;
import com.jieyue.wechat.search.utils.UserUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 我的
 * Created by song on 2018/1/30.
 */
public class MineFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.iv_setting)
    ImageView iv_setting;
    @BindView(R.id.iv_mine_header)
    ImageView iv_mine_header;
    @BindView(R.id.btn_sign_out)
    Button btn_sign_out;
    @BindView(R.id.ll_mine_logined)
    LinearLayout ll_mine_logined;
    @BindView(R.id.ll_mine_login)
    LinearLayout ll_mine_login;
    @BindView(R.id.ll_mine_4)
    LinearLayout ll_mine_4;
    @BindView(R.id.ll_mine_5)
    LinearLayout ll_mine_5;
    @BindView(R.id.ll_mine_6)
    LinearLayout ll_mine_6;
    @BindView(R.id.ll_mine_7)
    LinearLayout ll_mine_7;
    @BindView(R.id.ll_mine_8)
    LinearLayout ll_mine_8;
    @BindView(R.id.ll_mine_9)
    LinearLayout ll_mine_9;
    @BindView(R.id.ll_mine_10)
    LinearLayout ll_mine_10;
    @BindView(R.id.ll_mine_11)
    LinearLayout ll_mine_11;
    @BindView(R.id.ll_mine_12)
    LinearLayout ll_mine_12;
    @BindView(R.id.ll_mine_13)
    LinearLayout ll_mine_13;
    @BindView(R.id.ll_mine_14)
    LinearLayout ll_mine_14;
    @BindView(R.id.mine_tv_capital)
    TextView mine_tv_capital;
    @BindView(R.id.mine_tv_earned)
    TextView mine_tv_earned;
    @BindView(R.id.mine_tv_duein)
    TextView mine_tv_duein;
    @BindView(R.id.mine_iv_show)
    ImageView mine_iv_show;
    @BindView(R.id.tv_mine_phone)
    TextView tv_mine_phone;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private UserAccount mUserAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private void initView(View view) {

        //一定要解绑 在onDestroyView里
        unbinder = ButterKnife.bind(this, view);
        refreshLayout.isEnableLoadmore();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (UserUtils.isLogin()){
                    //TODO   刷新用户账户金额信息
                    refreshLayout.finishRefresh();
//                    getFeeAccountInformation();
                }else{
                    refreshLayout.finishRefresh();
                }

            }
        });
    }

    private void initData() {
        //登录状态
        if (UserUtils.isLogin()) {
            iv_mine_header.setVisibility(View.VISIBLE);
            ll_mine_logined.setVisibility(View.VISIBLE);
            ll_mine_login.setVisibility(View.GONE);
            String phone = ShareData.getShareStringData(ShareData.USER_PHONE);
            tv_mine_phone.setText(StringUtils.getUserName(phone));
//            getFeeAccountInformation();
        } else {
            iv_mine_header.setVisibility(View.GONE);
            ll_mine_logined.setVisibility(View.GONE);
            ll_mine_login.setVisibility(View.VISIBLE);
        }
    }


    @OnClick({R.id.iv_setting, R.id.btn_sign_out, R.id.mine_tv_capital, R.id.mine_iv_show, R.id.ll_mine_4, R.id.ll_mine_5, R.id.ll_mine_6, R.id.ll_mine_7, R.id.ll_mine_8, R.id.ll_mine_9, R.id.ll_mine_10, R.id.ll_mine_11, R.id.ll_mine_12, R.id.ll_mine_13, R.id.ll_mine_14})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:             //设置
                if (!isLogin()) return;
                goPage(SettingActivity.class);
                break;
            case R.id.btn_sign_out:          //登录
                if (!isLogin()) return;
                goPage(LoginActivity.class);
                break;
            case R.id.mine_tv_capital:        //我的资产
//                if (!isLogin()) return;
//                goPage(MyCapitalActivity.class);
                break;
            case R.id.mine_iv_show:        //显示与隐藏资金
                if (!isLogin()) return;
                if (ShareData.getShareBooleanData(ShareData.SHOW_AMOUNT)){
                    ShareData.setShareBooleanData(ShareData.SHOW_AMOUNT, false);
                } else {
                    ShareData.setShareBooleanData(ShareData.SHOW_AMOUNT, true);
                }
                if (!ShareData.getShareBooleanData(ShareData.SHOW_AMOUNT)) {
//                    mine_tv_capital.setText(formatString(mUserAccount.getAccountBalance()));
//                    mine_tv_earned.setText(formatString(mUserAccount.getTotalReturnFee()));
//                    mine_tv_duein.setText(formatString(mUserAccount.getAmountToReturn()));
                    mine_iv_show.setImageResource(R.drawable.icon_mine_2_2);
                } else {
                    mine_tv_capital.setText("****");
                    mine_tv_earned.setText("****");
                    mine_tv_duein.setText("****");
                    mine_iv_show.setImageResource(R.drawable.icon_mine_2_1);
                }
                break;

            case R.id.ll_mine_4:        //资金管理
                if (!isLogin()) return;
                goPage(MyPublishListActivity.class);
                break;

            case R.id.ll_mine_5:      //返佣管理
                toast("敬请期待");
                break;

            case R.id.ll_mine_6:      //账单
                toast("敬请期待");
                break;

            case R.id.ll_mine_7:     //银行卡
                if (!isLogin()) return;
                goPage(BankCardListActivity.class);
                break;

            case R.id.ll_mine_8:    //邀请好友
                toast("敬请期待");
                break;

            case R.id.ll_mine_9:     //业绩榜单
                toast("敬请期待");
                break;

            case R.id.ll_mine_10:    //客户经理
                toast("敬请期待");
                break;

            case R.id.ll_mine_11:     //帮助中心
                goWebPage("帮助中心", UrlConfig.URL_HELP_CENTER);
                break;

            case R.id.ll_mine_12:     //关于我们
                goWebPage("关于我们", UrlConfig.URL_ABOUT_US);
                break;

            case R.id.ll_mine_13:    //联系我们
                goWebPage("联系我们", UrlConfig.URL_CONTACT_US);
                break;

            case R.id.ll_mine_14:   //我要吐槽
                if (!isLogin()) return;
                goWebPage("我要吐槽", String.format(Locale.US, UrlConfig.URL_SUGGEST,
                        DeviceUtils.getDeviceUniqueId(getActivity()),
                        UserManager.getUserId()));
                break;

//            case R.id.bt_mine_withdraw_deposit: // 提现
//                isBindBankCard();
//                goPage(WithdrawDepositActivity.class);
//                break;

            default:
                break;

        }
    }

    private void getFeeAccountInformation(){
        RequestParams params = new RequestParams(UrlConfig.URL_QUERY_RETURN_FEE_ACCOUNT);
        params.add("pid", DeviceUtils.getDeviceUniqueId(getActivity()));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        startRequest(Task.QUERY_RETURN_FEE_ACCOUNT, params, UserAccount.class, false);
    }

    private void isBindBankCard() {
        RequestParams params = new RequestParams(UrlConfig.URL_QUERY_BIND_BANK_CARD_INFO);
        params.add("pid", DeviceUtils.getDeviceUniqueId(getActivity()));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        params.add("curPage", 1);
        params.add("pageSize", 10);
        startRequest(Task.BIND_BANK_CARD_INFO, params, BankCardListResponse.class, true);
    }

    private void isSetPayPassword() {
        RequestParams params = new RequestParams(UrlConfig.URL_WHETHER_SET_PAYMENT_PASSWORD);
        params.add("pid", DeviceUtils.getDeviceUniqueId(getActivity()));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        startRequest(Task.WHETHER_SET_PAY_PASSWORD, params, WhetherSetPaymentPasswordResponse.class, true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
//        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            //相当于Fragment的onPause
            LogUtils.e("界面不可见");
        } else {
            // 相当于Fragment的onResume
            LogUtils.e("界面可见");
        }
        super.onHiddenChanged(hidden);
    }

    private void showVerificationDialog(String tip, int tag) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialog_no_bg);
        View myView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_is_can_withdraw_deposit_layout, null);
        builder.setView(myView);
        TextView tvTip = myView.findViewById(R.id.tv_tip);
        tvTip.setText(tip);
        myView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        final Dialog dialog = builder.create();
        myView.findViewById(R.id.bt_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        myView.findViewById(R.id.bt_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (tag) {
                    case 1:
                        goPage(BindBankCardActivity.class);
                        break;

                    case 2:
                        goPage(SetWithdrawPasswordActivity.class);
                        break;
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.BIND_BANK_CARD_INFO:
                if (handlerRequestErr(data)) {
                    BankCardListResponse response = (BankCardListResponse) data.getBody();
                    List<BindBankCardInfoBean> list = response.getList();
                    if (list != null && list.size() > 0) {
                        isSetPayPassword();
                    } else {
                        showVerificationDialog("您需要先绑定银行卡才可提现", 1);
                    }
                }
                break;

            case Task.WHETHER_SET_PAY_PASSWORD:
                if (handlerRequestErr(data)) {
                    WhetherSetPaymentPasswordResponse response = (WhetherSetPaymentPasswordResponse) data.getBody();
                    if (response.getIsPayPass().equals("1")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("balanceOfAccount", mine_tv_capital.getText().toString());
                        bundle.putDouble("balanceValue", mUserAccount.getAccountBalance());
                        goPage(WithdrawDepositActivity.class, bundle);
                    } else {
                        showVerificationDialog("您需要先设置支付密码才可提现", 2);
                    }
                }
                break;

            case Task.QUERY_RETURN_FEE_ACCOUNT:
                refreshLayout.finishRefresh();
                if (handlerRequestErr(data)){
                    mUserAccount = (UserAccount)data.getBody();
                    if (!ShareData.getShareBooleanData(ShareData.SHOW_AMOUNT)){
                        mine_tv_capital.setText(formatString(mUserAccount.getAccountBalance()));
                        double accountBalance = mUserAccount.getAccountBalance();
                        mine_tv_capital.setText(formatString(accountBalance));
                        mine_tv_earned.setText(formatString(mUserAccount.getTotalReturnFee()));
                        mine_tv_duein.setText(formatString(mUserAccount.getAmountToReturn()));

                    } else {
                        mine_tv_capital.setText("****");
                        mine_tv_earned.setText("****");
                        mine_tv_duein.setText("****");
                    }
                }
                break;
        }
    }

    public String formatString(double data) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        return df.format(data);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(MessageEvent event){
//        if (event.getTag() == Constants.GO_WITHDRAW_DEPOSIT){
//            isSetPayPassword();
//        }
//    }

}

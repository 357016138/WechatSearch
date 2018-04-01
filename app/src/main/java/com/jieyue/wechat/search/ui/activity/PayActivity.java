package com.jieyue.wechat.search.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.bj.paysdk.domain.TrPayResult;
import com.base.bj.paysdk.listener.PayResultListener;
import com.base.bj.paysdk.utils.TrPay;
import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.bean.CoinBean;
import com.jieyue.wechat.search.bean.DataBean;
import com.jieyue.wechat.search.bean.OrderBean;
import com.jieyue.wechat.search.common.BaseActivity;
import com.jieyue.wechat.search.common.ShareData;
import com.jieyue.wechat.search.network.HttpType;
import com.jieyue.wechat.search.network.RequestParams;
import com.jieyue.wechat.search.network.ResultData;
import com.jieyue.wechat.search.network.Task;
import com.jieyue.wechat.search.network.UrlConfig;
import com.jieyue.wechat.search.utils.DeviceUtils;
import com.jieyue.wechat.search.utils.StringUtils;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class PayActivity extends BaseActivity {


    @BindView(R.id.tv_money_num)
    TextView tv_money_num;
    @BindView(R.id.tv_coin_num)
    TextView tv_coin_num;
    @BindView(R.id.rl_payment_alipay)
    RelativeLayout rl_payment_alipay;
    @BindView(R.id.rl_payment_wechat)
    RelativeLayout rl_payment_wechat;
    @BindView(R.id.rl_payment_tiny_coin)
    RelativeLayout rl_payment_tiny_coin;
    @BindView(R.id.ll_btn)
    LinearLayout ll_btn;
    @BindView(R.id.btn_submit)
    TextView btn_submit;

    private static final String NOTIFYURL = "http://www.vxsousuo.com/order/pay/notify.json";
    private String orderId;
    private OrderBean orderBean;

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_pay);
    }

    @Override
    public void dealLogicBeforeInitView() {
        orderId = getIntentData().getString("orderId");

        orderId = "b184f2828eaa44bdbb1f369970b76e68";

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        topBar.setTitle("选择支付方式");
        topBar.setLineVisible(true);
    }

    @Override
    public void dealLogicAfterInitView() {

        getTinyCoinNum();
        getOrderDes();
    }



    @OnClick({R.id.rl_payment_alipay, R.id.rl_payment_wechat,R.id.rl_payment_tiny_coin, R.id.btn_submit})
    @Override
    public void onClickEvent(View view) {
        Intent intent =null;
        switch (view.getId()) {
            case R.id.rl_payment_alipay:                  //支付宝支付

                goToPayByAliPay(orderBean.getOrderName(),orderId,orderBean.getPayMoney(),"0",NOTIFYURL,ShareData.getShareStringData(ShareData.USER_ID));

                break;
            case R.id.rl_payment_wechat:                 //微信支付

                goToPayByWechat(orderBean.getOrderName(),orderId,orderBean.getPayMoney(),"0",NOTIFYURL,ShareData.getShareStringData(ShareData.USER_ID));

                break;
            case R.id.rl_payment_tiny_coin:              //微币支付
                goToPayByCoin();
                break;
            case R.id.btn_submit:                 //提交


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


    /***
     * 查询微币数量
     * */
    private void getTinyCoinNum() {
        RequestParams params = new RequestParams(UrlConfig.URL_GET_TINY_COIN_NUM);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        startRequest(Task.GET_TINY_COIN_NUM, params, CoinBean.class);

    }

    /***
     * 获取订单详情
     * */
    private void getOrderDes() {
        RequestParams params = new RequestParams(UrlConfig.URL_GET_ORDER_DES);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        params.add("orderId", orderId);
        startRequest(Task.GET_ORDER_DES, params, OrderBean.class);

    }
    /***
     * 用微币支付
     * */
    private void goToPayByCoin() {

       /**
        * userId  Long	是	用户ID
         orderId  String	是	订单id
        payAmount int  是	支付微币金额 单位分  小于等于0 写0
        couponId String 否	优惠券id
        appkey   String 是	软件的appkey  Trpay给的值  安卓 48b8f3bf366148a9aa0ba89e2b5d3ec1
        version String 是	版本，固定值    1.0
        payType int  是	   支付方式固定值 4    说明 1支付宝2微信3银联  4 微币支付  5优惠券支付
        orderName String 否	商品名称    发布微信群
        payTime String  否	支付时间  2016-08-12 22:10:32
        * */

        RequestParams params = new RequestParams(UrlConfig.URL_PAY_BY_COIN);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("userId", ShareData.getShareStringData(ShareData.USER_ID));
        params.add("orderId", orderBean.getOrderId());
        params.add("payAmount", orderBean.getPayMoney());
        params.add("couponId", "");
        params.add("appkey", "48b8f3bf366148a9aa0ba89e2b5d3ec1");
        params.add("version", "1.0");
        params.add("payType", 4);
        params.add("orderName", orderBean.getOrderName());
        params.add("payTime", System.currentTimeMillis());
        startRequest(Task.PAY_BY_COIN, params, OrderBean.class);

    }


    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.GET_TINY_COIN_NUM:
                if (handlerRequestErr(data)) {
                    //获取微币数量
                    CoinBean coinBean = (CoinBean) data.getBody();
                    tv_coin_num.setText("可用个数:"+coinBean.getVcoin());

                }
                break;
            case Task.GET_ORDER_DES:
                if (handlerRequestErr(data)) {
                    orderBean = (OrderBean) data.getBody();
                    long payMoney = orderBean.getPayMoney()/100;
                    tv_money_num.setText("¥"+formatString(payMoney));
                }
                break;
            case Task.PAY_BY_COIN:
                if (handlerRequestErr(data)) {
                    toast(data.getRspMsg());
                    finish();
                }
                break;
            default:
                break;
        }
    }

    public String formatString(long data) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(data);
    }


    /**
     * 2.发起支付宝支付
     * @param tradename       商品名称
     * @param outtradeno      商户系统订单号(商户系统内唯一)
     * @param amount          商品价格（单位：分。如1.5元传150）
     * @param backparams      商户系统回调参数
     * @param notifyurl       商户系统回调地址
     * @param userid          商户系统用户ID(如：trpay@52yszd.com，商户系统内唯一)*/
    public void goToPayByAliPay(String tradename,String outtradeno,long amount,String backparams,String notifyurl,String userid){

        TrPay.getInstance(PayActivity.this).callAlipay(tradename, outtradeno, amount, backparams, notifyurl, userid, new PayResultListener() {
            /**
             * 支付完成回调
             * @param context        上下文
             * @param outtradeno   商户系统订单号
             * @param resultCode   支付状态(RESULT_CODE_SUCC：支付成功、RESULT_CODE_FAIL：支付失败)
             * @param resultString  支付结果
             * @param payType      支付类型（1：支付宝 2：微信 3：银联）
             * @param amount       支付金额
             * @param tradename   商品名称
             */
            @Override
            public void onPayFinish(Context context, String outtradeno, int resultCode, String resultString, int payType, Long                                                           amount, String tradename) {
                if (resultCode == TrPayResult.RESULT_CODE_SUCC.getId()) {
                    //支付成功逻辑处理
                } else if (resultCode == TrPayResult.RESULT_CODE_FAIL.getId()) {
                    //支付失败逻辑处理
                }
            }
        });


    }
    /**
     * 3.发起微信支付
     * @param tradename   商品名称
     * @param outtradeno   商户系统订单号(商户系统内唯一)
     * @param amount        商品价格（单位：分。如1.5元传150）
     * @param backparams 商户系统回调参数
     * @param notifyurl       商户系统回调地址
     * @param userid          商户系统用户ID(如：trpay@52yszd.com，商户系统内唯一)
     */

    public void goToPayByWechat(String tradename,String outtradeno,long amount,String backparams,String notifyurl,String userid){

        TrPay.getInstance(PayActivity.this).callWxPay(tradename, outtradeno, amount, backparams, notifyurl, userid, new PayResultListener() {
            /**
             * 支付完成回调
             * @param context        上下文
             * @param outtradeno   商户系统订单号
             * @param resultCode   支付状态(RESULT_CODE_SUCC：支付成功、RESULT_CODE_FAIL：支付失败)
             * @param resultString  支付结果
             * @param payType      支付类型（1：支付宝 2：微信 3：银联）
             * @param amount       支付金额
             * @param tradename   商品名称
             */
            @Override
            public void onPayFinish(Context context, String outtradeno, int resultCode, String resultString, int payType, Long                                                           amount, String tradename) {
                if (resultCode == TrPayResult.RESULT_CODE_SUCC.getId()) {
                    //支付成功逻辑处理
                } else if (resultCode == TrPayResult.RESULT_CODE_FAIL.getId()) {
                    //支付失败逻辑处理
                }
            }
        });

    }


}

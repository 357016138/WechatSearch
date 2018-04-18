package com.jieyue.wechat.search.ui.activity;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.bean.ProductDetailBean;
import com.jieyue.wechat.search.common.BaseActivity;
import com.jieyue.wechat.search.network.RequestParams;
import com.jieyue.wechat.search.network.ResultData;
import com.jieyue.wechat.search.network.Task;
import com.jieyue.wechat.search.network.UrlConfig;
import com.jieyue.wechat.search.utils.DateUtils;
import com.jieyue.wechat.search.utils.DeviceUtils;
import com.jieyue.wechat.search.utils.UserManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * 详情页
 *
 * */
public class ProductDetailActivity extends BaseActivity {

    private String uniqueId;
    @BindView(R.id.iv_pic1)
    ImageView iv_pic1;
    @BindView(R.id.tv_detail_des)
    TextView tv_detail_des;
    @BindView(R.id.tv_detail_look)
    TextView tv_detail_look;
    @BindView(R.id.tv_detail_time)
    TextView tv_detail_time;
    @BindView(R.id.tv_detail_category)
    TextView tv_detail_category;
    @BindView(R.id.tv_detail_address)
    TextView tv_detail_address;
    @BindView(R.id.tv_detail_tag)
    TextView tv_detail_tag;


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
        topBar.setLineVisible(true);
    }

    @Override
    public void dealLogicAfterInitView() {
        getProductDetail(uniqueId);
        addLookCount(uniqueId);
    }

    @OnClick({R.id.tv_add})
    @Override
    public void onClickEvent(View view) {

        switch (view.getId()) {
            case R.id.tv_add:                //跳转到微信
                toWeChatScanDirect();
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
     *  "userId": 1,
     "groupInfoId": 3,
     "uniqueId": "fb47093524a24a76bc237a679cf456bf",
     "groupName": "发布微信群测试搜索功能",
     "groupImage": "http://p5bahoihf.bkt.clouddn.com/Fg18AkbIDyB_QYegczxTugQVD4ct",
     "coverImage": "http://p5bahoihf.bkt.clouddn.com/Fg18AkbIDyB_QYegczxTugQVD4ct",
     "userWechat": "tjggtlx",
     "tags": "测试|搜索|微信群",
     "description": "这是一个微信群搜索测试发布,为了试验搜索功能是否可用,各位请勿加入.",
     "codeType": 2,
     "updateDate": 1522997899000,
     "province": "北京市",
     "city": "丰台区",
     "parentCategory": "兴趣",
     "category": "文学",
     "lookCount": 15
     * */
    private void updateDetailInfo(ProductDetailBean dataBean) {


        topBar.setTitle(dataBean.getGroupName());
        tv_detail_des.setText(dataBean.getDescription());
        tv_detail_look.setText(dataBean.getLookCount()+" 个关注");
        tv_detail_time.setText( DateUtils.formatDate(dataBean.getUpdateDate()));
        tv_detail_category.setText(dataBean.getParentCategory()+" "+dataBean.getCategory());
        tv_detail_address.setText(dataBean.getProvince()+" "+dataBean.getCity());
        tv_detail_tag.setText(dataBean.getTags());
        Glide.with(this).load(dataBean.getGroupImage()).into(iv_pic1);


    }


    /**
     * 跳转到微信
     */
    private void getWechatApi(){
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // TODtO: handle exception
            toast("检查到您手机没有安装微信，请安装后使用该功能");
        }
    }

    public void toWeChatScanDirect() {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI"));
            intent.putExtra("LauncherUI.From.Scaner.Shortcut", true);
            intent.setFlags(335544320);
            intent.setAction("android.intent.action.VIEW");
            startActivity(intent);
        } catch (Exception e) {
        }
    }

}

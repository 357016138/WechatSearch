package com.jieyue.wechat.search.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.google.gson.reflect.TypeToken;
import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.adapter.SearchAdapter;
import com.jieyue.wechat.search.bean.BannerBean;
import com.jieyue.wechat.search.bean.ProvinceBean;
import com.jieyue.wechat.search.bean.SearchBean;
import com.jieyue.wechat.search.common.BaseFragment;
import com.jieyue.wechat.search.common.Constants;
import com.jieyue.wechat.search.common.ShareData;
import com.jieyue.wechat.search.listener.OperateListener;
import com.jieyue.wechat.search.network.RequestParams;
import com.jieyue.wechat.search.network.ResultData;
import com.jieyue.wechat.search.network.Task;
import com.jieyue.wechat.search.network.UrlConfig;
import com.jieyue.wechat.search.service.MessageEvent;
import com.jieyue.wechat.search.ui.activity.MsgNoticeActivity;
import com.jieyue.wechat.search.ui.activity.ProductDetailActivity;
import com.jieyue.wechat.search.ui.activity.SearchActivity;
import com.jieyue.wechat.search.utils.DeviceUtils;
import com.jieyue.wechat.search.utils.UserUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 首页
 * Created by song on 2018/1/30.
 */
public class HomeFragment extends BaseFragment implements OperateListener {
    Unbinder unbinder;
    @BindView(R.id.iv_msg_new)
    ImageView iv_new_msg;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rl_search)
    RelativeLayout rl_search;
    @BindView(R.id.fragmentBill_recyclerview)
    RecyclerView fragmentBill_recyclerview;
    @BindView(R.id.scrollview)
    ScrollView scrollview;
    @BindView(R.id.banner)
    Banner banner;
    private List<BannerBean> BANNER_ITEMS = new ArrayList<>();

    private SearchAdapter adapter;
    private int pageNum = 1;            // 当前页码
    private final int pageSize = 45;   // 每页条数
    private Activity activity;

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
        activity = getActivity();
        EventBus.getDefault().register(this);

        LinearLayoutManager llm = new LinearLayoutManager(activity);
        llm.setOrientation(LinearLayout.VERTICAL);
        fragmentBill_recyclerview.setLayoutManager(llm);
//        int spacingInPixels = 12;
//        fragmentBill_recyclerview.addItemDecoration(new RecyclerViewItemDecoration(spacingInPixels));
        //recyclerview 布局设置end

        adapter = new SearchAdapter(activity, 0);
        fragmentBill_recyclerview.setAdapter(adapter);
        adapter.setOperateListener(this);


        /**
         * 下拉刷新
         * */
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                getNewDataList();
            }
        });
        /**
         *上拉加载更多
         * */
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageNum += 1;
                getNewDataList();
            }
        });


//
//        scrollview.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//
//            }
//        });


    }

    /**
     * 初始化数据
     * */
    private void initData() {
        getNewDataList();
        getBannerData();
    }

    @OnClick({R.id.rl_msg,R.id.rl_search})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.rl_search:
                goPage(SearchActivity.class);
                break;
            case R.id.rl_msg:
                if (!isLogin()) return;
                goPage(MsgNoticeActivity.class);
//                goPage(PayActivity.class);
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


    /***
     * 搜索信息列表
     * */
    private void getNewDataList() {
        RequestParams params = new RequestParams(UrlConfig.URL_GET_NEW_DATA_LIST);
        params.add("pid", DeviceUtils.getDeviceUniqueId(getActivity()));
        params.add("searchUserId", ShareData.getShareStringData(ShareData.USER_ID));
        params.add("pageSize", pageSize);
        params.add("pageNum", pageNum);
        params.add("sort", "2");
        startRequest(Task.GET_NEW_DATA_LIST, params, SearchBean.class);

    }
    /***
     * 获取banner信息
     * */
    private void getBannerData() {
        RequestParams params = new RequestParams(UrlConfig.URL_BANNER_DATA);
        startRequest(Task.BANNER_DATA, params,  new TypeToken<List<BannerBean>>() {}.getType(), false);
    }

    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.GET_NEW_DATA_LIST:
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadmore();
                if (handlerRequestErr(data)) {
                    SearchBean searchBean = (SearchBean) data.getBody();
                    //------------------数据异常情况-------------------
                    if (searchBean == null || searchBean.getGroups() == null || searchBean.getGroups().size() <= 0) {
//                        if (pageNum == 1) {
//                            showNodata();
//                        }
//                        return;
                    }
                    //-----------------数据正常情况--------------------
                    List<SearchBean.ProductBean> dataListProm = searchBean.getGroups();
                    if (pageNum == 1) {
//                        showList();
                        adapter.setData(dataListProm);
                    } else {
                        adapter.getData().addAll(dataListProm);
                    }
                    //如果返回数据不够10条，就不能继续上拉加载更多
                    refreshLayout.setEnableLoadmore(dataListProm.size() >= pageSize);
//                    if (priceBillBean.getTotalPages() == 1 || dataListProm.size() >= pageSize) {
//                        fragmentBill_refreshLayout.setEnableLoadmore(false);
//                    }
                    adapter.notifyDataSetChanged();

                }
                break;
            case Task.BANNER_DATA:
                if (handlerRequestErr(data)) {
                    List<BannerBean> bannerBeanList = (List<BannerBean>) data.getBody();
                    if (bannerBeanList != null&& bannerBeanList.size() > 0) {
                        List<String> bannerList = new ArrayList<>();
                        for(BannerBean bannerItem : bannerBeanList) {
                            if(bannerItem != null) {
                                bannerList.add(bannerItem.getImageUrl());
                            }
                        }
                        banner.update(bannerList);
//                        BANNER_ITEMS.clear();
//                        BANNER_ITEMS.addAll(bannerBeanList);
                        banner.setOnBannerListener(new OnBannerListener() {
                            @Override
                            public void OnBannerClick(int position) {
                                if(bannerBeanList.get(position).getPageUrl() != null){
//                                    goWebPage("", bannerBeanList.get(position).getPageUrl());
                                    goWebPage("", "https://www.baidu.com/");
                                }
                            }
                        });
                    }
                }
                break;

            default:
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

    @Override
    public void operate(String operateType, Object str) {
        Bundle bd = new Bundle();
        String uniqueId = (String) str;
        bd.putString("uniqueId", uniqueId);
        switch (operateType) {
            case "1":                   //条目点击事件
                goPage(ProductDetailActivity.class, bd);
                break;
            default:
                break;
        }
    }

    public static class BannerItem {

        public String url;
        public String title;
        public String linkUrl;

        public BannerItem() {
        }

        public BannerItem(String title, String url, String linkUrl) {
            this.url = url;
            this.title = title;
            this.linkUrl = linkUrl;
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }



}

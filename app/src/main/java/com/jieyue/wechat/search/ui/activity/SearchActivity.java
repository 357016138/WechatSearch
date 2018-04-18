package com.jieyue.wechat.search.ui.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.adapter.SearchAdapter;
import com.jieyue.wechat.search.bean.SearchBean;
import com.jieyue.wechat.search.common.BaseActivity;
import com.jieyue.wechat.search.common.ShareData;
import com.jieyue.wechat.search.listener.OperateListener;
import com.jieyue.wechat.search.network.RequestParams;
import com.jieyue.wechat.search.network.ResultData;
import com.jieyue.wechat.search.network.Task;
import com.jieyue.wechat.search.network.UrlConfig;
import com.jieyue.wechat.search.utils.DeviceUtils;
import com.jieyue.wechat.search.utils.RecyclerViewItemDecoration;
import com.jieyue.wechat.search.utils.StringUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.jieyue.wechat.search.common.BaseActivity.BasePageSet.NO_TOPBAR_DEFAULT_PAGE;

public class SearchActivity extends BaseActivity implements TextView.OnEditorActionListener,OperateListener {
    @BindView(R.id.iv_search)
    ImageView iv_search;
    @BindView(R.id.et_search)
    EditText et_search;
    @BindView(R.id.no_data_refreshLayout)
    SmartRefreshLayout no_data_refreshLayout;
    @BindView(R.id.fragmentBill_refreshLayout)
    SmartRefreshLayout fragmentBill_refreshLayout;
    @BindView(R.id.fragmentBill_recyclerview)
    RecyclerView fragmentBill_recyclerview;
    private SearchAdapter adapter;

    private int pageNum = 1;            // 当前页码
    private final int pageSize = 15;   // 每页条数

    @Override
    public void setContentLayout() {
        setContentView(R.layout.activity_search,NO_TOPBAR_DEFAULT_PAGE);
    }

    @Override
    public void dealLogicBeforeInitView() {

    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        et_search.setOnEditorActionListener(this);   //初始化监听


        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayout.VERTICAL);
        fragmentBill_recyclerview.setLayoutManager(llm);
        int spacingInPixels = 12;
        fragmentBill_recyclerview.addItemDecoration(new RecyclerViewItemDecoration(spacingInPixels));
        //recyclerview 布局设置end

        adapter = new SearchAdapter(this, 0);
        fragmentBill_recyclerview.setAdapter(adapter);
        adapter.setOperateListener(this);

        //禁用下拉刷新
        no_data_refreshLayout.setEnableRefresh(false);
        fragmentBill_refreshLayout.setEnableRefresh(false);
        /**
         *上拉加载更多
         * */
        fragmentBill_refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                pageNum += 1;
//                getListData(curPage, PAGESIZE, "0", false);
            }
        });
    }

    @Override
    public void dealLogicAfterInitView() {
        //弹出软键盘
        DeviceUtils.showSoftInputFromWindow(this,et_search);
    }

    @OnClick({R.id.iv_search,R.id.iv_back})
    @Override
    public void onClickEvent(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                getSearchContent(true);
                break;
            case R.id.iv_back:
                finish();
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
    public void OnTopLeftClick() {
    }

    @Override
    public void OnTopRightClick() {

    }

    private void getSearchContent(boolean flag) {

        if (flag){
            String mKeyWord = et_search.getText().toString().replace(" ","");
            if(StringUtils.isEmpty(mKeyWord)) {
                toast("请输入搜索内容");
                return;
            }

            getSearchList(mKeyWord);
            iv_search.setFocusable(true);
            iv_search.setFocusableInTouchMode(true);
        }

    }

    /***
     * 搜索信息列表
     * */
    private void getSearchList(String mKeyWord) {
        RequestParams params = new RequestParams(UrlConfig.URL_GET_SEARCH_LIST);
        params.add("pid", DeviceUtils.getDeviceUniqueId(this));
        params.add("searchUserId", ShareData.getShareStringData(ShareData.USER_ID));
        params.add("keyword", mKeyWord);
        params.add("pageSize", pageSize);
        params.add("pageNum", pageNum);
        params.add("sort", "1");

        startRequest(Task.GET_SEARCH_LIST, params, SearchBean.class);

    }


    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.GET_SEARCH_LIST:
                fragmentBill_refreshLayout.finishRefresh();
                fragmentBill_refreshLayout.finishLoadmore();
                no_data_refreshLayout.finishRefresh();
                if (handlerRequestErr(data)) {
                    SearchBean searchBean = (SearchBean) data.getBody();
                    //------------------数据异常情况-------------------
                    if (searchBean == null || searchBean.getGroups() == null || searchBean.getGroups().size() <= 0) {
                        if (pageNum == 1) {
                            showNodata();
                        }
                        return;
                    }
                    //-----------------数据正常情况--------------------
                    List<SearchBean.ProductBean> dataListProm = searchBean.getGroups();
                    if (pageNum == 1) {
                        showList();
                        adapter.setData(dataListProm);
                    } else {
                        adapter.getData().addAll(dataListProm);
                    }
                    //如果返回数据不够10条，就不能继续上拉加载更多
                    fragmentBill_refreshLayout.setEnableLoadmore(dataListProm.size() >= pageSize);
//                    if (priceBillBean.getTotalPages() == 1 || dataListProm.size() >= pageSize) {
//                        fragmentBill_refreshLayout.setEnableLoadmore(false);
//                    }
                    adapter.notifyDataSetChanged();

                } else {
                    if (pageNum == 1) {
                        showNodata();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void showNodata() {
        no_data_refreshLayout.setVisibility(View.VISIBLE);
        fragmentBill_refreshLayout.setVisibility(View.GONE);
    }

    private void showList() {
        no_data_refreshLayout.setVisibility(View.GONE);
        fragmentBill_refreshLayout.setVisibility(View.VISIBLE);
    }

    //EditText的搜索键监听
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            getSearchContent(true);
            return true;
        }
        return false;
    }

    /**
     * 点击事件的回调
     *
     * */
    @Override
    public void operate(String operateType, Object bean) {

    }
}

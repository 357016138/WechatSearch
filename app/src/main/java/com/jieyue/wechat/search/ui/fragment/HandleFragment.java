package com.jieyue.wechat.search.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.adapter.LoanOrderListAdapter;
import com.jieyue.wechat.search.bean.LoanOrderListBean;
import com.jieyue.wechat.search.common.BaseFragment;
import com.jieyue.wechat.search.common.Constants;
import com.jieyue.wechat.search.network.RequestParams;
import com.jieyue.wechat.search.network.ResultData;
import com.jieyue.wechat.search.network.Task;
import com.jieyue.wechat.search.network.UrlConfig;
import com.jieyue.wechat.search.service.MessageEvent;
import com.jieyue.wechat.search.ui.activity.ConsultPriceActivity;
import com.jieyue.wechat.search.utils.DeviceUtils;
import com.jieyue.wechat.search.utils.UserManager;
import com.jieyue.wechat.search.utils.UserUtils;
import com.jieyue.wechat.search.view.BankCardListItemDivider;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 贷款订单（审核中）
 */
public class HandleFragment extends BaseFragment {

    private Unbinder unbinder;
    @BindView(R.id.no_data_refreshLayout)
    SmartRefreshLayout no_data_refreshLayout;
    @BindView(R.id.btn_apply)
    Button btn_apply;
    @BindView(R.id.fragmentBill_refreshLayout)
    SmartRefreshLayout fragmentBill_refreshLayout;
    @BindView(R.id.fragmentBill_recyclerview)
    RecyclerView fragmentBill_recyclerview;

    private List<LoanOrderListBean.LoanListBean> loanList;
    private LoanOrderListAdapter mAdapter;
    private int curPage = 1; // 当前页码
    private final int PAGESIZE = 15; // 每月条数

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loan_bill_all, container, false);
        initView(view);
        initData();
        return view;
    }

    /**
     * 初始化控件 用ButterKnife 简约
     */
    private void initView(View view) {
        //一定要解绑 在onDestroyView里
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
//        fragmentBill_refreshLayout.autoRefresh();
        //recyclerview 布局设置start
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayout.VERTICAL);
//        fragmentBill_refreshLayout.autoRefresh();
        fragmentBill_recyclerview.setLayoutManager(llm);
        fragmentBill_recyclerview.addItemDecoration(new BankCardListItemDivider(getActivity(), 10));
        mAdapter = new LoanOrderListAdapter(getActivity());
        fragmentBill_recyclerview.setAdapter(mAdapter);

        //下拉刷新
        Refresh();

        //上拉加载更多
        lordmore();

        no_data_refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                curPage = 1;
                request(curPage, PAGESIZE, "1", false);
            }
        });

        mAdapter.setOnItemClickLitener(new LoanOrderListAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                String orderNum = loanList.get(position).getOrderNum();
                String pid = DeviceUtils.getDeviceUniqueId(getActivity());
                String userId = UserManager.getUserId();
                goWebPage("贷款订单详情", String.format(Locale.US, UrlConfig.URL_LOAN_ORDER_DETAIL,
                        pid, orderNum, userId));
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPage(ConsultPriceActivity.class);
            }
        });

    }

    //下拉刷新
    private void Refresh() {
        fragmentBill_refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                curPage = 1;
                request(curPage, PAGESIZE, "1", false);
            }
        });

    }

    //上拉加载更多
    private void lordmore() {
        fragmentBill_refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                curPage += 1;
                request(curPage, PAGESIZE, "1", false);
            }
        });
    }

    //发起请求
    private void request(int curPage, int pagesize, String inquiryStatus, boolean showDialog) {
        RequestParams params = new RequestParams(UrlConfig.URL_LOAN_ORDER_LIST);
        params.add("pid", DeviceUtils.getDeviceUniqueId(getActivity()));
        params.add("userId", UserManager.getUserId());
        params.add("inquiryStatus", inquiryStatus);
        params.add("curPage", curPage + "");
        params.add("pageSize", pagesize + "");
        startRequest(Task.Loan_Order_List, params, LoanOrderListBean.class, showDialog);
    }

    //数据回调
    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.Loan_Order_List:
                fragmentBill_refreshLayout.finishRefresh();
                fragmentBill_refreshLayout.finishLoadmore();
                no_data_refreshLayout.finishRefresh();
                if (handlerRequestErr(data)) {
                    LoanOrderListBean loanOrderListBean = (LoanOrderListBean) data.getBody();
                    //------------------数据异常情况-------------------
                    if (loanOrderListBean == null || loanOrderListBean.getLoanList() == null || loanOrderListBean.getLoanList().size() <= 0) {
                        if (curPage == 1) {
                            showNodata();
                        }
                        return;
                    }
                    //-----------------数据正常情况--------------------
                    loanList = loanOrderListBean.getLoanList();
                    if (curPage == 1) {
                        showList();
                        mAdapter.setData(loanList);
                    } else {
                        mAdapter.getData().addAll(loanList);
                    }
                    //如果返回数据不够10条，就不能继续上拉加载更多
                    //fragmentBill_refreshLayout.setEnableLoadmore(loanList.size() >= PAGESIZE);
                    if (loanOrderListBean.getTotalPages() == 1 || loanList.size() >= PAGESIZE) {
                        fragmentBill_refreshLayout.setEnableLoadmore(false);
                    }
                    mAdapter.notifyDataSetChanged();

                } else {
                    if (curPage == 1) {
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

    /**
     * 初始化数据
     */
    private void initData() {
        request(curPage, PAGESIZE, "1", true);
    }

    @Override
    public void onClickEvent(View view) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getTag() == Constants.GET_REFRESH_ORDER_LIST) {
            if (UserUtils.isLogin()) {
                curPage = 1;
                request(curPage, PAGESIZE, "1", false);
            }

        }

    }

}

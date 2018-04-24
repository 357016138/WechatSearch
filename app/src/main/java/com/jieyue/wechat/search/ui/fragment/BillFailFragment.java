package com.jieyue.wechat.search.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;
import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.adapter.PublishBillAdapter;
import com.jieyue.wechat.search.bean.DataBean;
import com.jieyue.wechat.search.bean.PublishBillBean;
import com.jieyue.wechat.search.common.BaseFragment;
import com.jieyue.wechat.search.listener.OperateListener;
import com.jieyue.wechat.search.network.RequestParams;
import com.jieyue.wechat.search.network.ResultData;
import com.jieyue.wechat.search.network.Task;
import com.jieyue.wechat.search.network.UrlConfig;
import com.jieyue.wechat.search.ui.activity.ConsultPriceActivity;
import com.jieyue.wechat.search.ui.activity.PayActivity;
import com.jieyue.wechat.search.ui.activity.PreferenceProductActivity;
import com.jieyue.wechat.search.ui.activity.ProductDetailActivity;
import com.jieyue.wechat.search.ui.activity.PublishWechatGroupActivity;
import com.jieyue.wechat.search.ui.activity.RecommendProductActivity;
import com.jieyue.wechat.search.utils.DeviceUtils;
import com.jieyue.wechat.search.utils.RecyclerViewItemDecoration;
import com.jieyue.wechat.search.utils.UserManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by Administrator on 2018/4/19.
 *
 * 询价订单（失败）
 */

public class BillFailFragment extends BaseFragment implements OperateListener {

    private Unbinder unbinder;
    @BindView(R.id.no_data_refreshLayout)
    SmartRefreshLayout no_data_refreshLayout;
    @BindView(R.id.btn_apply)
    Button btn_apply;
    @BindView(R.id.fragmentBill_refreshLayout)
    SmartRefreshLayout fragmentBill_refreshLayout;
    @BindView(R.id.fragmentBill_recyclerview)
    RecyclerView fragmentBill_recyclerview;

    private PublishBillAdapter adapter;
    private int pageNum = 1;             // 当前页码
    private final int PAGESIZE = 15;     // 每页条数
    private PublishBillBean publishBillBean;
    private List<PublishBillBean> beanList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_price_bill_progress, container, false);
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
//        fragmentBill_refreshLayout.autoRefresh();
        //recyclerview 布局设置start
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayout.VERTICAL);
        fragmentBill_recyclerview.setLayoutManager(llm);
        //recyclerview 布局设置end

        adapter = new PublishBillAdapter(getActivity(), 0);
        fragmentBill_recyclerview.setAdapter(adapter);
        adapter.setOperateListener(this);

        /**
         *下拉刷新
         * */
        fragmentBill_refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                getListData(pageNum, 3, false);
            }
        });

        /**
         *上拉加载更多
         * */
        fragmentBill_refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {

                pageNum += 1;
                getListData(pageNum, 3, false);
            }
        });

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goPage(PublishWechatGroupActivity.class);
            }
        });

        no_data_refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNum = 1;
                getListData(pageNum, 3, false);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * 初始化数据
     */
    private void initData() {

        getListData(pageNum, 3, true);

    }

    @Override
    public void onClickEvent(View view) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 获取list数据
     */
    public void getListData(int pageNum, int codeType, boolean showDialog) {
        RequestParams params = new RequestParams(UrlConfig.URL_PRICE_BILL);
        params.add("pid", DeviceUtils.getDeviceUniqueId(getActivity()));
        params.add("userId", UserManager.getUserId());
        params.add("pageNum", pageNum);
        params.add("codeType", codeType);
        startRequest(Task.PRICE_BILL, params, new TypeToken<List<PublishBillBean>>() {}.getType(), showDialog);
    }
    /**
     * 删除订单订单
     */
    public void deleteOrder(String uniqueId) {
        RequestParams params = new RequestParams(UrlConfig.URL_DELETE_ORDER);
        params.add("pid", DeviceUtils.getDeviceUniqueId(getActivity()));
        params.add("userId", UserManager.getUserId());
        params.add("uniqueId", uniqueId);
        startRequest(Task.DELETE_ORDER, params, DataBean.class);
    }
    @Override
    public void onRefresh(Call call, int tag, ResultData data) {
        super.onRefresh(call, tag, data);
        switch (tag) {
            case Task.PRICE_BILL:
                fragmentBill_refreshLayout.finishRefresh();
                fragmentBill_refreshLayout.finishLoadmore();
                no_data_refreshLayout.finishRefresh();
                if (handlerRequestErr(data)) {
                    beanList = (List<PublishBillBean>) data.getBody();
                    //------------------数据异常情况-------------------
                    if (beanList == null||beanList.size() == 0) {
                        if (pageNum == 1) {
                            showNodata();
                        }
                        return;
                    }
                    //-----------------数据正常情况--------------------
                    if (pageNum == 1) {
                        showList();
                        adapter.setData(beanList);
                    } else {
                        adapter.getData().addAll(beanList);
                    }
                    //如果返回数据不够10条，就不能继续上拉加载更多
                    fragmentBill_refreshLayout.setEnableLoadmore(beanList.size() >= PAGESIZE);
                    adapter.notifyDataSetChanged();

                } else {
                    if (pageNum == 1) {
                        showNodata();
                    }
                }
                break;
            case Task.DELETE_ORDER:
                if (handlerRequestErr(data)) { //删除成功
                    toast("删除成功");
                    beanList.remove(publishBillBean);
                    adapter.notifyDataSetChanged();
                }else{
                    toast("删除失败");
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

    @Override
    public void operate(String operateType, Object bean) {
        Bundle bd = new Bundle();
        publishBillBean = (PublishBillBean) bean;
        bd.putString("uniqueId", publishBillBean.getOrderId());
        switch (operateType) {
            case "1":              //条目点击事件
                goPage(ProductDetailActivity.class, bd);
                break;
            case "2":              //编辑 根据状态去判断 弹出
                showMenu(publishBillBean.getCodeType(),publishBillBean.getOrderId());
                break;
            default:
                break;
        }
    }

    /**
     * 弹出菜单数据初始化
     */
    private void showMenu(String codeType,String uniqueId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.dialog_no_bg);
        View myView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_layout, null);
        builder.setView(myView);
        Button bt_1 = myView.findViewById(R.id.bt_1);            //支付
        Button bt_3 = myView.findViewById(R.id.bt_3);            //刷新
        Button bt_4 = myView.findViewById(R.id.bt_4);            //修改
        Button bt_5 = myView.findViewById(R.id.bt_5);            //删除
        Button bt_cancel = myView.findViewById(R.id.bt_cancel);  //取消

        if ("0".equals(codeType)){
            bt_1.setVisibility(View.VISIBLE);
        }
        if ("2".equals(codeType)){
            bt_3.setVisibility(View.VISIBLE);
        }

        myView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        final Dialog dialog = builder.create();

        //修改
        bt_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("修改");
                dialog.dismiss();
            }
        });
        //删除
        bt_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOrder(uniqueId);
                dialog.dismiss();
            }
        });
        //取消
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}

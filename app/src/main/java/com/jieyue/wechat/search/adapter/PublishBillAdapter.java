package com.jieyue.wechat.search.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jieyue.wechat.search.R;
import com.jieyue.wechat.search.bean.PublishBillBean;
import com.jieyue.wechat.search.listener.OperateListener;

import java.util.List;

/**
 * Created by Administrator on 2018/4/19.
 * 发布订单列表的适配器
 *
 */

public class PublishBillAdapter extends RecyclerView.Adapter {

    private Context context;
    private OperateListener listener;
    private List<PublishBillBean> list;
    private int flag = 0;

    public PublishBillAdapter(Context context,int flag) {
        this.context = context;
        this.flag = flag;
    }

    public void setData(List<PublishBillBean> list) {
        this.list = list;
    }

    public List<PublishBillBean> getData() {
        return list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_price_bill, parent, false);
        PublishBillAdapter.MyViewHolder myViewHolder = new PublishBillAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PublishBillAdapter.MyViewHolder myViewHolder = (PublishBillAdapter.MyViewHolder) holder;
        PublishBillBean publishBillBean = list.get(position);


        myViewHolder.tv_price_bill_item_title.setText(publishBillBean.getTitle());

        myViewHolder.rl_price_bill_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.operate("1",publishBillBean);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list == null? 0 : list.size();
    }

    private static class MyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rl_price_bill_item;
        private TextView tv_price_bill_item_title;

        public MyViewHolder(View itemView) {
            super(itemView);
            rl_price_bill_item = itemView.findViewById(R.id.rl_price_bill_item);
            tv_price_bill_item_title = itemView.findViewById(R.id.tv_price_bill_item_title);


        }

    }

    public void setOperateListener(OperateListener listener) {
        this.listener = listener;
    }
}

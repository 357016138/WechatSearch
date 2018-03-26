package com.jieyue.wechat.search.response;

import com.google.gson.annotations.SerializedName;
import com.jieyue.wechat.search.bean.BindBankCardInfoBean;

import java.util.List;

/**
 * Created by RickBerg on 2018/3/6 0006.
 */

public class BankCardListResponse {

    @SerializedName("bankCardList")
    private List<BindBankCardInfoBean> list;

    public List<BindBankCardInfoBean> getList() {
        return list;
    }

    public void setList(List<BindBankCardInfoBean> list) {
        this.list = list;
    }
}

package com.jieyue.wechat.search.common;

/**
 * Created by Administrator on 2018/1/17 0017.
 * 常量类
 */

public class Constants {

    //客服电话
    public static final String SERVICE_PHONE_NUMBER = "95145";
    //微信公众号
    public static final String WX_NUM = "";
    //重置交易密码结果回调标识
    public static final int RESET_BILL_PASSWORD_REQUEST_CODE = 4000;
    //绑定银行卡的请求码
    public static final int FLAG_OPEN_BIND_BANK_CARD = 4001;
    //忘记密码的请求码
    public static final int FLAG_FORGET_PASSWORD = 4002;

    public static final int MENU_CAMERA = 1001;//相机
    public static final int MENU_ALBUM = 1002;//相册
    public static final int MENU_CANCEL = 1003;//取消
    public static final int REQUEST_PREVIEW = 1007;
    public static final int REQUEST_IMAGE = 1008;
    public static final int REQUEST_SUBMIT = 1009;

    public static final int FINISTH = 3001;
    public static final int GO_WITHDRAW_DEPOSIT = 3002;
    public static final int GET_NEW_MSG = 3003;
    //刷新询价订单列表
    public static final int GET_REFRESH_ORDER_LIST = 3004;
    //跳转到贷款订单列表
    public static final int JUMP_TO_LOAN_LIST = 3005;
    //跳转到询价订单列表
    public static final int JUMP_TO_PRICE_LIST = 3006;

    //查看大图页所用
    public static final String HTTP = "http";
    public static final String IMAGE_WALL_INDEX = "imageWallIndex";
    public static final String IMAGE_WALL_DATA = "imageWallData";

    // 居民身份证号的组成元素
    public static final String[] IDCARD = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "x", "X", " "};

    public static String getProductTag(String type) {
        switch (type) {
            case "1":
                return "额度高";
            case "2":
                return "放款快";
            case "3":
                return "费率低";
            case "4":
                return "热门";
        }
        return "其它";
    }
}

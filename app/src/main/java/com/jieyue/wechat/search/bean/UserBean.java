package com.jieyue.wechat.search.bean;

/**
 * 用户信息
 * Created by fan on 2018/1/22.
 */
public class UserBean {

//"busiCode":"HLAB02","city":"北京市(市辖区)","cityCode":"110100","frontTransNo":"20180303142820853",
// "interfaceNo":"5002","inviter":"","phone":"15311436473","retCode":"0000","retMsg":"登录成功",
// "retTime":"2018-03-03 14:28:20","serverTransNo":"50021725065002939583","userId":"9"

    /**
     * busiCode :
     * city :
     * cityCode :
     * frontTransNo :
     * interfaceNo :
     * inviter :
     * phone :
     * retTime :
     * serverTransNo:
     * userId:
     */

    private String busiCode;
    private String city;
    private String cityCode;
    private String frontTransNo;
    private String interfaceNo;
    private String inviter;
    private String phone;
    private String retTime;
    private String serverTransNo;
    private String userId;
    private String isPayPass;


    public String getBusiCode() {
        return busiCode;
    }

    public void setBusiCode(String busiCode) {
        this.busiCode = busiCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getFrontTransNo() {
        return frontTransNo;
    }

    public void setFrontTransNo(String frontTransNo) {
        this.frontTransNo = frontTransNo;
    }

    public String getInterfaceNo() {
        return interfaceNo;
    }

    public void setInterfaceNo(String interfaceNo) {
        this.interfaceNo = interfaceNo;
    }

    public String getInviter() {
        return inviter;
    }

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRetTime() {
        return retTime;
    }

    public void setRetTime(String retTime) {
        this.retTime = retTime;
    }

    public String getServerTransNo() {
        return serverTransNo;
    }

    public void setServerTransNo(String serverTransNo) {
        this.serverTransNo = serverTransNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsPayPass() {
        return isPayPass;
    }

    public void setIsPayPass(String isPayPass) {
        this.isPayPass = isPayPass;
    }
}

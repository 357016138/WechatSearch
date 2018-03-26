package com.jieyue.wechat.search.utils;

/**
 * 配置文件类
 */
public class ConfigUtils {
    public static final ConfigType configType = ConfigType.UAT;


    public static String getIp() {
        switch (configType) {
            case DEV:
                return "http://172.18.101.14:8080";
            case CS:
                return "http://172.18.101.14:8080";
            case UAT:
                return "http://59.110.172.203";
            case RELEASE:
                return "https://snailhouse.api.iqianjindai.com";
        }
        return "";
    }

    /**
     * H5单独部署地址
     * */
    public static String getH5Ip() {
        switch (configType) {
            case DEV:
                return "http://172.18.101.14";
            case CS:
                return "http://172.18.101.14";
            case UAT:
                return "http://59.110.172.203";
            case RELEASE:
                return "https://snailhouse.static.iqianjindai.com";
        }
        return "";
    }

    public static String getAES() {
        switch (configType) {
            case DEV:
            case CS:
            case UAT:
            case RELEASE:
                return "0123456789123456";
        }
        return "";
    }

    public static String getBucket() {
        switch (configType) {
            case DEV:
            case CS:
                return "iqianjindai";
            case UAT:
            case RELEASE:
                return "iqianjindai-prod";
        }
        return "";
    }

    public static String getEndPoint() {
        switch (configType) {
            case DEV:
            case CS:
                return "http://oss-cn-beijing.aliyuncs.com";
            case UAT:
            case RELEASE:
                return "http://resource.iqianjindai.com";
        }
        return "";
    }

    public static boolean getIsEncrypt() {
        switch (configType) {
            case DEV:
                return false;
            case CS:
            case UAT:
            case RELEASE:
                return true;
        }
        return true;
    }

    public enum ConfigType {
        DEV, CS, UAT, RELEASE
    }

}

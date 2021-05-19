package com.qingshop.mall.framework.enums;

public enum ConfigKey {
    CONFIG_STORAGE("CONFIG_STORAGE","云存储配置"),
    SYSTEM_IS_SET("SYSTEM_IS_SET","系统是否设置"),
    ;

    private String value;
    private String describe;

    private ConfigKey(String value, String describe) {
        this.value = value;
        this.describe = describe;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescribe() {
        return this.describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

}
package com.bench.cache.enums;

import com.bench.common.enums.EnumBase;

/**
 * @className AppCacheClientEnum
 * @autor cold
 * @DATE 2021/6/21 23:14
 **/
public enum AppCacheClientEnum implements EnumBase {
    REDIS_CLIENT("redis客户端");
    AppCacheClientEnum(String message){
        this.message = message;

    }
    private String message;
    @Override
    public String message() {
        return null;
    }

    @Override
    public Number value() {
        return null;
    }
}

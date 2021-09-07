package com.demo.common.exception;

/**
 * 断言处理类,抛出异常
 *
 * @author molong
 * @date 2021/9/6
 */
public class Asserts {

    /**
     * 错误断言，可以给一个 data
     * @param msg 异常信息
     */
    public static void fail(String msg) {
        throw new ApiException(msg);
    }

    /**
     * 错误断言，可以给一个 data
     * @param msg 异常信息
     * @param data 异常时返回的数据
     */
    public static void fail(String msg, Object data) {
        throw new ApiException(msg, data);
    }

    /**
     * 判断是否符合,不符合就错误
     * @param condition 条件
     * @param message   错误消息
     */
    public static void isTrue(boolean condition,String  message) {
        if (!condition) {
            fail(message);
        }
    }
}

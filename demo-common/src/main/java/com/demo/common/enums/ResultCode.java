package com.demo.common.enums;

import lombok.Getter;

/**
 * 全局返回值说明
 *
 * @author molong
 * @date 2021/9/6
 */
public enum ResultCode {
    /**
     * 操作成功
     */
    SUCCESS(0, "操作成功"),
    /**
     * 操作失败
     */
    FAILED(1, "操作失败"),
    /**
     * 参数检验失败
     */
    VALIDATE_FAILED(404, "参数检验失败"),
    /**
     * 暂未登录或token已经过期
     */
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    /**
     * 没有相关权限
     */
    FORBIDDEN(403, "没有相关权限"),
    ;

    /**
     * 错误码
     */
    @Getter
    private final long code;
    /**
     * 消息
     */
    @Getter
    private final String message;

    ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

}

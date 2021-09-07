package com.demo.common.exception;

/**
 * 自定义异常
 *
 * @author molong
 * @date 2021/9/6
 */
public class ApiException extends RuntimeException {
    private Object data;
    private String msg;


    public Object getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }


    public ApiException(String message, Object data) {
        super(message);
        this.data = data;
    }

}

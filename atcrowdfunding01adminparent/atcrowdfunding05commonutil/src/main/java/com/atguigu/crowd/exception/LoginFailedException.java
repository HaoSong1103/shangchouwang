package com.atguigu.crowd.exception;

/**
 * @author 宋浩
 * @version 1.0
 * @Classname LoginFailedException
 * @Description TODO
 * @Date 2020/6/11 10:19
 */
public class LoginFailedException extends RuntimeException {
    private static final long serialVersionID = 1L;

    public LoginFailedException() {
    }

    public LoginFailedException(String message) {
        super(message);
    }

    public LoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginFailedException(Throwable cause) {
        super(cause);
    }

    public LoginFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

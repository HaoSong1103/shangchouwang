package com.atguigu.crowd.util;

/**
 * @author 宋浩
 * @version 1.0
 * @Classname ResultEntity
 * @Description 统一整个项目中Ajax请求的返回值类型
 * @Date 2020/6/10 17:15
 */
public class ResultEntity<T> {
//    用来封装当前请求处理的结果是成功还是失败
    private String result;
    //请求处理失败时返回的错误消息
    private String message;
    //要返回的数据
    private T date;

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";
    public static final String NO_MESSAGE = "NO_MESSAGE";
    public static final String NO_DATA = "NO_DATA";

    /**
     * 返回操作结果为成功，不带数据
     * @return
     */
    public static <E> ResultEntity<E> successWithoutData() {
        return new ResultEntity<E>(SUCCESS, NO_MESSAGE, null);
    }

    /**
     * 返回操作结果为成功，携带数据
     * @param data
     * @return
     */
    public static <E> ResultEntity<E> successWithData(E data) {
        return new ResultEntity<E>(SUCCESS, NO_MESSAGE, data);
    }

    /**
     * 返回操作结果为失败，不带数据
     * @param message
     * @return
     */
    public static <E> ResultEntity<E> failed(String message) {
        return new ResultEntity<E>(FAILED, message, null);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getDate() {
        return date;
    }

    public void setDate(T date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ResultEntity{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", date=" + date +
                '}';
    }

    public ResultEntity() {
    }

    public ResultEntity(String result, String message, T date) {
        this.result = result;
        this.message = message;
        this.date = date;
    }
}

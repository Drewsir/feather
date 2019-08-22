package com.drewsir.feather.server.action.res;

/**
 * Function:
 *      WorkRes 响应类，所有的响应数据都需要封装到这个对象中。
 * @author drewsir
 *         Date: 2018/3/31 16:03
 * @since JDK 1.8
 */
public class WorkRes<T> {
    private String code;

    private String message;

    private T dataBody ;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getDataBody() {
        return dataBody;
    }

    public void setDataBody(T dataBody) {
        this.dataBody = dataBody;
    }
}

package com.cutefool.commons.core.page;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Objects;


/**
 * API Response
 *
 * @author 271007729@qq.com
 * @date 2019-07-02 10:44
 */

@Slf4j
@Data
@SuppressWarnings({"unused"})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> implements Responsive, Serializable {

    private static final long serialVersionUID = -5413501204183149353L;

    /**
     * 请求标识
     */
    private String code;

    /**
     * 请求消息
     */
    private String msg;

    /**
     * 请求具体的数据
     */
    private T data;


    /**
     * 业务标志
     */
    private String mark;

    /**
     * private
     */
    private Response() {

    }

    /**
     * private
     */
    private Response(T data) {
        this(data, ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage());
    }

    /**
     * private
     */
    private Response(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * private
     */
    private Response(T data, String code, String msg) {
        this(code, msg);
        this.data = data;
    }

    /**
     * success response
     *
     * @return response
     */
    public static Response<Boolean> ok() {
        return new Response<>(true);
    }

    /**
     * success response
     *
     * @return response
     */
    public static <E> Response<E> empty() {
        return new Response<>(null);
    }

    /**
     * success response
     *
     * @param <E>  data type
     * @param data data
     * @return response
     */
    public static <E> Response<E> ok(E data) {
        return new Response<>(data);
    }

    /**
     * success response
     *
     * @param <E>  data type
     * @param data data
     * @param msg  msg
     * @return response
     */
    public static <E> Response<E> ok(E data, String msg) {
        return new Response<>(data, ResponseCode.SUCCESS.getCode(), msg);
    }


    /**
     * failure response
     *
     * @param <E> data type
     * @return response
     */
    public static <E> Response<E> failure() {
        return failure(ResponseCode.SYSTEM_ERROR.getMessage());
    }

    /**
     * failure response
     *
     * @param <E> data type
     * @param msg failure msg
     * @return response
     */

    public static <E> Response<E> failure(String msg) {
        return failure(ResponseCode.SYSTEM_ERROR.getCode(), msg);
    }

    /**
     * failure response
     *
     * @param <E>  data type
     * @param code failure code
     * @param msg  failure message
     * @return response
     */

    public static <E> Response<E> failure(String code, String msg) {
        return new Response<>(code, msg);
    }


    /**
     * failure response
     *
     * @param responseCode failure Response
     * @param <E>          data type
     * @return response
     */
    public static <E> Response<E> failure(ResponseCode responseCode) {
        return new Response<>(responseCode.getCode(), responseCode.getMessage());
    }


    /**
     * failure response
     *
     * @param <E>  data type
     * @param data data
     * @param code failure code
     * @param msg  failure message
     * @return response
     */

    public static <E> Response<E> failure(E data, String code, String msg) {
        return new Response<>(data, code, msg);
    }

    /**
     * check response is success
     *
     * @return true: success,false: fail
     */
    @Override
    @JsonIgnore
    public boolean isSuccess() {
        boolean equals = Objects.equals(this.code, ResponseCode.SUCCESS.getCode());

        T data = this.getData();
        if (data instanceof Boolean) {
            return (Boolean) data && equals;
        }
        return equals;
    }

    /**
     * check response is isFailure
     *
     * @return true: success,false: fail
     */
    @Override
    @JsonIgnore
    public boolean isFailure() {
        return !this.isSuccess();
    }
}

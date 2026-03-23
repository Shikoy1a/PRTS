package com.travel.common;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 统一 API 响应封装。
 *
 * <p>
 * 前后端约定所有接口返回 {@code {code, data, message}} 结构，便于前端统一处理。
 * code 约定：
 * <ul>
 *     <li>200：成功</li>
 *     <li>4xx：客户端错误（参数错误、权限问题等）</li>
 *     <li>5xx：服务端异常</li>
 * </ul>
 * </p>
 *
 * @param <T> 实际返回数据类型
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T>
{

    /**
     * 业务状态码，200 表示成功。
     */
    private int code;

    /**
     * 返回数据负载。
     */
    private T data;

    /**
     * 业务说明信息。
     */
    private String message;

    private ApiResponse()
    {
    }

    private ApiResponse(int code, T data, String message)
    {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    /**
     * 构建成功响应，带数据。
     *
     * @param data 返回数据
     * @param <T>  数据类型
     * @return 响应对象
     */
    public static <T> ApiResponse<T> success(T data)
    {
        return new ApiResponse<>(200, data, "success");
    }

    /**
     * 构建成功响应，带数据与自定义消息。
     *
     * @param data    返回数据
     * @param message 提示信息
     * @param <T>     数据类型
     * @return 响应对象
     */
    public static <T> ApiResponse<T> success(T data, String message)
    {
        return new ApiResponse<>(200, data, message);
    }

    /**
     * 构建仅包含消息的成功响应。
     *
     * @param message 成功消息
     * @return 响应对象
     */
    public static ApiResponse<Void> successMessage(String message)
    {
        return new ApiResponse<>(200, null, message);
    }

    /**
     * 构建失败响应。
     *
     * @param code    业务错误码
     * @param message 错误说明
     * @return 响应对象
     */
    public static <T> ApiResponse<T> failure(int code, String message)
    {
        return new ApiResponse<>(code, null, message);
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}


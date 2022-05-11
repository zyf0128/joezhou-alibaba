package com.joezhou.app.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.text.SimpleDateFormat;

/**
 * @author JoeZhou
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class JsonResult {

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应描述
     */
    private String message;

    /**
     * 响应数据
     */
    private Object data;

    /**
     * 操作成功，响应码为 `1`，描述为 `success`，无响应数据。
     *
     * @return JsonResult对象
     */
    @SneakyThrows
    public static JsonResult ok() {
        return ok(1, "success", null);
    }

    /**
     * 操作成功，响应码为 `1`，描述为 `success`，响应数据自定义。
     *
     * @param data 响应数据
     * @return JsonResult对象
     */
    @SneakyThrows
    public static JsonResult ok(Object data) {
        return ok(1, "success", data);
    }

    /**
     * 操作成功，响应码和描述自定义，无响应数据。
     *
     * @param code    响应状态码，正数表示成功，零或负数表示失败
     * @param message 响应成功描述
     * @return JsonResult对象
     */
    @SneakyThrows
    public static JsonResult ok(Integer code, String message) {
        return ok(code, message, null);
    }

    /**
     * 操作成功，响应码，描述和响应数据均自定义。
     *
     * @param code    响应状态码，正数表示成功，零或负数表示失败
     * @param message 响应成功描述
     * @param data    响应数据
     * @return JsonResult对象
     */
    @SneakyThrows
    public static JsonResult ok(Integer code, String message, Object data) {
        return new JsonResult(code, message, data);
    }

    /**
     * 操作失败，响应码为 `0`，描述为 `fail`，无响应数据。
     *
     * @return JsonResult对象
     */
    @SneakyThrows
    public static JsonResult fail() {
        return fail(0, "fail");
    }

    /**
     * 操作失败，响应码和描述自定义，无响应数据。
     *
     * @param code    响应状态码，正数表示成功，零或负数表示失败
     * @param message 响应失败描述
     * @return JsonResult对象
     */
    @SneakyThrows
    public static JsonResult fail(Integer code, String message) {
        return new JsonResult(code, message, null);
    }

    /**
     * 将响应数据格式化为JSON字符串，null值忽略，时间类型使用 `yyyy/MM/dd hh:mm:ss` 格式。
     *
     * @param data 响应数据
     * @return 格式化后的字符串
     */
    @SneakyThrows
    public static String format(Object data) {
        // 格式化时忽略null值
        // 格式化时的日期属性不使用时间戳
        // 格式化时的日期属性使用 `yyyy/MM/dd hh:mm:ss` 格式
        return new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setDateFormat(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss"))
                .writeValueAsString(data);
    }

    /**
     * 将JSON字符串解析成指定类型并返回。
     *
     * @param json 符合JSON格式的字符串
     * @return 参数指定的类型
     */
    @SneakyThrows
    public static <T> T parse(String json, Class<T> c) {

        return new ObjectMapper()
                .setDateFormat(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss"))
                .readValue(json, c);
    }
}
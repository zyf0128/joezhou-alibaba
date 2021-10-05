package com.joezhou.app.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Jackson工具：用户将数据转成前端需要的JSON响应格式，需要添加以下依赖
 * com.fasterxml.jackson.core.jackson-core(2.9.7)
 * com.fasterxml.jackson.core.jackson-annotations(2.9.7)
 * com.fasterxml.jackson.core.jackson-databind(2.9.7)
 *
 * @author JoeZhou
 */
public class JacksonUtil {

    /**
     * 通过Jackson将数据转换为前端需要的JSON响应格式字符串
     *
     * @param code 响应状态码，正数表示成功，零或负数表示失败
     * @param msg  响应消息
     * @param data 响应数据
     * @return 前端需要的JSON响应格式字符串
     */
    @SneakyThrows
    public static String build(int code, String msg, Object data) {
        Map<String, Object> map = new HashMap<>(3);
        map.put("code", code);
        map.put("msg", msg);
        map.put("data", data);
        ObjectMapper objectMapper = new ObjectMapper()
                // 最终的转换结果中不包含null值
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                // 最终的时间类型属性不会被转成时间戳
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                // 将所有的时间类型属性转成指定格式
                .setDateFormat(new SimpleDateFormat("yyyy/MM/dd hh:mm:ss"));
        return objectMapper.writeValueAsString(map);
    }

    /**
     * 仅传递响应数据时，响应状态码视为1，响应消息视为success
     *
     * @param data 响应数据
     * @return 前端需要的JSON响应格式字符串
     */
    public static String build(Object data) {
        return build(1, "success", data);
    }

    /**
     * 无响应数据时，可仅传递响应状态码和响应消息
     *
     * @param code 响应状态码
     * @param msg  响应消息
     * @return 前端需要的JSON响应格式字符串
     */
    public static String build(int code, String msg) {
        return build(code, msg, null);
    }

    /**
     * 将符合JSON格式的字符串解析Map类型并返回
     *
     * @param jsonStr 符合JSON格式的字符串
     * @return 解析后的Map数据
     */
    @SneakyThrows
    public static Map parseToMap(String jsonStr) {
        return new ObjectMapper().readValue(jsonStr, Map.class);
    }
}
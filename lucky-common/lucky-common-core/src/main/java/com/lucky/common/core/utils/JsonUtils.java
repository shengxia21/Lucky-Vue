package com.lucky.common.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.lucky.common.core.exception.UtilException;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * JSON处理工具类，基于Jackson实现
 *
 * @author lucky
 */
@Slf4j
public class JsonUtils {

    /**
     * 默认ObjectMapper实例（线程安全）
     */
    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

    private JsonUtils() {
    }

    /**
     * 创建默认的ObjectMapper
     */
    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // 序列化时忽略null属性
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 反序列化时忽略未知属性
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 不输出格式化后的字符串
        mapper.disable(SerializationFeature.INDENT_OUTPUT);
        return mapper;
    }

    /**
     * 获取ObjectMapper实例
     */
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    /**
     * 将对象转换为JSON字符串
     *
     * @param obj 对象
     * @return JSON字符串
     */
    public static String toJSONString(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("JSON序列化失败: {}", e.getMessage());
            throw new UtilException("JSON序列化失败: " + e.getMessage(), e);
        }
    }

    /**
     * 将对象转换为JSON字符串，排除指定字段
     * 通过将对象转换为Map后移除指定字段实现，适用于POJO和Map类型
     *
     * @param obj           对象
     * @param excludeFields 需要排除的字段名集合
     * @return JSON字符串
     */
    public static String toJSONString(Object obj, String... excludeFields) {
        if (obj == null) {
            return null;
        }
        if (excludeFields == null || excludeFields.length == 0) {
            return toJSONString(obj);
        }
        try {
            Set<String> excludes = new HashSet<>(Arrays.asList(excludeFields));
            // 基础类型无需过滤字段，直接序列化
            if (obj instanceof CharSequence || obj instanceof Number || obj instanceof Boolean) {
                return OBJECT_MAPPER.writeValueAsString(obj);
            }
            // Map和POJO统一转换为Map后移除排除字段，保证过滤行为一致
            Map<String, Object> map = OBJECT_MAPPER.convertValue(obj, new TypeReference<>() {
            });
            if (map != null) {
                excludes.forEach(map::remove);
            }
            return OBJECT_MAPPER.writeValueAsString(map);
        } catch (IllegalArgumentException | JsonProcessingException e) {
            log.error("JSON序列化失败: {}", e.getMessage());
            throw new UtilException("JSON序列化失败: " + e.getMessage(), e);
        }
    }

    /**
     * 将JSON字符串解析为JsonNode，便于灵活读取字段
     *
     * @param text JSON字符串
     * @return JsonNode
     */
    public static JsonNode parseObject(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readTree(text);
        } catch (JsonProcessingException e) {
            log.error("JSON解析失败: {}", e.getMessage());
            throw new UtilException("JSON解析失败: " + e.getMessage(), e);
        }
    }

    /**
     * 将JSON字符串解析为指定类型的对象
     *
     * @param text  JSON字符串
     * @param clazz 目标类型
     * @return 目标对象
     */
    public static <T> T parseObject(String text, Class<T> clazz) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(text, clazz);
        } catch (JsonProcessingException e) {
            log.error("JSON解析失败: {}", e.getMessage());
            throw new UtilException("JSON解析失败: " + e.getMessage(), e);
        }
    }

    /**
     * 将JSON字符串解析为指定类型的对象（支持泛型）
     *
     * @param text          JSON字符串
     * @param typeReference 目标类型
     * @return 目标对象
     */
    public static <T> T parseObject(String text, TypeReference<T> typeReference) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(text, typeReference);
        } catch (JsonProcessingException e) {
            log.error("JSON解析失败: {}", e.getMessage());
            throw new UtilException("JSON解析失败: " + e.getMessage(), e);
        }
    }

    /**
     * 将JSON字符串解析为Map
     *
     * @param text JSON字符串
     * @return Map对象
     */
    public static Map<String, Object> parseMap(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readValue(text, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            log.error("JSON解析失败: {}", e.getMessage());
            throw new UtilException("JSON解析失败: " + e.getMessage(), e);
        }
    }

    /**
     * 将Map转换为指定类型的对象
     *
     * @param map   Map对象
     * @param clazz 目标类型
     * @return 目标对象
     */
    public static <T> T convertValue(Map<?, ?> map, Class<T> clazz) {
        if (map == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.convertValue(map, clazz);
        } catch (IllegalArgumentException e) {
            log.error("对象转换失败: {}", e.getMessage());
            throw new UtilException("对象转换失败: " + e.getMessage(), e);
        }
    }

}

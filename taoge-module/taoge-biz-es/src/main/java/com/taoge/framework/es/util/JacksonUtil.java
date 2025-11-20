package com.taoge.framework.es.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;


@SuppressWarnings({"unchecked", "rawtypes"})
public class JacksonUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
    }

    public static String toString(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String json, Class<T> valueType) {
        if (isBlank(json)) {
            return null;
        }
        try {
            return MAPPER.readValue(json, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <E, T extends Collection<E>> T fromJson(String json, Class<? extends Collection> collectionType, Class<E> valueType) {
        if (isBlank(json)) {
            json = "[]";
        }

        try {
            return (T) MAPPER.readValue(json, TypeFactory.defaultInstance().constructCollectionType(collectionType, valueType));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <K, V, T extends Map<K, V>> T fromJson(String json, Class<? extends Map> mapType, Class<K> keyType, Class<V> valueType) {
        if (isBlank(json)) {
            json = "{}";
        }

        try {
            return (T) MAPPER.readValue(json, TypeFactory.defaultInstance().constructMapType(mapType, keyType, valueType));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isBlank(String json) {
        return StringUtils.isBlank(json);
    }

}

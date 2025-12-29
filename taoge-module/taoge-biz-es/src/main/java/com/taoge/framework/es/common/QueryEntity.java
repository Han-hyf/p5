package com.taoge.framework.es.common;

import lombok.Data;
import org.elasticsearch.common.unit.DistanceUnit;

import java.util.*;

public class QueryEntity {
    private final Map<String, List<Attr>> queryMap;

    /**
     * 创建查询
     */
    public QueryEntity() {
        queryMap = new HashMap<>();
    }

    /**
     * 增加查询条件，“=”
     *
     * @param name  参数名
     * @param value 参数值
     */
    public void eq(String name, Object value) {
        addQuery(name, QueryType.EQ, value, null);
    }

    /**
     * 增加查询条件，“!=”
     *
     * @param name  参数名
     * @param value 参数值
     */
    public void notEq(String name, Object value) {
        addQuery(name, QueryType.NOT_EQ, value, null);
    }

    /**
     * 增加查询条件，“in”
     *
     * @param name  参数名
     * @param value 参数值（需要集合参数）
     */
    public void in(String name, Collection<?> value) {
        addQuery(name, QueryType.IN, value, null);
    }

    /**
     * 增加查询条件，“not in”
     *
     * @param name  参数名
     * @param value 参数值（需要集合参数）
     */
    public void notIn(String name, Collection<?> value) {
        addQuery(name, QueryType.NOT_IN, value, null);
    }

    /**
     * 增加查询条件，“<”
     *
     * @param name  参数名
     * @param value 参数值
     */
    public void lt(String name, Object value) {
        addQuery(name, QueryType.LT, value, null);
    }

    /**
     * 增加查询条件，“>”
     *
     * @param name  参数名
     * @param value 参数值
     */
    public void gt(String name, Object value) {
        addQuery(name, QueryType.GT, value, null);
    }

    /**
     * 增加查询条件，“<=”
     *
     * @param name  参数名
     * @param value 参数值
     */
    public void lte(String name, Object value) {
        addQuery(name, QueryType.LTE, value, null);
    }

    /**
     * 增加查询条件，“>=”
     *
     * @param name  参数名
     * @param value 参数值
     */
    public void gte(String name, Object value) {
        addQuery(name, QueryType.GTE, value, null);
    }

    /**
     * 增加查询条件，“between ... and ... ”
     *
     * @param name   参数名
     * @param value1 参数值1
     * @param value2 参数值2
     */
    public void betweenAnd(String name, Object value1, Object value2) {
        addQuery(name, QueryType.BETWEEN_AND, value1, value2);
    }

    /**
     * 增加查询条件，“like '%...%'”，模糊匹配
     *
     * @param name  参数名
     * @param value 参数值
     */
    public void like(String name, Object value) {
        addQuery(name, QueryType.LIKE, value, null);
    }

    /**
     * 增加查询条件，“like '%...'”，模糊匹配值前半段
     *
     * @param name  参数名
     * @param value 参数值
     */
    public void likeStart(String name, Object value) {
        addQuery(name, QueryType.START_LIKE, value, null);
    }

    /**
     * 增加查询条件，“like '...%'”，模糊匹配值后半段
     *
     * @param name  参数名
     * @param value 参数值
     */
    public void likeEnd(String name, Object value) {
        addQuery(name, QueryType.END_LIKE, value, null);
    }

    /**
     * 增加查询条件，“$name$ not exists”
     *
     * @param name 参数名
     */
    public void notExists(String name) {
        addQuery(name, QueryType.NOT_EXISTS, null, null);
    }

    /**
     * 增加查询条件，“$name$ exists”
     *
     * @param name 参数名
     */
    public void exists(String name) {
        addQuery(name, QueryType.EXISTS, null, null);
    }

    /**
     * 搜索，分词方法
     *
     * @param name  根据搜索引擎的分词进行查询
     * @param value
     */
    public void analysis(String name, Object value) {
        addQuery(name, QueryType.ANALYSIS, value, null);
    }

    /**
     * 增加查询条件，“=”
     *
     * @param name  参数名
     * @param value 参数值
     */
    public void eq(boolean condition, String name, Object value) {
        addQuery(condition, name, QueryType.EQ, value, null);
    }

    /**
     * 增加查询条件，“!=”
     *
     * @param name  参数名
     * @param value 参数值
     */
    public void notEq(boolean condition, String name, Object value) {
        addQuery(condition, name, QueryType.NOT_EQ, value, null);
    }

    /**
     * 增加查询条件，“in”
     *
     * @param name  参数名
     * @param value 参数值（需要集合参数）
     */
    public void in(boolean condition, String name, Collection<?> value) {
        addQuery(condition, name, QueryType.IN, value, null);
    }

    /**
     * 增加查询条件，“not in”
     *
     * @param name  参数名
     * @param value 参数值（需要集合参数）
     */
    public void notIn(boolean condition, String name, Collection<?> value) {
        addQuery(condition, name, QueryType.NOT_IN, value, null);
    }

    /**
     * 增加查询条件，“<”
     *
     * @param name  参数名
     * @param value 参数值
     */
    public void lt(boolean condition, String name, Object value) {
        addQuery(condition, name, QueryType.LT, value, null);
    }

    /**
     * 增加查询条件，“>”
     *
     * @param name  参数名
     * @param value 参数值
     */
    public void gt(boolean condition, String name, Object value) {
        addQuery(condition, name, QueryType.GT, value, null);
    }

    /**
     * 增加查询条件，“<=”
     *
     * @param name  参数名
     * @param value 参数值
     */
    public void lte(boolean condition, String name, Object value) {
        addQuery(condition, name, QueryType.LTE, value, null);
    }

    /**
     * 增加查询条件，“>=”
     *
     * @param name  参数名
     * @param value 参数值
     */
    public void gte(boolean condition, String name, Object value) {
        addQuery(condition, name, QueryType.GTE, value, null);
    }

    /**
     * 增加查询条件，“between ... and ... ”
     *
     * @param name   参数名
     * @param value1 参数值1
     * @param value2 参数值2
     */
    public void betweenAnd(boolean condition, String name, Object value1, Object value2) {
        addQuery(condition, name, QueryType.BETWEEN_AND, value1, value2);
    }

    /**
     * 增加查询条件，“like '%...%'”，模糊匹配
     *
     * @param name  参数名
     * @param value 参数值
     */
    public void like(boolean condition, String name, Object value) {
        addQuery(condition, name, QueryType.LIKE, value, null);
    }

    /**
     * 增加查询条件，“like '%...'”，模糊匹配值前半段
     *
     * @param name  参数名
     * @param value 参数值
     */
    public void likeStart(boolean condition, String name, Object value) {
        addQuery(condition, name, QueryType.START_LIKE, value, null);
    }

    /**
     * 增加查询条件，“like '...%'”，模糊匹配值后半段
     *
     * @param name  参数名
     * @param value 参数值
     */
    public void likeEnd(boolean condition, String name, Object value) {
        addQuery(condition, name, QueryType.END_LIKE, value, null);
    }

    /**
     * 增加查询条件，“$name$ not exists”
     *
     * @param name 参数名
     */
    public void notExists(boolean condition, String name) {
        addQuery(condition, name, QueryType.NOT_EXISTS, null, null);
    }

    /**
     * 增加查询条件，“$name$ exists”
     *
     * @param name 参数名
     */
    public void exists(boolean condition, String name) {
        addQuery(condition, name, QueryType.EXISTS, null, null);
    }

    /**
     * 搜索，分词方法
     *
     * @param name  根据搜索引擎的分词进行查询
     */
    public void analysis(boolean condition, String name, Object value) {
        addQuery(condition, name, QueryType.ANALYSIS, value, null);
    }

    public void geo(boolean condition, String name, String lat, String lon, double distance, DistanceUnit unit) {
        GeoAttr geoAttr = new GeoAttr();
        geoAttr.setLat(lat);
        geoAttr.setLon(lon);
        geoAttr.setDistance(distance);
        geoAttr.setUnit(unit);
        addQuery(condition, name, QueryType.GEO, geoAttr, null);
    }

    protected void addQuery(String name, QueryType type, Object value1, Object value2) {
        addQuery(true, name, type, value1, value2);
    }

    protected void addQuery(boolean condition, String name, QueryType type, Object value1, Object value2) {
        if (!condition) {
            return;
        }
        Attr attr = new Attr(name, type, value1, value2);
        List<Attr> attrList = queryMap.get(name);
        if (attrList == null) {
            attrList = new ArrayList<>();
        }
        attrList.add(attr);
        queryMap.put(name, attrList);
    }

    public Map<String, List<Attr>> getQueryMap() {
        return queryMap;
    }

    public static class Attr {
        private final String name;
        private final QueryType type;
        private final Object value1;
        private final Object value2;

        public Attr(String name, QueryType type, Object value1, Object value2) {
            this.name = name;
            this.type = type;
            this.value1 = value1;
            this.value2 = value2;
        }

        public String getName() {
            return name;
        }

        public QueryType getType() {
            return type;
        }

        public Object getValue1() {
            return value1;
        }

        public Object getValue2() {
            return value2;
        }
    }

    @Data
    public static class GeoAttr {
        private String lat;
        private String lon;
        private double distance;
        private DistanceUnit unit;
    }

    /**
     * 查询类型枚举
     */
    public enum QueryType {
        /**
         * =
         */
        EQ,
        /**
         * !=
         */
        NOT_EQ,
        /**
         * in()
         */
        IN,
        /**
         * not in()
         */
        NOT_IN,
        /**
         * <
         */
        LT,
        /**
         * >
         */
        GT,
        /**
         * <=
         */
        LTE,
        /**
         * >=
         */
        GTE,
        /**
         * between ... and ...
         */
        BETWEEN_AND,
        /**
         * like '%...%'
         */
        LIKE,
        /**
         * like '%...'
         */
        START_LIKE,
        /**
         * like '...%'
         */
        END_LIKE,
        /**
         * not exists
         */
        NOT_EXISTS,
        /**
         * exists
         */
        EXISTS,
        /**
         * 分词
         */
        ANALYSIS,
        /**
         * 地图搜索
         */
        GEO

    }


}

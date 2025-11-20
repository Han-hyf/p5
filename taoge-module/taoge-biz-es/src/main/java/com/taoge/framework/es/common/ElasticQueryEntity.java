package com.taoge.framework.es.common;

import org.elasticsearch.search.sort.SortMode;
import org.elasticsearch.search.sort.SortOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * ElasticSearch搜索类
 */
public class ElasticQueryEntity extends QueryEntity {
    private List<ElasticOrder> orderList;
    private EsPage esPage;

    public EsPage getPage() {
        return esPage;
    }

    public void setPage(EsPage esPage) {
        this.esPage = esPage;
    }

    public void addOrderByAsc(String name) {
        addOrderBy(name, SortOrder.ASC);
    }

    public void addOrderByDesc(String name) {
        addOrderBy(name, SortOrder.DESC);
    }

    /**
     * 添加查询结果排序
     *
     * @param name      排序字段
     * @param sortOrder 排序类型
     */
    private void addOrderBy(String name, SortOrder sortOrder) {
        addOrderBy(name, sortOrder, null);
    }

    /**
     * 添加排序及聚合规则
     *
     * @param name      排序字段
     * @param sortOrder 排序类型
     * @param sortMode  聚合类型（max, min, avg, sum）
     */
    public void addOrderBy(String name, SortOrder sortOrder, SortMode sortMode) {
        if (orderList == null) {
            orderList = new ArrayList<>();
        }
        ElasticOrder elasticOrder = new ElasticOrder(name, sortOrder, sortMode);
        orderList.add(elasticOrder);
    }

    public List<ElasticOrder> getOrderList() {
        return orderList;
    }

    /**
     * ElasticSearch排序类
     */
    public static class ElasticOrder {
        private final String key; // 排序字段名
        private final SortOrder sortOrder; // 排序类型
        private final SortMode mode; // 聚合类型

        public ElasticOrder(String key, SortOrder orderType, SortMode mode) {
            this.key = key;
            this.sortOrder = orderType;
            this.mode = mode;
        }

        public String getKey() {
            return key;
        }

        public SortOrder getSortOrder() {
            return sortOrder;
        }

        public SortMode getMode() {
            return mode;
        }

    }

}

package com.taoge.framework.es.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.taoge.framework.es.common.ElasticQueryEntity;
import com.taoge.framework.es.common.EsPage;
import com.taoge.framework.es.common.IdField;
import com.taoge.framework.es.exception.EsException;
import com.taoge.framework.es.util.JacksonUtil;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.util.ConcurrentReferenceHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ElasticService<T> {
    /**
     * elastic客户端
     */
    @Resource
    private RestHighLevelClient restHighLevelClient;
    @Resource
    private ElasticsearchOperations elasticsearchOperations;

    static Logger log = LoggerFactory.getLogger(ElasticService.class);
    private final String index;
    /**
     * 记录泛型的class
     */
    private Class<?> entityClass = null;
    /**
     * 缓存类-注解-field
     */
    private static final Map<Class<?>, IdField> Id_FIELD_MAP = new ConcurrentReferenceHashMap<>();

    /**
     * 不分页查询，查询条数上限
     */
    private static final Integer LIST_MAX_SIZE = 200;

    public ElasticService() {
        try {
            Type genType = getClass().getGenericSuperclass();
            if (genType instanceof ParameterizedType) {
                Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
                entityClass = (Class<?>) params[0];
            }
            if (null == entityClass) {
                throw new EsException("entityClass is null");
            }
            log.info("init entityClass:{}", entityClass);

            // 校验 entity 类配置了Document注解
            if (entityClass.isAnnotationPresent(Document.class)) {
                Document index = entityClass.getAnnotation(Document.class);
                if (StringUtils.isBlank(index.indexName())) {
                    throw new EsException(entityClass.getName() + " indexName is null");
                }
                this.index = index.indexName();
            } else {
                throw new EsException(entityClass.getName() + " Document annotation is null");
            }

            // 校验 entity 类配置了Id主键注解
            IdField idField = getIdField(entityClass);
            if (null == idField) {
                throw new EsException(entityClass.getName() + " id annotation is null");
            }
        } catch (Exception e) {
            log.error("entityClass:{}, load class error:{}", entityClass, e);
            throw e;
        }
    }

    @PostConstruct
    public void createIndex() {
        if (existsIndex()) {
            return;
        }
        IndexOperations indexOperations = elasticsearchOperations.indexOps(entityClass);
        org.springframework.data.elasticsearch.core.document.Document settings = org.springframework.data.elasticsearch.core.document.Document.create();
        indexOperations.create(settings);
        putMapping();
    }

    public void putMapping() {
        IndexOperations indexOperations = elasticsearchOperations.indexOps(entityClass);
        indexOperations.putMapping(indexOperations.createMapping());
    }

    public void deleteIndex() {
        IndexOperations indexOperations = elasticsearchOperations.indexOps(entityClass);
        indexOperations.delete();
    }

    /**
     * 是否存在索引
     */
    private boolean existsIndex() {
        GetIndexRequest request = new GetIndexRequest(index);
        try {
            return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 查询索引
     */
    public GetIndexResponse getIndex() {
        GetIndexRequest request = new GetIndexRequest(index);
        try {
            return restHighLevelClient.indices().get(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ElasticQueryEntity buildQueryEntity() {
        return new ElasticQueryEntity();
    }

    public ElasticQueryEntity buildQueryEntity(Integer pageNum, Integer pageSize) {
        ElasticQueryEntity queryEntity = new ElasticQueryEntity();
        queryEntity.setPage(new EsPage(pageNum, pageSize));
        return queryEntity;
    }

    /**
     * 更新
     */
    public UpdateResponse update(T o) {
        try {
            UpdateRequest updateRequest = new UpdateRequest(index, getId(o));
            updateRequest.doc(JacksonUtil.toString(o), XContentType.JSON);
            updateRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            return restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("es update error, e:", e);
            throw new EsException("es update error");
        }
    }

    /**
     * 批量更新
     */
    public BulkResponse bulkUpdate(List<T> list) {
        BulkRequest bulkRequest = new BulkRequest();
        list.forEach(entity -> {
            UpdateRequest updateRequest = new UpdateRequest(index, getId(entity));
            updateRequest.doc(JacksonUtil.toString(entity), XContentType.JSON);
            bulkRequest.add(updateRequest);
        });
        try {
            bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            return restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.info("es bulk update error:", e);
            throw new EsException("es bulk update error");
        }
    }

    /**
     * 保存（upsert）
     */
    public UpdateResponse save(T o) {
        try {
            UpdateRequest updateRequest = new UpdateRequest(index, getId(o));
            updateRequest.docAsUpsert(true);
            updateRequest.doc(JacksonUtil.toString(o), XContentType.JSON);
            updateRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            return restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        } catch (Exception e) {
            log.error("es upsert error, e:", e);
            throw new EsException("es upsert error");
        }
    }

    /**
     * 批量保存（upsert）
     */
    public BulkResponse bulkSave(List<T> list) {
        BulkRequest bulkRequest = new BulkRequest();
        list.forEach(entity -> {
            UpdateRequest updateRequest = new UpdateRequest(index, getId(entity));
            updateRequest.docAsUpsert(true);
            updateRequest.doc(JacksonUtil.toString(entity), XContentType.JSON);
            bulkRequest.add(updateRequest);
        });
        try {
            bulkRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            return restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.info("es bulk update error:", e);
            throw new EsException("es bulk update error");
        }
    }

    /**
     * 按主键删除
     *
     * @param id 主键
     */
    public boolean delete(String id) {
        try {
            DeleteRequest deleteRequest = new DeleteRequest(index, id);
            deleteRequest.setRefreshPolicy(WriteRequest.RefreshPolicy.IMMEDIATE);
            restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
            return true;
        } catch (Exception e) {
            log.error("es deleteByPrimary error, e:", e);
            throw new EsException("es deleteByPrimary error");
        }
    }

    /**
     * 按主键查询
     *
     * @param id 主键
     * @return T
     */
    public T get(String id) {
        return (T) get(id, entityClass);
    }

    public <E> E get(String id, Class<E> clazz) {
        try {
            GetRequest request = new GetRequest(index).id(id);
            GetResponse response = restHighLevelClient.get(request, RequestOptions.DEFAULT);
            return JacksonUtil.fromJson(JacksonUtil.toString(response.getSource()), clazz);
        } catch (Exception e) {
            log.error("es get error, e:", e);
            throw new EsException("es get error");
        }
    }

    /**
     * 不分页查询，最多返回200条
     *
     * @param q 查询条件
     */
    public List<T> list(ElasticQueryEntity q) {
        return (List<T>) list(q, entityClass);
    }

    /**
     * 不分页查询，返回指定class类
     *
     * @param q      查询条件
     * @param eClass 需要转成的class
     */
    public <E> List<E> list(ElasticQueryEntity q, Class<E> eClass) {
        q.setPage(new EsPage(1, LIST_MAX_SIZE));
        return pageList(q, eClass).getList();
    }

    /**
     * 分页查询
     *
     * @param q 查询条件
     */
    public PageInfo<T> pageList(ElasticQueryEntity q) {
        return (PageInfo<T>) esPageList(q, entityClass);
    }

    /**
     * 分页查询，返回指定class类
     *
     * @param q      查询条件
     * @param eClass 需要转成的class
     */
    public <E> PageInfo<E> pageList(ElasticQueryEntity q, Class<E> eClass) {
        return esPageList(q, eClass);
    }

    /**
     * es分页查询
     *
     * @param q 查询条件
     */
    private <E> PageInfo<E> esPageList(ElasticQueryEntity q, Class<E> clazz) {
        SearchSourceBuilder searchSourceBuilder = assembleQueryBuilder(q);
        List<E> list = null;
        EsPage esPage = q.getPage();
        if (null == esPage) {
            esPage = new EsPage();
        }
        searchSourceBuilder.from((esPage.getPageNum() - 1) * esPage.getPageSize()).size(esPage.getPageSize());
        Page<E> page = new Page<>(esPage.getPageNum(), esPage.getPageSize());
        try {
            SearchRequest searchRequest = new SearchRequest(index);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            list = convertHitsToT(searchResponse.getHits(), clazz);
            page.setTotal(searchResponse.getHits().getTotalHits().value);
        } catch (Exception e) {
            log.error("pageList error, e:", e);
        }
        if (list == null) {
            list = new ArrayList<>(0);
            page.setTotal(0);
        }
        page.addAll(list);
        return page.toPageInfo();
    }

    /**
     * 组装es查询条件
     *
     * @param q 查询条件
     */
    private SearchSourceBuilder assembleQueryBuilder(ElasticQueryEntity q) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 条件查询
        if (null != q.getQueryMap() && q.getQueryMap().size() > 0) {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            for (String s : q.getQueryMap().keySet()) {
                fillQuery(boolQueryBuilder, q.getQueryMap().get(s));
            }
            searchSourceBuilder.query(boolQueryBuilder);
        }

        // 排序
        if (null != q.getOrderList() && q.getOrderList().size() > 0) {
            for (ElasticQueryEntity.ElasticOrder order : q.getOrderList()) {
                if (null == order.getSortOrder()) {
                    continue;
                }
                FieldSortBuilder sortBuilder = SortBuilders.fieldSort(order.getKey());
                sortBuilder.order(order.getSortOrder());
                // 处理聚合方式
                if (null != order.getMode()) {
                    sortBuilder.sortMode(order.getMode());
                }
                searchSourceBuilder.sort(sortBuilder);
            }
        }
        return searchSourceBuilder;
    }

    /**
     * 为公用查询组织条件
     *
     * @param boolQueryBuilder 查询builder
     * @param attrList         要追加的条件集合
     */
    private void fillQuery(BoolQueryBuilder boolQueryBuilder, List<ElasticQueryEntity.Attr> attrList) {
        if (attrList != null && attrList.size() > 0) {
            for (ElasticQueryEntity.Attr attr : attrList) {
                switch (attr.getType()) {
                    case EQ:
                        boolQueryBuilder.must(QueryBuilders.termQuery(attr.getName(), attr.getValue1()));
                        break;
                    case NOT_EQ:
                        boolQueryBuilder.mustNot(QueryBuilders.termQuery(attr.getName(), attr.getValue1()));
                        break;
                    case LT:
                        boolQueryBuilder.must(QueryBuilders.rangeQuery(attr.getName()).lt(attr.getValue1()));
                        break;
                    case LTE:
                        boolQueryBuilder.must(QueryBuilders.rangeQuery(attr.getName()).lte(attr.getValue1()));
                        break;
                    case GT:
                        boolQueryBuilder.must(QueryBuilders.rangeQuery(attr.getName()).gt(attr.getValue1()));
                        break;
                    case GTE:
                        boolQueryBuilder.must(QueryBuilders.rangeQuery(attr.getName()).gte(attr.getValue1()));
                        break;
                    case IN:
                        if (attr.getValue1() instanceof Collection) {
                            boolQueryBuilder.must(QueryBuilders.termsQuery(attr.getName(), (Collection<?>) attr.getValue1()));
                        }
                        break;
                    case NOT_IN:
                        if (attr.getValue1() instanceof Collection) {
                            boolQueryBuilder.mustNot(QueryBuilders.termsQuery(attr.getName(), (Collection<?>) attr.getValue1()));
                        }
                        break;
                    case NOT_EXISTS:
                        boolQueryBuilder.mustNot(QueryBuilders.existsQuery(attr.getName()));
                        break;
                    case EXISTS:
                        boolQueryBuilder.must(QueryBuilders.existsQuery(attr.getName()));
                        break;
                    case LIKE:
                        boolQueryBuilder.filter(QueryBuilders.wildcardQuery(attr.getName(), "*" + attr.getValue1() + "*"));
                        break;
                    case START_LIKE:
                        boolQueryBuilder.filter(QueryBuilders.wildcardQuery(attr.getName(), "*" + attr.getValue1()));
                        break;
                    case END_LIKE:
                        boolQueryBuilder.filter(QueryBuilders.wildcardQuery(attr.getName(), attr.getValue1() + "*"));
                        break;
                    case BETWEEN_AND:
                        boolQueryBuilder.must(QueryBuilders.rangeQuery(attr.getName()).from(attr.getValue1()).to(attr.getValue2()));
                        break;
                    case ANALYSIS:
                        boolQueryBuilder.filter(QueryBuilders.matchQuery(attr.getName(), attr.getValue1()));
                        break;
                }
            }
        }
    }

    /**
     * 将SearchHits查询结果转为javabean
     */
    private <E> List<E> convertHitsToT(SearchHits hits, Class<E> clazz) {
        if (null != hits && hits.getTotalHits().value > 0) {
            List<E> list = new ArrayList<>();
            for (SearchHit hit : hits) {
                list.add(JacksonUtil.fromJson(JacksonUtil.toString(hit.getSourceAsMap()), clazz));
            }
            return list;
        }
        return null;
    }

    private String getId(T o) {
        IdField idField = getIdField(o.getClass());
        if (null == idField) {
            return null;
        }
        try {
            Object id = idField.getGetMethod().invoke(o);
            if (null != id) {
                return id.toString();
            }
        } catch (Exception e) {
            log.error("getId error:", e);
        }
        return null;
    }

    /**
     * 查询id主键字段
     *
     * @param clazz class
     */
    private IdField getIdField(Class<?> clazz) {
        IdField idField = Id_FIELD_MAP.get(clazz);
        if (null != idField) {
            return idField;
        }
        //获取当前类的所有字段
        Field[] declaredFields = clazz.getDeclaredFields();
        //遍历字段
        for (Field field : declaredFields) {
            //判断当前字段是否有注解
            try {
                if (field.isAnnotationPresent(Id.class)) {
                    idField = new IdField();
                    idField.setField(field);
                    idField.setClazz(clazz);
                    idField.setGetMethod(clazz.getDeclaredMethod("get" + StringUtils.capitalize(field.getName())));
                    break;
                }
            } catch (Exception e) {
                log.error("setIdField error:", e);
            }
        }
        if (null == idField) {
            throw new EsException("class:" + clazz + " id is null");
        }
        Id_FIELD_MAP.put(clazz, idField);
        return idField;
    }
}

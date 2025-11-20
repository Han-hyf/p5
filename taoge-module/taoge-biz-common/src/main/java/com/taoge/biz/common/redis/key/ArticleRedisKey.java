package com.taoge.biz.common.redis.key;

public class ArticleRedisKey {

    private static final String prefix = "rest:article:";



    public static String getArticleDetailKey(Long id){
        return prefix + "detail:" + id;
    }

    /**
     * 文章统计数据key
     */
    public static String getArticleStatisticsKey(Long id){
        return prefix + "statistics:" + id;
    }

    /**
     * 正在进行统计pv的文章的id集合
     */
    public static String getArticleStatisticsIngPvIds(){
        return prefix + "statistics:ing:pv:ids";
    }

    /**
     * 正在进行统计pv的文章id
     * @param id
     * @return
     */
    public static String getArticleStatisticsIngPvId(Long id){
        return prefix + "statistics:ing:pv:" + id;
    }
    /**
     * 正在进行统计uv的文章的id集合
     */
    public static String getArticleStatisticsIngUvIds(){
        return prefix + "statistics:ing:uv:ids";
    }

    /**
     * 正在进行统计uv的文章id
     * @param id
     * @return
     */
    public static String getArticleStatisticsIngUvId(Long id){
        return prefix + "statistics:ing:uv:" + id;
    }


    /**
     * 用户浏览历史key
     * @param userId
     * @return
     */
    public static String getArticleRecentlyKey(Long userId){
        return prefix + "recently:" + userId;
    }
}

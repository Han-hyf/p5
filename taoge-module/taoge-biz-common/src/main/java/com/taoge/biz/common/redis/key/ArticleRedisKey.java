package com.taoge.biz.common.redis.key;

/**
 * 文章类redis key
 */
public class ArticleRedisKey {
    private static final String prefix = "rest:article:";

    public static String getArticleDetailKey(Long id) {
        return prefix + "detail:" + id;
    }

    /**
     * 文章统计数据key
     *
     * @param id 文章id
     */
    public static String getArticleStatisticsKey(Long id) {
        return prefix + "statistics:" + id;
    }

    /**
     * 正在进行统计pv的文章id集合
     */
    public static String getArticleStatisticsIngPvIds() {
        return prefix + "statistics:ing:pv:ids";
    }

    /**
     * 正在进行统计pv的文章id
     *
     * @param id 文章id
     */
    public static String getArticleStatisticsIngPvId(Long id) {
        return prefix + "statistics:ing:pv:" + id;
    }

    /**
     * 正在进行统计uv的文章id集合
     */
    public static String getArticleStatisticsIngUvIds() {
        return prefix + "statistics:ing:uv:ids";
    }

    /**
     * 正在进行统计uv的文章id
     *
     * @param id 文章id
     */
    public static String getArticleStatisticsIngUvId(Long id) {
        return prefix + "statistics:ing:uv:" + id;
    }

    /**
     * 用户浏览历史
     *
     * @param userId 用户ID
     */
    public static String getArticleRecentlyKey(Long userId) {
        return prefix + "recently:" + userId;
    }
}

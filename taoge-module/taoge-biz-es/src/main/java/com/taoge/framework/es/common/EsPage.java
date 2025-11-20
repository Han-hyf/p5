package com.taoge.framework.es.common;

import java.io.Serializable;

/**
 * 数据分页处理辅助类，负责页码的计算
 */
public class EsPage implements Serializable {
    private int pageNum = 1;
    private int pageSize = 10;

    public EsPage() {
    }

    public EsPage(Integer pageNum, Integer pageSize) {
        if (null != pageNum) {
            this.setPageNum(pageNum);
        }
        if (null != pageSize) {
            this.setPageSize(pageSize);
        }
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum <= 0 ? 1 : pageNum;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}

package io.github.churunfa.security.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author crf
 */
public class PageBean<E> {
    private Integer pageSize = 10;
    private Integer pageNo = 1;
    private Integer totalPages;
    private Integer recordCount;
    private List<E> pageData = new ArrayList<>();
    private Boolean hasNextPage;
    private Boolean hasPreviousPage;

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void init() {
        totalPages = (recordCount + pageSize - 1) / pageSize;
        hasNextPage = (pageNo < totalPages);
        hasPreviousPage = (pageNo > 1);
    }

    public Boolean getHasNextPage() {
        return hasNextPage;
    }

    public Boolean getHasPreviousPage() {
        return hasPreviousPage;
    }

    @Override
    public String toString() {
        return "PageBean{" +
                "pageSize=" + pageSize +
                ", pageNo=" + pageNo +
                ", totalPages=" + totalPages +
                ", recordCount=" + recordCount +
                ", pageData=" + pageData +
                ", hasNextPage=" + hasNextPage +
                ", hasPreviousPage=" + hasPreviousPage +
                '}';
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }

    public List<E> getPageData() {
        return pageData;
    }

    public void setPageData(List<E> pageData) {
        this.pageData = pageData;
    }

    public void setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public void setHasPreviousPage(Boolean hasPreviousPage) {
        this.hasPreviousPage = hasPreviousPage;
    }
}

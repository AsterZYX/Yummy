package aster.yummy.vo;

import lombok.Data;

import java.util.List;

@Data
public class PageVO<T> {

    Integer currentPage;

    Integer size;

    Integer totalPages;

    Long totalItems;

    Integer emptyNum;

    List<T> content;

    public PageVO(Integer currentPage, Integer size, Integer totalPages, Long totalItems, List<T> content, Integer emptyNum) {
        this.currentPage = currentPage;
        this.size = size;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
        this.content = content;
        this.emptyNum = emptyNum;
    }
}

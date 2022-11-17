package com.esthetic.reservations.api.dto;

import java.util.ArrayList;

public class ResponseDTO<T> {

    private ArrayList<T> content;
    private int pageNumber;
    private int pageSize;
    private Long count;
    private int totalPages;
    private Boolean last;

    public ResponseDTO() {
    }

    public ArrayList<T> getContent() {
        return this.content;
    }

    public void setContent(ArrayList<T> content) {
        this.content = content;
    }

    public int getPageNumber() {
        return this.pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Long getCount() {
        return this.count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public int getTotalPages() {
        return this.totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public Boolean isLast() {
        return this.last;
    }

    public Boolean getLast() {
        return this.last;
    }

    public void setLast(Boolean last) {
        this.last = last;
    }

}

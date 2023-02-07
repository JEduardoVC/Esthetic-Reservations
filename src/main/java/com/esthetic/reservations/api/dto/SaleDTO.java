package com.esthetic.reservations.api.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.model.SaleItem;
import com.esthetic.reservations.api.model.UserEntity;

public class SaleDTO extends GenericModelDTO {
    
    private Branch branch;

    private UserEntity client;

    private List<SaleItem> productList;

    private LocalDateTime saleDate;

    public SaleDTO() {
    }

    public SaleDTO(Branch branch, UserEntity client, List<SaleItem> productList, LocalDateTime saleDate) {
        this.branch = branch;
        this.client = client;
        this.productList = productList;
        this.saleDate = saleDate;
    }


    public Branch getBranch() {
        return this.branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public UserEntity getClient() {
        return this.client;
    }

    public void setClient(UserEntity client) {
        this.client = client;
    }

    public List<SaleItem> getProductList() {
        return this.productList;
    }

    public void setProductList(List<SaleItem> productList) {
        this.productList = productList;
    }

    public LocalDateTime getSaleDate() {
        return this.saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }


}

package com.esthetic.reservations.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SaleDTO extends GenericModelDTO {
    
    private SaleBranchDTO branch;

    private MinUserEntityDTO client;

    private List<SaleItemDTO> products;

    private LocalDateTime saleDate;

    public SaleDTO() {
    }

    public SaleDTO(SaleBranchDTO branch, MinUserEntityDTO client, List<SaleItemDTO> products, LocalDateTime saleDate) {
        this.branch = branch;
        this.client = client;
        this.products = products;
        this.saleDate = saleDate;
    }


    public SaleBranchDTO getBranch() {
        return this.branch;
    }

    public void setBranch(SaleBranchDTO branch) {
        this.branch = branch;
    }

    /**
     * @return the client
     */
    public MinUserEntityDTO getClient() {
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(MinUserEntityDTO client) {
        this.client = client;
    }

    public List<SaleItemDTO> getProducts() {
        return this.products;
    }

    public void setProducts(List<SaleItemDTO> products) {
        this.products = products;
    }

    public LocalDateTime getSaleDate() {
        return this.saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }


}

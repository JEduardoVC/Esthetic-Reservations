package com.esthetic.reservations.api.dto;

import java.util.List;

public class NewSaleDTO {

    private Long branchId;

    private Long clientId;

    private List<NewSaleItemDTO> products;

    public NewSaleDTO() {
    }

    public NewSaleDTO(Long branchId, Long clientId, List<NewSaleItemDTO> products) {
        this.branchId = branchId;
        this.clientId = clientId;
        this.products = products;
    }
    
    public Long getBranchId() {
        return this.branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public Long getClientId() {
        return this.clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }


    public List<NewSaleItemDTO> getProducts() {
        return this.products;
    }

    public void setProducts(List<NewSaleItemDTO> products) {
        this.products = products;
    }
    
}

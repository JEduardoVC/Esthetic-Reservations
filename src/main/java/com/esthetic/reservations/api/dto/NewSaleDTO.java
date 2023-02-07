package com.esthetic.reservations.api.dto;

import java.util.List;

public class NewSaleDTO {

    private Long branchId;

    private Long clientId;

    private List<NewSaleItemDTO> productList;

    public NewSaleDTO() {
    }

    public NewSaleDTO(Long branchId, Long clientId, List<NewSaleItemDTO> productList) {
        this.branchId = branchId;
        this.clientId = clientId;
        this.productList = productList;
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


    public List<NewSaleItemDTO> getProductList() {
        return this.productList;
    }

    public void setProductList(List<NewSaleItemDTO> productList) {
        this.productList = productList;
    }
    
}

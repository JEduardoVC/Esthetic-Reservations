package com.esthetic.reservations.api.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class NewSaleDTO {

    @NotNull(message = "Se requiere el ID de la sucursal donde se realizó la venta.")
    private Long branchId;
    
    @NotNull(message = "Se requiere el ID del cliente al que se realizó la venta.")
    private Long clientId;

    @NotNull(message = "Se requiere la lista de productos.")
    @NotEmpty(message = "Se reqiere la lista de productos.")
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

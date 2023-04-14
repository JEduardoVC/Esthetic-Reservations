package com.esthetic.reservations.api.dto;

import javax.validation.constraints.NotNull;

public class NewSaleItemDTO {

    @NotNull(message = "Se requiere el ID del producto.")
    private Long productId;

    @NotNull(message = "Se requiere la cantidad de este producto.")
    private Long quantity;

    public NewSaleItemDTO() {
    }

    public NewSaleItemDTO(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return this.productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

}

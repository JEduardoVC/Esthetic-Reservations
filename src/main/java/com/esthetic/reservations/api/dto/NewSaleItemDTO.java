package com.esthetic.reservations.api.dto;

public class NewSaleItemDTO {

    private Long productId;

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

package com.esthetic.reservations.api.dto;

import javax.validation.constraints.NotNull;

public class SaleItemDTO extends GenericModelDTO {

    @NotNull(message = "Se requiere la información del producto.")
    private MinInventory product;

    @NotNull(message = "Se requiere el subtotal de este producto.")
    private Double subtotal;

    @NotNull(message = "Se requiere la cantidad de este producto.")
    private Long quantity;

    /**
     * 
     */
    public SaleItemDTO() {
        super();
    }

    /**
     * @param product
     * @param subtotal
     * @param quantity
     */
    public SaleItemDTO(MinInventory product, Double subtotal, Long quantity) {
        super();
        this.product = product;
        this.subtotal = subtotal;
        this.quantity = quantity;
    }

    /**
     * @param id
     * @param saleId
     * @param product
     * @param subtotal
     * @param quantity
     */
    public SaleItemDTO(Long id, MinInventory product, Double subtotal, Long quantity) {
        super(id);
        this.product = product;
        this.subtotal = subtotal;
        this.quantity = quantity;
    }

    /**
     * @return the product
     */
    public MinInventory getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(MinInventory product) {
        this.product = product;
    }

    /**
     * @return the subtotal
     */
    public Double getSubtotal() {
        return subtotal;
    }

    /**
     * @param subtotal the subtotal to set
     */
    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * @return the quantity
     */
    public Long getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "SaleItemDTO [product=" + product + ", subtotal=" + subtotal + ", quantity=" + quantity + "]";
    }

}

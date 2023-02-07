package com.esthetic.reservations.api.dto;

import com.esthetic.reservations.api.model.Inventory;

public class SaleItemDTO extends GenericModelDTO {

    private Long saleId;
    
    private Inventory product;

    private Double subtotal;

    private Long quantity;
    

    /**
     * 
     */
    public SaleItemDTO() {
        super();
    }

    /**
     * @param saleId
     * @param product
     * @param subtotal
     * @param quantity
     */
    public SaleItemDTO(Long saleId, Inventory product, Double subtotal, Long quantity) {
        super();
        this.saleId = saleId;
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
    public SaleItemDTO(Long id, Long saleId, Inventory product, Double subtotal, Long quantity) {
        super(id);
        this.saleId = saleId;
        this.product = product;
        this.subtotal = subtotal;
        this.quantity = quantity;
    }


    /**
     * @return the saleId
     */
    public Long getSaleId() {
        return saleId;
    }


    /**
     * @param saleId the saleId to set
     */
    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }


    /**
     * @return the product
     */
    public Inventory getProduct() {
        return product;
    }


    /**
     * @param product the product to set
     */
    public void setProduct(Inventory product) {
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
 
    
}

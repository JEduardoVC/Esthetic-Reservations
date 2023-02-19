package com.esthetic.reservations.api.dto;

public class SaleItemDTO extends GenericModelDTO {

    private Long saleId;
    
    private MinInventory product;

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
    public SaleItemDTO(Long saleId, MinInventory product, Double subtotal, Long quantity) {
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
    public SaleItemDTO(Long id, Long saleId, MinInventory product, Double subtotal, Long quantity) {
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
        return "SaleItemDTO [saleId=" + saleId + ", product=" + product + ", subtotal=" + subtotal + ", quantity="
                + quantity + "]";
    }
 
    
    
}

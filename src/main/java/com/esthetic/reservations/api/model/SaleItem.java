package com.esthetic.reservations.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "sale_item")
public class SaleItem extends BaseModel<SaleItem> {

    @OneToOne(targetEntity = Inventory.class)
    @JoinColumn(name = "id_product", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Inventory product;

    @Column(name = "subtotal")
    private Double subtotal;

    @Column(name = "quantity")
    private Long quantity;
    
    

    /**
     * 
     */
    public SaleItem() {
        super();
    }

    /**
     * @param sale
     * @param product
     * @param subtotal
     * @param quantity
     */
    public SaleItem(Inventory product, Double subtotal, Long quantity) {
        super();
        this.product = product;
        this.subtotal = subtotal;
        this.quantity = quantity;
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

    @Override
    public void copy(SaleItem saleItem) {
        this.product = saleItem.product;
        this.quantity = saleItem.quantity;
        this.subtotal = saleItem.subtotal;
    }

}

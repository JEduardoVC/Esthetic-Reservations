package com.esthetic.reservations.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "sale_item")
public class SaleItem extends BaseModel<SaleItem> {

    @Column(name = "id_sale")
    private Long saleId;
    
    @OneToOne(targetEntity = Inventory.class)
    @JoinColumn(name = "id_product", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Inventory product;

    @Column(name = "subtotal")
    private Double subtotal;

    @Column(name = "quantity")
    private Long quantity;


    public SaleItem() {
        super();
    }

    /**
     * @param saleId
     * @param product
     * @param subtotal
     * @param quantity
     */
    public SaleItem(Long saleId, Inventory product, Double subtotal, Long quantity) {
        super();
        this.saleId = saleId;
        this.product = product;
        this.subtotal = subtotal;
        this.quantity = quantity;
    }
    
    /**
     * @param saleId
     * @param product
     * @param subtotal
     * @param quantity
     */
    public SaleItem(Long id, Long saleId, Inventory product, Double subtotal, Long quantity) {
        super(id);
        this.saleId = saleId;
        this.product = product;
        this.subtotal = subtotal;
        this.quantity = quantity;
    }

    public Long getSaleId() {
        return this.saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }

    public Double getSubtotal() {
        return this.subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @Override
    public void copy(SaleItem item) {
        this.saleId = item.saleId;
        this.subtotal = item.subtotal;
        this.quantity = item.quantity;
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

    @Override
    public String toString() {
        return "SaleItem [saleId=" + saleId + ", product=" + product.toString() + ", subtotal=" + subtotal + ", quantity="
                + quantity + "]";
    }

    
    
}

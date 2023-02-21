package com.esthetic.reservations.api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "sale")
public class Sale extends BaseModel<Sale> {

    @ManyToOne(targetEntity = Branch.class)
    @JoinColumn(name = "id_branch", referencedColumnName = "id")
    private Branch branch;

    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "id_client", referencedColumnName = "id")
    private UserEntity client;

    @Column(name = "total")
    private Double total;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "sale_date")
    private LocalDateTime saleDate;

    @OneToMany(targetEntity = SaleItem.class)
    @JoinTable(name = "sale_products", joinColumns = @JoinColumn(name = "id_sale", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "id_sale_item", referencedColumnName = "id"))
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<SaleItem> saleProducts = new ArrayList<>();

    /**
     * 
     */
    public Sale() {
        super();
    }

    /**
     * @param branch
     * @param client
     * @param total
     * @param quantity
     * @param saleDate
     * @param saleProducts
     */
    public Sale(Branch branch, UserEntity client, Double total, Long quantity, LocalDateTime saleDate,
            List<SaleItem> saleProducts) {
        super();
        this.branch = branch;
        this.client = client;
        this.total = total;
        this.quantity = quantity;
        this.saleDate = saleDate;
        this.saleProducts.clear();
        for (SaleItem saleItem : saleProducts) {
            this.saleProducts.add(saleItem);
        }
    }

    /**
     * @return the branch
     */
    public Branch getBranch() {
        return branch;
    }

    /**
     * @param branch the branch to set
     */
    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    /**
     * @return the client
     */
    public UserEntity getClient() {
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(UserEntity client) {
        this.client = client;
    }

    /**
     * @return the total
     */
    public Double getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(Double total) {
        this.total = total;
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

    /**
     * @return the saleDate
     */
    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    /**
     * @param saleDate the saleDate to set
     */
    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    /**
     * @return the saleProducts
     */
    public List<SaleItem> getSaleProducts() {
        return saleProducts;
    }

    /**
     * @param saleProducts the saleProducts to set
     */
    public void setSaleProducts(List<SaleItem> saleProducts) {
        this.saleProducts = saleProducts;
    }

    @Override
    public void copy(Sale sale) {
        this.branch = sale.branch;
        this.client = sale.client;
        this.total = sale.total;
        this.quantity = sale.quantity;
        this.saleDate = sale.saleDate;
        this.saleProducts = new ArrayList<>();
        for (SaleItem saleItem : sale.saleProducts) {
            this.saleProducts.add(saleItem);
        }
    }

}

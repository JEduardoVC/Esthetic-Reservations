package com.esthetic.reservations.api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    
    @OneToMany(targetEntity = SaleItem.class, cascade = CascadeType.PERSIST)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<SaleItem> products = new ArrayList<>();

    public Sale() {
        super();
    }

    public Sale(Branch branch, UserEntity client, Double total, Long quantity, LocalDateTime saleDate) {
        this.branch = branch;
        this.client = client;
        this.total = total;
        this.quantity = quantity;
        this.saleDate = saleDate;
    }

    public Sale(Long id, Branch branch, UserEntity client, Double total, Long quantity, LocalDateTime saleDate) {
        super(id);
        this.branch = branch;
        this.client = client;
        this.total = total;
        this.quantity = quantity;
        this.saleDate = saleDate;
    }

    public Branch getBranch() {
        return this.branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public UserEntity getClient() {
        return this.client;
    }

    public void setClient(UserEntity client) {
        this.client = client;
    }

    public Double getTotal() {
        return this.total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Long getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getSaleDate() {
        return this.saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    /**
     * @return the products
     */
    public List<SaleItem> getProducts() {
        return products;
    }

    /**
     * @param products the products to set
     */
    public void setProducts(List<SaleItem> products) {
        this.products = products;
    }

    @Override
    public void copy(Sale sale) {
        this.branch = sale.branch;
        this.client = sale.client;
        this.total = sale.total;
        this.quantity = sale.quantity;
        this.saleDate = sale.saleDate;
        this.products = sale.products;
    }

}

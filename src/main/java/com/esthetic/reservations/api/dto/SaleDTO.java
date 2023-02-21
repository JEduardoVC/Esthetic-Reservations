package com.esthetic.reservations.api.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SaleDTO extends GenericModelDTO {

    private SaleBranchDTO branch;

    private MinUserEntityDTO client;

    private Double total;

    private Long quantity;

    private List<SaleItemDTO> productsList;

    private LocalDateTime saleDate;

    /**
     * 
     */
    public SaleDTO() {
        super();
    }

    /**
     * @param branch
     * @param client
     * @param total
     * @param quantity
     * @param productsList
     * @param saleDate
     */
    public SaleDTO(SaleBranchDTO branch, MinUserEntityDTO client, Double total, Long quantity,
            List<SaleItemDTO> productsList, LocalDateTime saleDate) {
        super();
        this.branch = branch;
        this.client = client;
        this.total = total;
        this.quantity = quantity;
        this.productsList = productsList;
        this.saleDate = saleDate;
    }

    /**
     * @return the branch
     */
    public SaleBranchDTO getBranch() {
        return branch;
    }

    /**
     * @param branch the branch to set
     */
    public void setBranch(SaleBranchDTO branch) {
        this.branch = branch;
    }

    /**
     * @return the client
     */
    public MinUserEntityDTO getClient() {
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(MinUserEntityDTO client) {
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
     * @return the productsList
     */
    public List<SaleItemDTO> getProductsList() {
        return productsList;
    }

    /**
     * @param productsList the productsList to set
     */
    public void setProductsList(List<SaleItemDTO> productsList) {
        this.productsList = productsList;
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

}

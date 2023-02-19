package com.esthetic.reservations.api.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.esthetic.reservations.api.model.SaleItem;

@Transactional
@Component("SaleItemRepository")
public interface SaleItemRepository extends GenericRepository<SaleItem, Long> {

    public Page<SaleItem> findAllBySaleId(Long saleId, Pageable pageable);
    
    public void deleteAllBySaleId(Long saleId);

}
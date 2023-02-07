package com.esthetic.reservations.api.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.model.SaleItem;

@Repository
@Transactional
@Component("SaleItemRepository")
public interface SaleItemRepository extends GenericRepository<SaleItem, Long> {

    public Page<SaleItem> findAllBySaleId(Long saleId, Pageable pageable);

}
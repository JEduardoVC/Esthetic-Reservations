package com.esthetic.reservations.api.repository;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.esthetic.reservations.api.model.Sale;

@Transactional
@Component("SaleRepository")
public interface SaleRepository extends GenericRepository<Sale, Long> {

    public boolean existsById(Long id);

}
package com.esthetic.reservations.api.service;

import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.dto.SaleDTO;
import com.esthetic.reservations.api.dto.SaleItemDTO;
import com.esthetic.reservations.api.model.Sale;
import com.esthetic.reservations.api.model.SaleItem;

public interface SaleItemService extends GenericService<SaleItem, SaleItemDTO> {

    ResponseDTO<SaleItemDTO> findAllBySaleId(int pageNumber, int pageSize, String sortBy, String sortDir, Long saleId);

    public void deleteById(Long id);
    
    public void deleteAllBySaleId(Long saleId);
    
}

package com.esthetic.reservations.api.service;

import com.esthetic.reservations.api.dto.SaleItemDTO;
import com.esthetic.reservations.api.model.SaleItem;

public interface SaleItemService extends GenericService<SaleItem, SaleItemDTO> {

    public void deleteById(Long id);
    
}

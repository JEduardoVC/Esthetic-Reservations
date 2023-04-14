package com.esthetic.reservations.api.service;

import com.esthetic.reservations.api.dto.NewSaleDTO;
import com.esthetic.reservations.api.dto.SaleDTO;
import com.esthetic.reservations.api.model.Sale;

public interface SaleService extends GenericService<Sale, SaleDTO> {

    public boolean existsById(Long id);
    
    public SaleDTO update(NewSaleDTO editedSaleDTO, Long id);

}

package com.esthetic.reservations.api.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.esthetic.reservations.api.dto.SaleItemDTO;
import com.esthetic.reservations.api.model.SaleItem;
import com.esthetic.reservations.api.repository.SaleItemRepository;
import com.esthetic.reservations.api.repository.SaleRepository;
import com.esthetic.reservations.api.service.SaleItemService;

@Service
public class SaleItemServiceImpl extends GenericServiceImpl<SaleItem, SaleItemDTO>
		implements SaleItemService {

    private SaleItemRepository saleItemRepository;
    private SaleRepository saleRepository;
    private InventoryServiceImpl inventoryService;

    public SaleItemServiceImpl(SaleItemRepository repository, ModelMapper modelMapper, SaleRepository saleRepository,InventoryServiceImpl inventoryService) {
        super(repository, modelMapper, SaleItem.class, SaleItemDTO.class);
        this.saleItemRepository = repository;
        this.inventoryService = inventoryService;
        this.saleRepository = saleRepository;
    }
    
    @Override
    public void deleteById(Long id) {
        this.saleItemRepository.deleteById(id);
    }

}
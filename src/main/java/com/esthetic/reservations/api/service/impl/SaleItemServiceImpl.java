package com.esthetic.reservations.api.service.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.esthetic.reservations.api.dto.InventoryDTO;
import com.esthetic.reservations.api.dto.MinBranchDTO;
import com.esthetic.reservations.api.dto.MinInventory;
import com.esthetic.reservations.api.dto.NewSaleDTO;
import com.esthetic.reservations.api.dto.NewSaleItemDTO;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.dto.SaleDTO;
import com.esthetic.reservations.api.dto.SaleItemDTO;
import com.esthetic.reservations.api.exception.BadRequestException;
import com.esthetic.reservations.api.exception.EstheticAppException;
import com.esthetic.reservations.api.exception.ResourceNotFoundException;
import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.model.Inventory;
import com.esthetic.reservations.api.model.Sale;
import com.esthetic.reservations.api.model.SaleItem;
import com.esthetic.reservations.api.model.UserEntity;
import com.esthetic.reservations.api.repository.SaleItemRepository;
import com.esthetic.reservations.api.repository.SaleRepository;
import com.esthetic.reservations.api.service.SaleItemService;
import com.esthetic.reservations.api.service.SaleService;
import com.esthetic.reservations.api.util.AppConstants;

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
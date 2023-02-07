package com.esthetic.reservations.api.service.impl;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.esthetic.reservations.api.dto.InventoryDTO;
import com.esthetic.reservations.api.dto.NewSaleDTO;
import com.esthetic.reservations.api.dto.NewSaleItemDTO;
import com.esthetic.reservations.api.dto.SaleDTO;
import com.esthetic.reservations.api.exception.EstheticAppException;
import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.model.Inventory;
import com.esthetic.reservations.api.model.Sale;
import com.esthetic.reservations.api.model.SaleItem;
import com.esthetic.reservations.api.model.UserEntity;
import com.esthetic.reservations.api.repository.SaleItemRepository;
import com.esthetic.reservations.api.repository.SaleRepository;
import com.esthetic.reservations.api.service.SaleService;

@Service
public class SaleServiceImpl extends GenericServiceImpl<Sale, SaleDTO>
		implements SaleService {

	private SaleRepository saleRepository;
	private SaleItemRepository saleItemRepository;
	private InventoryServiceImpl inventoryService;
	private BranchServiceImpl branchService;
	private UserServiceImpl userService;

	public SaleServiceImpl(SaleRepository repository, ModelMapper modelMapper,SaleItemRepository saleItemRepository, InventoryServiceImpl inventoryService, BranchServiceImpl branchService, UserServiceImpl userService) {
		super(repository, modelMapper, Sale.class, SaleDTO.class);
		this.saleRepository = repository;
		this.saleItemRepository = saleItemRepository;
		this.inventoryService = inventoryService;
		this.branchService = branchService;
		this.userService = userService;
	}
	
	/**
	 * Register a new sale.
	 * @param saleDTO The NewSaleDTO of the sale.
	 * @return The DTO of the new sale.
	 */
	public SaleDTO save(NewSaleDTO saleDTO) {
		List<NewSaleItemDTO> newSaleItemDTOs = saleDTO.getProductList();
		// Check stock and reserve
		List<Inventory> originalItems = new ArrayList<>();
		for(NewSaleItemDTO newItem : newSaleItemDTOs) {
		    Inventory item = inventoryService.mapToModel(inventoryService.findById(newItem.getProductId()));
			originalItems.add(new Inventory(item));
			if(item.getStore() < newItem.getQuantity()){
				for(Inventory original : originalItems){
					inventoryService.getRepository().save(original);
				}
				throw new EstheticAppException(HttpStatus.BAD_REQUEST, "Not enough stock for " + item.getInventory_name() + " only " + item.getStore() + " left.");
			}
			item.setStore(item.getStore() - newItem.getQuantity());
			try {
				inventoryService.getRepository().save(item);
			} catch (Exception e){
				for(Inventory original : originalItems){
					inventoryService.getRepository().save(original);
				}
				throw new EstheticAppException(HttpStatus.BAD_REQUEST, "Sale cannot be processed. " + e.getMessage());
			}
		}
		// Create Sale
		Double total = 0d;
		for(NewSaleItemDTO newItem : newSaleItemDTOs) {
		    InventoryDTO item = inventoryService.findById(newItem.getProductId());
			total += item.getPrice() * newItem.getQuantity();
		}
		LocalDateTime sale_date = LocalDateTime.now();
		UserEntity client = userService.mapToModel(userService.findById(saleDTO.getClientId()));
		Branch branch = branchService.mapToModel(branchService.findById(saleDTO.getBranchId()));
		Sale newSale;
		try{
			newSale = saleRepository.saveAndFlush(new Sale(branch, client, total, (long) newSaleItemDTOs.size(), sale_date));
		} catch (Exception e){
			for(Inventory original : originalItems){ // Undo stock changes
				inventoryService.getRepository().save(original);
			}
			throw new EstheticAppException(HttpStatus.BAD_REQUEST, "Sale cannot be processed. " + e.getMessage());
		}
		// Save item list
		List<Long> savedIDs = new ArrayList<>();
		List<SaleItem> saleItems = new ArrayList<>();
		for(NewSaleItemDTO newItem : newSaleItemDTOs) {
		    InventoryDTO item = inventoryService.findById(newItem.getProductId());
			total = item.getPrice() * newItem.getQuantity();
			SaleItem saleItem = new SaleItem(newSale.getId(), inventoryService.mapToModel(item), total, newItem.getQuantity());
			try{
				SaleItem newSaleItem = saleItemRepository.save(saleItem);
				saleItems.add(newSaleItem);
				savedIDs.add(newSaleItem.getId());
			} catch(Exception e){ // an error occurred
				saleRepository.deleteById(newSale.getId()); // delete the sale
				for(Long savedID : savedIDs){ // and all the items in list
					saleItemRepository.deleteById(savedID);
				}
				for(Inventory original : originalItems){ // Undo stock changes
					inventoryService.getRepository().save(original);
				}
				throw new EstheticAppException(HttpStatus.BAD_REQUEST, "Sale cannot be processed. " + e.getMessage());
			}
		}
		newSale.setProductList(saleItems);
		try{
            newSale = saleRepository.save(newSale);
        } catch (Exception e){
            for(Inventory original : originalItems){ // Undo stock changes
                inventoryService.getRepository().save(original);
            }
            throw new EstheticAppException(HttpStatus.BAD_REQUEST, "Sale cannot be processed. " + e.getMessage());
        }
		return mapToDTO(newSale);
	}

    @Override
    public boolean existsById(Long id) {
        return saleRepository.existsById(id);
    }

}
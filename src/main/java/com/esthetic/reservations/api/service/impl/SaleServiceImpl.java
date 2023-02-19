package com.esthetic.reservations.api.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.esthetic.reservations.api.dto.InventoryDTO;
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
import com.esthetic.reservations.api.repository.SaleRepository;
import com.esthetic.reservations.api.service.SaleService;
import com.esthetic.reservations.api.util.AppConstants;

@Service
public class SaleServiceImpl extends GenericServiceImpl<Sale, SaleDTO>
		implements SaleService {

	private SaleRepository saleRepository;
	private SaleItemServiceImpl saleItemService;
	private InventoryServiceImpl inventoryService;
	private BranchServiceImpl branchService;
	private UserServiceImpl userService;

	public SaleServiceImpl(SaleRepository repository, ModelMapper modelMapper, SaleItemServiceImpl saleItemService, InventoryServiceImpl inventoryService, BranchServiceImpl branchService, UserServiceImpl userService) {
		super(repository, modelMapper, Sale.class, SaleDTO.class);
		this.saleRepository = repository;
		this.saleItemService = saleItemService;
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
		List<NewSaleItemDTO> newSaleItemDTOs = saleDTO.getProducts();
		// Check stock and reserve
		List<Inventory> originalItems = new ArrayList<>();
		for(NewSaleItemDTO newItem : newSaleItemDTOs) {
		    Inventory item = inventoryService.mapToModel(inventoryService.findById(newItem.getProductId()));
			originalItems.add(new Inventory(item));
			if(item.getStore() < newItem.getQuantity()){
				for(Inventory original : originalItems){
					inventoryService.getRepository().save(original);
				}
				throw new EstheticAppException(HttpStatus.BAD_REQUEST, "Not enough stock for " + item.getInventory_name() + ". " + item.getStore() + " left.");
			}
			item.setStore(item.getStore() - newItem.getQuantity());
			try {
				inventoryService.getRepository().save(item);
			} catch (Exception e){
				for(Inventory original : originalItems){
					inventoryService.getRepository().save(original);
				}
				throw new EstheticAppException(HttpStatus.BAD_REQUEST, "La venta no puede ser procesada. " + e.getMessage());
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
			throw new EstheticAppException(HttpStatus.BAD_REQUEST, "La venta no puede ser procesada. " + e.getMessage());
		}
		// Save item list
		List<Long> savedIDs = new ArrayList<>();
		List<SaleItem> saleItems = new ArrayList<>();
		for(NewSaleItemDTO newItem : newSaleItemDTOs) {
		    InventoryDTO item = inventoryService.findById(newItem.getProductId());
			total = item.getPrice() * newItem.getQuantity();
			SaleItem saleItem = new SaleItem(newSale.getId(), inventoryService.mapToModel(item), total, newItem.getQuantity());
			try{
				SaleItem newSaleItem = saleItemService.mapToModel(saleItemService.save(saleItemService.mapToDTO(saleItem)));
				saleItems.add(newSaleItem);
				savedIDs.add(newSaleItem.getId());
			} catch(Exception e){ // an error occurred
				saleRepository.deleteById(newSale.getId()); // delete the sale
				for(Long savedID : savedIDs){ // and all the items in list
					saleItemService.deleteById(savedID);
				}
				for(Inventory original : originalItems){ // Undo stock changes
					inventoryService.getRepository().save(original);
				}
				throw new EstheticAppException(HttpStatus.BAD_REQUEST, "La venta no puede ser procesada. " + e.getMessage());
			}
		}
		newSale.setProducts(saleItems);
		try{
            newSale = saleRepository.save(newSale);
        } catch (Exception e){
            for(Inventory original : originalItems){ // Undo stock changes
                inventoryService.getRepository().save(original);
            }
            throw new EstheticAppException(HttpStatus.BAD_REQUEST, "La venta no puede ser procesada. " + e.getMessage());
        }
		return mapToDTO(newSale);
	}
	
	/**
	 * Delete the sale restoring the stock.
	 * @param id the id of the sale to delete.
	 */
	@Override
    public void delete(Long id) {
	    // Sale not exists
	    if(!existsById(id)) {
	        throw new ResourceNotFoundException("Venta", "no encontrada", "id", String.valueOf(id));
	    }
	    ResponseDTO<SaleItemDTO> saleItems = saleItemService.findAllBySaleId(Integer.parseInt(AppConstants.PAGE_NUMBER), Integer.parseInt(AppConstants.PAGE_SIZE), AppConstants.DEFAULT_SORT_ORDER, AppConstants.DEFAULT_SORT_DIR,
	            id);
	    // Restore stock for the products
	    ArrayList<Inventory> originalInventory = new ArrayList<>();
	    for(SaleItemDTO saleItemDTO : saleItems.getContent()) {
            Inventory product = inventoryService.mapToModel(inventoryService.findById(saleItemDTO.getProduct().getId()));
            originalInventory.add(new Inventory(product)); // Save the current inventory just in case
            try {
                // Restore the stock
                product.setStore(product.getStore() + saleItemDTO.getQuantity());
                inventoryService.save(inventoryService.mapToDTO(product));
            } catch (Exception e) {
                // Undo changes
                for(Inventory inventory : originalInventory) {
                    inventoryService.save(inventoryService.mapToDTO(inventory));
                }
                throw new BadRequestException("Venta", "no puede ser eliminada. " + e.getMessage());
            }
        }
	    // Delete sale.
        saleRepository.deleteById(id);
        // Delete sale items
        try {
            saleItemService.deleteAllBySaleId(id);            
        } catch (Exception e) {
            // Undo changes
            for(Inventory inventory : originalInventory) {
                inventoryService.save(inventoryService.mapToDTO(inventory));
            }
            throw new BadRequestException("Venta", "no puede ser eliminada. " + e.getMessage());
        }
    }
	
    @Override
    public boolean existsById(Long id) {
        return saleRepository.existsById(id);
    }

    @Override
    public SaleDTO update(NewSaleDTO saleDTO, Long id) {
        saleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Venta", "no encontrada", "id", String.valueOf(id)));
        // The current sale is deleted and a new one is created.
        delete(id);
        // Create the new one
        List<NewSaleItemDTO> newSaleItemDTOs = saleDTO.getProducts();
        // Check stock and reserve
        List<Inventory> originalItems = new ArrayList<>();
        for(NewSaleItemDTO newItem : newSaleItemDTOs) {
            Inventory item = inventoryService.mapToModel(inventoryService.findById(newItem.getProductId()));
            originalItems.add(new Inventory(item));
            if(item.getStore() < newItem.getQuantity()){
                for(Inventory original : originalItems){
                    inventoryService.getRepository().save(original);
                }
                throw new EstheticAppException(HttpStatus.BAD_REQUEST, "Not enough stock for " + item.getInventory_name() + ". " + item.getStore() + " left.");
            }
            item.setStore(item.getStore() - newItem.getQuantity());
            try {
                inventoryService.getRepository().save(item);
            } catch (Exception e){
                for(Inventory original : originalItems){
                    inventoryService.getRepository().save(original);
                }
                throw new EstheticAppException(HttpStatus.BAD_REQUEST, "La venta no puede ser procesada. " + e.getMessage());
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
            newSale = saleRepository.saveAndFlush(new Sale(id, branch, client, total, (long) newSaleItemDTOs.size(), sale_date));
        } catch (Exception e){
            for(Inventory original : originalItems){ // Undo stock changes
                inventoryService.getRepository().save(original);
            }
            throw new EstheticAppException(HttpStatus.BAD_REQUEST, "La venta no puede ser procesada. " + e.getMessage());
        }
        // Save item list
        List<Long> savedIDs = new ArrayList<>();
        List<SaleItem> saleItems = new ArrayList<>();
        for(NewSaleItemDTO newItem : newSaleItemDTOs) {
            InventoryDTO item = inventoryService.findById(newItem.getProductId());
            total = item.getPrice() * newItem.getQuantity();
            SaleItem saleItem = new SaleItem(newSale.getId(), inventoryService.mapToModel(item), total, newItem.getQuantity());
            try{
                SaleItem newSaleItem = saleItemService.mapToModel(saleItemService.save(saleItemService.mapToDTO(saleItem)));
                saleItems.add(newSaleItem);
                savedIDs.add(newSaleItem.getId());
            } catch(Exception e){ // an error occurred
                saleRepository.deleteById(newSale.getId()); // delete the sale
                for(Long savedID : savedIDs){ // and all the items in list
                    saleItemService.deleteById(savedID);
                }
                for(Inventory original : originalItems){ // Undo stock changes
                    inventoryService.getRepository().save(original);
                }
                throw new EstheticAppException(HttpStatus.BAD_REQUEST, "La venta no puede ser procesada. " + e.getMessage());
            }
        }
        newSale.setProducts(saleItems);
        try{
            newSale = saleRepository.save(newSale);
        } catch (Exception e){
            for(Inventory original : originalItems){ // Undo stock changes
                inventoryService.getRepository().save(original);
            }
            throw new EstheticAppException(HttpStatus.BAD_REQUEST, "La venta no puede ser procesada. " + e.getMessage());
        }
        return mapToDTO(newSale);
    }

}
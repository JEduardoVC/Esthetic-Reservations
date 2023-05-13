package com.esthetic.reservations.api.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.esthetic.reservations.api.dto.AppointmentDTO;
import com.esthetic.reservations.api.dto.NewSaleDTO;
import com.esthetic.reservations.api.dto.NewSaleItemDTO;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.dto.SaleDTO;
import com.esthetic.reservations.api.dto.SaleItemDTO;
import com.esthetic.reservations.api.exception.BadRequestException;
import com.esthetic.reservations.api.exception.ResourceNotFoundException;
import com.esthetic.reservations.api.model.Appointment;
import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.model.Inventory;
import com.esthetic.reservations.api.model.Sale;
import com.esthetic.reservations.api.model.SaleItem;
import com.esthetic.reservations.api.model.UserEntity;
import com.esthetic.reservations.api.repository.SaleRepository;
import com.esthetic.reservations.api.service.SaleService;

@Service
public class SaleServiceImpl extends GenericServiceImpl<Sale, SaleDTO> implements SaleService {

    private SaleRepository saleRepository;
    private SaleItemServiceImpl saleItemService;
    private InventoryServiceImpl inventoryService;
    private BranchServiceImpl branchService;
    private UserServiceImpl userService;

    public SaleServiceImpl(SaleRepository repository, ModelMapper modelMapper, SaleItemServiceImpl saleItemService,
            InventoryServiceImpl inventoryService, BranchServiceImpl branchService, UserServiceImpl userService) {
        super(repository, modelMapper, Sale.class, SaleDTO.class);
        this.saleRepository = repository;
        this.saleItemService = saleItemService;
        this.inventoryService = inventoryService;
        this.branchService = branchService;
        this.userService = userService;
    }

    /**
     * Register a new sale.
     * 
     * @param saleDTO The NewSaleDTO of the sale.
     * @return The DTO of the new sale.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public SaleDTO save(NewSaleDTO saleDTO) {
        Sale newSale = createSale(saleDTO);
        newSale = saleRepository.save(newSale);
        return this.mapToDTO(newSale);
    }

    /**
     * Creates a Sale without storing it into the database
     * 
     * @param saleDTO
     * @return the Sale entity created.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    private Sale createSale(NewSaleDTO saleDTO) {
        List<NewSaleItemDTO> saleItemDTOs = saleDTO.getProducts();
        List<SaleItem> productsList = new ArrayList<>();
        Double total = 0d;
        Long quantity = 0l;
        Boolean enoughStock = true;
        List<String> stockErrors = new ArrayList<>();
        for (NewSaleItemDTO saleItemDTO : saleItemDTOs) {
            Inventory currentItem = inventoryService.mapToModel(inventoryService.findById(saleItemDTO.getProductId()));
            // Check for stock availability
            if (currentItem.getStore() < saleItemDTO.getQuantity()) {
                enoughStock = false;
                stockErrors.add(String.format("%d %s disponibles", currentItem.getStore(), currentItem.getInventory_name()));
            }
            if(enoughStock){
                // Modify the inventory
                currentItem.setStore(currentItem.getStore() - saleItemDTO.getQuantity());
                currentItem = inventoryService.mapToModel(inventoryService.save(inventoryService.mapToDTO(currentItem)));
                // Create the sale items
                SaleItem newSaleItem = new SaleItem(currentItem, saleItemDTO.getQuantity() * currentItem.getPrice(),
                        saleItemDTO.getQuantity());
                newSaleItem = saleItemService.mapToModel(saleItemService.save(saleItemService.mapToDTO(newSaleItem)));
                productsList.add(newSaleItem);
                total += newSaleItem.getSubtotal();
                quantity += newSaleItem.getQuantity();
            }
        }
        if(!enoughStock){
            int n = stockErrors.size();
            String msg = "Sólo hay ";
            int i = 0;
            for (String error : stockErrors) {
                msg += error;
                if(i == stockErrors.size() - 1){
                    break;
                }
                msg += i + 1 == stockErrors.size() - 1 ? " y " : ", ";
                i++;
            }
            throw new BadRequestException(String.format("Producto%s sin stock: ", n > 1 ? "s" : ""), msg);
        }
        // Create the sale
        Branch saleBranch = branchService.mapToModel(branchService.findById(saleDTO.getBranchId()));
        UserEntity saleClient = userService.mapToModel(userService.findById(saleDTO.getClientId()));
        LocalDateTime saleDate = LocalDateTime.now();
        Sale newSale = new Sale(saleBranch, saleClient, total, quantity, saleDate, productsList);
        return newSale;
    }

    /**
     * Delete the sale restoring the stock.
     * 
     * @param id the id of the sale to delete.
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long id) {
        Sale saleToDelete = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", "no encontrada", "id", String.valueOf(id)));
        // Delete the sale
        saleRepository.deleteById(id);
        // Restore stock.
        List<SaleItem> saleItemsToDelete = saleToDelete.getSaleProducts();
        restoreStockOf(saleItemsToDelete);
        for(SaleItem saleItem : saleItemsToDelete) {
            saleItemService.deleteById(saleItem.getId());
        }
    }

    private void restoreStockOf(List<SaleItem> saleItems) {
        // Restore stock.
        for (SaleItem saleItem : saleItems) {
            // Modify inventory
            Inventory actualInventory = inventoryService
                    .mapToModel(inventoryService.findById(saleItem.getProduct().getId()));
            actualInventory.setStore(saleItem.getQuantity() + actualInventory.getStore());
            inventoryService.save(inventoryService.mapToDTO(actualInventory));
        }
    }

    @Override
    public boolean existsById(Long id) {
        return saleRepository.existsById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public SaleDTO update(NewSaleDTO editedSaleDTO, Long id) {
        Sale oldSale = saleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", "no encontrada", "id", String.valueOf(id)));
        List<SaleItem> oldItems = oldSale.getSaleProducts();
        restoreStockOf(oldItems);
//        oldSale.getSaleProducts().clear();
//        oldSale = saleRepository.save(oldSale);
        
        for(SaleItem oldItem : oldItems) {
            SaleItem actualOldItem = saleItemService.mapToModel(saleItemService.findById(oldItem.getId()));
            oldSale.getSaleProducts().remove(actualOldItem);
        }
        oldSale.setSaleProducts(new ArrayList<>());
        oldSale = saleRepository.save(oldSale);
        
        for(SaleItem oldItem : oldItems) {
            SaleItem actualOldItem = saleItemService.mapToModel(saleItemService.findById(oldItem.getId()));
            saleItemService.deleteById(actualOldItem.getId());
        }
        
        Sale editedSale = createSale(editedSaleDTO);
        oldSale.copy(editedSale);
        Sale newSale = saleRepository.save(oldSale);
        return mapToDTO(newSale);
    }
    
    public ResponseDTO<SaleDTO> findAllByIdAndByBranchId(Long id_usuario, Long id_branch) {
    	ArrayList<Sale> sales = saleRepository.findByIdAndByBranch(String.valueOf(id_usuario), String.valueOf(id_branch));
    	ArrayList<SaleDTO> arraySales = new ArrayList<>();
		for(Sale sale : sales) {
			SaleDTO saleDTO = mapToDTO(sale);
			saleDTO.setProductsList(getSalesProductDTO(sale.getSaleProducts()));
			arraySales.add(saleDTO);
		}
		ResponseDTO<SaleDTO> response = new ResponseDTO<>();
		response.setContent(arraySales);
		return response;
    }
    
    public List<SaleItemDTO> getSalesProductDTO(List<SaleItem> sales) {
    	List<SaleItemDTO> newListSaleDTO = new ArrayList<>();
    	sales.forEach(sale -> {
    		newListSaleDTO.add(saleItemService.mapToDTO(sale));
    	});
    	return newListSaleDTO;
    }

}

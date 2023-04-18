package com.esthetic.reservations.api.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.esthetic.reservations.api.dto.InventoryDTO;
import com.esthetic.reservations.api.dto.MinInventory;
import com.esthetic.reservations.api.dto.ResponseDTO;
import com.esthetic.reservations.api.model.Branch;
import com.esthetic.reservations.api.model.Inventory;
import com.esthetic.reservations.api.repository.InventoryRepository;
import com.esthetic.reservations.api.service.InventoryService;

@Service
public class InventoryServiceImpl extends GenericServiceImpl<Inventory, InventoryDTO> implements InventoryService {

	private BranchServiceImpl branchServiceImpl;
	private InventoryRepository inventoryRepository;
	
	public InventoryServiceImpl(InventoryRepository repository, ModelMapper model, BranchServiceImpl branchServiceImpl) {
		super(repository, model, Inventory.class, InventoryDTO.class);
		this.branchServiceImpl = branchServiceImpl;
		this.inventoryRepository = repository;
	}
	
	public ResponseDTO<InventoryDTO> findAllByBranchId(Long id) {
		Branch sucursal = branchServiceImpl.mapToModel(branchServiceImpl.findById(id));
		ArrayList<Inventory> inventario = inventoryRepository.findAllByIdBranch(sucursal);
		ArrayList<InventoryDTO> newInventario = new ArrayList<>();
		for (Inventory inventory : inventario) {
			newInventario.add(mapToDTO(inventory));
		}
		ResponseDTO<InventoryDTO> response = new ResponseDTO<>();
		response.setContent(newInventario);
		return response;
	}
	
	public InventoryDTO save(MinInventory inventario, MultipartFile file) {
		Branch sucursal = branchServiceImpl.mapToModel(branchServiceImpl.findById(inventario.getId_branch()));
		String rutaAbsoluta = "C://Esthetic-Reservation/Inventario";
		String nameImage = "";
		try {
			byte[] imagenBytes = file.getBytes();
			nameImage = generateRandomString(20);
			Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + nameImage + ".jpg");
			Files.createDirectories(Paths.get(rutaAbsoluta));
			Files.write(rutaCompleta, imagenBytes);
		} catch (IOException e) {
			return null;
		}
		Inventory newInventory = new Inventory(inventario.getInventory_name(), inventario.getPrice(), inventario.getStore(), nameImage, sucursal, inventario.getDescription(), inventario.getCapacibility());
		return mapToDTO(getRepository().save(newInventory));
	}

	public InventoryDTO update(MinInventory inventario, MultipartFile file, Long id) {
		Inventory inventory = getRepository().findById(id).orElse(null);
		Branch sucursal = branchServiceImpl.mapToModel(branchServiceImpl.findById(inventario.getId_branch()));
		String rutaAbsoluta = "C://Esthetic-Reservation/Inventario";
		String nameImage = "";
		if(file == null) {
			inventory.setInventory_name(inventario.getInventory_name());
			inventory.setPrice(inventario.getPrice());
			inventory.setStore(inventario.getStore());
			inventory.setDescription(inventario.getDescription());
			inventory.setCapacibility(inventario.getCapacibility());
			return mapToDTO(getRepository().save(inventory));
		}
		try {
			byte[] imagenBytes = file.getBytes();
			nameImage = generateRandomString(20);
			Path rutaCompleta = Paths.get(rutaAbsoluta + "//" + nameImage + ".jpg");
			Files.createDirectories(Paths.get(rutaAbsoluta));
			Files.delete(Paths.get(rutaAbsoluta + "//" + inventory.getImagen() + ".jpg"));
			Files.write(rutaCompleta, imagenBytes);
		} catch (IOException e) {
			return null;
		}
		inventory.setInventory_name(inventario.getInventory_name());
		inventory.setPrice(inventario.getPrice());
		inventory.setStore(inventario.getStore());
		inventory.setDescription(inventario.getDescription());
		inventory.setCapacibility(inventario.getCapacibility());
		return mapToDTO(getRepository().save(inventory));
	}
	
	public void delete(Long id) {
		Inventory inventory = getRepository().findById(id).orElse(null);
		String rutaAbsoluta = "C://Esthetic-Reservation/Inventario";
		try {
			Files.delete(Paths.get(rutaAbsoluta + "//" + inventory.getImagen() + ".jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		getRepository().deleteById(id);
	}
	
	public String generateRandomString(int length) {
        String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
        String CHAR_UPPER = CHAR_LOWER.toUpperCase();
        String NUMBER = "0123456789";
        String DATA_FOR_RANDOM_STRING = CHAR_LOWER + CHAR_UPPER + NUMBER;
        SecureRandom random = new SecureRandom();
        if (length < 1) throw new IllegalArgumentException();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int rndCharAt = random.nextInt(DATA_FOR_RANDOM_STRING.length());
            char rndChar = DATA_FOR_RANDOM_STRING.charAt(rndCharAt);
            sb.append(rndChar);
        }
        return sb.toString();
	}
}

package com.esthetic.reservations.api.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;

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
			Cloudinary cloudinary = new Cloudinary(getConfigCloudinary());
			String url = cloudinary.url().transformation(new Transformation<>()).generate(inventory.getImagen());
			inventory.setImagen(url);
			newInventario.add(mapToDTO(inventory));
		}
		ResponseDTO<InventoryDTO> response = new ResponseDTO<>();
		response.setContent(newInventario);
		return response;
	}
	
	public InventoryDTO save(MinInventory inventario, MultipartFile file) {
		Branch sucursal = branchServiceImpl.mapToModel(branchServiceImpl.findById(inventario.getId_branch()));
		Cloudinary cloudinary = new Cloudinary(getConfigCloudinary());
		String nameImage = "";
		try {
			File img = convert(file);
			Map<?, ?> hola =  cloudinary.uploader().upload(img, ObjectUtils.emptyMap());
			nameImage = hola.get("public_id").toString();
		} catch (IOException e) {
			return null;
		}
		Inventory newInventory = new Inventory(inventario.getInventory_name(), inventario.getPrice(), inventario.getStore(), nameImage, sucursal, inventario.getDescription(), inventario.getCapacibility());
		return mapToDTO(getRepository().save(newInventory));
	}

	public InventoryDTO update(MinInventory inventario, MultipartFile file, Long id) {
		Inventory inventory = getRepository().findById(id).orElse(null);
		Cloudinary cloudinary = new Cloudinary(getConfigCloudinary());
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
			cloudinary.uploader().destroy(inventory.getImagen(), ObjectUtils.emptyMap());
			File img = convert(file);
			Map<?, ?> hola =  cloudinary.uploader().upload(img, ObjectUtils.emptyMap());
			nameImage = hola.get("public_id").toString();
		} catch (IOException e) {
			return null;
		}
		inventory.setInventory_name(inventario.getInventory_name());
		inventory.setPrice(inventario.getPrice());
		inventory.setStore(inventario.getStore());
		inventory.setDescription(inventario.getDescription());
		inventory.setImagen(nameImage);
		inventory.setCapacibility(inventario.getCapacibility());
		return mapToDTO(getRepository().save(inventory));
	}
	
	public void eliminar(Long id) {
		Inventory inventory = getRepository().findById(id).orElse(null);
		try {
			Cloudinary cloudinary = new Cloudinary(getConfigCloudinary());
			cloudinary.uploader().destroy(inventory.getImagen(), ObjectUtils.emptyMap());
			getRepository().deleteById(id);
		} catch (Exception e) {

		}
	}
	
	private File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(multipartFile.getBytes());
        fo.close();
        return file;
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map getConfigCloudinary() {
		Map config = new HashMap<>();
		config.put("cloud_name", "hm1kcpclw");
		config.put("api_key", "542412146313699");
		config.put("api_secret", "DqEyR-5WNRKmkey4vwpolCS__MY");
		config.put("secur", true);
		return config;
	}
}

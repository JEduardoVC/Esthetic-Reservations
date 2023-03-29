package com.esthetic.reservations.api.dto;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.esthetic.reservations.api.model.Branch;

@Entity
@Table(name = "inventory")
public class InventoryDTO extends GenericModelDTO {
	private String inventory_name;
	
	private Double price;
	
	private Long store;
	
	private String imagen;
	
	private Branch id_branch;

	public InventoryDTO() {
		super();
	}

	public InventoryDTO(Long id, String inventory_name, Double price, Long store, String imagen, Branch id_branch) {
		super(id);
		this.inventory_name = inventory_name;
		this.price = price;
		this.store = store;
		this.imagen = imagen;
		this.id_branch = id_branch;
	}

	public String getInventory_name() {
		return inventory_name;
	}

	public void setInventory_name(String inventory_name) {
		this.inventory_name = inventory_name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Long getStore() {
		return store;
	}

	public void setStore(Long store) {
		this.store = store;
	}
	
	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public Branch getId_branch() {
		return id_branch;
	}

	public void setId_branch(Branch id_branch) {
		this.id_branch = id_branch;
	}
}

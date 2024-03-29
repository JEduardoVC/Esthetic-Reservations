package com.esthetic.reservations.api.dto;

public class MinInventory extends GenericModelDTO {
	private String inventory_name;
	
	private Double price;
	
	private Long store;
	
	private String imagen;
	
	private Long id_branch;
	
	private String description;
	
	private String capacibility;

	public MinInventory() {
		super();
	}
	
	public MinInventory(Long id, String inventory_name, Double price, Long store, String imagen, String description, String capacibility, Long id_branch) {
		super(id);
		this.inventory_name = inventory_name;
		this.price = price;
		this.store = store;
		this.imagen = imagen;
		this.description = description;
		this.capacibility = capacibility;		
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

	public Long getId_branch() {
		return id_branch;
	}

	public void setId_branch(Long id_branch) {
		this.id_branch = id_branch;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCapacibility() {
		return capacibility;
	}

	public void setCapacibility(String capacibility) {
		this.capacibility = capacibility;
	}
}

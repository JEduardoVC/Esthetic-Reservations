package com.esthetic.reservations.api.dto;

public class MinInventory extends GenericModelDTO {
	private String inventory_name;
	
	private Integer price;
	
	private Integer store;
	
	private String imagen;
	
	private Long id_branch;

	public MinInventory() {
		super();
	}

	public MinInventory(Long id, String inventory_name, Integer price, Integer store, String imagen, Long id_branch) {
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

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getStore() {
		return store;
	}

	public void setStore(Integer store) {
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
}

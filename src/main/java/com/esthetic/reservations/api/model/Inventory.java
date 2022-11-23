package com.esthetic.reservations.api.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "inventory")
public class Inventory extends BaseModel<Inventory> {
	private String inventory_name;
	
	private Integer price;
	
	private Integer store;
	
	@JoinColumn(name = "id_branch")
	@ManyToOne
	private Branch id_branch;

	public Inventory() {
		super();
	}

	public Inventory(Long id, String inventory_name, Integer price, Integer store, Branch id_branch) {
		super(id);
		this.inventory_name = inventory_name;
		this.price = price;
		this.store = store;
		this.id_branch = id_branch;
	}
	
	public Inventory(String inventory_name, Integer price, Integer store, Branch id_branch) {
		super();
		this.inventory_name = inventory_name;
		this.price = price;
		this.store = store;
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

	public Branch getId_branch() {
		return id_branch;
	}

	public void setId_branch(Branch id_branch) {
		this.id_branch = id_branch;
	}

	@Override
	public void copy(Inventory object) {
		this.inventory_name = object.inventory_name;
		this.price = object.price;
		this.store = object.store;
		this.id_branch = object.id_branch;
	}
	
	
}
